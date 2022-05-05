package com.xpier.example.rest;

import com.xpier.example.model.response.TranslateResponse;
import com.xpier.example.service.TranslateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TranslationsController extends AbstractController {

    private final TranslateService service;

    @GetMapping(value = "/translations")
    public ResponseEntity<Mono<TranslateResponse>> getTranslations(@RequestParam String langCode, @RequestParam String wordId) {
        var result = service.getTranslations(langCode, wordId);
        return ResponseEntity.ok(result);
    }
}
