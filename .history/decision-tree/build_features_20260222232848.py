def extract_features(url: str) -> dict:
    url = clean_url(url)
    p = urlparse(url)
    host = (p.netloc or "").lower()
    path = p.path or ""
    query = p.query or ""

    ext = tldextract.extract(url)
    subdomain = ext.subdomain or ""
    suffix = ext.suffix or ""

    return {
        "length_url": len(url),
        "length_hostname": len(host),
        "nb_dots": host.count("."),
        "nb_hyphens": host.count("-"),
        "nb_slash": url.count("/"),
        "nb_qm": url.count("?"),
        "nb_and": url.count("&"),
        "nb_at": url.count("@"),
        "nb_eq": url.count("="),
        "ratio_digits_url": sum(ch.isdigit() for ch in url) / len(url) if len(url) else 0.0,
        "ratio_digits_host": sum(ch.isdigit() for ch in host) / len(host) if len(host) else 0.0,
        "nb_subdomains": max(0, len(host.split(".")) - 2) if host else 0,
        "has_ip": has_ip(host),
        "tld_in_subdomain": 1 if (suffix and suffix in subdomain) else 0,
        "entropy_hostname": entropy(host),
        "path_len": len(path),
        "query_len": len(query),
    }