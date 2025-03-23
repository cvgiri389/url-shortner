package com.example.urlshortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.urlshortener.model.UrlHistory;
import com.example.urlshortener.repository.UrlHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlHistoryRepository urlHistoryRepository;

    public String shortenUrl(String originalUrl) {
        // Logic to generate a shortened URL
        String shortenedUrl = generateShortenedUrl(originalUrl);

        // Save to history
        UrlHistory urlHistory = new UrlHistory();
        urlHistory.setOriginalUrl(originalUrl);
        urlHistory.setShortenedUrl(shortenedUrl);
        urlHistory.setCreatedDate(LocalDateTime.now());
        urlHistory.setClickCount(0); // Initialize click count to 0
        urlHistoryRepository.save(urlHistory);

        return shortenedUrl;
    }

    private String generateShortenedUrl(String originalUrl) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UrlHistory> getUrlHistory() {
        return urlHistoryRepository.findAll(); // Retrieve all URL history
    }

    public void incrementClickCount(String shortenedUrl) {
        UrlHistory urlHistory = urlHistoryRepository.findByShortenedUrl(shortenedUrl);
        if (urlHistory != null) {
            urlHistory.setClickCount(urlHistory.getClickCount() + 1);
            urlHistoryRepository.save(urlHistory);
        }
    }
}
