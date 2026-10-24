package com.zerooversell.campaign.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class CampaignExceptionHandler {
    @ExceptionHandler(CampaignNotFoundException.class)
    ResponseEntity<Map<String, String>> notFound(CampaignNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("code", "CAMPAIGN_NOT_FOUND"));
    }
}
