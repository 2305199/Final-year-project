import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import classification_report, confusion_matrix
from joblib import dump

from feature_extractor import extract_10, FEATURE_NAMES

CSV_PATH = "data/dataset_phishing.csv"
MODEL_OUT = "dt_10_features.joblib"

def main():
    df = pd.read_csv(CSV_PATH)

    if "url" not in df.columns or "status" not in df.columns:
        raise ValueError("dataset.csv must contain 'url' and 'status' columns")

    X = df["url"].astype(str).apply(extract_10).tolist()
    y = df["status"].astype(str).tolist()

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y
    )

    clf = DecisionTreeClassifier(random_state=42, class_weight="balanced")
    clf.fit(X_train, y_train)

    preds = clf.predict(X_test)
    print("Confusion matrix:\n", confusion_matrix(y_test, preds))
    print("\nReport:\n", classification_report(y_test, preds))

    dump({"model": clf, "feature_names": FEATURE_NAMES}, MODEL_OUT)
    print(f"\nSaved model to: {MODEL_OUT}")

if __name__ == "__main__":
    main()
