package com.example.autoparts.config;

import com.example.autoparts.filter.AdminFilter;
import com.example.autoparts.filter.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistration() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AuthFilter());

        registrationBean.addUrlPatterns(
                "/admin/*",
                "/cart",
                "/cart/*",
                "/orders",
                "/orders/*",
                "/reviews/*",
                "/checkout/*"
        );

        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AdminFilter> adminFilterRegistration() {
        FilterRegistrationBean<AdminFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AdminFilter());

        registrationBean.addUrlPatterns(
                "/admin/*",
                "/categories/*",
                "/products/add",
                "/products/save",
                "/products/edit/*",
                "/products/delete/*",
                "/orders/update-status"
        );

        registrationBean.setOrder(2);

        return registrationBean;
    }
}