import pandas as pd
import re
from urllib.parse import unquote

CSV_IN = "data/phishing-dataset.csv"              # <-- your raw dataset file
CSV_OUT = "data/clean_dataset.csv"

URL_COL = "url"
LABEL_COL = "label"

CHUNK_SIZE = 200_000

def clean_url(u: str) -> str:
    u = str(u).strip()
    u = u.replace("&amp;", "&")
    u = unquote(u)

    # remove whitespace inside
    u = re.sub(r"\s+", "", u)

    # ensure scheme for consistent parsing
    if not re.match(r"^https?://", u, re.IGNORECASE):
        u = "http://" + u

    # normalize common stuff
    u = u.replace("\\", "/")
    return u

def main():
    seen = set()
    wrote_header = False
    total_in = 0
    total_out = 0

    for chunk in pd.read_csv(CSV_IN, chunksize=CHUNK_SIZE, usecols=[URL_COL, LABEL_COL]):
        total_in += len(chunk)
        chunk = chunk.dropna(subset=[URL_COL, LABEL_COL])
        chunk[LABEL_COL] = chunk[LABEL_COL].astype(int)

        # clean urls
        chunk[URL_COL] = chunk[URL_COL].astype(str).apply(clean_url)

        # deduplicate globally
        keep_rows = []
        for url, label in zip(chunk[URL_COL].tolist(), chunk[LABEL_COL].tolist()):
            key = url  # dedupe by URL only (like most papers)
            if key not in seen:
                seen.add(key)
                keep_rows.append((url, label))

        if keep_rows:
            out_df = pd.DataFrame(keep_rows, columns=[URL_COL, LABEL_COL])
            out_df.to_csv(CSV_OUT, index=False, mode="a", header=not wrote_header)
            wrote_header = True
            total_out += len(out_df)

        print(f"Processed {total_in:,} rows -> kept {total_out:,} unique URLs")

    print("Saved:", CSV_OUT)

if __name__ == "__main__":
    main()