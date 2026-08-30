import React, { useEffect, useState } from 'react';

const CATEGORIES = {
  INCOME: ['Salary', 'Freelancing', 'Investments', 'Other'],
  EXPENSE: ['Food', 'Shopping', 'Travel', 'Rent', 'Bills', 'Entertainment', 'Other'],
};

const emptyForm = {
  title: '',
  amount: '',
  type: 'EXPENSE',
  category: CATEGORIES.EXPENSE[0],
  date: new Date().toISOString().slice(0, 10),
};

export default function AddTransactionForm({ onSave, editingTransaction, onCancelEdit }) {
  const [form, setForm] = useState(emptyForm);

  useEffect(() => {
    if (editingTransaction) {
      setForm({
        title: editingTransaction.title,
        amount: editingTransaction.amount,
        type: editingTransaction.type,
        category: editingTransaction.category,
        date: editingTransaction.date,
      });
    }
  }, [editingTransaction]);

  const handleTypeChange = (type) => {
    setForm((f) => ({ ...f, type, category: CATEGORIES[type][0] }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.title.trim() || Number(form.amount) <= 0) return;
    onSave({ ...form, amount: Number(form.amount) });
    setForm(emptyForm);
  };

  return (
    <form className="transaction-form" onSubmit={handleSubmit}>
      <h2>{editingTransaction ? 'Edit transaction' : 'Add a transaction'}</h2>

      <div className="type-toggle">
        <button type="button"
                className={form.type === 'INCOME' ? 'active income' : ''}
                onClick={() => handleTypeChange('INCOME')}>Income</button>
        <button type="button"
                className={form.type === 'EXPENSE' ? 'active expense' : ''}
                onClick={() => handleTypeChange('EXPENSE')}>Expense</button>
      </div>

      <div className="form-row">
        <label>
          Title
          <input type="text" value={form.title} required
                 placeholder="e.g. Salary, Groceries"
                 onChange={(e) => setForm({ ...form, title: e.target.value })} />
        </label>

        <label>
          Amount (₹)
          <input type="number" min="0.01" step="0.01" value={form.amount} required
                 placeholder="0.00"
                 onChange={(e) => setForm({ ...form, amount: e.target.value })} />
        </label>
      </div>

      <div className="form-row">
        <label>
          Category
          <select value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })}>
            {CATEGORIES[form.type].map((c) => <option key={c} value={c}>{c}</option>)}
          </select>
        </label>

        <label>
          Date
          <input type="date" value={form.date} required
                 onChange={(e) => setForm({ ...form, date: e.target.value })} />
        </label>
      </div>

      <div className="form-actions">
        <button type="submit" className="primary">
          {editingTransaction ? 'Save changes' : 'Add transaction'}
        </button>
        {editingTransaction && (
          <button type="button" className="ghost" onClick={onCancelEdit}>Cancel</button>
        )}
      </div>
    </form>
  );
}
