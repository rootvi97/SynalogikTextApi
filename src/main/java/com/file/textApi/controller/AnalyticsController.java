package com.file.textApi.controller;

import com.file.textApi.service.WordAnalyticService;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * A controller for analyzing text files.
 */
@Slf4j
@Tag(name = "Analytics", description = "File Analytics management APIs")
@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    private final WordAnalyticService wordAnalyticService;

    /**
     * Gson - simple Java-based library to serialize Java objects to JSON and vice versa developed by google
     * */
    private static final Gson gson = new Gson();

    @Autowired
    public AnalyticsController(WordAnalyticService wordAnalyticService) {
        this.wordAnalyticService = wordAnalyticService;
    }

    /**
     * Analyzes a text file and returns the results.
     */
    @Operation(
            summary = "Analyzes a text file and returns the results.",
            description = "API to read the contents of a plain text file and enable the display of the total number of words, the average word length, the most frequently occurring word length, and a list of the number of words of each length.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }) })
    @PostMapping(name = "/analyze", consumes = {"multipart/form-data"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> analyzeTextFile(@RequestPart(name = "file") @NonNull MultipartFile file) {
        if (file.isEmpty()) {
            log.warn("No file provided");
            return ResponseEntity.badRequest().body("No file provided");
        }

        // Read the contents of the uploaded file
        StringBuilder content = new StringBuilder();
        String output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
            output = wordAnalyticService.analyzeText(content.toString());
        } catch (Exception e) {
            log.error("An error occurred while processing the file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the file: " + e.getMessage());
        }
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
