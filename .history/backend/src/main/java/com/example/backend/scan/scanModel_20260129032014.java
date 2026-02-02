package com.example.backend.scan;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder.In;

@Entity
@Table(name = "`scan`")
public class scanModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Date;
    private Integer TotalURLsScanned;
    private Integer phishingUrlsFound;
    private Integer safeUrlsFound;

    public scanModel() {

    }

    public scanModel(Long id, String date, Integer totalURLsScanned, Integer phishingUrlsFound,
            Integer safeUrlsFound) {
        this.id = id;
        Date = date;
        TotalURLsScanned = totalURLsScanned;
        this.phishingUrlsFound = phishingUrlsFound;
        this.safeUrlsFound = safeUrlsFound;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Integer getTotalURLsScanned() {
        return TotalURLsScanned;
    }

    public void setTotalURLsScanned(Integer totalURLsScanned) {
        TotalURLsScanned = totalURLsScanned;
    }

    public Integer getPhishingUrlsFound() {
        return phishingUrlsFound;
    }

    public void setPhishingUrlsFound(Integer phishingUrlsFound) {
        this.phishingUrlsFound = phishingUrlsFound;
    }

    public Integer getSafeUrlsFound() {
        return safeUrlsFound;
    }

    public void setSafeUrlsFound(Integer safeUrlsFound) {
        this.safeUrlsFound = safeUrlsFound;
    }

}
