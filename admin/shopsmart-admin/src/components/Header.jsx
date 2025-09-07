import React from 'react';
import { UserCircle } from 'lucide-react';

const Header = ({ title }) => {
  return (
    <header className="flex justify-between items-center mb-6">
      <h1 className="text-3xl font-bold">{title}</h1>
      <div className="flex items-center gap-3">
        <span className="text-gray-300">Admin</span>
        <UserCircle size={32} className="text-gray-400" />
      </div>
    </header>
  );
};

export default Header;