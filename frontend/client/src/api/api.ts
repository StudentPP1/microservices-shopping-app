import axios from 'axios';
import keycloak from './keycloak';

const api = axios.create({
  baseURL: 'http://localhost:9000/api',
});

api.interceptors.request.use(async (config) => {
  try {
    await keycloak.updateToken(5);
  } catch (error) {
    console.error('Failed to refresh token', error);
    keycloak.logout();
    throw error;
  }
  console.log('Token refreshed successfully');
  console.log('Current token:', keycloak.token);
  if (keycloak.token) {
    config.headers.Authorization = `Bearer ${keycloak.token}`;
  }
  return config;
});

export default api;
