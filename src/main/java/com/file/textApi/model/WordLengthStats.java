package com.file.textApi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WordLengthStats {
    private int length;
    private int count;
}
