package com.example.shortenurl.services;

import com.example.shortenurl.exceptions.UrlNotFoundException;
import com.example.shortenurl.exceptions.UserNotFoundException;
import com.example.shortenurl.models.*;
import com.example.shortenurl.repositories.*;
import com.example.shortenurl.utils.ShortUrlGenerator;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UrlServiceImpl implements UrlService {

    private final UserRepository userRepository;
    private final ShortenedUrlRepository shortenedUrlRepository;
    private final UrlAccessLogRepository urlAccessLogRepository;

    public UrlServiceImpl(UserRepository userRepository,
                          ShortenedUrlRepository shortenedUrlRepository,
                          UrlAccessLogRepository urlAccessLogRepository) {
        this.userRepository = userRepository;
        this.shortenedUrlRepository = shortenedUrlRepository;
        this.urlAccessLogRepository = urlAccessLogRepository;
    }

    @Override
    public ShortenedUrl shortenUrl(String url, int userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // expiry in seconds
        long ttl;
        switch (user.getUserPlan()) {
            case FREE -> ttl = 86400; // 1 day
            case TEAM -> ttl = 86400 * 7;
            case BUSINESS -> ttl = 86400 * 30;
            case ENTERPRISE -> ttl = 86400 * 365;
            default -> ttl = 86400;
        }

        long expiresAt = Instant.now().getEpochSecond() + ttl;

        String shortUrl;
        do {
            shortUrl = ShortUrlGenerator.generateShortUrl();
        } while (shortenedUrlRepository.findByShortUrl(shortUrl).isPresent());

        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setOriginalUrl(url);
        shortenedUrl.setShortUrl(shortUrl);
        shortenedUrl.setExpiresAt(expiresAt);
        shortenedUrl.setUser(user);

        return shortenedUrlRepository.save(shortenedUrl);
    }

    @Override
    public String resolveShortenedUrl(String shortUrl) throws UrlNotFoundException {
        ShortenedUrl shortenedUrl = shortenedUrlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found"));

        long now = Instant.now().getEpochSecond();
        if (now > shortenedUrl.getExpiresAt()) {
            throw new UrlNotFoundException("Short URL expired");
        }

        UrlAccessLog log = new UrlAccessLog();
        log.setShortenedUrl(shortenedUrl);
        log.setAccessedAt(now);
        urlAccessLogRepository.save(log);

        return shortenedUrl.getOriginalUrl();
    }
}

