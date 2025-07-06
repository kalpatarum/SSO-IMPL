// src/HomePage.js
import React from 'react';
import { Link } from 'react-router-dom';

const HomePage = () => {
  return (
    <div>
      <h1>Welcome to Product Dashboard</h1>
      <p>
        <Link to="/products">Manage Products</Link>
      </p>
    </div>
  );
};

export default HomePage;