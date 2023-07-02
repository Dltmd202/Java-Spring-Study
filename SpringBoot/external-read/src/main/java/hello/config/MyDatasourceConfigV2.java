package hello.config;

import hello.datasource.MyDataSource;
import hello.datasource.MyDataSourcePropertiesV1;
import hello.datasource.MyDataSourcePropertiesV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@EnableConfigurationProperties(MyDataSourcePropertiesV2.class)
public class MyDatasourceConfigV2 {
    private final MyDataSourcePropertiesV2 properties;

    public MyDatasourceConfigV2(MyDataSourcePropertiesV2 propertiesV1) {
        this.properties = propertiesV1;
    }

    @Bean
    public MyDataSource dataSource(){
        return new MyDataSource(
                properties.getUrl(),
                properties.getUsername(),
                properties.getPassword(),
                properties.getEtc().getMaxConnection(),
                properties.getEtc().getTimeout(),
                properties.getEtc().getOptions()
        );
    }
}
