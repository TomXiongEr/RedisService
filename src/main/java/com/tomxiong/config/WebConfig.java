/*
package com.tomxiong.config;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.tomxiong.interceptor.CommonInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


*/
/**
 * @author TOM XIONG
 * @description mvc配置
 * @date 2019/7/5 17:18
 *//*

*/
/*
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /*
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resourceViewResolver = new InternalResourceViewResolver();
        resourceViewResolver.setViewClass(JstlView.class);
        resourceViewResolver.setPrefix("/templates/html/");
        resourceViewResolver.setSuffix(".html");
        return resourceViewResolver;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Add formatters and/or converters
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建fastJson消息转换器
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //创建配置类
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //修改配置返回内容的过滤
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty
        );
        fastConverter.setFastJsonConfig(fastJsonConfig);
        MediaType jsonUtf8 = MediaType.APPLICATION_JSON_UTF8;
        List<MediaType> medias = new ArrayList<>();
        medias.add(jsonUtf8);
        fastConverter.setSupportedMediaTypes(medias);
        //将fastjson添加到视图消息转换器列表内
        converters.add(fastConverter);
    }*//*


    */
/*@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CommonInterceptor()).addPathPatterns("/**").order(1);
    }*//*

}
*/
