package com.xpier.example.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entry {

    private List<Pronunciation> pronunciations;

    private List<Senses> senses;
}
