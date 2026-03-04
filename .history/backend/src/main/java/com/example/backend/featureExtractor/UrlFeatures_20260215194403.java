package com.example.backend.featureExtractor;

public class UrlFeatures {

    private int ip;
    private int nb_at;
    private int nb_qm;
    private int nb_and;
    private int nb_semicolumn;
    private int nb_www;
    private double ratio_digits_host;
    private int tld_in_subdomain;
    private int phish_hints;
    private int brand_in_subdomain;

    public int getip() {
        return ip;
    }

    public void setip(int ip) {
        this.ip = ip;
    }

    public int getnb_at() {
        return nb_at;
    }

    public void setnb_at(int nb_at) {
        this.nb_at = nb_at;
    }

    public int getnb_qm() {
        return nb_qm;
    }

    public void setnb_qm(int nb_qm) {
        this.nb_qm = nb_qm;
    }

    public int getnb_and() {
        return nb_and;
    }

    public void setnb_and(int nb_and) {
        this.nb_and = nb_and;
    }

    public int getnb_semicolumn() {
        return nb_semicolumn;
    }

    public void setnb_semicolumn(int nb_semicolumn) {
        this.nb_semicolumn = nb_semicolumn;
    }

    public int getnb_www() {
        return nb_www;
    }

    public void setnb_www(int nb_www) {
        this.nb_www = nb_www;
    }

    public double getratio_digits_host() {
        return ratio_digits_host;
    }

    public void setratio_digits_host(double ratio_digits_host) {
        this.ratio_digits_host = ratio_digits_host;
    }

    public int gettld_in_subdomain() {
        return tld_in_subdomain;
    }

    public void settld_in_subdomain(int tld_in_subdomain) {
        this.tld_in_subdomain = tld_in_subdomain;
    }

    public int getphish_hints() {
        return phish_hints;
    }

    public void setphish_hints(int phish_hints) {
        this.phish_hints = phish_hints;
    }

    public int getbrand_in_subdomain() {
        return brand_in_subdomain;
    }

    public void setbrand_in_subdomain(int brand_in_subdomain) {
        this.brand_in_subdomain = brand_in_subdomain;
    }

}
