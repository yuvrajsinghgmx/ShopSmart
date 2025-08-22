# API Testing Guide (Postman)

This guide explains how to test the ShopSmart API using the provided Postman collection.

## 1. Prerequisites

- The backend server must be running.
- You must have a valid Firebase `id_token` for a user authenticated via phone number on a client application. For testing purposes, you will need to generate this token from a simple frontend or a test script. **The backend does not handle OTP generation; it only verifies the final token from Firebase.**

## 2. Import the Collection

- Open Postman → **Import** → Select the file: `postman/ShopSmart.postman_collection.json`.

## 3. Set Up Environment Variables

The collection uses variables to manage the server URL and authentication tokens.

- In Postman, click on the collection "ShopSmart API".
- Go to the **Variables** tab.
- Set `baseUrl` to your running server's address (e.g., `http://127.0.0.1:8000`).

## 4. Authentication Flow

The authentication process uses Firebase for identity verification and JWT for API sessions.

1.  **Firebase Login / Sign Up**
    -   Open the **Auth → Firebase Login** request.
    -   In the `Body`, replace the placeholder value of `id_token` with a valid token obtained from your Firebase client.
    -   Send the request. The server will verify the token, create a user if they don't exist, and return JWT `access` and `refresh` tokens.
    -   The Postman test script will automatically save these tokens to the collection variables `accessToken` and `refreshToken`.

2.  **Using Authenticated Endpoints**
    -   All other requests in the collection are already configured to use the `accessToken` for authorization via a Bearer Token. You don't need to manually set it for each request.

## 5. Onboarding Flow

1.  **Get Onboarding Details**
    -   After logging in, call **Onboarding → Get Onboarding Details**. For a new user, this will show default values and `onboarding_completed: false`.

2.  **Update Onboarding**
    -   Use the **Onboarding → Update Onboarding (as Customer/Shop Owner)** request.
    -   Fill in your details (`full_name`, `role`, address, etc.).
    -   To complete the process, set `onboarding_completed` to `true`.
    -   Send the request.
