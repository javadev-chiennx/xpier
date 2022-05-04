package com.xpier.example.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableAsync
@Slf4j
@RequiredArgsConstructor
public class BeansInitializer implements AsyncConfigurer {

    @Bean
    public WebClient oxfordWebClient() {
        return WebClient.builder().baseUrl("https://od-api.oxforddictionaries.com/api/v2").build();
    }
}
