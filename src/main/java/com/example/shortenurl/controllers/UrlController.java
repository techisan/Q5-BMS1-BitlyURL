package com.example.shortenurl.controllers;

import com.example.shortenurl.dtos.ResolveShortenUrlRequestDto;
import com.example.shortenurl.dtos.ResolveShortenUrlResponseDto;
import com.example.shortenurl.dtos.ShortenUrlRequestDto;
import com.example.shortenurl.dtos.ShortenUrlResponseDto;
import com.example.shortenurl.services.UrlService;

import com.example.shortenurl.dtos.*;
import com.example.shortenurl.models.ShortenedUrl;
import com.example.shortenurl.services.UrlService;
import com.example.shortenurl.exceptions.*;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    public ShortenUrlResponseDto shortenUrl(ShortenUrlRequestDto requestDto) {
        ShortenUrlResponseDto responseDto = new ShortenUrlResponseDto();
        try {
            ShortenedUrl shortenedUrl = urlService.shortenUrl(requestDto.getOriginalUrl(), requestDto.getUserId());
            responseDto.setShortUrl(shortenedUrl.getShortUrl());
            responseDto.setExpiresAt(shortenedUrl.getExpiresAt());
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (UserNotFoundException e) {
            responseDto.setStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }

    public ResolveShortenUrlResponseDto resolveShortenedUrl(ResolveShortenUrlRequestDto requestDto) {
        ResolveShortenUrlResponseDto responseDto = new ResolveShortenUrlResponseDto();
        try {
            String originalUrl = urlService.resolveShortenedUrl(requestDto.getShortenUrl());
            responseDto.setOriginalUrl(originalUrl);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (UrlNotFoundException e) {
            responseDto.setStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }
}

