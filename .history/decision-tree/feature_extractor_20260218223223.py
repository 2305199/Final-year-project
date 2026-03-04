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
    w_domain = _SPLIT.split((domain or "").lower())
    w_sub = _SPLIT.split((subdomain or "").lower())
    w_path = _SPLIT.split((path or "").lower())
    return list(filter(None, (w_domain + w_path + w_sub)))

def extract_10(url: str):
    parsed = urlparse(url)
    hostname = parsed.netloc or ""
    path = parsed.path or ""

    ext = tldextract.extract(url)
    domain = ext.domain or ""
    subdomain = ext.subdomain or ""
    tld = ext.suffix or ""

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
