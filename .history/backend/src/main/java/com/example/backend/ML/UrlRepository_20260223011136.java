package com.example.backend.ML;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlModel, Long> {
}