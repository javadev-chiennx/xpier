package com.xpier.example.service;

import com.xpier.example.enums.ErrorCode;
import com.xpier.example.exception.BusinessException;
import com.xpier.example.model.entities.Word;
import com.xpier.example.model.response.TranslateResponse;
import com.xpier.example.reposiroty.UserRepository;
import com.xpier.example.reposiroty.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TranslateServiceImpl implements TranslateService {

    private final WebClient oxfordWebClient;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final UserRepository userRepository;

    private final WordRepository wordRepository;

    @Value("${oxford.app.id}")
    private String appId;

    @Value("${oxford.app.key}")
    private String appKey;

    @Override
    @Transactional
    public TranslateResponse getTranslations(String langCode, String wordId) {
        /*Call oxford API*/
        TranslateResponse vocabulary;
        try {
            vocabulary = oxfordWebClient.get().uri("/entries/{langCode}/{wordId}", langCode, wordId)
                    .header("app_id", appId)
                    .header("app_key", appKey)
                    .retrieve()
                    .bodyToMono(TranslateResponse.class)
                    .block();
        } catch (WebClientResponseException exception) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        } catch (Exception exception) {
            throw new BusinessException(ErrorCode.ILLEGAL_ARGUMENTS);
        }
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
        return vocabulary;
    }
}
