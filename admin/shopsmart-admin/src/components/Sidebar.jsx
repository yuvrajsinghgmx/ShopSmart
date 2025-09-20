import { LayoutDashboard, Store, Package, Users, BarChart2, Settings, LogOut } from 'lucide-react';

const navItems = [
  { name: 'Dashboard', icon: <LayoutDashboard size={20} /> },
  { name: 'Manage Shops', icon: <Store size={20} /> },
  { name: 'Manage Products', icon: <Package size={20} /> },
  { name: 'Users', icon: <Users size={20} /> },
  { name: 'Reports', icon: <BarChart2 size={20} /> },
  { name: 'Settings', icon: <Settings size={20} /> },
];

const NavLink = ({ name, icon, isActive, onClick }) => (
  <a
    href="#"
    onClick={(e) => {
      e.preventDefault();
      onClick(name);
    }}
    className={`flex items-center gap-3 px-3 py-2 rounded-md transition-colors cursor-pointer ${
      isActive
        ? 'font-semibold bg-blue-300'
        : 'hover:bg-blue-200'
    }`}
  >
    {icon}<span>{name}</span>
  </a>
);

const Sidebar = ({ activePage, setActivePage, onLogout }) => (
  <aside className="w-60 bg-sidebar-dark h-screen p-4 flex flex-col">
    <div>
      <div className="text-2xl font-bold mb-8">ShopSmart Admin</div>
      <nav className="flex flex-col gap-2">
        {navItems.map((item) => (
          <NavLink 
            key={item.name} 
            name={item.name} 
            icon={item.icon} 
            isActive={activePage === item.name} 
            onClick={setActivePage} 
          />
        ))}
      </nav>
    </div>
    <div className="mt-auto">
      <a
        href="#"
        onClick={(e) => { e.preventDefault(); onLogout(); }}
        className="flex items-center gap-3 px-3 py-2 rounded-md transition-colors cursor-pointer hover:bg-gray-700"
      >
        <LogOut size={20} /><span>Logout</span>
      </a>
    </div>
  </aside>
);

export default Sidebar;