import React from 'react';
import { Button, Container, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

export default function HomePage() {
  const navigate = useNavigate();
  return (
    <Container sx={{ textAlign: 'center', mt: 8 }}>
      <Typography variant="h3">Welcome to the Product Portal</Typography>
      <Button
        variant="contained"
        size="large"
        sx={{ mt: 4 }}
        onClick={() => navigate('/products')}
      >
        Go to Products
      </Button>
    </Container>
  );
}
