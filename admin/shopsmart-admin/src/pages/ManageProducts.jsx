import { useState } from 'react';
import Header from '../components/Header';
import { useProducts } from '../hooks/useProducts';
import { Search, LoaderCircle, AlertTriangle } from 'lucide-react';
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

const ProductDetailsDisplay = ({ product, loading, error, onImageClick }) => {
  if (loading) {
    return <div className="flex justify-center items-center gap-2"><LoaderCircle className="animate-spin" /><span>Loading details...</span></div>;
  }
  if (error) {
    return <div className="text-center">{error}</div>;
  }
  if (!product) return null;

  return (
    <div className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <DetailItem label="Product ID" value={product.product_id} />
        <DetailItem label="Product Name" value={product.name} />
        <DetailItem label="Shop" value={`${product.shop_details.name} (ID: ${product.shop_details.shop_id})`} />
        <DetailItem label="Price" value={`₹${product.price}`} />
        <DetailItem label="Category" value={product.category} />
        <DetailItem label="Product Type" value={product.product_type} />
        <DetailItem label="Stock Quantity" value={product.stock_quantity} />
        <DetailItem label="Average Rating" value={`${product.average_rating} (${product.reviews_count} reviews)`} />
      </div>
      <DetailItem label="Description" value={product.description} />
      <DetailItem label="Created At" value={new Date(product.created_at).toLocaleString()} />
      <div>
        <h3 className="text-lg font-semibold mb-2">Images</h3>
        <div className="flex flex-wrap gap-4">
          {product.images.length > 0 ? product.images.map((img, i) => (
            <img 
              key={i} 
              src={img} 
              alt={`Product image ${i + 1}`} 
              className="w-32 h-32 object-cover rounded-md border-2 border-gray-600 cursor-zoom-in transition-transform hover:scale-105"
              onClick={() => onImageClick(img)}
            />
          )) : <p className="text-gray-400">No images provided.</p>}
        </div>
      </div>
    </div>
  );
};


const ManageProducts = () => {
  const { 
    loading, error, products, setSearchTerm, handleAction,
    isModalOpen, selectedProductDetails, detailsLoading, detailsError, handleViewDetails, closeModal 
  } = useProducts();

  const [fullscreenImage, setFullscreenImage] = useState(null);

  const renderTableBody = () => {
    if (loading) {
      return (
        <tr>
          <td colSpan="7" className="text-center p-6">
            <div className="flex justify-center items-center gap-2">
              <LoaderCircle className="animate-spin" />
              <span>Loading Products...</span>
            </div>
          </td>
        </tr>
      );
    }

    if (error) {
      return (
         <tr>
            <td colSpan="7" className="text-center p-6">
              <div className="flex justify-center items-center gap-2">
                <AlertTriangle />
                <span>Error: {error}</span>
              </div>
            </td>
          </tr>
      );
    }
    
    if (products.length === 0) {
       return (
          <tr>
            <td colSpan="7" className="text-center p-6">No products found.</td>
          </tr>
       );
    }

    return products.map(product => (
      <tr key={product.pk} className="border-b border-gray-700">
        <td className="p-3">{product.id}</td>
        <td className="p-3">
           <span 
            className="cursor-pointer hover:underline"
            onClick={() => handleViewDetails(product.pk)}
          >
            {product.name}
          </span>
        </td>
        <td className="p-3">{product.shopName}</td>
        <td className="p-3">₹{product.price}</td>
        <td className="p-3">{product.category}</td>
        <td className="p-3">{product.stock_quantity}</td>
        <td className="p-3 text-center">
          <button
            onClick={() => handleAction(product.pk, 'delete')}
            className="text-black cursor-pointer px-3 py-1 rounded-md text-sm hover:opacity-80 transition-opacity"
          >
            Delete
          </button>
        </td>
      </tr>
    ));
  };

  return (
    <div>
      <Header title="Manage Products" />
      <div className="mb-6 relative">
        <input
          type="text"
          placeholder="Search by Product ID, Name, or Shop..."
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full p-3 pl-10 rounded-lg border-2 border-gray-600 focus:outline-none"
        />
        <Search size={20} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
      </div>

      <div className="p-6 rounded-lg shadow-md overflow-x-auto">
        <table className="w-full min-w-[900px] text-left">
          <thead className="border-b border-gray-600">
            <tr>
              <th className="p-3">Product ID</th>
              <th className="p-3">Product Name</th>
              <th className="p-3">Shop Name</th>
              <th className="p-3">Price</th>
              <th className="p-3">Category</th>
              <th className="p-3">Stock</th>
              <th className="p-3 text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            {renderTableBody()}
          </tbody>
        </table>
      </div>
      <Modal isOpen={isModalOpen} onClose={closeModal} title="Product Details">
        <ProductDetailsDisplay 
          product={selectedProductDetails} 
          loading={detailsLoading} 
          error={detailsError}
          onImageClick={setFullscreenImage}
        />
      </Modal>
      {fullscreenImage && <FullscreenImageViewer src={fullscreenImage} onClose={() => setFullscreenImage(null)} />}
    </div>
  );
};

export default ManageProducts;