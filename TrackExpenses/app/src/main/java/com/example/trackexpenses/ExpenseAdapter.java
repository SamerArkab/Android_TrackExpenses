package com.example.trackexpenses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private ArrayList<Expense> expenseList;
    private Context context;

    public ExpenseAdapter(Context context) {
        this.context = context;
        this.expenseList = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setExpense(ArrayList<Expense> expenses) {
        Log.d("ExpenseAdapterSetExpense", String.valueOf(expenses.size()));
        this.expenseList = expenses;
        notifyDataSetChanged(); //Triggers the adapter, which will call "onBindViewHolder"
    }

    public void editExpense(int position) {
        FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        Expense expenseAddEdit = expenseList.get(position);
        bundle.putSerializable("my_expense", expenseAddEdit);
        bundle.putInt("my_expense_position", position);
        Fragment addEditFrag = new ExpenseFragAddEdit();
        addEditFrag.setArguments(bundle);

        transaction.replace(R.id.fragContainer, addEditFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_element, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        holder.bind(expenseList.get(position));
        Expense log = expenseList.get(position);
        Log.d("ExpenseAdapterOnBindViewHolder", log.getName() + log.getAmount() + log.getDate() + log.getDescription());
    }

    @Override
    public int getItemCount() {
        Log.d("ExpenseAdapterGetItemCount", String.valueOf(expenseList.size()));
        return expenseList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvAmount, tvDate, tvDescription;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvExpenseName);
            tvAmount = itemView.findViewById(R.id.tvExpenseAmount);
            tvDate = itemView.findViewById(R.id.tvExpenseDate);
            tvDescription = itemView.findViewById(R.id.tvExpenseDescription);
        }

        public void bind(Expense expense) {
            tvName.setText(expense.getName());
            tvAmount.setText(String.valueOf(expense.getAmount()));
            tvDate.setText(expense.getDate());
            tvDescription.setText(expense.getDescription());
        }
    }
}
