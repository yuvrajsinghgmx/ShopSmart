import Header from '../components/Header';
import DashboardCard from '../components/DashboardCard';
import { Store, Users, Layers, Hourglass } from 'lucide-react';

const Dashboard = () => (
  <div>
    <Header title="Dashboard" />
    <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
      <DashboardCard title="Total Shops" value="142" icon={<Store size={32}/>} />
      <DashboardCard title="Active Users" value="2,560" icon={<Users size={32}/>} />
      <DashboardCard title="Categories" value="17" icon={<Layers size={32}/>} />
      <DashboardCard title="Pending Approvals" value="4" icon={<Hourglass size={32}/>} />
    </section>
    
    <section className="p-6 rounded-lg shadow-md">
      <h2 className="text-xl font-semibold mb-4">Recent Activity</h2>
      <div className="overflow-x-auto">
        <table className="w-full text-left">
          <thead className="border-b border-gray-600">
            <tr>
              <th className="p-3">Shop ID</th>
              <th className="p-3">Activity</th>
              <th className="p-3">Date</th>
              <th className="p-3">Status</th>
            </tr>
          </thead>
          <tbody>
            <tr className="border-b border-gray-700">
              <td className="p-3">#S102</td>
              <td className="p-3">New Shop Registration</td>
              <td className="p-3">2024-08-28</td>
              <td className="p-3">Pending</td>
            </tr>
            <tr className="border-b border-gray-700">
              <td className="p-3">#S101</td>
              <td className="p-3">Profile Update</td>
              <td className="p-3">2024-08-27</td>
              <td className="p-3">Approved</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
);

export default Dashboard;