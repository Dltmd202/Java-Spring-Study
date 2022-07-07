package com.sidepr.mono.sns.configures;


import com.sidepr.mono.sns.global.fileuploader.FileUploader;
import com.sidepr.mono.sns.global.fileuploader.local.LocalFileUploader;
import com.sidepr.mono.sns.user.security.Jwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfigure {

    @Bean
    public Jwt jwt(JwtTokenConfigure jwtTokenConfigure) {
        return new Jwt(jwtTokenConfigure.getIssuer(), jwtTokenConfigure.getClientSecret(), jwtTokenConfigure.getExpirySeconds());
    }

    @Bean
    public FileUploader fileUploader(){
        return new LocalFileUploader();
    }
}
