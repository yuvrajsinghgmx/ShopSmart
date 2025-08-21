import os
import uuid
from typing import List, Optional
from django.conf import settings
import firebase_admin
from firebase_admin import credentials, storage
from django.core.files.uploadedfile import InMemoryUploadedFile, TemporaryUploadedFile
from .firebase_init import cred 

class FirebaseStorageManager:
    """
    Manager class for Firebase Storage operations
    """
    
    def __init__(self):
        if not firebase_admin._apps:
            # Initialize Firebase Admin SDK using credentials from settings
            if settings.FIREBASE_CREDENTIALS:
                firebase_admin.initialize_app(cred, {
                    'storageBucket': settings.FIREBASE_STORAGE_BUCKET
                })
            else:
                raise ValueError("Firebase credentials not found in settings")
        
        self.bucket = storage.bucket()
    
    def upload_image(self, image_file, folder: str, max_size_mb: int = 5) -> Optional[str]:
        """
        Upload a single image to Firebase Storage
        
        Args:
            image_file: Django uploaded file
            folder: Storage folder (e.g., 'shops', 'products', 'documents')
            max_size_mb: Maximum file size in MB
            
        Returns:
            str: Public URL of uploaded image or None if failed
        """
        try:
            # Validate file size
            if image_file.size > max_size_mb * 1024 * 1024:
                raise ValueError(f"File size exceeds {max_size_mb}MB limit")
            
            # Generate unique filename
            file_extension = os.path.splitext(image_file.name)[1]
            unique_filename = f"{folder}/{uuid.uuid4()}{file_extension}"
            
            # Upload to Firebase Storage
            blob = self.bucket.blob(unique_filename)
            blob.upload_from_file(image_file, content_type=image_file.content_type)
            
            # Make the blob publicly accessible
            blob.make_public()
            
            return blob.public_url
            
        except Exception as e:
            print(f"Error uploading image: {str(e)}")
            return None
    
    def upload_multiple_images(self, image_files: List, folder: str, max_count: int, max_size_mb: int = 5) -> List[str]:
        """
        Upload multiple images to Firebase Storage
        
        Args:
            image_files: List of Django uploaded files
            folder: Storage folder
            max_count: Maximum number of images allowed
            max_size_mb: Maximum file size in MB per image
            
        Returns:
            List[str]: List of public URLs of uploaded images
        """
        if len(image_files) > max_count:
            raise ValueError(f"Maximum {max_count} images allowed")
        
        uploaded_urls = []
        for image_file in image_files:
            url = self.upload_image(image_file, folder, max_size_mb)
            if url:
                uploaded_urls.append(url)
        
        return uploaded_urls
    
    def delete_image(self, image_url: str) -> bool:
        """
        Delete an image from Firebase Storage
        
        Args:
            image_url: Public URL of the image
            
        Returns:
            bool: True if deleted successfully, False otherwise
        """
        try:
            # Extract blob name from URL
            blob_name = self.extract_blob_name_from_url(image_url)
            if blob_name:
                blob = self.bucket.blob(blob_name)
                blob.delete()
                return True
        except Exception as e:
            print(f"Error deleting image: {str(e)}")
        return False
    
    def delete_multiple_images(self, image_urls: List[str]) -> int:
        """
        Delete multiple images from Firebase Storage
        
        Args:
            image_urls: List of public URLs
            
        Returns:
            int: Number of images successfully deleted
        """
        deleted_count = 0
        for url in image_urls:
            if self.delete_image(url):
                deleted_count += 1
        return deleted_count
    
    def extract_blob_name_from_url(self, image_url: str) -> Optional[str]:
        """
        Extract blob name from Firebase Storage public URL
        """
        try:
            # Parse the URL to extract blob name
            # Format: https://storage.googleapis.com/{bucket}/o/{blob_name}?alt=media&token={token}
            if 'googleapis.com' in image_url and '/o/' in image_url:
                blob_part = image_url.split('/o/')[1]
                blob_name = blob_part.split('?')[0]
                # URL decode the blob name
                import urllib.parse
                return urllib.parse.unquote(blob_name)
        except Exception:
            pass
        return None
