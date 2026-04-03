import pandas as pd
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from sklearn.metrics import classification_report, confusion_matrix, roc_curve, auc
from joblib import dump

CSV_PATH = "data/balanced_features.csv"
MODEL_OUT = "svm-model.joblib"
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
        SVC(
            kernel="rbf",
            C=1.0,
            probability=True,
            random_state=42
        )
    )

    model.fit(X_train, y_train)

    preds = model.predict(X_test)
    y_probs = model.predict_proba(X_test)[:, 1]

    fpr, tpr, thresholds = roc_curve(y_test, y_probs)
    roc_auc = auc(fpr, tpr)

    plt.figure()
    plt.plot(fpr, tpr, label="SVM (AUC = %0.2f)" % roc_auc)
    plt.plot([0, 1], [0, 1], linestyle='--')
    plt.xlabel("False Positive Rate")
    plt.ylabel("True Positive Rate")
    plt.title("ROC Curve for Phishing Detection Model")
    plt.legend(loc="lower right")
    plt.show()

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