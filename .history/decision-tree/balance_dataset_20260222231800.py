import pandas as pd

CSV_IN = "data/clean_dataset.csv"
CSV_OUT = "data/balanced.csv"

URL_COL = "url"
LABEL_COL = "label"

def main():
    df = pd.read_csv(CSV_IN)
    df[LABEL_COL] = df[LABEL_COL].astype(int)

    df_legit = df[df[LABEL_COL] == 0]
    df_phish = df[df[LABEL_COL] == 1]

    n = len(df_phish)
    df_legit_down = df_legit.sample(n=n, random_state=42)

    df_bal = pd.concat([df_legit_down, df_phish], ignore_index=True)
    df_bal = df_bal.sample(frac=1, random_state=42).reset_index(drop=True)

    df_bal.to_csv(CSV_OUT, index=False)
    print("Saved:", CSV_OUT)
    print(df_bal[LABEL_COL].value_counts())

if __name__ == "__main__":
    main()