from fastapi import FastAPI, Query
from pydantic import BaseModel
from typing import List
from joblib import load
from feature_extractor import extract_10

MODEL_PATH = "dt_10_features.joblib"

app = FastAPI()

# Load model once at startup
bundle = load(MODEL_PATH)
model = bundle["model"]


@app.get("/health")
def health():
    return {"status": "ok"}


@app.get("/predict")
def predict(url: str = Query(..., min_length=1)):
    features = extract_10(url)
    prediction = model.predict([features])[0]
    return {"url": url, "prediction": prediction, "features": features}


class UrlBatch(BaseModel):
    urls: List[str]


@app.post("/predict-batch")
def predict_batch(batch: UrlBatch):
    results = []
    for url in batch.urls:
        feats = extract_10(url)
        pred = model.predict([feats])[0]
        results.append({"url": url, "prediction": pred, "features": feats})
    return {"count": len(results), "results": results}
