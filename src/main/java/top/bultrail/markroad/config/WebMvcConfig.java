package top.bultrail.markroad.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 原始版本 layui+elementUI+bootstrap
//        registry.addViewController("/map").setViewName("mark_en");
//        registry.addViewController("/").setViewName("mark_en");

        //前后端分离
//        registry.addViewController("/").setViewName("forward:/index.html");

    }
}
