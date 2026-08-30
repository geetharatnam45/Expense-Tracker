import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Register() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await register(name, email, password);
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Could not create your account.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <p className="auth-eyebrow">Expense Tracker</p>
        <h1>Open an account</h1>
        <p className="auth-subtext">Start keeping an honest ledger of income and spending.</p>

        {error && <div className="auth-error">{error}</div>}

        <label>
          Name
          <input type="text" value={name} required
                 onChange={(e) => setName(e.target.value)} placeholder="Your name" />
        </label>

        <label>
          Email
          <input type="email" value={email} required
                 onChange={(e) => setEmail(e.target.value)} placeholder="you@example.com" />
        </label>

        <label>
          Password
          <input type="password" value={password} required minLength={6}
                 onChange={(e) => setPassword(e.target.value)} placeholder="At least 6 characters" />
        </label>

        <button type="submit" disabled={loading}>
          {loading ? 'Creating account…' : 'Create account'}
        </button>

        <p className="auth-switch">
          Already have an account? <Link to="/login">Log in</Link>
        </p>
      </form>
    </div>
  );
}
