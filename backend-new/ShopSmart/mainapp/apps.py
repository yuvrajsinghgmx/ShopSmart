import json
from django.apps import AppConfig
from django.conf import settings
import firebase_admin
from firebase_admin import credentials


class MainappConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'mainapp'

    def ready(self):
        """
        Initialize Firebase Admin SDK on Django app startup.
        """
        if not firebase_admin._apps:
            try:
                firebase_credentials_json = settings.FIREBASE_CREDENTIALS_JSON
                if not firebase_credentials_json:
                    raise ValueError("FIREBASE_CREDENTIALS_JSON environment variable not set.")
                
                cred_dict = json.loads(firebase_credentials_json)
                cred_dict["private_key"] = cred_dict["private_key"].replace("\\n", "\n")
                
                cred = credentials.Certificate(cred_dict)
                firebase_admin.initialize_app(cred, {
                    'storageBucket': settings.FIREBASE_STORAGE_BUCKET
                })
                print("Firebase Admin SDK initialized successfully.")
            except Exception as e:
                # Log the error for debugging purposes
                print(f"Error initializing Firebase Admin SDK: {e}")