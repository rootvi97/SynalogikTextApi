package com.file.textApi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class WordStatsResponse {
    private int wordCount;
    private double averageWordLength;
    private List<WordLengthStats> wordLengthStats;
    private String mostFrequentWordLengths;
}
