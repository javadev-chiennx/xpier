package com.xpier.example.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Transient
    public static final String SEQUENCE_NAME = "word_sequence";

    @Id
    private Long id;

    @Indexed(unique = true)
    @Field(value = "user_name")
    private String userName;

    @Field(value = "full_name")
    private String fullName;

    @Field(value = "hire_date")
    private Date hireDate;
}
