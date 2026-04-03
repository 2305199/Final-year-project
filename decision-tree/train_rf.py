import pandas as pd
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, confusion_matrix, roc_curve, auc
from joblib import dump

CSV_PATH = "data/balanced_features.csv"
MODEL_OUT = "rf-model.joblib"
LABEL_COL = "label"

def main():
    df = pd.read_csv(CSV_PATH)

    X = df.drop(columns=[LABEL_COL]).values # takes all feature columns not label
    y = df[LABEL_COL].astype(int).values # makes column to integer  

    X_train, X_test, y_train, y_test = train_test_split(   # 70 training 30 testing
        X, y, test_size=0.3, random_state=42, stratify=y # stratify=y makes sure same amount of each class in train and test
    )

    model = RandomForestClassifier(
        n_estimators=200, # 200 trees
        random_state=42, # same split every time
        n_jobs=-1 # use all CPU cores to train faster
    )

    model.fit(X_train, y_train)  # train the model on training data

    preds = model.predict(X_test) # predict on test data

    print("Confusion matrix:\n", confusion_matrix(y_test, preds)) # show confusion report
    print("\nReport:\n", classification_report(y_test, preds)) # show classification report

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