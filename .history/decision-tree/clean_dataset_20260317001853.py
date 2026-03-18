import pandas as pd
import re
from urllib.parse import unquote

CSV_IN = "data/phishing-dataset.csv"
CSV_OUT = "data/clean_dataset.csv"

URL_COL = "url"
LABEL_COL = "label"

def clean_url(u: str) -> str:
    u = str(u).strip()
    u = u.replace("&amp;", "&")
    u = unquote(u)
    u = re.sub(r"\s+", "", u)

    if not re.match(r"^https?://", u, re.IGNORECASE):
        u = "http://" + u
        u = u.replace("\\", "/")
    return u

def main():
    df = pd.read_csv(CSV_IN, usecols=[URL_COL, LABEL_COL])

    df = df.dropna(subset=[URL_COL, LABEL_COL])
    df[URL_COL] = df[URL_COL].astype(str).apply(clean_url)

    df = df.drop_duplicates(subset=[URL_COL])

    df.to_csv(CSV_OUT, index=False)

    print("Saved:", CSV_OUT)
    print("Total rows:", len(df))

if __name__ == "__main__":
    main()