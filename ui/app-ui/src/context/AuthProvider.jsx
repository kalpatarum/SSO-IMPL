import React, { createContext, useContext, useEffect, useState } from 'react';
const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [checked, setChecked] = useState(false);
  const API = import.meta.env.VITE_API_BASE_URL;

  useEffect(() => {
    console.log("Checking auth from:", `${API}/me`);
    fetch(`${API}/me`, { credentials: 'include' })
      .then(res => {
        console.log("Auth status code:", res.status);
        if (res.status === 401) {
          window.location.href = `${API}/oauth2/authorization/my-client`;
        } else return res.json();
      })
      .then(data => {
        setUser(data?.name || null);
        setChecked(true);
      })
      .catch(() => setChecked(true));
  }, []);

  return (
    <AuthContext.Provider value={{ user, checked, setUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
