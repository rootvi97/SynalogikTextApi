package com.file.textApi.service.impl;

import com.file.textApi.model.WordLengthStats;
import com.file.textApi.model.WordStatsResponse;
import com.file.textApi.service.WordAnalyticService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class WordAnalyticServiceImpl implements WordAnalyticService {

    /**
     * Analyzes a text file and returns the results.
     */
    @Override
    public String analyzeText(String text) {
        List<String> words = Arrays.asList(text.split("\\s+"));
        WordStatsResponse wordStatsResponse = WordStatsResponse.builder()
                .wordCount(getWordCount(words))
                .averageWordLength(getAverageWordLength(words))
                .wordLengthStats(getWordLengthFrequency(words))
                .mostFrequentWordLengths(getMostFrequentWordLength(words))
                .build();

        return generateOutputString(wordStatsResponse);
    }

    /**
     * Generates an output string from the given word stats response and returns string output
     */
    private String generateOutputString(WordStatsResponse wordStatsResponse) {
        StringBuilder outputText = new StringBuilder();
        outputText
                .append("Word count = ")
                .append(wordStatsResponse.getWordCount())
                .append("\n")
                .append("Average word length = ")
                .append(wordStatsResponse.getAverageWordLength())
                .append("\n");
        for (WordLengthStats stats : wordStatsResponse.getWordLengthStats()) {
            outputText.append("Number of words of length ").append(stats.getLength())
                    .append(" is ").append(stats.getCount()).append("\n");
        }
        outputText.append(wordStatsResponse.getMostFrequentWordLengths());
        return outputText.toString();
    }

    /**
     * Returns the number of words in the given list of words.
     */
    private int getWordCount(List<String> words) {
        return words.size();
    }

    /**
     * Returns the average word length in the given list of words.
     */
    private double getAverageWordLength(List<String> words) {
        int totalLength = 0;
        for (String word : words) {
            totalLength += word.length();
        }
        return (double) Math.round(((double) totalLength / getWordCount(words)) * 1000) / 1000;
    }

    /**
     * Returns a list of word length stats for the given list of words, calculates the word length frequency.
     */
    private List<WordLengthStats> getWordLengthFrequency(List<String> words) {
        Map<Integer, Integer> lengthFrequency = new HashMap<>();
        for (String word : words) {
            int length = word.length();
            lengthFrequency.put(length, lengthFrequency.getOrDefault(length, 0) + 1);
        }

        List<WordLengthStats> statsList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : lengthFrequency.entrySet()) {
            statsList.add(WordLengthStats.builder()
                    .length(entry.getKey())
                    .count(entry.getValue())
                    .build());
        }
        return statsList;
    }

    /**
     * Returns a string describing the most frequently occurring word lengths in the given list of words.
     */
    private String getMostFrequentWordLength(List<String> words) {
        List<WordLengthStats> wordLengthStats = getWordLengthFrequency(words);
        int maxFrequency = 0;
        int mostFrequentLength = 0;
        Set<Integer> mostFrequentLengths = new HashSet<>();

        for (WordLengthStats stats : wordLengthStats) {
            if (stats.getCount() > maxFrequency) {
                maxFrequency = stats.getCount();
                mostFrequentLength = stats.getLength();
                mostFrequentLengths.clear();
                mostFrequentLengths.add(mostFrequentLength);
            } else if (stats.getCount() == maxFrequency) {
                mostFrequentLengths.add(stats.getLength());
            }
        }

        if (mostFrequentLengths.size() == 1) {
            return "The most frequently occurring word length is " + mostFrequentLength + ", for word lengths of " + mostFrequentLength;
        } else {
            StringBuilder lengths = new StringBuilder();
            for (int length : mostFrequentLengths) {
                lengths.append(length).append(" & ");
            }
            lengths.delete(lengths.length() - 3, lengths.length()); // Remove the last " & "
            return "The most frequently occurring word length is " + mostFrequentLength + ", for word lengths of " + lengths;
        }
    }
}
