import { useState } from 'react';
import Header from '../components/Header';
import { useShops } from '../hooks/useShops';
import { Search, LoaderCircle, AlertTriangle, ArrowUp, ArrowDown, ChevronsUpDown } from 'lucide-react';
import Modal from '../components/Modal';

const DetailItem = ({ label, value }) => (
  <div>
    <p className="text-sm text-black">{label}</p>
    <p className="text-md font-semibold">{value || 'N/A'}</p>
  </div>
);

const FullscreenImageViewer = ({ src, onClose }) => (
  <div 
    className="fixed inset-0 bg-black bg-opacity-80 z-[60] flex justify-center items-center p-4 cursor-zoom-out" 
    onClick={onClose}
  >
    <img 
      src={src} 
      alt="Fullscreen view" 
      className="max-w-full max-h-full object-contain"
      onClick={(e) => e.stopPropagation()} 
    />
  </div>
);

const ShopDetailsDisplay = ({ shop, loading, error, onImageClick }) => {
  if (loading) {
    return <div className="flex justify-center items-center gap-2"><LoaderCircle className="animate-spin" /><span>Loading details...</span></div>;
  }
  if (error) {
    return <div className="text-red-300 text-center">{error}</div>;
  }
  if (!shop) return null;

  return (
    <div className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <DetailItem label="Shop ID" value={shop.shop_id} />
        <DetailItem label="Shop Name" value={shop.name} />
        <DetailItem label="Owner" value={shop.owner_name} />
        <DetailItem label="Category" value={shop.category} />
        <DetailItem label="Shop Type" value={shop.shop_type} />
        <DetailItem label="Products" value={`${shop.products_count} items`} />
        <DetailItem label="Status" value={shop.is_approved ? 'Approved' : 'Pending'} />
        <DetailItem label="Average Rating" value={`${shop.average_rating} (${shop.reviews_count} reviews)`} />
      </div>
      <DetailItem label="Address" value={shop.address} />
      <DetailItem label="Description" value={shop.description} />
      <DetailItem label="Created At" value={new Date(shop.created_at).toLocaleString()} />
      <div>
        <h3 className="text-lg font-semibold mb-2">Shop Images</h3>
        <div className="flex flex-wrap gap-4">
          {shop.images && shop.images.length > 0 ? shop.images.map((img, i) => (
            <img 
              key={i} 
              src={img} 
              alt={`Shop image ${i + 1}`} 
              className="w-32 h-32 object-cover rounded-md border-2 border-gray-600 cursor-zoom-in transition-transform hover:scale-105"
              onClick={() => onImageClick(img)}
            />
          )) : <p className="text-gray-400">No shop images provided.</p>}
        </div>
      </div>
      <div>
        <h3 className="text-lg font-semibold mb-2">Document Images</h3>
        <div className="flex flex-wrap gap-4">
          {shop.document_images && shop.document_images.length > 0 ? shop.document_images.map((img, i) => (
            <img 
              key={i} 
              src={img} 
              alt={`Document image ${i + 1}`} 
              className="w-32 h-32 object-cover rounded-md border-2 border-gray-600 cursor-zoom-in transition-transform hover:scale-105"
              onClick={() => onImageClick(img)}
            />
          )) : <p className="text-gray-400">No document images provided.</p>}
        </div>
      </div>
    </div>
  );
};

const ManageShops = () => {
  const { 
    loading, error, shops, setSearchTerm, handleAction,
    isModalOpen, selectedShopDetails, detailsLoading, detailsError, handleViewDetails, closeModal,
    sortConfig, requestSort,
    pagination, currentPage, setCurrentPage,
  } = useShops();
  
  const [fullscreenImage, setFullscreenImage] = useState(null);

  const getStatusClass = (status) => {
    return status === 'Approved' ? 'font-semibold text-green-400' : 'font-semibold text-red-400';
  };
  
  const getSortIcon = (key) => {
    if (sortConfig.key !== key) {
      return <ChevronsUpDown size={16} className="ml-1 inline-block opacity-30" />;
    }
    return sortConfig.direction === 'asc' ? <ArrowUp size={16} className="ml-1 inline-block" /> : <ArrowDown size={16} className="ml-1 inline-block" />;
  };

  const tableHeaders = [
    { key: 'shop_id', label: 'Shop ID' },
    { key: 'name', label: 'Shop Name' },
    { key: 'category', label: 'Category' },
    { key: 'owner__full_name', label: 'Owner' },
    { key: 'is_approved', label: 'Status' },
    { key: 'created_at', label: 'Created At'},
  ];

  const renderTableBody = () => {
    if (loading) {
      return (
        <tr>
          <td colSpan={tableHeaders.length + 1} className="text-center p-6">
            <div className="flex justify-center items-center gap-2">
              <LoaderCircle className="animate-spin" /> <span>Loading Shops...</span>
            </div>
          </td>
        </tr>
      );
    }

    if (error) {
      return (
         <tr>
            <td colSpan={tableHeaders.length + 1} className="text-center p-6">
              <div className="flex justify-center items-center gap-2 text-red-400 font-bold">
                <AlertTriangle /><span>Error: {error}</span>
              </div>
            </td>
          </tr>
      );
    }
    
    if (shops.length === 0) {
       return (
          <tr>
            <td colSpan={tableHeaders.length + 1} className="text-center p-6">No shops found.</td>
          </tr>
       );
    }

    return shops.map(shop => (
      <tr key={shop.pk} className="border-b border-gray-700">
        <td className="p-3">{shop.id}</td>
        <td className="p-3">
          <span className="cursor-pointer hover:underline" onClick={() => handleViewDetails(shop.pk)}>
            {shop.name}
          </span>
        </td>
        <td className="p-3">{shop.category}</td>
        <td className="p-3">{shop.owner}</td>
        <td className={`p-3 ${getStatusClass(shop.status)}`}>{shop.status}</td>
        <td className="p-3">{new Date(shop.created_at).toLocaleDateString()}</td>
        <td className="p-3 text-center">
          <button onClick={() => handleAction(shop.pk, 'approve')} className="text-black cursor-pointer px-3 py-1 rounded-md text-sm hover:opacity-80 transition-opacity mr-2">Approve</button>
          <button onClick={() => handleAction(shop.pk, 'reject')} className="text-black cursor-pointer px-3 py-1 rounded-md text-sm hover:opacity-80 transition-opacity mr-2">Reject</button>
          <button onClick={() => handleAction(shop.pk, 'delete')} className="text-black cursor-pointer px-3 py-1 rounded-md text-sm hover:opacity-80 transition-opacity">Delete</button>
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
          placeholder="Search by Shop ID, Name, Owner or Category..."
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
                  <div className="flex items-center gap-2">
                    {header.label} {getSortIcon(header.key)}
                  </div>
                </th>
              ))}
              <th className="p-3 text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            {renderTableBody()}
          </tbody>
        </table>
      </div>

      <div className="flex justify-between items-center mt-4 px-6">
        <span className="text-sm">Showing {shops.length} of {pagination.count} results</span>
        <div className="flex items-center gap-2">
          <button 
            onClick={() => setCurrentPage(currentPage - 1)}
            disabled={!pagination.previous}
            className="px-4 py-2 rounded disabled:opacity-50 disabled:cursor-not-allowed hover:bg-blue-200"
          >
            Previous
          </button>
          <span className="px-4 py-2 font-semibold">Page {currentPage}</span>
          <button
            onClick={() => setCurrentPage(currentPage + 1)}
            disabled={!pagination.next}
            className="px-4 py-2 rounded disabled:opacity-50 disabled:cursor-not-allowed hover:bg-blue-200"
          >
            Next
          </button>
        </div>
      </div>

      <Modal isOpen={isModalOpen} onClose={closeModal} title="Shop Details">
        <ShopDetailsDisplay 
          shop={selectedShopDetails} 
          loading={detailsLoading} 
          error={detailsError} 
          onImageClick={setFullscreenImage}
        />
      </Modal>

      {fullscreenImage && <FullscreenImageViewer src={fullscreenImage} onClose={() => setFullscreenImage(null)} />}
    </div>
  );
};

export default ManageShops;