package com.sum.url_shortener.service;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

@Service
public class Base62Encoder {
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    public static String encode(long num) {
        BigInteger n = BigInteger.valueOf(num);

        if (n.signum() < 0) {
            n = n.add(BigInteger.ONE.shiftLeft(64)); // treat as unsigned
        }

        StringBuilder sb = new StringBuilder();

        while (n.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = n.divideAndRemainder(BigInteger.valueOf(62));
            sb.append(CHARS.charAt(divmod[1].intValue()));
            n = divmod[0];
        }

        return sb.reverse().toString();
    }
}
