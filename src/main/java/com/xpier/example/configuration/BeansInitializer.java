package com.xpier.example.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
@Slf4j
@RequiredArgsConstructor
public class BeansInitializer implements AsyncConfigurer {

    @Value("${oxford.app.id}")
    private String appId;

    @Value("${oxford.app.key}")
    private String appKey;

    @Value("${spring.web.client}")
    private int timeOut;

    @Bean
    public WebClient oxfordWebClient() {
        var httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeOut)
                .responseTimeout(Duration.ofMillis(timeOut))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(timeOut, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(timeOut, TimeUnit.MILLISECONDS)));

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("app_id", appId);
        headers.add("app_key", appKey);
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(headers))
                .baseUrl("https://od-api.oxforddictionaries.com/api/v2")
                .build();
    }
}
