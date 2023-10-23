package com.file.textApi;

import com.file.textApi.controller.AnalyticsController;
import com.file.textApi.service.WordAnalyticService;
import com.file.textApi.service.impl.WordAnalyticServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalyticsControllerTest {

    private AnalyticsController analyticsController;

    @BeforeEach
    void setUp() {
        WordAnalyticService wordAnalyticService = new WordAnalyticServiceImpl();
        analyticsController = new AnalyticsController(wordAnalyticService);
    }

    @Test
    void analyzeTextFile_ValidFile() {
        String expectedOutput = """
                Word count = 9
                Average word length = 4.667
                Number of words of length 1 is 1
                Number of words of length 2 is 1
                Number of words of length 3 is 1
                Number of words of length 4 is 2
                Number of words of length 5 is 2
                Number of words of length 8 is 1
                Number of words of length 10 is 1
                The most frequently occurring word length is 4, for word lengths of 4 & 5""";

        String content = "Hello world & good morning. The date is 18/05/2016";
        MultipartFile file = new MockMultipartFile("file", "sample.txt", "text/plain", content.getBytes());

        ResponseEntity<String> response = analyticsController.analyzeTextFile(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOutput, response.getBody());
    }

    @Test
    void analyzeTextFile_EmptyFile() {
        MultipartFile file = new MockMultipartFile("file", "empty.txt", "text/plain", new byte[0]);

        ResponseEntity<String> response = analyticsController.analyzeTextFile(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No file provided", response.getBody());
    }

}

