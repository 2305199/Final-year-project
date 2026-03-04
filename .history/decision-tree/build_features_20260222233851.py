import pandas as pd
import re, math
from urllib.parse import urlparse, unquote
import tldextract

CSV_IN = "data/balanced.csv"
CSV_OUT = "data/URL-Balanced-dataset.csv"

URL_COL = "url"
LABEL_COL = "label"

def clean_url(u: str) -> str:
    u = str(u).strip()
    u = u.replace("&amp;", "&")
    u = unquote(u)
    if not re.match(r"^https?://", u, re.IGNORECASE):
        u = "http://" + u
    return u

def has_ip(host: str) -> int:
    return 1 if re.match(r"^\d{1,3}(\.\d{1,3}){3}$", host) else 0

def entropy(s: str) -> float:
    if not s:
        return 0.0
    counts = {}
    for ch in s:
        counts[ch] = counts.get(ch, 0) + 1
    n = len(s)
    return -sum((c/n) * math.log2(c/n) for c in counts.values())

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

def main():
    df = pd.read_csv(CSV_IN).dropna(subset=[URL_COL, LABEL_COL])

    feat_df = pd.DataFrame(df[URL_COL].astype(str).apply(extract_features).tolist())

    out = pd.concat([
        df[[URL_COL]].reset_index(drop=True),
        feat_df,
        df[[LABEL_COL]].reset_index(drop=True)
    ], axis=1)

    out.to_csv(CSV_OUT, index=False)
    print("Saved:", CSV_OUT)
    print("Shape:", out.shape)

if __name__ == "__main__":
    main()