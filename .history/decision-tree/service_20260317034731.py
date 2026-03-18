from fastapi import FastAPI, Query
from pydantic import BaseModel
from typing import List
from joblib import load


from build_features import extract_features
MODEL_PATH = "rf-model.joblib"

app = FastAPI()


bundle = load(MODEL_PATH) # load the saved model
model = bundle["model"]  # gets the model from bundle


feature_names = bundle.get("feature_names", None) # gets features in exact order of training
if feature_names is None:
    raise RuntimeError(
        "Model bundle is missing 'feature_names'. Re-save the model with feature_names." 
    )

@app.get("/test")
def tests():    # test if endpoint works
    return {"its working"}

@app.get("/predict")
def predict(url: str = Query(..., min_length=1)): # takes url from query with min lenght of 1 so not emmpty
    feats_dict = extract_features(url)  # converts urls into features like in training

   
    row = [feats_dict[name] for name in feature_names] # creates the list just like in order in training

    pred = model.predict([row])[0] # makes prediction features as input and output is label 

   
    if str(pred).isdigit(): # converting label to string phishing/string
        pred_label = "phishing" if int(pred) == 1 else "legitimate" # if result is 1 phishing else 0
    else:
        pred_label = str(pred) 

    return {"url": url, "prediction": pred_label, "features": feats_dict} # return the urls prediction and features

class UrlBatch(BaseModel):
    urls: List[str]

@app.post("/predict-batch")
def predict_batch(batch: UrlBatch):
    results = []
    for url in batch.urls:
        feats_dict = extract_features(url)
        row = [feats_dict[name] for name in feature_names]
        pred = model.predict([row])[0]

        if str(pred).isdigit():
            pred_label = "phishing" if int(pred) == 1 else "legitimate"
        else:
            pred_label = str(pred)

        results.append({"url": url, "prediction": pred_label, "features": feats_dict})

    return {"count": len(results), "results": results}