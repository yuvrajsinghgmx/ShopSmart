import Header from '../components/Header';
import { useProducts } from '../hooks/useProducts';
import { Search, LoaderCircle, AlertTriangle } from 'lucide-react';

const ManageProducts = () => {
  const { loading, error, products, setSearchTerm, handleAction } = useProducts();

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
            <td colSpan="7" className="text-center p-6 text-danger">
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
      <tr key={product.pk} className="border-b border-gray-700 hover:bg-sidebar-dark">
        <td className="p-3">{product.id}</td>
        <td className="p-3">{product.name}</td>
        <td className="p-3">{product.shopName}</td>
        <td className="p-3">₹{product.price}</td>
        <td className="p-3">{product.category}</td>
        <td className="p-3">{product.stock_quantity}</td>
        <td className="p-3 text-center">
          <button
            onClick={() => handleAction(product.pk, 'delete')}
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
      <Header title="Manage Products" />
      <div className="mb-6 relative">
        <input
          type="text"
          placeholder="Search by Product ID, Name, or Shop..."
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full bg-card-dark text-text-light p-3 pl-10 rounded-lg border-2 border-gray-600 focus:border-accent focus:outline-none"
        />
        <Search size={20} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
      </div>

      <div className="bg-card-dark p-6 rounded-lg shadow-md overflow-x-auto">
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
    </div>
  );
};

export default ManageProducts;