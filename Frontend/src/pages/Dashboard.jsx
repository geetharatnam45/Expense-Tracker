import React, { useCallback, useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import {
  getTransactions, createTransaction, updateTransaction,
  deleteTransaction, getMonthlyReport,
} from '../services/api';
import SummaryCards from '../components/SummaryCards';
import AddTransactionForm from '../components/AddTransactionForm';
import TransactionList from '../components/TransactionList';
import ExpenseChart from '../components/ExpenseChart';
import IncomeExpenseChart from '../components/IncomeExpenseChart';

export default function Dashboard() {
  const { user, logout } = useAuth();
  const [transactions, setTransactions] = useState([]);
  const [report, setReport] = useState(null);
  const [editingTransaction, setEditingTransaction] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const now = new Date();

  const loadData = useCallback(async () => {
    setError('');
    try {
      const [txRes, reportRes] = await Promise.all([
        getTransactions(),
        getMonthlyReport(now.getFullYear(), now.getMonth() + 1),
      ]);
      setTransactions(txRes.data);
      setReport(reportRes.data);
    } catch (err) {
      setError('Could not load your data. Please try again.');
    } finally {
      setLoading(false);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const handleSave = async (formData) => {
    try {
      if (editingTransaction) {
        await updateTransaction(editingTransaction.id, formData);
        setEditingTransaction(null);
      } else {
        await createTransaction(formData);
      }
      await loadData();
    } catch (err) {
      setError('Could not save the transaction.');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this transaction?')) return;
    try {
      await deleteTransaction(id);
      await loadData();
    } catch (err) {
      setError('Could not delete the transaction.');
    }
  };

  if (loading) return <div className="loading-screen">Loading your ledger…</div>;

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <div>
          <p className="auth-eyebrow">Expense Tracker</p>
          <h1>Hi, {user?.name?.split(' ')[0]}</h1>
        </div>
        <button className="ghost" onClick={logout}>Log out</button>
      </header>

      {error && <div className="auth-error">{error}</div>}

      <SummaryCards
        totalIncome={report?.totalIncome}
        totalExpense={report?.totalExpense}
        balance={report?.savings}
      />

      <div className="charts-grid">
        <div className="chart-card">
          <h2>Spending by category</h2>
          <ExpenseChart expenseByCategory={report?.expenseByCategory} />
        </div>
        <div className="chart-card">
          <h2>Income vs expense</h2>
          <IncomeExpenseChart
            totalIncome={report?.totalIncome || 0}
            totalExpense={report?.totalExpense || 0}
          />
        </div>
      </div>

      <div className="main-grid">
        <AddTransactionForm
          onSave={handleSave}
          editingTransaction={editingTransaction}
          onCancelEdit={() => setEditingTransaction(null)}
        />
        <TransactionList
          transactions={transactions}
          onEdit={setEditingTransaction}
          onDelete={handleDelete}
        />
      </div>
    </div>
  );
}
