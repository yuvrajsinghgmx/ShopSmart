import { useState, useEffect, useMemo, useCallback } from 'react';
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

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedShopDetails, setSelectedShopDetails] = useState(null);
  const [detailsLoading, setDetailsLoading] = useState(false);
  const [detailsError, setDetailsError] = useState(null);

  const fetchShops = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getShops();
      setShops(data.map(mapShopData));
    } catch (err) {
      setError(err.message || 'Failed to fetch shops.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchShops();
  }, [fetchShops]);

  const filteredShops = useMemo(() => {
    if (!searchTerm) return shops;
    return shops.filter(shop =>
      shop.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
      shop.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [shops, searchTerm]);

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
      fetchShops();
    } catch (err) {
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
    shops: filteredShops,
    setSearchTerm,
    handleAction,
    isModalOpen,
    selectedShopDetails,
    detailsLoading,
    detailsError,
    handleViewDetails,
    closeModal,
  };
};