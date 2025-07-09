import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthProvider';

export default function ProtectedRoute({ children }) {
  const { user, checked } = useAuth();
  if (!checked) return <div>Loading...</div>;
  if (!user) return null;
  console.log("ProtectedRoute", { checked, user });
  return user ? children : <Navigate to="/" />;
}
