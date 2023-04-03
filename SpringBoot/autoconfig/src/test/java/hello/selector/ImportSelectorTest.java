package hello.selector;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;

public class ImportSelectorTest {

    @Test
    void staticConfig(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(StaticConfig.class);
        HelloConfig bean = context.getBean(HelloConfig.class);
        assertThat(bean).isNotNull();
    }

    @Test
    void selectorConfig(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SelectorConfig.class);
        HelloConfig bean = context.getBean(HelloConfig.class);
        assertThat(bean).isNotNull();
    }

    @Configuration
    @Import(HelloConfig.class)
    public static class StaticConfig{

    }

    @Configuration
    @Import(HelloImportSelector.class)
    public static class SelectorConfig{

    }
}
