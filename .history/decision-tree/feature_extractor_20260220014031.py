import re
from urllib.parse import urlparse
import tldextract
import url_features as urlfe

_SPLIT = re.compile(r"\-|\.|\/|\?|\=|\@|\&|\%|\:|\_")

FEATURE_NAMES = [
    "ip", "nb_at", "nb_qm", "nb_and", "nb_semicolumn", "nb_www",
    "ratio_digits_host", "tld_in_subdomain", "phish_hints", "brand_in_subdomain"
]

def _words_raw(domain: str, subdomain: str, path: str):
    w_domain = _SPLIT.split((domain or "").lower())  # split domain into words
    w_sub = _SPLIT.split((subdomain or "").lower()) # split subdomain into words
    w_path = _SPLIT.split((path or "").lower())   # split path into words
    return list(filter(None, (w_domain + w_path + w_sub))) # combine and the different parts and remove empty tokens

def extract_10(url: str):
    parsed = urlparse(url) # break URL into parts
    hostname = parsed.netloc or "" # get domain and subdomain
    path = parsed.path or "" # get path part of URL

    ext = tldextract.extract(url) # extract domain, subdomain and tld using tldextract (handles edge cases better than urlparse)
    domain = ext.domain or "" # get registered domain (e.g. "example" from "www.example.com")
    subdomain = ext.subdomain or "" # get subdomain (e.g. "www" from "www.example.com")
    tld = ext.suffix or "" # get top-level domain (e.g. "com" from "www.example.com")

    tokens = _words_raw(domain, subdomain, path) 

    return [
        urlfe.having_ip_address(url),           # ip
        urlfe.count_at(url),                    # nb_at
        urlfe.count_exclamation(url),           # nb_qm (counts '?')
        urlfe.count_and(url),                   # nb_and
        urlfe.count_semicolumn(url),            # nb_semicolumn
        urlfe.check_www(tokens),                # nb_www
        urlfe.ratio_digits(hostname) if hostname else 0.0,  # ratio_digits_host
        urlfe.tld_in_subdomain(tld, subdomain), # tld_in_subdomain
        urlfe.phish_hints(url),                 # phish_hints
        urlfe.brand_in_path(domain, subdomain), # brand_in_subdomain
    ]   
# Each part of the URL is broken into parts to be used for feature extraction
# then the 10 features are extracted using functions defined in url_features.py 
#returned as a list in the same order as FEATURE_NAMES