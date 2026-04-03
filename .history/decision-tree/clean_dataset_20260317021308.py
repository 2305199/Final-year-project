import pandas as pd
import re
from urllib.parse import unquote

CSV_IN = "data/phishing-dataset.csv"
CSV_OUT = "data/clean_dataset.csv"

URL_COL = "url"   
LABEL_COL = "label"

def clean_url(u: str) -> str:
    u = str(u).strip() # makes sure its string and removes space before and at end 
    u = u.replace("&amp;", "&") # fixes html encoding of &
    u = unquote(u) # converts encoded characters to normal
    u = re.sub(r"\s+", "", u) # removes all spaces in url

    if not re.match(r"^https?://", u, re.IGNORECASE): # check if url has http
        u = "http://" + u # if not add it
        u = u.replace("\\", "/") # fix the slashes to proper side
    return u  # the cleaned url

def main():
    df = pd.read_csv(CSV_IN) # read the csv

    df = df.dropna() # remove rows with missing values 
    df[URL_COL] = df[URL_COL].astype(str).apply(clean_url) # make sure url is string and clean it using the funciton 

    df = df.drop_duplicates(subset=[URL_COL]) # remove duplicates, keep the first time it comes not later ones

    df.to_csv(CSV_OUT, index=False)  # save the dataset not add index column one before the URLS

    print("Saved:", CSV_OUT)
    print("Total rows:", len(df))

if __name__ == "__main__":
    main()