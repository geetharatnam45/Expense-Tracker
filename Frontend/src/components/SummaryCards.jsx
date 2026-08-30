import React from 'react';

const formatCurrency = (value) =>
  new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(value || 0);

export default function SummaryCards({ totalIncome, totalExpense, balance }) {
  return (
    <div className="summary-grid">
      <div className="summary-card income">
        <span className="summary-label">Income this month</span>
        <span className="summary-value">{formatCurrency(totalIncome)}</span>
      </div>
      <div className="summary-card expense">
        <span className="summary-label">Expenses this month</span>
        <span className="summary-value">{formatCurrency(totalExpense)}</span>
      </div>
      <div className={`summary-card balance ${balance < 0 ? 'negative' : ''}`}>
        <span className="summary-label">Balance</span>
        <span className="summary-value">{formatCurrency(balance)}</span>
      </div>
    </div>
  );
}
