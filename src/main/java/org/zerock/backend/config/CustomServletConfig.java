package org.zerock.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zerock.backend.controller.interceptors.JWTInterceptor;
import org.zerock.backend.formatters.LocalDateFormatter;
import org.zerock.backend.util.JWTUtil;


@Configuration
@EnableWebMvc
@Log4j2
@RequiredArgsConstructor
public class CustomServletConfig implements WebMvcConfigurer {


    private final JWTUtil jwtUtil;

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addFormatter(new LocalDateFormatter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE")
                .maxAge(300)
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new JWTInterceptor(jwtUtil)).addPathPatterns("/api1/**");
    }
}














