import { useState, useEffect, useMemo, useCallback } from 'react';
import { getProducts, deleteProduct, getProductDetails } from '../services/api';

const mapProductData = (product) => ({
  ...product,
  pk: product.id,
  id: product.product_id,
  shopName: product.shop_name,
});

export const useProducts = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  // State for modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedProductDetails, setSelectedProductDetails] = useState(null);
  const [detailsLoading, setDetailsLoading] = useState(false);
  const [detailsError, setDetailsError] = useState(null);

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
          return;
        }
      }
      fetchProducts();
    } catch (err) {
      alert(`Error performing action: ${err.message}`);
    }
  };
  
  const handleViewDetails = useCallback(async (pk) => {
    try {
      setDetailsLoading(true);
      setDetailsError(null);
      setIsModalOpen(true);
      const data = await getProductDetails(pk);
      setSelectedProductDetails(data);
    } catch (err) {
      setDetailsError(err.message || 'Failed to fetch product details.');
    } finally {
      setDetailsLoading(false);
    }
  }, []);

  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedProductDetails(null);
    setDetailsError(null);
  };

  return {
    loading,
    error,
    products: filteredProducts,
    setSearchTerm,
    handleAction,
    isModalOpen,
    selectedProductDetails,
    detailsLoading,
    detailsError,
    handleViewDetails,
    closeModal,
  };
};