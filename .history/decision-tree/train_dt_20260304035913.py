import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, confusion_matrix
from joblib import dump

CSV_PATH = "data/balanced_features.csv"
MODEL_OUT = "rf-model.joblib"
LABEL_COL = "label"

def main():
    df = pd.read_csv(CSV_PATH)

    X = df.drop(columns=[LABEL_COL]).values
    y = df[LABEL_COL].astype(int).values

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.3, random_state=42, stratify=y
    )

    model = RandomForestClassifier(
        n_estimators=500,
        random_state=42,
        n_jobs=-1
    )

    model.fit(X_train, y_train)

    preds = model.predict(X_test)
    print("Confusion matrix:\n", confusion_matrix(y_test, preds))
    print("\nReport:\n", classification_report(y_test, preds))

    dump({"model": model, "feature_names": list(df.drop(columns=[LABEL_COL]).columns)}, MODEL_OUT)
    print("\nSaved model to:", MODEL_OUT)

if __name__ == "__main__":
    main()