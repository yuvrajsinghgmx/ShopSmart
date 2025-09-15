import { useState } from 'react';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import ManageShops from './pages/ManageShops';
import ComingSoon from './pages/ComingSoon';

function App() {
  const [activePage, setActivePage] = useState('Dashboard');

  const renderPage = () => {
    switch (activePage) {
      case 'Dashboard':
        return <Dashboard />;
      case 'Manage Shops':
        return <ManageShops />;
      case 'Manage Products':
      case 'Users':
      case 'Reports':
      case 'Settings':
        return <ComingSoon />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <div className="flex min-h-screen">
      <Sidebar activePage={activePage} setActivePage={setActivePage} />
      <main className="flex-1 p-6 lg:p-8">
        {renderPage()}
      </main>
    </div>
  );
}

export default App;
