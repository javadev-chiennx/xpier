package com.xpier.example.reposiroty;

import com.xpier.example.model.entities.Word;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends MongoRepository<Word, Long> {
    Word findByWord(String word);
}
