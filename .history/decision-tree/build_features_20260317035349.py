import pandas as pd
import re
from urllib.parse import urlparse
import tldextract
from collections import Counter
from scipy.stats import entropy
import ipaddress

CSV_IN = "data/balanced.csv"
CSV_OUT = "data/balanced_features.csv"

URL_COL = "url"
LABEL_COL = "label"

def has_ip(host: str) -> int: # checking if host has IP
    try:
        ipaddress.ip_address(host) # check if ip is valid
        return 1 # if it is then its phishing 
    except ValueError: # if not valid its safe
        return 0

def calculate_entropy(s: str) -> float:  # function to calcuate how random hostname is
    if not s: 
        return 0.0 
    counts = Counter(s) # check how many times characters come in the string count it

    return entropy(list(counts.values()), base=2) # gets the values from counts turns it to list  
    #calculates entropy using entropy() base 2 we are measuring in bits 

def extract_features(url: str) -> dict: # url is inpt output is the features
    p = urlparse(url)
    host = (p.netloc or "").lower() # get host part of url netloc is domain part
    path = p.path or "" # get path of url if not then empty
    query = p.query or ""  # get the query part its part after ?

    ext = tldextract.extract(url)  # tldextract to make domain into parts subdomain domain suffix
    subdomain = ext.subdomain or "" # get subdomain if not then empty
    suffix = ext.suffix or "" # get suffix if not then empty ending like .com
 
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
        "ratio_digits_url": sum(ch.isdigit() for ch in url) / len(url) if len(url) else 0.0, # divide length of url with digits num
        "ratio_digits_host": sum(ch.isdigit() for ch in host) / len(host) if len(host) else 0.0,  # divide host length with digits 
        "nb_subdomains": max(0, len(host.split(".")) - 2) if host else 0, # count dots in host -2 not counting domain and suffix max 0 make sure not negative
        "has_ip": has_ip(host), # check if hsot has ip
        "tld_in_subdomain": 1 if (suffix and suffix in subdomain) else 0, 
        "entropy_hostname": calculate_entropy(host),
        "path_len": len(path),
        "query_len": len(query),
    }

def main():
    df = pd.read_csv(CSV_IN)
    feat_df = pd.DataFrame(df[URL_COL].astype(str).apply(extract_features).tolist()) # apply extract features to all urls turn them to list 
    out = pd.concat([feat_df, df[[LABEL_COL]].reset_index(drop=True)], axis=1) # joins features columns and labels axis=1 join by columns

    out.to_csv(CSV_OUT, index=False)
    print("Saved:", CSV_OUT)
    print("Shape:", out.shape)

if __name__ == "__main__":
    main()