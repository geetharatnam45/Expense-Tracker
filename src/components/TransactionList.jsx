import React, { useMemo, useState } from 'react';

const formatCurrency = (value) =>
  new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(value);

export default function TransactionList({ transactions, onEdit, onDelete }) {
  const [keyword, setKeyword] = useState('');
  const [typeFilter, setTypeFilter] = useState('ALL');
  const [categoryFilter, setCategoryFilter] = useState('ALL');

  const categories = useMemo(
    () => ['ALL', ...new Set(transactions.map((t) => t.category))],
    [transactions]
  );

  const filtered = useMemo(() => {
    return transactions.filter((t) => {
      const matchesKeyword = t.title.toLowerCase().includes(keyword.toLowerCase());
      const matchesType = typeFilter === 'ALL' || t.type === typeFilter;
      const matchesCategory = categoryFilter === 'ALL' || t.category === categoryFilter;
      return matchesKeyword && matchesType && matchesCategory;
    });
  }, [transactions, keyword, typeFilter, categoryFilter]);

  return (
    <div className="transaction-list">
      <div className="list-header">
        <h2>Ledger</h2>
        <div className="list-filters">
          <input type="text" placeholder="Search by title…" value={keyword}
                 onChange={(e) => setKeyword(e.target.value)} />
          <select value={typeFilter} onChange={(e) => setTypeFilter(e.target.value)}>
            <option value="ALL">All types</option>
            <option value="INCOME">Income</option>
            <option value="EXPENSE">Expense</option>
          </select>
          <select value={categoryFilter} onChange={(e) => setCategoryFilter(e.target.value)}>
            {categories.map((c) => <option key={c} value={c}>{c === 'ALL' ? 'All categories' : c}</option>)}
          </select>
        </div>
      </div>

      {filtered.length === 0 ? (
        <p className="empty-state">No transactions match yet — add one above to start your ledger.</p>
      ) : (
        <table className="ledger-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Title</th>
              <th>Category</th>
              <th className="amount-col">Amount</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((t) => (
              <tr key={t.id}>
                <td className="mono">{t.date}</td>
                <td>{t.title}</td>
                <td><span className="pill">{t.category}</span></td>
                <td className={`amount-col mono ${t.type === 'INCOME' ? 'income-text' : 'expense-text'}`}>
                  {t.type === 'INCOME' ? '+' : '−'}{formatCurrency(t.amount)}
                </td>
                <td className="row-actions">
                  <button onClick={() => onEdit(t)} title="Edit">Edit</button>
                  <button onClick={() => onDelete(t.id)} title="Delete" className="danger">Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
