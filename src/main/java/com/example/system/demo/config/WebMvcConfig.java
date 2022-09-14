package com.example.system.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/map").setViewName("mark_en");

//        registry.addViewController("/switch").setViewName("mark_en");

//        registry.addViewController("/covert").setViewName("mark01");

//        registry.addViewController("/test01").setViewName("index");

        registry.addViewController("/").setViewName("mark_en");

//        registry.addViewController("/to/register").setViewName("register");

    }
}
