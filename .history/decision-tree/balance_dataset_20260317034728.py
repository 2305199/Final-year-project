import pandas as pd
from imblearn.under_sampling import RandomUnderSampler

CSV_IN = "data/clean_dataset.csv"
CSV_OUT = "data/balanced.csv"

URL_COL = "url"
LABEL_COL = "label"

def main():
    df = pd.read_csv(CSV_IN)

    # Features and labels are split
    X = df[[URL_COL]]
    y = df[LABEL_COL]

    # Create the undersampler will undersample majority class
    rus = RandomUnderSampler(sampling_strategy=1, random_state=42)

    # do the undersampling
    X_resampled, y_resampled = rus.fit_resample(X, y)

    # Combine back into a dataframe
    df_balanced = pd.concat([X_resampled, y_resampled], axis=1)

    df_balanced.to_csv(CSV_OUT, index=False) # save it

    print("Saved:", CSV_OUT)
    print(df_balanced[LABEL_COL].value_counts())   # count the number of rows in each column

if __name__ == "__main__":
    main()




    