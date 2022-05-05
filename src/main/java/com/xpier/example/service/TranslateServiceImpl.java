package com.xpier.example.service;

import com.xpier.example.enums.ErrorCode;
import com.xpier.example.exception.BusinessException;
import com.xpier.example.model.entities.Word;
import com.xpier.example.model.response.Eg;
import com.xpier.example.model.response.TranslateResponse;
import com.xpier.example.model.response.Vocabulary;
import com.xpier.example.reposiroty.UserRepository;
import com.xpier.example.reposiroty.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslateServiceImpl implements TranslateService {

    private final WebClient oxfordWebClient;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final UserRepository userRepository;

    private final WordRepository wordRepository;


    /**
     * This is example get translations from Oxford APIs
     *
     * @param langCode Language code
     * @param wordId   Text search
     * @author chiennx
     */

    @Override
    public Mono<Vocabulary> getTranslations(String langCode, String wordId) {
        /*Call oxford API*/
        var responseMono = oxfordWebClient.get().uri("/entries/{langCode}/{wordId}", langCode, wordId)
                .retrieve()
                .bodyToMono(TranslateResponse.class)
                .doOnSuccess(item -> {
                    /*Get current user demo*/
                    var currentUser = userRepository.findById(1L).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
                    /*Save tag search into mongo DB.*/
                    var word = wordRepository.findByWord(wordId);
                    if (word == null) {
                        wordRepository.insert(new Word(sequenceGeneratorService.generateSequence(Word.SEQUENCE_NAME), wordId, Set.of(currentUser)));
                    } else {
                        var users = word.getUsers();
                        if (!users.contains(currentUser)) {
                            users.add(currentUser);
                            word.setUsers(users);
                            wordRepository.save(word);
                        }
                    }
                })
                .doOnError(throwable -> log.error("Occur error: {}", throwable.getMessage()))
                .block();

        /*Mapping response to DTO*/
        var vocabulary = Vocabulary.builder().word(wordId).examples(new ArrayList<>()).build();
        if (responseMono != null && !responseMono.getResults().isEmpty()) {
            /*get first item*/
            var content = responseMono.getResults().get(0);
            var lexicalEntries = content.getLexicalEntries();
            lexicalEntries.forEach(lexicalEntry -> {
                var entries = lexicalEntry.getEntries();
                entries.forEach(entry -> {
                    if (vocabulary.getSpelling() == null) {
                        var pronunciations = entry.getPronunciations().stream().filter(item ->
                                StringUtils.hasText(item.getAudioFile())).collect(Collectors.toList());
                        if (!pronunciations.isEmpty()) {
                            var pronunciation = pronunciations.get(0);
                            vocabulary.setSpelling(pronunciation.getPhoneticSpelling());
                            vocabulary.setAudioUrl(pronunciation.getAudioFile());
                        }
                    }
                    if (!entry.getSenses().isEmpty()) {
                        var sense = entry.getSenses().get(0);
                        if (!sense.getExamples().isEmpty())
                            vocabulary.getExamples().add(Eg.builder()
                                    .category(lexicalEntry.getLexicalCategory().getId())
                                    .example(sense.getExamples().get(0).getText()).build());
                    }
                });
            });
        }
        return Mono.just(vocabulary);
    }
}
