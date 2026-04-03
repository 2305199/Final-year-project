import pandas as pd

df = pd.read_csv("data/clean-dataset.csv")

print(df["label"].value_counts())