package com.example.mongochat.configures;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sidepr.mono.sns.post.dto.converter.PostCreateRequestConverter;
import com.sidepr.mono.sns.post.dto.converter.PostUpdateRequestConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//@Configuration
public class WebConfigure extends WebMvcConfigurationSupport {

//    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper()
                .registerModule(new SimpleModule())
                .registerModule(new JavaTimeModule());
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addConverter(new PostCreateRequestConverter(objectMapper()));
        registry.addConverter(new PostUpdateRequestConverter(objectMapper()));
    }
}