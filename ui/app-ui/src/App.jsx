import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import HomePage from './pages/HomePage';
import ProductsPage from './pages/ProductsPage';
import OrdersPage from './pages/OrdersPage';
import AdminPage from './pages/AdminPage';
import AccountPage from './pages/AccountPage';

function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route element={<Layout />}>
        {['products','orders','admin','account'].map((path, i) => {
          const Page = [ProductsPage, OrdersPage, AdminPage, AccountPage][i];
          return (
            <Route
              key={path}
              path={path}
              element={
                <ProtectedRoute>
                  <Page />
                </ProtectedRoute>
              }
            />
          );
        })}
      </Route>
    </Routes>
  );
}

export default App;
