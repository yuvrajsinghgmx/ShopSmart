const BASE_URL = import.meta.env.VITE_API_BASE_URL;
const TOKEN = import.meta.env.VITE_ADMIN_AUTH_TOKEN;

const apiRequest = async (endpoint, options = {}) => {
  const { body, ...customOptions } = options;
  const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${TOKEN}`,
    ...customOptions.headers,
  };

  const config = {
    ...customOptions,
    headers,
  };

  if (body) {
    config.body = JSON.stringify(body);
  }

  try {
    const response = await fetch(`${BASE_URL}/${endpoint}`, config);
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({ detail: response.statusText }));
      throw new Error(errorData.detail || 'An API error occurred');
    }
    if (response.status === 204) { // No Content
      return null;
    }
    return response.json();
  } catch (error) {
    console.error('API request error:', error);
    throw error;
  }
};

export const getShops = () => apiRequest('admin/shops/', { method: 'GET' });

export const updateShopStatus = (id, action) => {
  return apiRequest(`admin/shops/${id}/approve/`, {
    method: 'PUT',
    body: { approval_action: action }, // 'approve' or 'reject'
  });
};

export const deleteShop = (id) => {
  return apiRequest(`admin/shops/${id}/delete/`, {
    method: 'DELETE',
  });
};