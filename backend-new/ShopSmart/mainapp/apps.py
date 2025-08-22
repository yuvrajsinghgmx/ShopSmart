from django.apps import AppConfig
import logging

logger = logging.getLogger(__name__)

class MainappConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'mainapp'

    def ready(self):
        logger.info("MainappConfig is ready. Initializing services...")
        from .firebase_init import initialize_firebase
        initialize_firebase()
