package com.xpier.example.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pronunciation {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String audioFile;

    private String phoneticNotation;

    private String phoneticSpelling;
}
