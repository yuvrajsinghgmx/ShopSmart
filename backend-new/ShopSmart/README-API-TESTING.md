API Testing Guide (Postman)

1) Import the collection
- Open Postman → Import → Select `backend-new/ShopSmart/postman/ShopSmart.postman_collection.json`.

2) Set variables
- Set `baseUrl` to your server, e.g., `http://127.0.0.1:8000`.

3) Flow
- Send "Send OTP" with a phone number.
- Send "Verify OTP" with the same number and the OTP printed in server logs (or the demo value you configure). Copy `access` into `accessToken` variable.
- Call "Get Onboarding" (should return role and current onboarding fields).
- Call "Update Onboarding" with your details. Ensure `onboarding_completed` is true when done.

4) Required screenshots for PR
- Screenshot of successful "Verify OTP" response showing `is_new_user`, `requires_onboarding`, and `role`.
- Screenshot of "Get Onboarding" response (before update for a new user).
- Screenshot of "Update Onboarding" response (after update with `onboarding_completed: true`).


