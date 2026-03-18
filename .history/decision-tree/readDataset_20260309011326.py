import pandas as pd

df = pd.read_csv("data/balanced.csv")

print(df["label"].value_counts())