import Header from '../components/Header';
import { Rocket } from 'lucide-react';

const ComingSoon = () => (
  <div>
    <Header title="Coming Soon" />
    <div className="mt-20 flex flex-col items-center justify-center text-center">
      <Rocket size={64} className="text-accent mb-4" />
      <h1 className="text-4xl font-bold mb-2">Something Awesome is on the Way!</h1>
      <p className="text-lg text-gray-400">We're working hard to bring this feature to you.</p>
    </div>
  </div>
);

export default ComingSoon;