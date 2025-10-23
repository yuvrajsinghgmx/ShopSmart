import { useState, useEffect, useCallback } from 'react';
import { getUsers, deleteUser, getUserDetails, updateUser } from '../services/api';

const mapUserData = (user) => ({
  ...user,
  pk: user.id,
  status: user.is_active ? 'Active' : 'Inactive',
});

export const useUsers = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [sortConfig, setSortConfig] = useState({ key: 'date_joined', direction: 'desc' });
  const [pagination, setPagination] = useState({ count: 0, next: null, previous: null });
  const [currentPage, setCurrentPage] = useState(1);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedUserDetails, setSelectedUserDetails] = useState(null);
  const [detailsLoading, setDetailsLoading] = useState(false);
  const [detailsError, setDetailsError] = useState(null);

  const fetchUsers = useCallback(async (search, sort, page) => {
    try {
      setLoading(true);
      setError(null);
      const params = { page };
      if (search) params.search = search;
      if (sort.key) {
        params.ordering = sort.direction === 'desc' ? `-${sort.key}` : sort.key;
      }
      const data = await getUsers(params);
      setUsers(data.results.map(mapUserData));
      setPagination({ count: data.count, next: data.links.next, previous: data.links.previous });
    } catch (err) {
      setError(err.message || 'Failed to fetch users.');
      setUsers([]); 
    } finally {
      setLoading(false);
    }
  }, []);
  
  useEffect(() => {
    setCurrentPage(1);
  }, [searchTerm, sortConfig]);

  useEffect(() => {
    const handler = setTimeout(() => {
      fetchUsers(searchTerm, sortConfig, currentPage);
    }, 300);

    return () => {
      clearTimeout(handler);
    };
  }, [searchTerm, sortConfig, currentPage, fetchUsers]);

  const requestSort = (key) => {
    let direction = 'asc';
    if (sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
  };
  
  const handleAction = async (pk, action, payload = {}) => {
    try {
      if (action === 'delete') {
        if (window.confirm('Are you sure you want to delete this user? This is irreversible.')) {
          await deleteUser(pk);
          alert('User has been deleted!');
        } else {
          return;
        }
      } else if (action === 'update') {
        await updateUser(pk, payload);
        alert('User has been updated!');
      }
      fetchUsers(searchTerm, sortConfig, currentPage);
    } catch (err) {
      alert(`Error performing action: ${err.message}`);
    }
  };
  
  const handleViewDetails = useCallback(async (pk) => {
    try {
      setDetailsLoading(true);
      setDetailsError(null);
      setIsModalOpen(true);
      const data = await getUserDetails(pk);
      setSelectedUserDetails(data);
    } catch (err) {
      setDetailsError(err.message || 'Failed to fetch user details.');
    } finally {
      setDetailsLoading(false);
    }
  }, []);

  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedUserDetails(null);
    setDetailsError(null);
  };

  return {
    loading,
    error,
    users,
    setSearchTerm,
    handleAction,
    sortConfig,
    requestSort,
    isModalOpen,
    selectedUserDetails,
    detailsLoading,
    detailsError,
    handleViewDetails,
    closeModal,
    pagination,
    currentPage,
    setCurrentPage,
  };
};