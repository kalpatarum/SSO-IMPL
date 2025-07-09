import React from 'react';
import { AppBar, Toolbar, Button, Typography } from '@mui/material';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthProvider';

export default function Layout() {
  const { user, setUser } = useAuth();
  const navigate = useNavigate();
  const API = import.meta.env.VITE_API_BASE_URL;

  const logout = () => {
    fetch(`${API}/logout`, { method: 'POST', credentials: 'include' })
      .then(res => {
        if (res.ok) {
          setUser(null);
          window.location.href = '/';
        } else {
          console.error('Logout failed: non-200');
        }
      })
      .catch(err => {
        console.error('Logout failed', err);
      });
  };


  return (
    <>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Product Management
          </Typography>
          {['products','orders','admin','account'].map((path) => (
            <Button
              key={path}
              color="inherit"
              component={NavLink}
              to={`/${path}`}
              sx={{ textTransform: 'capitalize' }}
            >
              {path}
            </Button>
          ))}
          {user && (
            <Button color="inherit" onClick={logout}>
              Logout ({user})
            </Button>
          )}
        </Toolbar>
      </AppBar>
      <Outlet />
    </>
  );
}
