package com.xpier.example.service;

import com.xpier.example.model.response.TranslateResponse;
import com.xpier.example.model.response.Vocabulary;
import reactor.core.publisher.Mono;

public interface TranslateService {

    Mono<Vocabulary> getTranslations(String langCode, String wordId);
}
