import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from './ui/table';
import { Input } from './ui/input';
import { Button } from './ui/button';
import { Search, Users, CreditCard } from 'lucide-react';
import { apiService } from '../services/api';
import { Customer, Account } from '../types';

export default function CustomerAccountRelationship() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    fetchCustomersWithAccounts();
  }, []);

  const fetchCustomersWithAccounts = async () => {
    try {
      const data = await apiService.getCustomersWithAccounts();
      setCustomers(data);
    } catch (error) {
      setError('Failed to fetch customer relationships');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      fetchCustomersWithAccounts();
      return;
    }

    try {
      const data = await apiService.searchCustomers(searchTerm);
      const customersWithAccounts = await Promise.all(
        data.map(async (customer: Customer) => {
          try {
            const accounts = await apiService.getAccountsByCustomer(customer.id);
            return { ...customer, accounts };
          } catch {
            return { ...customer, accounts: [] };
          }
        })
      );
      setCustomers(customersWithAccounts);
    } catch (error) {
      setError('Search failed');
    }
  };

  const getAccountTypeLabel = (type: string) => {
    return type.replace('_', ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  };

  const getTotalBalance = (accounts: Account[] = []) => {
    if (!accounts || !Array.isArray(accounts)) return 0;
    return accounts.reduce((total, account) => total + account.balance, 0);
  };

  if (loading) {
    return <div className="flex items-center justify-center h-64">Loading relationships...</div>;
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Customer-Account Relationships</h1>
        <p className="text-gray-600">View the relationship between customers and their accounts</p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Search Customers</CardTitle>
          <CardDescription>Find customers and their associated accounts</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex space-x-2">
            <Input
              placeholder="Search customers..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            />
            <Button onClick={handleSearch}>
              <Search className="h-4 w-4 mr-2" />
              Search
            </Button>
          </div>
        </CardContent>
      </Card>

      {error && (
        <Card>
          <CardContent className="pt-6">
            <div className="text-red-600">{error}</div>
          </CardContent>
        </Card>
      )}

      <div className="grid gap-6">
        {customers.map((customer) => (
          <Card key={customer.id}>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle className="flex items-center space-x-2">
                    <Users className="h-5 w-5" />
                    <span>{customer.firstName} {customer.lastName}</span>
                  </CardTitle>
                  <CardDescription>
                    {customer.email} • {customer.phoneNumber}
                  </CardDescription>
                </div>
                <div className="text-right">
                  <div className="text-sm text-gray-600">Total Accounts</div>
                  <div className="text-2xl font-bold">{customer.accounts?.length || 0}</div>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                  <div>
                    <span className="font-medium">Customer ID:</span> {customer.id}
                  </div>
                  <div>
                    <span className="font-medium">Date of Birth:</span> {new Date(customer.dateOfBirth).toLocaleDateString()}
                  </div>
                  <div>
                    <span className="font-medium">Created:</span> {new Date(customer.createdAt).toLocaleDateString()}
                  </div>
                  <div>
                    <span className="font-medium">Address:</span> {customer.address}
                  </div>
                  <div>
                    <span className="font-medium">Created By:</span> {customer.createdByEmployeeName}
                  </div>
                  <div>
                    <span className="font-medium">Total Balance:</span> ${getTotalBalance(customer.accounts).toLocaleString()}
                  </div>
                </div>

                {customer.accounts && customer.accounts.length > 0 ? (
                  <div>
                    <h4 className="font-medium mb-3 flex items-center">
                      <CreditCard className="h-4 w-4 mr-2" />
                      Associated Accounts ({customer.accounts.length})
                    </h4>
                    <Table>
                      <TableHeader>
                        <TableRow>
                          <TableHead>Account Number</TableHead>
                          <TableHead>Type</TableHead>
                          <TableHead>Balance</TableHead>
                          <TableHead>Status</TableHead>
                          <TableHead>Interest Rate</TableHead>
                          <TableHead>Created</TableHead>
                        </TableRow>
                      </TableHeader>
                      <TableBody>
                        {customer.accounts.map((account) => (
                          <TableRow key={account.id}>
                            <TableCell className="font-medium">{account.accountNumber}</TableCell>
                            <TableCell>{getAccountTypeLabel(account.accountType)}</TableCell>
                            <TableCell>${account.balance.toLocaleString()}</TableCell>
                            <TableCell>
                              <span className={`px-2 py-1 rounded-full text-xs ${
                                account.status === 'ACTIVE' 
                                  ? 'bg-green-100 text-green-800' 
                                  : account.status === 'INACTIVE'
                                  ? 'bg-yellow-100 text-yellow-800'
                                  : account.status === 'SUSPENDED'
                                  ? 'bg-red-100 text-red-800'
                                  : 'bg-gray-100 text-gray-800'
                              }`}>
                                {account.status}
                              </span>
                            </TableCell>
                            <TableCell>{account.interestRate}%</TableCell>
                            <TableCell>{new Date(account.createdAt).toLocaleDateString()}</TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </div>
                ) : (
                  <div className="text-center py-8 text-gray-500">
                    <CreditCard className="h-12 w-12 mx-auto mb-4 opacity-50" />
                    <p>No accounts found for this customer</p>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {customers.length === 0 && !loading && (
        <Card>
          <CardContent className="pt-6">
            <div className="text-center py-8 text-gray-500">
              <Users className="h-12 w-12 mx-auto mb-4 opacity-50" />
              <p>No customers found</p>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
}
