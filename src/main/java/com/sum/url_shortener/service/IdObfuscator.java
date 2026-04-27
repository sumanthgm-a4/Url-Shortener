package com.sum.url_shortener.service;

import org.springframework.stereotype.Service;

@Service
public class IdObfuscator {

    private static final long SALT = 0x9E3779B97F4A7C15L;

    public static Long encode(Long id) {
        Long x = id;

        for (int i = 0; i < 7; i++) {
            x ^= mix(x ^ (SALT + i));
            x = Long.rotateLeft(x, 27);
        }

        return x;
    }

    private static Long mix(long z) {
        z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL;     // These constants are from MurmurHash3
        z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L;
        return z ^ (z >>> 33);
    }
}
