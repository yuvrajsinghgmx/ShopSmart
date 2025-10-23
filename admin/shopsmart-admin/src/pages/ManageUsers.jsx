import Header from '../components/Header';
import { useUsers } from '../hooks/useUsers';
import { Search, LoaderCircle, AlertTriangle, ArrowUp, ArrowDown, ChevronsUpDown } from 'lucide-react';
import Modal from '../components/Modal';

const DetailItem = ({ label, value }) => (
  <div>
    <p className="text-sm text-black">{label}</p>
    <p className="text-md font-semibold">{value || 'N/A'}</p>
  </div>
);

const UserDetailsDisplay = ({ user, loading, error }) => {
  if (loading) {
    return <div className="flex justify-center items-center gap-2"><LoaderCircle className="animate-spin" /><span>Loading details...</span></div>;
  }
  if (error) {
    return <div className="text-center">{error}</div>;
  }
  if (!user) return null;

  return (
    <div className="space-y-4">
      <div className="flex items-center gap-4">
        <img src={user.profile_image || `https://ui-avatars.com/api/?name=${user.full_name}&background=random`} alt="Profile" className="w-20 h-20 rounded-full object-cover border-2 border-gray-600" />
        <div>
          <h3 className="text-xl font-bold">{user.full_name}</h3>
          <p className="text-gray-600">@{user.username}</p>
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <DetailItem label="Email" value={user.email} />
        <DetailItem label="Phone Number" value={user.phone_number} />
        <DetailItem label="Role" value={user.role} />
        <DetailItem label="Status" value={user.is_active ? 'Active' : 'Inactive'} />
        <DetailItem label="Onboarding" value={user.onboarding_completed ? 'Completed' : 'Pending'} />
        <DetailItem label="Location Radius" value={`${user.location_radius_km} km`} />
        <DetailItem label="Joined" value={new Date(user.date_joined).toLocaleString()} />
        <DetailItem label="Last Login" value={user.last_login ? new Date(user.last_login).toLocaleString() : 'Never'} />
      </div>
      <DetailItem label="Address" value={user.current_address} />
      {user.shops_owned && (
        <div>
            <h3 className="text-lg font-semibold mb-2">Shops Owned</h3>
            {user.shops_owned.length > 0 ? (
                <ul className="list-disc list-inside">
                    {user.shops_owned.map(shop => (
                        <li key={shop.id}>{shop.name} ({shop.is_approved ? 'Approved' : 'Pending'})</li>
                    ))}
                </ul>
            ) : <p>No shops owned.</p>}
        </div>
      )}
    </div>
  );
};


const ManageUsers = () => {
  const { 
    loading, error, users, setSearchTerm, handleAction,
    isModalOpen, selectedUserDetails, detailsLoading, detailsError, handleViewDetails, closeModal,
    sortConfig, requestSort,
    pagination, currentPage, setCurrentPage,
  } = useUsers();

  const getStatusClass = (status) => {
    return status === 'Active' ? 'font-semibold text-green-400' : 'font-semibold text-red-400';
  };
  
  const getSortIcon = (key) => {
    if (sortConfig.key !== key) {
      return <ChevronsUpDown size={16} className="ml-1 inline-block opacity-30" />;
    }
    return sortConfig.direction === 'asc' ? <ArrowUp size={16} className="ml-1 inline-block" /> : <ArrowDown size={16} className="ml-1 inline-block" />;
  };

  const tableHeaders = [
    { key: 'id', label: 'User ID' },
    { key: 'full_name', label: 'Full Name' },
    { key: 'email', label: 'Email' },
    { key: 'phone_number', label: 'Phone' },
    { key: 'role', label: 'Role' },
    { key: 'is_active', label: 'Status' },
    { key: 'date_joined', label: 'Date Joined' },
  ];

  const renderTableBody = () => {
    if (loading) {
      return (
        <tr>
          <td colSpan={tableHeaders.length + 1} className="text-center p-6">
            <div className="flex justify-center items-center gap-2"><LoaderCircle className="animate-spin" /><span>Loading Users...</span></div>
          </td>
        </tr>
      );
    }
    if (error) {
      return (
         <tr><td colSpan={tableHeaders.length + 1} className="text-center p-6"><div className="flex justify-center items-center gap-2 text-red-400 font-bold"><AlertTriangle /><span>Error: {error}</span></div></td></tr>
      );
    }
    if (users.length === 0) {
       return (
          <tr><td colSpan={tableHeaders.length + 1} className="text-center p-6">No users found.</td></tr>
       );
    }

    return users.map(user => (
      <tr key={user.pk} className="border-b border-gray-700">
        <td className="p-3">{user.id}</td>
        <td className="p-3">
           <span className="cursor-pointer hover:underline" onClick={() => handleViewDetails(user.pk)}>{user.full_name}</span>
        </td>
        <td className="p-3">{user.email || 'N/A'}</td>
        <td className="p-3">{user.phone_number || 'N/A'}</td>
        <td className="p-3">{user.role || 'N/A'}</td>
        <td className={`p-3 ${getStatusClass(user.status)}`}>{user.status}</td>
        <td className="p-3">{new Date(user.date_joined).toLocaleDateString()}</td>
        <td className="p-3 text-center">
          <button onClick={() => handleAction(user.pk, 'update', { is_active: !user.is_active })} className="text-black cursor-pointer px-3 py-1 rounded-md text-sm hover:opacity-80 transition-opacity mr-2">
            {user.is_active ? 'Deactivate' : 'Activate'}
          </button>
          <button onClick={() => handleAction(user.pk, 'delete')} className="text-black cursor-pointer px-3 py-1 rounded-md text-sm hover:opacity-80 transition-opacity">Delete</button>
        </td>
      </tr>
    ));
  };

  return (
    <div>
      <Header title="Manage Users" />
      <div className="mb-6 relative">
        <input
          type="text"
          placeholder="Search by Name, Email, or Phone..."
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full p-3 pl-10 rounded-lg border-2 border-gray-600 focus:outline-none"
        />
        <Search size={20} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
      </div>

      <div className="p-6 rounded-lg shadow-md overflow-x-auto">
        <table className="w-full min-w-[900px] text-left">
          <thead className="border-b border-gray-600">
            <tr>
              {tableHeaders.map(header => (
                <th key={header.key} className="p-3 cursor-pointer select-none" onClick={() => requestSort(header.key)}>
                  <div className="flex items-center gap-2">{header.label} {getSortIcon(header.key)}</div>
                </th>
              ))}
              <th className="p-3 text-center">Actions</th>
            </tr>
          </thead>
          <tbody>{renderTableBody()}</tbody>
        </table>
      </div>

      <div className="flex justify-between items-center mt-4 px-6">
        <span className="text-sm">Showing {users.length} of {pagination.count} results</span>
        <div className="flex items-center gap-2">
          <button onClick={() => setCurrentPage(currentPage - 1)} disabled={!pagination.previous} className="px-4 py-2 rounded disabled:opacity-50 disabled:cursor-not-allowed hover:bg-blue-200">Previous</button>
          <span className="px-4 py-2 font-semibold">Page {currentPage}</span>
          <button onClick={() => setCurrentPage(currentPage + 1)} disabled={!pagination.next} className="px-4 py-2 rounded disabled:opacity-50 disabled:cursor-not-allowed hover:bg-blue-200">Next</button>
        </div>
      </div>

      <Modal isOpen={isModalOpen} onClose={closeModal} title="User Details">
        <UserDetailsDisplay user={selectedUserDetails} loading={detailsLoading} error={detailsError} />
      </Modal>
    </div>
  );
};

export default ManageUsers;