import React from 'react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';

export default function IncomeExpenseChart({ totalIncome, totalExpense }) {
  const data = [{ name: 'This month', income: totalIncome, expense: totalExpense }];

  return (
    <ResponsiveContainer width="100%" height={280}>
      <BarChart data={data} barGap={12}>
        <CartesianGrid strokeDasharray="3 3" stroke="#e4e0d6" />
        <XAxis dataKey="name" tick={{ fill: '#5b5648' }} />
        <YAxis tick={{ fill: '#5b5648' }} />
        <Tooltip formatter={(value) => `₹${value.toLocaleString('en-IN')}`} />
        <Bar dataKey="income" fill="#2F6F4E" name="Income" radius={[6, 6, 0, 0]} />
        <Bar dataKey="expense" fill="#B4432C" name="Expense" radius={[6, 6, 0, 0]} />
      </BarChart>
    </ResponsiveContainer>
  );
}
