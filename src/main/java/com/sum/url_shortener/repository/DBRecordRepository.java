package com.sum.url_shortener.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sum.url_shortener.entity.DBRecord;

@Repository
public interface DBRecordRepository extends JpaRepository<DBRecord, Integer> {

    Optional<DBRecord> findById(Integer id);
    Optional<DBRecord> findByLongUrl(String longUrl);
    Optional<DBRecord> findByShortUrl(String shortUrl);
    
}
