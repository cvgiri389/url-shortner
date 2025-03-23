package com.example.urlshortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.urlshortener.model.UrlHistory;
import com.example.urlshortener.service.UrlShortenerService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public String shortenUrl(@RequestBody String originalUrl) {
        return urlShortenerService.shortenUrl(originalUrl);
    }

    @GetMapping("/history")
    public List<UrlHistory> getUrlHistory() {
        return urlShortenerService.getUrlHistory(); // Return URL history
    }
}