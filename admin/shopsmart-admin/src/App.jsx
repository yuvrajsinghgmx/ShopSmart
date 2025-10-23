import { useState } from 'react';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import ManageShops from './pages/ManageShops';
import ManageProducts from './pages/ManageProducts';
import ManageUsers from './pages/ManageUsers';
import ComingSoon from './pages/ComingSoon';
import Login from './pages/Login';
import { logout } from './services/api';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('admin_token'));
  const [activePage, setActivePage] = useState('Dashboard');

  const handleLoginSuccess = () => {
    setIsAuthenticated(true);
    setActivePage('Dashboard');
  };

  const handleLogout = () => {
    logout();
    setIsAuthenticated(false);
  };

  if (!isAuthenticated) {
    return <Login onLoginSuccess={handleLoginSuccess} />;
  }

  const renderPage = () => {
    switch (activePage) {
      case 'Dashboard':
        return <Dashboard />;
      case 'Manage Shops':
        return <ManageShops />;
      case 'Manage Products':
        return <ManageProducts />;
      case 'Users':
        return <ManageUsers />;
      case 'Reports':
      case 'Settings':
        return <ComingSoon />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <div className="flex min-h-screen">
      <Sidebar activePage={activePage} setActivePage={setActivePage} onLogout={handleLogout} />
      <main className="flex-1 p-6 lg:p-8">
        {renderPage()}
      </main>
    </div>
  );
}

export default App;