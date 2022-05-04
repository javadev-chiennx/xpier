package com.xpier.example.router;

import com.xpier.example.model.response.TranslateResponse;
import com.xpier.example.service.TranslateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class RouterHandle {

    private final TranslateService service;

    @Value("${spring.router.endpoint}")
    private String baseUrl;

    @Bean
    public RouterFunction<ServerResponse> functionalRoutes() {
        return route(GET(baseUrl + "/translations"),
                request -> {
                    var langCode = request.queryParam("langCode").orElse(null);
                    var wordId = request.queryParam("wordId").orElse(null);
                    return ok().body(service.getTranslations(langCode, wordId), TranslateResponse.class);
                }
        );
    }
}
