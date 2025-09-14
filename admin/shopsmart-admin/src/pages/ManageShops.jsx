import Header from '../components/Header';
import { useShops } from '../hooks/useShops';
import { Search, LoaderCircle, AlertTriangle } from 'lucide-react';

const ManageShops = () => {
  const { loading, error, shops, setSearchTerm, handleAction } = useShops();

  const getStatusClass = (status) => {
    const classes = {
      Approved: 'text-approved font-semibold',
      Pending: 'text-pending font-semibold',
    };
    return classes[status] || 'text-gray-400';
  };

  const renderTableBody = () => {
    if (loading) {
      return (
        <tr>
          <td colSpan="6" className="text-center p-6">
            <div className="flex justify-center items-center gap-2">
              <LoaderCircle className="animate-spin" />
              <span>Loading Shops...</span>
            </div>
          </td>
        </tr>
      );
    }

    if (error) {
      return (
         <tr>
            <td colSpan="6" className="text-center p-6 text-danger">
              <div className="flex justify-center items-center gap-2">
                <AlertTriangle />
                <span>Error: {error}</span>
              </div>
            </td>
          </tr>
      )
    }
    
    if (shops.length === 0) {
       return (
          <tr>
            <td colSpan="6" className="text-center p-6">No shops found.</td>
          </tr>
       );
    }

    return shops.map(shop => (
      <tr key={shop.pk} className="border-b border-gray-700 hover:bg-sidebar-dark">
        <td className="p-3">{shop.id}</td>
        <td className="p-3">{shop.name}</td>
        <td className="p-3">{shop.category}</td>
        <td className="p-3">{shop.owner}</td>
        <td className={`p-3 ${getStatusClass(shop.status)}`}>{shop.status}</td>
        <td className="p-3 text-center">
          <button
            onClick={() => handleAction(shop.pk, 'approve')}
            className="bg-approved text-black cursor-pointer px-3 py-1 rounded-md text-sm hover:opacity-80 transition-opacity mr-2"
          >
            Approve
          </button>
          <button
            onClick={() => handleAction(shop.pk, 'reject')}
            className="bg-pending text-black cursor-pointer px-3 py-1 rounded-md text-sm hover:opacity-80 transition-opacity mr-2"
          >
            Reject
          </button>
          <button
            onClick={() => handleAction(shop.pk, 'delete')}
            className="bg-danger text-black cursor-pointer px-3 py-1 rounded-md text-sm hover:opacity-80 transition-opacity"
          >
            Delete
          </button>
        </td>
      </tr>
    ));
  };

  return (
    <div>
      <Header title="Manage Shops" />
      <div className="mb-6 relative">
        <input
          type="text"
          placeholder="Search by Shop ID or Name..."
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full bg-card-dark text-text-light p-3 pl-10 rounded-lg border-2 border-gray-600 focus:border-accent focus:outline-none"
        />
        <Search size={20} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
      </div>

      <div className="bg-card-dark p-6 rounded-lg shadow-md overflow-x-auto">
        <table className="w-full min-w-[800px] text-left">
          <thead className="border-b border-gray-600">
            <tr>
              <th className="p-3">Shop ID</th>
              <th className="p-3">Shop Name</th>
              <th className="p-3">Category</th>
              <th className="p-3">Owner</th>
              <th className="p-3">Status</th>
              <th className="p-3 text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            {renderTableBody()}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ManageShops;