package com.xpier.example.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor@AllArgsConstructor
public class Vocabulary {

    private String word;

    private List<Eg> examples;

    private String audioUrl;

    private String spelling;
}
