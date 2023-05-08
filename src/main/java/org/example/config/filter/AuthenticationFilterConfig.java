package org.example.config.filter;

import org.example.filter.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationFilterConfig {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> registrationBean(){
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/book");
        registrationBean.addUrlPatterns("/book/*");

        return registrationBean;
    }
}
