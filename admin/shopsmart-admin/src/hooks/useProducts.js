import { useState, useEffect, useMemo, useCallback } from 'react';
import { getProducts, deleteProduct } from '../services/api';

const mapProductData = (product) => ({
  ...product,
  // Map backend fields to frontend component expectations
  pk: product.id, // The numeric primary key for actions
  id: product.product_id, // The string ID for display
  shopName: product.shop_name,
});

export const useProducts = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  const fetchProducts = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getProducts();
      setProducts(data.map(mapProductData));
    } catch (err) {
      setError(err.message || 'Failed to fetch products.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  const filteredProducts = useMemo(() => {
    if (!searchTerm) return products;
    return products.filter(product =>
      product.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
      product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      product.shopName.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [products, searchTerm]);

  const handleAction = async (pk, action) => {
    try {
      if (action === 'delete') {
        if (window.confirm('Are you sure you want to delete this product?')) {
          await deleteProduct(pk);
          alert('Product has been deleted!');
        } else {
          return; // User cancelled
        }
      }
      // Refetch data to show updated state
      fetchProducts();
    } catch (err) {
      alert(`Error performing action: ${err.message}`);
    }
  };

  return {
    loading,
    error,
    products: filteredProducts,
    setSearchTerm,
    handleAction,
  };
};