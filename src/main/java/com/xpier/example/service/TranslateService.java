package com.xpier.example.service;

import com.xpier.example.model.response.TranslateResponse;
import reactor.core.publisher.Mono;

public interface TranslateService {

    Mono<TranslateResponse> getTranslations(String langCode, String wordId);
}
