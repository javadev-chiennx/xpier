package com.xpier.example.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor@AllArgsConstructor
public class Vocabulary {

    private String word;

    private List<Eg> examples;

    private String audioUrl;

    private String spelling;
}
