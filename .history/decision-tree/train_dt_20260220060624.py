import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, confusion_matrix
from joblib import dump

from feature_extractor import extract_10, FEATURE_NAMES

CSV_PATH = "data/dataset_phishing.csv"
MODEL_OUT = "dt_10_features.joblib"

def main():
    df = pd.read_csv(CSV_PATH)

    if "url" not in df.columns or "status" not in df.columns:
        raise ValueError("dataset.csv must contain 'url' and 'status' columns")

    X = df["url"].astype(str).apply(extract_10).tolist() #URL is converted to string the 10 feature numbers are called and tuned into list
    y = df["status"].astype(str).tolist() #status is converted to string and tuned into list
    # creates a feature matrix X are the 10 features as numbers
    

    X_train, X_test, y_train, y_test = train_test_split(  
        X, y, test_size=0.2, random_state=42, stratify=y
    )
    #splitting dataset into training and detection 0.2 = 20% for testing 80% for training
    #random state is set to 42 so split is the same each time
    # stratify=y ensures that the class distribution is the same in both training and test sets

    model = RandomForestClassifier(
    n_estimators=300,
    random_state=42
) # creates decision tree
    clf.fit(X_train, y_train) # trains the model on the training data

    preds = clf.predict(X_test) # makes predictions on the test data not training data using the trained model
    print("Confusion matrix:\n", confusion_matrix(y_test, preds)) # printing confusion matrix shows the actual legit, phish and predicted phish and legit
    print("\nReport:\n", classification_report(y_test, preds)) # classification report shows precision, recall and f1-score for each class

    dump({"model": clf, "feature_names": FEATURE_NAMES}, MODEL_OUT) # saves the trained model and the feature names to a file using joblib
    print(f"\nSaved model to: {MODEL_OUT}") 

if __name__ == "__main__":
    main()
