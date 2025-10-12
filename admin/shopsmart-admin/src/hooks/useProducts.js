import { useState, useEffect, useCallback } from 'react';
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
  const [sortConfig, setSortConfig] = useState({ key: 'created_at', direction: 'desc' });
  const [pagination, setPagination] = useState({ count: 0, next: null, previous: null });
  const [currentPage, setCurrentPage] = useState(1);

  // State for modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedProductDetails, setSelectedProductDetails] = useState(null);
  const [detailsLoading, setDetailsLoading] = useState(false);
  const [detailsError, setDetailsError] = useState(null);

  const fetchProducts = useCallback(async (search, sort, page) => {
    try {
      setLoading(true);
      setError(null);
      const params = { page };
      if (search) params.search = search;
      if (sort.key) {
        params.ordering = sort.direction === 'desc' ? `-${sort.key}` : sort.key;
      }
      const data = await getProducts(params);
      setProducts(data.results.map(mapProductData));
      setPagination({ count: data.count, next: data.links.next, previous: data.links.previous });
    } catch (err) {
      setError(err.message || 'Failed to fetch products.');
      setProducts([]); 
    } finally {
      setLoading(false);
    }
  }, []);
  
  useEffect(() => { // Back to page 1 if sort or search changes
    setCurrentPage(1);
  }, [searchTerm, sortConfig]);

  useEffect(() => {
    const handler = setTimeout(() => {
      fetchProducts(searchTerm, sortConfig, currentPage);
    }, 300); // Debounce search

    return () => {
      clearTimeout(handler);
    };
  }, [searchTerm, sortConfig, currentPage, fetchProducts]);

  const requestSort = (key) => {
    let direction = 'asc';
    if (sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
  };
  
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
      // Refetch with current settings
      fetchProducts(searchTerm, sortConfig, currentPage);
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
    products,
    setSearchTerm,
    handleAction,
    sortConfig,
    requestSort,
    isModalOpen,
    selectedProductDetails,
    detailsLoading,
    detailsError,
    handleViewDetails,
    closeModal,
    pagination,
    currentPage,
    setCurrentPage,
  };
};