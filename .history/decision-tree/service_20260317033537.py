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
def tests():
    return {"its working"}

@app.get("/predict")
def predict(url: str = Query(..., min_length=1)):
    feats_dict = extract_features(url)  # dict of 17 features

    # Convert dict -> ordered list in the exact training order
    row = [feats_dict[name] for name in feature_names]

    pred = model.predict([row])[0]

    # If your model returns 0/1, convert to label string
    if str(pred).isdigit():
        pred_label = "phishing" if int(pred) == 1 else "legitimate"
    else:
        pred_label = str(pred)

    return {"url": url, "prediction": pred_label, "features": feats_dict}

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