package com.xpier.example.service;

import com.xpier.example.enums.ErrorCode;
import com.xpier.example.exception.BusinessException;
import com.xpier.example.model.entities.Word;
import com.xpier.example.model.response.TranslateResponse;
import com.xpier.example.reposiroty.UserRepository;
import com.xpier.example.reposiroty.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;

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
     * @author chiennx
     * @param langCode Language code
     * @param wordId Text search
     *
     * */

    @Override
    public Mono<TranslateResponse> getTranslations(String langCode, String wordId) {
        /*Call oxford API*/
        return oxfordWebClient.get().uri("/entries/{langCode}/{wordId}", langCode, wordId)
                .retrieve()
                .bodyToMono(TranslateResponse.class)
                .doOnSuccess(item -> {
                    /*Get current user demo*/
                    var currentUser = userRepository.findById(1L).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
                    /*Save tag search into mongo DB.*/
                    var word = wordRepository.findByWord(item.getWord());
                    if (word == null) {
                        wordRepository.insert(new Word(sequenceGeneratorService.generateSequence(Word.SEQUENCE_NAME), item.getWord(), Set.of(currentUser)));
                    } else {
                        var users = word.getUsers();
                        if (!users.contains(currentUser)) {
                            users.add(currentUser);
                            word.setUsers(users);
                            wordRepository.save(word);
                        }
                    }
                }).doOnError(throwable -> log.error("Occur error: {}", throwable.getMessage()));
    }
}
