package com.mrzhang.mieapiclientsdk;

import com.mrzhang.mieapiclientsdk.client.MieApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//通过@配置(configuration)注解，将该类标记为一个配置类，告诉Spring这是一个用于配置的类
@Configuration
//能够读取application.yml的配置，读取到配置之后，把这个读到的配置设置到我们这里的属性中//这里给所有的配置加上前缀为“mieapi.client”
@ConfigurationProperties("mieapi.client")
//@数据注解是一个Lombook注解，自动生成了类的getter、setter方法
@Data
//@组件扫描(ComponentScan)注解用于自动扫描组件，使得Spring能够自动注册相应的Bean
@ComponentScan
public class MieApiClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public MieApiClient mieApiClientpiClient() {
        return new MieApiClient(accessKey, secretKey);
    }
}
