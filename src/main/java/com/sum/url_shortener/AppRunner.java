package com.sum.url_shortener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sum.url_shortener.service.UrlShortenerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    @Value("${DB_URL}")
    private String dbUrl;

    private final UrlShortenerService service;

    @Override
    public void run(String... args) {
        System.out.println(dbUrl);

        System.out.println(service.encode(1000000000000L));
        System.out.println(service.encode(1000000000001L));
        System.out.println(service.encode(1000000000002L));
        System.out.println(service.encode(1000000000003L));
        System.out.println(service.encode(1000000000004L));
    }
}
