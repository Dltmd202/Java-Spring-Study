package hello.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConfig {
    @Bean
    public HelloController helloController(){
        System.out.println("HelloConfig.helloController");
        return new HelloController();
    }
}
