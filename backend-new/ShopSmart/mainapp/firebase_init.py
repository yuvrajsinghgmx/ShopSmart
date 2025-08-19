import os
import json
import firebase_admin

from firebase_admin import credentials
from dotenv import load_dotenv

load_dotenv()

firebase_credentials_json = os.getenv("FIREBASE_CREDENTIALS_JSON")
cred_dict = json.loads(firebase_credentials_json)

cred_dict["private_key"] = cred_dict["private_key"].replace("\\n", "\n")

if not firebase_admin._apps:
    cred = credentials.Certificate(cred_dict)
    firebase_admin.initialize_app(cred)
