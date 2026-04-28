package com.sum.url_shortener.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sum.url_shortener.entity.UrlRequest;
import com.sum.url_shortener.service.DBRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShortUrlController {

    private final DBRecordService dbRecordService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortUrl(@RequestBody UrlRequest request) {
        return dbRecordService.generateShortUrl(request.getUrl());
    }

    @GetMapping("/fetch/{shortUrl}")
    public ResponseEntity<?> fetchLongUrl(@PathVariable String shortUrl) {
        return dbRecordService.fetchLongUrlFromShortUrl(shortUrl);
    }

}
