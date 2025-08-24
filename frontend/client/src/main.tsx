import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.tsx';
import keycloak from './api/keycloak.ts';

keycloak.init({
    onLoad: "check-sso",            // або "login-required"
    redirectUri: `${window.location.origin}/`,
    checkLoginIframe: false,
}).then((authenticated) => {
  if (authenticated) {
    ReactDOM.createRoot(document.getElementById('root')!).render(
      <React.StrictMode>
        <App />
      </React.StrictMode>
    );
  } else {
    keycloak.login();
  }
});