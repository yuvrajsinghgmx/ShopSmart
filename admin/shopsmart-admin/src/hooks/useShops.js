import { useState, useEffect, useMemo, useCallback } from 'react';
import { getShops, updateShopStatus, deleteShop } from '../services/api';

const mapShopData = (shop) => ({
  ...shop,
  // Map backend fields to frontend component expectations
  pk: shop.id, // The numeric primary key for actions
  id: shop.shop_id, // The string ID for display
  owner: shop.owner_name,
  status: shop.is_approved ? 'Approved' : 'Pending',
});

export const useShops = () => {
  const [shops, setShops] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

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
          return; // User cancelled
        }
      } else { // 'approve' or 'reject'
        const actionVerb = action === 'approve' ? 'Approved' : 'Rejected';
        await updateShopStatus(pk, action);
        alert(`Shop has been ${actionVerb}!`);
      }
      // Refetch data to show updated state
      fetchShops();
    } catch (err) {
      alert(`Error performing action: ${err.message}`);
    }
  };

  return {
    loading,
    error,
    shops: filteredShops,
    setSearchTerm,
    handleAction,
  };
};