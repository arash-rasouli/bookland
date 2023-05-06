package org.example.utils;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Util {
    public static byte[] getSHA(String input){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
            return input.getBytes(StandardCharsets.UTF_8);
        }
    }

    public static String hexToString(byte[] hash){
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64){
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public static String createJWT(String userEmail, String key){
        SignatureAlgorithm alg = SignatureAlgorithm.HS256;
        SecretKeySpec signKey = new SecretKeySpec(Util.getSHA(key), alg.getJcaName());

        Instant now = Instant.now();

        JwtBuilder jwtBuilder = Jwts.builder()
                .claim("userEmail", userEmail)
                .setId(UUID.randomUUID().toString())
                .setIssuer("BOOKLAND")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1L, ChronoUnit.HOURS)))
                .signWith(signKey, alg);

        return jwtBuilder.compact();
    }

    public static Claims decodeJWT(String token, String key){
        SignatureAlgorithm alg = SignatureAlgorithm.HS256;
        SecretKeySpec signKey = new SecretKeySpec(Util.getSHA(key), alg.getJcaName());

        try {
            return Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
        }catch (Exception e){
            return null;
        }
    }

    public static boolean validateJwt(Claims claims){
        if(!claims.getIssuer().equals("BOOKLAND")) return false;
        if(claims.getExpiration().before(Date.from(Instant.now()))) return false;
        return true;
    }
}
