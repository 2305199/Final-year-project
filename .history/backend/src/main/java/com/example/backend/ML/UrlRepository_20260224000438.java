package com.example.backend.ML;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlModel, Long> {

    List<UrlModel> findByScanID(Long scanID);
}