package com.transfer.backendbankmasr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")  // Allow CORS on all paths
//                .allowedOrigins("http://localhost:5173/**")  // Allow specific origin(s) by adding hosts
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow specific HTTP methods
//                .allowedHeaders("*")  // Allow all headers
//                .allowCredentials(false);  // Allow credentials (like cookies)
//    }

}
