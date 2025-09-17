const BASE_URL = import.meta.env.VITE_API_BASE_URL;

const apiRequest = async (endpoint, options = {}, needsAuth = true) => {
  const { body, ...customOptions } = options;
  const headers = {
    'Content-Type': 'application/json',
    ...customOptions.headers,
  };

  if (needsAuth) {
    const token = localStorage.getItem('admin_token');
    if (!token) {
      // This will effectively log the user out if a token is missing for a protected route.
      window.location.href = '/'; 
      throw new Error('No authentication token found. Please log in.');
    }
    headers['Authorization'] = `Bearer ${token}`;
  }

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

export const login = async (email, password) => {
  const data = await apiRequest('admin/login/', {
    method: 'POST',
    body: { email, password },
  }, false); // Login doesn't need auth
  
  if (data && data.access) {
    localStorage.setItem('admin_token', data.access);
  }
  return data;
};

export const logout = () => {
  localStorage.removeItem('admin_token');
};

export const getShops = () => apiRequest('admin/shops/', { method: 'GET' });

export const updateShopStatus = (id, action) => {
  return apiRequest(`admin/shops/${id}/approve/`, {
    method: 'PUT',
    body: { approval_action: action },
  });
};

export const deleteShop = (id) => {
  return apiRequest(`admin/shops/${id}/delete/`, {
    method: 'DELETE',
  });
};