export interface Employee {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  role: 'ADMIN' | 'EMPLOYEE';
  createdAt: string;
  updatedAt: string;
  customerCount?: number;
}

export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  address: string;
  dateOfBirth: string;
  ssn: string;
  createdAt: string;
  updatedAt: string;
  createdByEmployeeId: number;
  createdByEmployeeName: string;
  accounts?: Account[];
  accountCount?: number;
}

export interface Account {
  id: number;
  accountNumber: string;
  accountType: AccountType;
  balance: number;
  interestRate: number;
  creditLimit?: number;
  status: AccountStatus;
  createdAt: string;
  updatedAt: string;
  customerId: number;
  customerName: string;
  createdByEmployeeId: number;
  createdByEmployeeName: string;
}

export type AccountType = 
  | 'SAVINGS'
  | 'CHECKING'
  | 'FIXED_DEPOSIT'
  | 'CREDIT_CARD'
  | 'MONEY_MARKET'
  | 'BUSINESS_CHECKING';

export type AccountStatus = 'ACTIVE' | 'INACTIVE' | 'CLOSED' | 'SUSPENDED';

export interface CreateEmployeeRequest {
  username: string;
  password: string;
  firstName: string;
  lastName: string;
  email: string;
  role: 'ADMIN' | 'EMPLOYEE';
}

export interface CreateAccountRequest {
  customerId: number;
  accountType: AccountType;
  initialBalance: number;
  creditLimit?: number;
}

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  message?: string;
}
