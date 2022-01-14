package com.javamentor.qa.platform.webapp.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/users").setViewName("users");
        registry.addViewController("/tags").setViewName("tags");
        registry.addViewController("/questions").setViewName("questions");
        registry.addViewController("/questions/add").setViewName("askQuestion");
        registry.addViewController("/pagination").setViewName("testPagination");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Access Bootstrap static resource:
        registry.addResourceHandler("/js/jquery/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/jquery/3.6.0/");
        registry.addResourceHandler("/js/popper/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/popper.js/1.16.0/umd/");
        registry.addResourceHandler("/css/bootstrap/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap/4.6.1/");
    }

}
