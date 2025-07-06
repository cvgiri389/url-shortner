package com.example.urlshortener.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
public class UrlMapping {

    @Id
    private String shortCode;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column
    private String createdBy; // Stores the Google user ID

    @Column
    private LocalDateTime createdAt;

    @Column
    private int clickCount = 0;

    public UrlMapping() {}

    public UrlMapping(String shortCode, String originalUrl, String createdBy) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters for all fields
    public String getShortCode() {
        return shortCode;
    }
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getClickCount() {
        return clickCount;
    }
    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
    
    public void incrementClickCount() {
        this.clickCount++;
    }
}