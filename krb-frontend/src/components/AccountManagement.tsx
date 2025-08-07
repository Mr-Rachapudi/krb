import React, { useState, useEffect } from 'react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from './ui/table';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from './ui/dialog';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select';
import { Label } from './ui/label';
import { Alert, AlertDescription } from './ui/alert';
import { Plus, Edit, Trash2, DollarSign } from 'lucide-react';
import { apiService } from '../services/api';
import { Account, Customer, Employee, AccountType, AccountStatus, CreateAccountRequest } from '../types';

interface AccountManagementProps {
  user: Employee;
}

export default function AccountManagement({ user }: AccountManagementProps) {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [showCreateDialog, setShowCreateDialog] = useState(false);
  const [editingAccount, setEditingAccount] = useState<Account | null>(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const [formData, setFormData] = useState<CreateAccountRequest>({
    customerId: 0,
    accountType: 'SAVINGS',
    initialBalance: 0,
    creditLimit: undefined
  });

  const accountTypes: AccountType[] = [
    'SAVINGS', 'CHECKING', 'FIXED_DEPOSIT', 'CREDIT_CARD', 'MONEY_MARKET', 'BUSINESS_CHECKING'
  ];

  const accountStatuses: AccountStatus[] = ['ACTIVE', 'INACTIVE', 'CLOSED', 'SUSPENDED'];

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [accountsData, customersData] = await Promise.all([
        apiService.getAccounts(),
        apiService.getCustomers()
      ]);
      setAccounts(accountsData);
      setCustomers(customersData);
    } catch (error) {
      setError('Failed to fetch data');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    console.log('Form data being submitted:', formData);
    console.log('User ID:', user.id);
    console.log('Available customers:', customers);

    if (formData.customerId === 0) {
      setError('Please select a customer');
      return;
    }

    try {
      await apiService.createAccount(formData, user.id);
      setSuccess('Account created successfully');
      resetForm();
      fetchData();
    } catch (error) {
      console.error('Account creation error:', error);
      setError(error instanceof Error ? error.message : 'Operation failed');
    }
  };

  const handleStatusUpdate = async (accountId: number, status: AccountStatus) => {
    try {
      await apiService.updateAccountStatus(accountId, status);
      setSuccess('Account status updated successfully');
      fetchData();
    } catch (error) {
      setError('Failed to update account status');
    }
  };

  const handleBalanceUpdate = async (accountId: number, balance: number) => {
    try {
      await apiService.updateAccountBalance(accountId, balance);
      setSuccess('Account balance updated successfully');
      fetchData();
    } catch (error) {
      setError('Failed to update account balance');
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this account?')) return;

    try {
      await apiService.deleteAccount(id);
      setSuccess('Account deleted successfully');
      fetchData();
    } catch (error) {
      setError('Failed to delete account');
    }
  };

  const resetForm = () => {
    setFormData({
      customerId: 0,
      accountType: 'SAVINGS',
      initialBalance: 0,
      creditLimit: undefined
    });
    setEditingAccount(null);
    setShowCreateDialog(false);
  };

  const getAccountTypeLabel = (type: AccountType) => {
    return type.replace('_', ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  };

  if (loading) {
    return <div className="flex items-center justify-center h-64">Loading accounts...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Account Management</h1>
          <p className="text-gray-600">Manage customer bank accounts and transactions</p>
        </div>
        
        <Dialog open={showCreateDialog} onOpenChange={setShowCreateDialog}>
          <DialogTrigger asChild>
            <Button onClick={() => resetForm()}>
              <Plus className="h-4 w-4 mr-2" />
              Create Account
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-md">
            <DialogHeader>
              <DialogTitle>Create New Account</DialogTitle>
              <DialogDescription>
                Open a new bank account for a customer
              </DialogDescription>
            </DialogHeader>
            
            <form onSubmit={handleSubmit} className="space-y-4">
              {error && (
                <Alert variant="destructive">
                  <AlertDescription>{error}</AlertDescription>
                </Alert>
              )}
              
              <div className="space-y-2">
                <Label htmlFor="customerId">Customer</Label>
                <Select
                  value={formData.customerId > 0 ? formData.customerId.toString() : ""}
                  onValueChange={(value) => {
                    console.log('Customer selected:', value);
                    setFormData({...formData, customerId: parseInt(value)});
                  }}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Select a customer" />
                  </SelectTrigger>
                  <SelectContent>
                    {customers.map((customer) => (
                      <SelectItem key={customer.id} value={customer.id.toString()}>
                        {customer.firstName} {customer.lastName}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              
              <div className="space-y-2">
                <Label htmlFor="accountType">Account Type</Label>
                <Select
                  value={formData.accountType}
                  onValueChange={(value: AccountType) => setFormData({...formData, accountType: value})}
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    {accountTypes.map((type) => (
                      <SelectItem key={type} value={type}>
                        {getAccountTypeLabel(type)}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              
              <div className="space-y-2">
                <Label htmlFor="initialBalance">Initial Balance</Label>
                <Input
                  id="initialBalance"
                  type="number"
                  step="0.01"
                  min="0"
                  value={formData.initialBalance}
                  onChange={(e) => setFormData({...formData, initialBalance: parseFloat(e.target.value)})}
                  required
                />
              </div>
              
              {formData.accountType === 'CREDIT_CARD' && (
                <div className="space-y-2">
                  <Label htmlFor="creditLimit">Credit Limit</Label>
                  <Input
                    id="creditLimit"
                    type="number"
                    step="0.01"
                    min="0"
                    value={formData.creditLimit || ''}
                    onChange={(e) => setFormData({...formData, creditLimit: parseFloat(e.target.value)})}
                  />
                </div>
              )}
              
              <div className="flex justify-end space-x-2">
                <Button type="button" variant="outline" onClick={resetForm}>
                  Cancel
                </Button>
                <Button type="submit">
                  Create Account
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      {success && (
        <Alert>
          <AlertDescription>{success}</AlertDescription>
        </Alert>
      )}

      <Card>
        <CardHeader>
          <CardTitle>Accounts ({accounts.length})</CardTitle>
          <CardDescription>All bank accounts in the system</CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Account Number</TableHead>
                <TableHead>Customer</TableHead>
                <TableHead>Type</TableHead>
                <TableHead>Balance</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Created By</TableHead>
                <TableHead>Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {accounts.map((account) => (
                <TableRow key={account.id}>
                  <TableCell className="font-medium">{account.accountNumber}</TableCell>
                  <TableCell>{account.customerName}</TableCell>
                  <TableCell>{getAccountTypeLabel(account.accountType)}</TableCell>
                  <TableCell>${account.balance.toLocaleString()}</TableCell>
                  <TableCell>
                    <Select
                      value={account.status}
                      onValueChange={(value: AccountStatus) => handleStatusUpdate(account.id, value)}
                    >
                      <SelectTrigger className="w-32">
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        {accountStatuses.map((status) => (
                          <SelectItem key={status} value={status}>
                            {status}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </TableCell>
                  <TableCell>{account.createdByEmployeeName}</TableCell>
                  <TableCell>
                    <div className="flex space-x-2">
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => {
                          const newBalance = prompt('Enter new balance:', account.balance.toString());
                          if (newBalance && !isNaN(parseFloat(newBalance))) {
                            handleBalanceUpdate(account.id, parseFloat(newBalance));
                          }
                        }}
                      >
                        <DollarSign className="h-4 w-4" />
                      </Button>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => handleDelete(account.id)}
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
}
