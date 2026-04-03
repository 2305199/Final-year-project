import pandas as pd
from imblearn.under_sampling import RandomUnderSampler

CSV_IN = "data/clean_dataset.csv"
CSV_OUT = "data/balanced.csv"

URL_COL = "url"
LABEL_COL = "label"

def main():
    df = pd.read_csv(CSV_IN)
    df[LABEL_COL] = df[LABEL_COL].astype(int)

    # Features and labels
    X = df[[URL_COL]]
    y = df[LABEL_COL]

    # Create the RandomUnderSampler
    rus = RandomUnderSampler(sampling_strategy=1, random_state=42)

    # Apply undersampling
    X_resampled, y_resampled = rus.fit_resample(X, y)

    # Combine back into a dataframe
    df_balanced = pd.concat([X_resampled, y_resampled], axis=1)

    df_balanced.to_csv(CSV_OUT, index=False)

    print("Saved:", CSV_OUT)
    print(df_balanced[LABEL_COL].value_counts())

if __name__ == "__main__":
    main()