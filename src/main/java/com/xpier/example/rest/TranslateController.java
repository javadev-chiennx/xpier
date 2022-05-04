package com.xpier.example.rest;

import com.xpier.example.model.response.TranslateResponse;
import com.xpier.example.service.TranslateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
public class TranslateController {

    private final TranslateService service;

    @GetMapping("/translations")
    public ResponseEntity<Mono<TranslateResponse>> translations(@RequestParam String langCode, @RequestParam String wordId) {
        return ResponseEntity.ok(this.service.getTranslations(langCode, wordId));
    }
}
