package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.example.dto.ResponseInfo;
import org.example.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class AuthenticationFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if(req.getMethod().equals("OPTIONS")){
            chain.doFilter(request, response);
            return;
        }

        String jwt = req.getHeader("auth-token");
        String JWTKey;
        try {
            JWTKey = Util.getJWTKey();
            if(JWTKey == null){
                logger.error("[AuthenticationFilter] Unable to find bookland.jwt.key in secrets.properties");
                return;
            }
        }
        catch (Exception ex){
            logger.error("[AuthenticationFilter] Unable to read secrets.properties");
            return;
        }

        if(jwt != null && !jwt.equals("null")){
            Claims claims = Util.decodeJWT(jwt, JWTKey);
            if(claims != null && Util.validateJwt(claims)){
                request.setAttribute("userEmail", claims.get("userEmail"));
                chain.doFilter(request, response);
                return;
            }
        }
        logger.info("[AuthenticationFilter] request has blocked : Request Info : path : {} , method : {}", req.getServletPath(), req.getMethod());

        ResponseInfo responseInfo = new ResponseInfo(null, false, "Invalid auth-token. please send auth-token as header " +
                "<auth-token = jwt> (jwt provide by login) in your request");
        res.resetBuffer();
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        res.getOutputStream().print(new ObjectMapper().writeValueAsString(responseInfo));
        res.flushBuffer();
    }
}