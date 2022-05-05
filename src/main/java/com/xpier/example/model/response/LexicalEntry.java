package com.xpier.example.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LexicalEntry {

    private String text;

    private LexicalCategory lexicalCategory;

    private List<Entry> entries;
}
