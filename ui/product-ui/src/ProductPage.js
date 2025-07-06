// src/ProductPage.js
import React, { useEffect, useState } from 'react';

function ProductPage() {
  const [products, setProducts] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [newProduct, setNewProduct] = useState({
    name: '',
    description: '',
    price: ''
  });

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = () => {
    fetch('http://localhost:7001/api/products', {
      credentials: 'include'
    })
      .then(res => res.json())
      .then(data => setProducts(data))
      .catch(err => console.error('Error fetching products:', err));
  };

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };

  const handleInputChange = (e) => {
    setNewProduct({ ...newProduct, [e.target.name]: e.target.value });
  };

  const handleAddProduct = (e) => {
    e.preventDefault();
    fetch('http://localhost:7001/api/products/add', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      credentials: 'include',
      body: JSON.stringify(newProduct)
    })
      .then(res => {
        if (res.ok) {
          setNewProduct({ name: '', description: '', price: '' });
          fetchProducts();
        } else {
          alert('Failed to add product');
        }
      })
      .catch(err => console.error('Error adding product:', err));
  };

  const filteredProducts = products.filter(product =>
    product.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="product-table-container">
      <h1 className="page-title">Product Manager</h1>

      {/* Section 1: Add Product */}
      <section className="form-section">
        <h2>Add New Product</h2>
        <form onSubmit={handleAddProduct}>
          <input
            name="name"
            type="text"
            placeholder="Name"
            value={newProduct.name}
            onChange={handleInputChange}
            required
          />
          <input
            name="description"
            type="text"
            placeholder="Description"
            value={newProduct.description}
            onChange={handleInputChange}
            required
          />
          <input
            name="price"
            type="number"
            step="0.01"
            placeholder="Price"
            value={newProduct.price}
            onChange={handleInputChange}
            required
          />
          <button type="submit">Add</button>
        </form>
      </section>

      {/* Section 2: Search Product */}
      <section className="form-section">
        <h2>Search Products</h2>
        <input
          type="text"
          placeholder="Search by name"
          value={searchTerm}
          onChange={handleSearchChange}
        />
      </section>

      {/* Product Table */}
      <section>
        <table className="product-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Description</th>
              <th>Price ($)</th>
            </tr>
          </thead>
          <tbody>
            {filteredProducts.length > 0 ? (
              filteredProducts.map(p => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td>{p.name}</td>
                  <td>{p.description}</td>
                  <td>{p.price}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="4">No products found</td>
              </tr>
            )}
          </tbody>
        </table>
      </section>
    </div>
  );
}

export default ProductPage;
