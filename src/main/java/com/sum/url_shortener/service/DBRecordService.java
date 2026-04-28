package com.sum.url_shortener.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sum.url_shortener.entity.DBRecord;
import com.sum.url_shortener.repository.DBRecordRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DBRecordService {

    private final DBRecordRepository repository;
    private final UrlShortenerService urlShortenerService;

    public ResponseEntity<String> fetchLongUrlFromShortUrl(String shortUrl) {
        String longUrl = repository.findByShortUrl(shortUrl)
                .map(DBRecord::getLongUrl)
                .orElseThrow(() -> new RuntimeException("Short URL doesn't exist"));

        // return new ResponseEntity<>(longUrl, HttpStatus.PERMANENT_REDIRECT);

        // Redirect automatically, or atleast this is what clients expect
        return ResponseEntity.status(301)
                    .header("Location", longUrl)
                    // .header(longUrl, null)       // For any addn.l headers
                    .build();
    }   

    @Transactional
    public ResponseEntity<String> generateShortUrl(String longUrl) {

        // Return the shortUrl if the longUrl already exists
        if (repository.findByLongUrl(longUrl).isPresent()) {
            String shortUrl = repository.findByLongUrl(longUrl)
                .map(DBRecord::getShortUrl)
                .orElse(null);
            
            return new ResponseEntity<>(shortUrl, HttpStatus.OK);
        }

        // Save first
        DBRecord dbRecord = new DBRecord();
        dbRecord.setLongUrl(longUrl);
        repository.save(dbRecord);
        // Fetch the ID, encode it into shortUrl and update
        String shortUrl = urlShortenerService.encode(dbRecord.getId());
        dbRecord.setShortUrl(shortUrl);
        repository.save(dbRecord);

        return new ResponseEntity<>(shortUrl, HttpStatus.OK);
    }
}
