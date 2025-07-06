package com.example.urlshortener.controller;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.service.UrlShorteningService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class UrlShortenerController {

    private final UrlShorteningService urlShorteningService;

    public UrlShortenerController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @GetMapping("/")
    public String home(Model model, 
                      Authentication authentication,
                      @AuthenticationPrincipal OAuth2User oauth2User,
                      @RequestParam(required = false) String logout) {
        
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            String userId = authentication.getName(); // This is the Google user ID
            
            if (oauth2User != null) {
                username = oauth2User.getAttribute("name") != null ? 
                         oauth2User.getAttribute("name") : username;
            }
            model.addAttribute("username", username);
            
            // Add user's URL history to the model
            List<UrlMapping> userHistory = urlShorteningService.getUserHistory(userId);
            model.addAttribute("history", userHistory);
        }
        
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been signed out successfully.");
        }
        
        return "index";
    }

    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam("url") String url,
                           HttpServletRequest request,
                           Model model,
                           Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/?error=authrequired";
        }

        String userId = authentication.getName();
        String shortCode = urlShorteningService.shortenUrl(url, userId);
        String baseUrl = getBaseUrl(request);

        model.addAttribute("originalUrl", url);
        model.addAttribute("shortUrl", baseUrl + "/" + shortCode);
        return "redirect:/"; // Redirect to refresh the page and show history
    }

    @GetMapping("/{shortCode}")
    public String redirectToOriginal(@PathVariable String shortCode) {
        Optional<String> originalUrlOpt = urlShorteningService.getOriginalUrl(shortCode);
        return originalUrlOpt.map(s -> "redirect:" + s)
                           .orElse("redirect:/?error=notfound");
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String baseUrl = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) ||
            (scheme.equals("https") && serverPort != 443)) {
            baseUrl += ":" + serverPort;
        }
        baseUrl += contextPath;
        return baseUrl;
    }
}