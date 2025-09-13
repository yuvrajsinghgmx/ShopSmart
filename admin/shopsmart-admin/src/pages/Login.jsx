import { useState } from 'react';
import { login } from '../services/api';

const Login = ({ onLoginSuccess }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await login(email, password);
      onLoginSuccess();
    } catch (err) {
      setError(err.message || 'Login failed. Please check your credentials.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-background-dark">
      <div className="w-full max-w-md p-8 space-y-6 bg-card-dark rounded-lg shadow-lg">
        <h1 className="text-3xl font-bold text-center text-text-light">ShopSmart Admin</h1>
        <h2 className="text-xl text-center text-gray-400">Sign in to your account</h2>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-300">
              Email
            </label>
            <input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full px-3 py-2 mt-1 text-text-light bg-background-dark border border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-accent"
            />
          </div>
          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-300">
              Password
            </label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full px-3 py-2 mt-1 text-text-light bg-background-dark border border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-accent"
            />
          </div>
          {error && <p className="text-sm text-center text-danger">{error}</p>}
          <div>
            <button
              type="submit"
              disabled={loading}
              className="w-full px-4 py-2 font-bold border-2 border-blue-200 text-black rounded-full hover:bg-blue-100 focus:outline-none focus:ring-2 disabled:opacity-50"
            >
              {loading ? 'Signing in...' : 'Sign In'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;