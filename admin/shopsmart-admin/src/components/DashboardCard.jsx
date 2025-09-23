const DashboardCard = ({ title, value, icon }) => (
  <div className="flex-1 p-6 rounded-lg shadow-md">
    <div className="flex items-center">
      <div className="mr-4">
        {icon}
      </div>
      <div>
        <h2 className="text-lg text-gray-300">{title}</h2>
        <p className="text-2xl font-bold">{value}</p>
      </div>
    </div>
  </div>
);

export default DashboardCard;