const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

class ApiService {
  private async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  async login(credentials: { username: string; password: string }): Promise<any> {
    return this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify(credentials),
    });
  }

  async logout() {
    return this.request('/auth/logout', {
      method: 'POST',
    });
  }

  async getEmployees(): Promise<any[]> {
    return this.request('/employees');
  }

  async getEmployeeById(id: number) {
    return this.request(`/employees/${id}`);
  }

  async createEmployee(employee: any) {
    return this.request('/employees', {
      method: 'POST',
      body: JSON.stringify(employee),
    });
  }

  async updateEmployee(id: number, employee: any) {
    return this.request(`/employees/${id}`, {
      method: 'PUT',
      body: JSON.stringify(employee),
    });
  }

  async deleteEmployee(id: number) {
    return this.request(`/employees/${id}`, {
      method: 'DELETE',
    });
  }

  async getCustomers(): Promise<any[]> {
    return this.request('/customers');
  }

  async getCustomersWithAccounts(): Promise<any[]> {
    return this.request('/customers/with-accounts');
  }

  async getCustomerById(id: number) {
    return this.request(`/customers/${id}`);
  }

  async createCustomer(customer: any, employeeId: number) {
    return this.request(`/customers?employeeId=${employeeId}`, {
      method: 'POST',
      body: JSON.stringify(customer),
    });
  }

  async updateCustomer(id: number, customer: any) {
    return this.request(`/customers/${id}`, {
      method: 'PUT',
      body: JSON.stringify(customer),
    });
  }

  async deleteCustomer(id: number) {
    return this.request(`/customers/${id}`, {
      method: 'DELETE',
    });
  }

  async searchCustomers(term: string): Promise<any[]> {
    return this.request(`/customers/search?term=${encodeURIComponent(term)}`);
  }

  async getAccounts(): Promise<any[]> {
    return this.request('/accounts');
  }

  async getAccountById(id: number) {
    return this.request(`/accounts/${id}`);
  }

  async getAccountsByCustomer(customerId: number): Promise<any[]> {
    return this.request(`/accounts/customer/${customerId}`);
  }

  async createAccount(account: any, employeeId: number) {
    return this.request(`/accounts?employeeId=${employeeId}`, {
      method: 'POST',
      body: JSON.stringify(account),
    });
  }

  async updateAccountStatus(id: number, status: string) {
    return this.request(`/accounts/${id}/status?status=${status}`, {
      method: 'PUT',
    });
  }

  async updateAccountBalance(id: number, balance: number) {
    return this.request(`/accounts/${id}/balance?balance=${balance}`, {
      method: 'PUT',
    });
  }

  async deleteAccount(id: number) {
    return this.request(`/accounts/${id}`, {
      method: 'DELETE',
    });
  }

  async getAccountStats() {
    return Promise.all([
      this.request('/accounts/count'),
      this.request('/accounts/total-balance'),
      this.request('/accounts/stats/by-type'),
      this.request('/accounts/stats/by-status'),
    ]);
  }

  async getCustomerCount() {
    return this.request('/customers/count');
  }

  async getEmployeeCount() {
    return this.request('/employees/count');
  }
}

export const apiService = new ApiService();
