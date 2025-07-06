package com.example.urlshortener.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlMappingRepository;

@Service
public class UrlShorteningService {

    private final UrlMappingRepository repository;
    private static final String ALPHANUM = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();
    public static final int SHORT_CODE_LENGTH = 7;

    public UrlShorteningService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String shortenUrl(String originalUrl, String userId) {
        // Generate unique short code
        String shortCode;
        do {
            shortCode = generateRandomCode();
        } while (repository.existsById(shortCode));

        UrlMapping mapping = new UrlMapping(shortCode, originalUrl, userId);
        repository.save(mapping);
        return shortCode;
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(ALPHANUM.charAt(random.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }

    @Transactional
    public Optional<String> getOriginalUrl(String shortCode) {
        Optional<UrlMapping> mappingOpt = repository.findById(shortCode);
        mappingOpt.ifPresent(mapping -> {
            mapping.incrementClickCount();
            repository.save(mapping);
        });
        return mappingOpt.map(UrlMapping::getOriginalUrl);
    }

    public List<UrlMapping> getUserHistory(String userId) {
        return repository.findByCreatedByOrderByCreatedAtDesc(userId);
    }
}