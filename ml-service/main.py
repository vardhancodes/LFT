from fastapi import FastAPI
from pydantic import BaseModel
import random

# Initialize the FastAPI application
app = FastAPI(title="LFT Machine Learning Engine")

# Define the exact data structure we expect from Java
class TradingFeatures(BaseModel):
    current_price: float
    sma_20: float
    rsi_14: float
    price_to_sma_ratio: float

# This endpoint matches the URL in our Java MlClient (http://localhost:8000/predict)
@app.post("/predict")
def predict_signal(features: TradingFeatures):
    print(f"Received features from Java: {features}")
    
    # -------------------------------------------------------------
    # MOCK ML MODEL LOGIC
    # In a real scenario, this is where you would load a pickled 
    # scikit-learn (RandomForest, XGBoost) or PyTorch model.
    # e.g., prediction = model.predict([[features.sma_20, ...]])
    # -------------------------------------------------------------
    
    # Let's write a simple programmatic "model" for now
    confidence = round(random.uniform(0.65, 0.95), 2)
    
    # If the price is below its moving average and RSI is low (cheap)
    if features.price_to_sma_ratio < 0.98 and features.rsi_14 < 40:
        prediction = "BUY"
    # If the price is way above its moving average and RSI is high (expensive)
    elif features.price_to_sma_ratio > 1.02 and features.rsi_14 > 60:
        prediction = "SELL"
    # Otherwise, do nothing
    else:
        prediction = "HOLD"
        confidence = round(random.uniform(0.40, 0.60), 2)

    # The expected JSON response format our Java MlClient needs
    result = {
        "prediction": prediction,
        "confidence": confidence
    }
    
    print(f"ML Decision: {result}")
    return result