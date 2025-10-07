import { useState, useEffect, useCallback } from 'react';
import { getShops, updateShopStatus, deleteShop, getShopDetails } from '../services/api';

const mapShopData = (shop) => ({
  ...shop,
  pk: shop.id,
  id: shop.shop_id,
  owner: shop.owner_name,
  status: shop.is_approved ? 'Approved' : 'Pending',
});

export const useShops = () => {
  const [shops, setShops] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [sortConfig, setSortConfig] = useState({ key: 'created_at', direction: 'desc' });
  const [pagination, setPagination] = useState({ count: 0, next: null, previous: null });
  const [currentPage, setCurrentPage] = useState(1);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedShopDetails, setSelectedShopDetails] = useState(null);
  const [detailsLoading, setDetailsLoading] = useState(false);
  const [detailsError, setDetailsError] = useState(null);

  const fetchShops = useCallback(async (search, sort, page) => {
    try {
      setLoading(true);
      setError(null);
      const params = { page };
      if (search) {
        params.search = search;
      }
      if (sort.key) {
        params.ordering = sort.direction === 'desc' ? `-${sort.key}` : sort.key;
      }
      const data = await getShops(params);
      setShops(data.results.map(mapShopData));
      setPagination({ count: data.count, next: data.links.next, previous: data.links.previous });
    } catch (err) {
      setError(err.message || 'Failed to fetch shops.');
      setShops([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { // Back to page 1 if sort or search changes
    setCurrentPage(1);
  }, [searchTerm, sortConfig]);

  useEffect(() => {
    const handler = setTimeout(() => {
      fetchShops(searchTerm, sortConfig, currentPage);
    }, 300); // Debounce 

    return () => {
      clearTimeout(handler);
    };
  }, [searchTerm, sortConfig, currentPage, fetchShops]);

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
        if (window.confirm('Are you sure you want to delete this shop?')) {
          await deleteShop(pk);
          alert(`Shop has been deleted!`);
        } else {
          return;
        }
      } else {
        const actionVerb = action === 'approve' ? 'Approved' : 'Rejected';
        await updateShopStatus(pk, action);
        alert(`Shop has been ${actionVerb}!`);
      }
      // Refetch with current settings
      fetchShops(searchTerm, sortConfig, currentPage);
    } catch (err)
        {
      alert(`Error performing action: ${err.message}`);
    }
  };

  const handleViewDetails = useCallback(async (pk) => {
    try {
      setDetailsLoading(true);
      setDetailsError(null);
      setIsModalOpen(true);
      const data = await getShopDetails(pk);
      setSelectedShopDetails(data);
    } catch (err) {
      setDetailsError(err.message || 'Failed to fetch shop details.');
    } finally {
      setDetailsLoading(false);
    }
  }, []);

  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedShopDetails(null);
    setDetailsError(null);
  };

  return {
    loading,
    error,
    shops,
    setSearchTerm,
    handleAction,
    sortConfig,
    requestSort,
    isModalOpen,
    selectedShopDetails,
    detailsLoading,
    detailsError,
    handleViewDetails,
    closeModal,
    pagination,
    currentPage,
    setCurrentPage,
  };
};