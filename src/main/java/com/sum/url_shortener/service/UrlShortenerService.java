package com.sum.url_shortener.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
public class UrlShortenerService {

    public String encode(Long id) {
        Long obfValue = IdObfuscator.encode(id);
        return Base62Encoder.encode(obfValue);
    }
}
