package com.example.trackexpenses;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseFragAddEdit extends Fragment {
    private ExpenseViewModel expenseViewModel;
    TextView name, amount, date, description;
    private boolean isNumber = true, isDate = true, hasName = true, newExpense = true;
    private int expensePos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_edit_expense_frag, container, false);
        name = view.findViewById(R.id.edNameAdd);
        amount = view.findViewById(R.id.edAmountAdd);
        date = view.findViewById(R.id.edDateAdd);
        description = view.findViewById(R.id.edDescAdd);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Expense myExpense = (Expense) bundle.getSerializable("my_expense");
            expensePos = bundle.getInt("my_expense_position");
            if (myExpense != null) {
                name.setText(myExpense.getName());
                amount.setText(String.valueOf(myExpense.getAmount()));
                date.setText(myExpense.getDate());
                description.setText(myExpense.getDescription());

                newExpense = false;
            }
        }

        view.findViewById(R.id.btnCancel).setOnClickListener(new HandleClick());

        view.findViewById(R.id.btnSaveExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNumber = true;
                isDate = true;
                hasName = true;
                String nameTmp = name.getText().toString();
                if (nameTmp.isEmpty()) {
                    Toast.makeText(getContext(), "Must enter name", Toast.LENGTH_SHORT).show();
                    name.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    hasName = false;
                }
                double amountTmp = 0;
                try {
                    amountTmp = Double.parseDouble(amount.getText().toString());
                    if (amountTmp < 0)
                        isNumber = false;
                } catch (NumberFormatException e) {
                    isNumber = false;
                }
                if (!isNumber) {
                    Toast.makeText(getContext(), "Must enter a positive number in the amount field", Toast.LENGTH_SHORT).show();
                    amount.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                }
                String dateTmp = date.getText().toString();
                if (!(dateTmp.matches("[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{4}"))) {
                    Toast.makeText(getContext(), "Date's Format: DD.MM.YYYY (or D.M.YYYY)", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Separated by dots (.)", Toast.LENGTH_SHORT).show();
                    date.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    isDate = false;
                }
                if (dateTmp.matches("[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{4}")) {
                    String[] tmpStr = dateTmp.split("\\.");
                    int day = Integer.parseInt(tmpStr[0]);
                    int month = Integer.parseInt(tmpStr[1]);
                    if (day > 31 || day < 1 || month > 12 || month < 1) {
                        Toast.makeText(getContext(), "Incorrect date (D:1-31, M:1-12)", Toast.LENGTH_SHORT).show();
                        date.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        isDate = false;
                    }
                }
                String descriptionTmp = description.getText().toString();
                if (isNumber && isDate && hasName && newExpense) {
                    Expense newExpense = new Expense(nameTmp, amountTmp, dateTmp, descriptionTmp);
                    Log.d("AddEditExpenseFrag", newExpense.getName() + newExpense.getAmount() + newExpense.getDate() + newExpense.getDescription());
                    expenseViewModel.addExpense(newExpense);
                    getFragmentManager().popBackStack();
                }
                if (isNumber && isDate && hasName && !newExpense) {
                    Expense newExpense = new Expense(nameTmp, amountTmp, dateTmp, descriptionTmp);
                    Log.d("AddEditExpenseFrag2", newExpense.getName() + newExpense.getAmount() + newExpense.getDate() + newExpense.getDescription());
                    expenseViewModel.editTheExpense(newExpense, expensePos);
                    getFragmentManager().popBackStack();
                }
            }
        });
        return view;
    }

    private class HandleClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            getFragmentManager().popBackStack();
        }
    }
}
