import React from 'react';
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts';

// Ledger-consistent palette - deep, muted tones rather than default bright chart colors
const COLORS = ['#B4432C', '#C89B3C', '#2F6F4E', '#3E5C76', '#7A4B6D', '#8C8C6B'];

export default function ExpenseChart({ expenseByCategory }) {
  const data = Object.entries(expenseByCategory || {}).map(([name, value]) => ({ name, value }));

  if (data.length === 0) {
    return <p className="empty-state">No expenses recorded this month yet.</p>;
  }

  return (
    <ResponsiveContainer width="100%" height={280}>
      <PieChart>
        <Pie data={data} dataKey="value" nameKey="name" cx="50%" cy="50%"
             outerRadius={100} paddingAngle={2}>
          {data.map((entry, index) => (
            <Cell key={entry.name} fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip formatter={(value) => `₹${value.toLocaleString('en-IN')}`} />
        <Legend />
      </PieChart>
    </ResponsiveContainer>
  );
}
