package com.example.urlshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.urlshortener.model.UrlHistory;

public interface UrlHistoryRepository extends JpaRepository<UrlHistory, Long> {
    UrlHistory findByShortenedUrl(String shortenedUrl); // Custom query method
}