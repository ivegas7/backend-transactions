package com.tenpo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tenpo.interceptor.RateLimitInterceptor;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    //Políticas de CORS
    //Permitir o denegar el acceso de un dominio a recursos de otro dominio.
  
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")  
                .allowedMethods("GET", "POST", "PUT", "DELETE")  
                .allowedHeaders("*"); 
    }
    
    
    private final RateLimitInterceptor rateLimitInterceptor;
    
    public WebConfig(RateLimitInterceptor rateLimitInterceptor) {
        this.rateLimitInterceptor = rateLimitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/**");
    }
    
    
}
