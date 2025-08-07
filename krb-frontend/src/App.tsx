import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import CustomerManagement from './components/CustomerManagement';
import AccountManagement from './components/AccountManagement';
import CustomerAccountRelationship from './components/CustomerAccountRelationship';
import AdminPanel from './components/AdminPanel';
import Navbar from './components/Navbar';
import { Employee } from './types';

function App() {
  const [currentUser, setCurrentUser] = useState<Employee | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      setCurrentUser(JSON.parse(savedUser));
    }
    setLoading(false);
  }, []);

  const handleLogin = (user: Employee) => {
    setCurrentUser(user);
    localStorage.setItem('currentUser', JSON.stringify(user));
  };

  const handleLogout = () => {
    setCurrentUser(null);
    localStorage.removeItem('currentUser');
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-lg">Loading...</div>
      </div>
    );
  }

  if (!currentUser) {
    return <Login onLogin={handleLogin} />;
  }

  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        <Navbar user={currentUser} onLogout={handleLogout} />
        <main className="container mx-auto px-4 py-8">
          <Routes>
            <Route path="/" element={<Dashboard user={currentUser} />} />
            <Route path="/customers" element={<CustomerManagement user={currentUser} />} />
            <Route path="/accounts" element={<AccountManagement user={currentUser} />} />
            <Route path="/relationships" element={<CustomerAccountRelationship />} />
            {currentUser.role === 'ADMIN' && (
              <Route path="/admin" element={<AdminPanel />} />
            )}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
