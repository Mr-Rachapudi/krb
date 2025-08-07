import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Users, CreditCard, DollarSign, TrendingUp } from 'lucide-react';
import { apiService } from '../services/api';
import { Employee } from '../types';

interface DashboardProps {
  user: Employee;
}

interface DashboardStats {
  customerCount: number;
  accountCount: number;
  totalBalance: number;
  employeeCount: number;
}

export default function Dashboard({ user }: DashboardProps) {
  const [stats, setStats] = useState<DashboardStats>({
    customerCount: 0,
    accountCount: 0,
    totalBalance: 0,
    employeeCount: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [customerCount, employeeCount, accountStats] = await Promise.all([
          apiService.getCustomerCount(),
          apiService.getEmployeeCount(),
          apiService.getAccountStats()
        ]);

        setStats({
          customerCount,
          employeeCount,
          accountCount: accountStats[0],
          totalBalance: accountStats[1]
        });
      } catch (error) {
        console.error('Failed to fetch dashboard stats:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-lg">Loading dashboard...</div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-600">Welcome back, {user.firstName}!</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Customers</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.customerCount}</div>
            <p className="text-xs text-muted-foreground">
              Active customer accounts
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Accounts</CardTitle>
            <CreditCard className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.accountCount}</div>
            <p className="text-xs text-muted-foreground">
              Bank accounts managed
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Balance</CardTitle>
            <DollarSign className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              ${stats.totalBalance.toLocaleString()}
            </div>
            <p className="text-xs text-muted-foreground">
              Combined account balances
            </p>
          </CardContent>
        </Card>

        {user.role === 'ADMIN' && (
          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Employees</CardTitle>
              <TrendingUp className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stats.employeeCount}</div>
              <p className="text-xs text-muted-foreground">
                Active staff members
              </p>
            </CardContent>
          </Card>
        )}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
            <CardDescription>Common tasks you can perform</CardDescription>
          </CardHeader>
          <CardContent className="space-y-2">
            <div className="flex flex-col space-y-2">
              <a href="/customers" className="text-blue-600 hover:text-blue-800">
                → Create New Customer
              </a>
              <a href="/accounts" className="text-blue-600 hover:text-blue-800">
                → Open New Account
              </a>
              <a href="/relationships" className="text-blue-600 hover:text-blue-800">
                → View Customer Relationships
              </a>
              {user.role === 'ADMIN' && (
                <a href="/admin" className="text-blue-600 hover:text-blue-800">
                  → Manage Employees
                </a>
              )}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Recent Activity</CardTitle>
            <CardDescription>Your recent actions in the system</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="text-sm text-gray-600">
              <p>Activity tracking will be implemented in future updates.</p>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
