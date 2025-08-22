import os
import json
import logging
import firebase_admin
from firebase_admin import credentials
from dotenv import load_dotenv

load_dotenv()
logger = logging.getLogger(__name__)

def initialize_firebase():
    if firebase_admin._apps:
        logger.warning("Firebase app already initialized.")
        return

    firebase_credentials_json = os.getenv("FIREBASE_CREDENTIALS_JSON")

    if not firebase_credentials_json:
        logger.error("FIREBASE_CREDENTIALS_JSON environment variable not set. Firebase Admin SDK cannot be initialized.")
        return

    try:
        cred_dict = json.loads(firebase_credentials_json)
        # The private key from environment variables often has escaped newlines
        cred_dict["private_key"] = cred_dict["private_key"].replace("\\n", "\n")
        
        cred = credentials.Certificate(cred_dict)
        firebase_admin.initialize_app(cred)
        logger.info("Firebase Admin SDK initialized successfully.")

if not firebase_admin._apps:
    cred = credentials.Certificate(cred_dict)
    firebase_admin.initialize_app(cred, {
        "storageBucket": os.getenv("FIREBASE_STORAGE_BUCKET")
    })