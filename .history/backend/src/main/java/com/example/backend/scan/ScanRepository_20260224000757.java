package com.example.backend.scan;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanRepository extends JpaRepository<ScanModel, Long> {
}