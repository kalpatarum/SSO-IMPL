import React, { useEffect, useState } from 'react';
import { Container, TextField, Button, Table, TableHead, TableRow, TableCell, TableBody, Paper, Box } from '@mui/material';
import { useAuth } from '../context/AuthProvider';

export default function ProductsPage() {
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState('');
  const [newProduct, setNew] = useState({ name:'', description:'', price:'' });
  const API = import.meta.env.VITE_API_BASE_URL;
  const { user } = useAuth();

  useEffect(() => {
    if (user) fetchProducts();
  }, [user]);

  const fetchProducts = () => {
    fetch(`${API}/api/products`, { credentials:'include' })
      .then(res => res.json())
      .then(setProducts);
  };

  const add = (e) => {
    e.preventDefault();
    fetch(`${API}/api/products/add`, {
      method:'POST',
      headers:{'Content-Type':'application/json'},
      credentials:'include',
      body: JSON.stringify(newProduct),
    })
      .then(res => res.ok && (setNew({name:'',description:'',price:''}), fetchProducts()));
  };

  const filtered = products.filter(p => p.name.toLowerCase().includes(search.toLowerCase()));

  return (
    <Container sx={{ mt:3 }}>
      <Box component="form" onSubmit={add} sx={{ mb:3, display:'flex', gap:2, flexWrap:'wrap' }}>
        <TextField label="Name" name="name" value={newProduct.name} onChange={e=>setNew({...newProduct,name:e.target.value})} required />
        <TextField label="Description" name="description" value={newProduct.description} onChange={e=>setNew({...newProduct,description:e.target.value})} required />
        <TextField label="Price" name="price" type="number" inputProps={{ step: "0.01" }} value={newProduct.price} onChange={e=>setNew({...newProduct,price:e.target.value})} required />
        <Button type="submit" variant="contained">Add</Button>
      </Box>
      <TextField label="Search" value={search} onChange={e=>setSearch(e.target.value)} sx={{ mb:2 }} fullWidth />
      <Paper>
        <Table>
          <TableHead><TableRow>
            <TableCell>ID</TableCell><TableCell>Name</TableCell><TableCell>Description</TableCell><TableCell>Price</TableCell>
          </TableRow></TableHead>
          <TableBody>
            {filtered.length ? filtered.map(p=>(
              <TableRow key={p.id}>
                <TableCell>{p.id}</TableCell>
                <TableCell>{p.name}</TableCell>
                <TableCell>{p.description}</TableCell>
                <TableCell>{p.price}</TableCell>
              </TableRow>
            )) : (
              <TableRow><TableCell colSpan={4} align="center">No products found</TableCell></TableRow>
            )}
          </TableBody>
        </Table>
      </Paper>
    </Container>
  );
}
