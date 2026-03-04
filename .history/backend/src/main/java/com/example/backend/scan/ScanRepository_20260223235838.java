package com.example.backend.scan;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlModel, Long> {
    List<UrlModel> findByScanID(Long scanID);
}