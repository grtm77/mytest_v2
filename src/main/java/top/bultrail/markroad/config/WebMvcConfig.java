package top.bultrail.markroad.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/map").setViewName("mark_en");
        registry.addViewController("/").setViewName("mark_en");

        // 没有地图，container容器
//        registry.addViewController("/map").setViewName("mark_en_2");
//        registry.addViewController("/").setViewName("mark_en_2");

        // 不显示地图 container容器+elementUI按钮组
//        registry.addViewController("/map").setViewName("mark_en_3");
//        registry.addViewController("/").setViewName("mark_en_3");

        // 能显示地图 container容器+elementUI 按钮组按钮组未绑定
//        registry.addViewController("/map").setViewName("mark_en_4");
//        registry.addViewController("/").setViewName("mark_en_4");


    }
}
