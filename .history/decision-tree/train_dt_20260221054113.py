import pandas as pd
import numpy as np

from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, confusion_matrix
from joblib import dump

from feature_extractor import extract_10, FEATURE_NAMES  # keep your lexical extractor

CSV_PATH = "data/phishing-dataset.csv"     # <-- put your real file name here
MODEL_OUT = "rf_lexical_10.joblib"

URL_COL = "url"
LABEL_COL = "label"

CHUNK_SIZE = 50000  # safe for big datasets

def map_label(v):
    # assumes: 1 = phishing, 0 = legitimate
    # if your dataset is reversed, swap these two returns
    return "phishing" if int(v) == 1 else "legitimate"

def main():
    X_all = []
    y_all = []

    for chunk in pd.read_csv(CSV_PATH, chunksize=CHUNK_SIZE):
        chunk = chunk.dropna(subset=[URL_COL, LABEL_COL])

        urls = chunk[URL_COL].astype(str)
        labels = chunk[LABEL_COL].apply(map_label)

        # Lexical scan: URL -> numeric feature vector
        X_chunk = urls.apply(extract_10).tolist()
        y_chunk = labels.tolist()

        X_all.extend(X_chunk)
        y_all.extend(y_chunk)

    X_all = np.array(X_all, dtype=float)
    y_all = np.array(y_all)

    print("Final X shape:", X_all.shape)  # should be (N, 10)

    X_train, X_test, y_train, y_test = train_test_split(
        X_all, y_all, test_size=0.2, random_state=42, stratify=y_all
    )

model = RandomForestClassifier(
    n_estimators=500,
    max_depth=None,
    min_samples_split=2,
    class_weight="balanced",
    random_state=42,
    n_jobs=-1
)

    model.fit(X_train, y_train)

    preds = model.predict(X_test)
    print("Confusion matrix:\n", confusion_matrix(y_test, preds))
    print("\nReport:\n", classification_report(y_test, preds))

    dump({"model": model, "feature_names": FEATURE_NAMES}, MODEL_OUT)
    print(f"\nSaved model to: {MODEL_OUT}")

if __name__ == "__main__":
    main()