package com.xpier.example.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Document(collection = "word")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Word {

    @Transient
    public static final String SEQUENCE_NAME = "word_sequence";

    @Id
    private long id;

    @Indexed(unique = true)
    @Field(value = "word")
    private String word;

    @DBRef
    @Lazy
    private Set<User> users;
}
