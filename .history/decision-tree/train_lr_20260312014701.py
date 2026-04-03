import pandas as pd
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import classification_report, confusion_matrix, roc_curve, auc
from joblib import dump

CSV_PATH = "data/balanced_features.csv"
MODEL_OUT = "logreg-model.joblib"
LABEL_COL = "label"

def main():
    df = pd.read_csv(CSV_PATH)

    X = df.drop(columns=[LABEL_COL]).values
    y = df[LABEL_COL].astype(int).values

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.3, random_state=42, stratify=y
    )

    model = make_pipeline(
        StandardScaler(),
        LogisticRegression(max_iter=1000, random_state=42)
    )

    model.fit(X_train, y_train)

    preds = model.predict(X_test)

    y_probs = model.predict_proba(X_test)[:, 1]


    print("Confusion matrix:\n", confusion_matrix(y_test, preds))
    print("\nReport:\n", classification_report(y_test, preds))

    dump(
        {
            "model": model,
            "feature_names": list(df.drop(columns=[LABEL_COL]).columns)
        },
        MODEL_OUT
    )

    print("\nSaved model to:", MODEL_OUT)

if __name__ == "__main__":
    main()