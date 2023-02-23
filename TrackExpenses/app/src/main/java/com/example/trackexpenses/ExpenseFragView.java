package com.example.trackexpenses;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ExpenseFragView extends Fragment {
    private RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private static ExpenseAdapter expenseAdapter;
    private ExpenseViewModel expenseViewModel;
    private ArrayList<Expense> expenseList;
    private static int sortingPref = 0;
    public static int itemPos;
    private SharedPreferences prefs;
    private static final String SORT_PREFERENCE = "sort_preference";
    private ExpenseDateChangeReceiver myReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        myReceiver = new ExpenseDateChangeReceiver(expenseViewModel);

        IntentFilter filter = new IntentFilter();
        filter.addAction("Custom_Date_BroadcastReceiver");
        getActivity().registerReceiver(myReceiver, filter);

        Intent intent = new Intent("Custom_Date_BroadcastReceiver");
        intent.putExtra("Current_Month", Calendar.getInstance().get(Calendar.MONTH) + 1);
        intent.putExtra("Current_Year", Calendar.getInstance().get(Calendar.YEAR));
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        sortingPref = getSortPreference();

        View view = inflater.inflate(R.layout.view_expenses_frag, container, false);
        recyclerView = view.findViewById(R.id.rvExpenses);
        expenseAdapter = new ExpenseAdapter(getContext());
        recyclerView.setAdapter(expenseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                itemPos = position;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MyAlertDialogFragment alertDialog = MyAlertDialogFragment.newInstance("Edit expense", "Do you want to edit this expense's details?", "Yes", "No");
                alertDialog.show(fm, "fragment_alert");
            }

            @Override
            public void onLongClick(View view, int position) {
                itemPos = position;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MyAlertDialogFragment alertDialog = MyAlertDialogFragment.newInstance("Delete expense", "Are you sure you want to delete this expense?", "Yes", "No");
                alertDialog.show(fm, "fragment_alert");
            }
        }));

        expenseViewModel.getExpense().observe(getViewLifecycleOwner(), new Observer<ArrayList<Expense>>() {
            @Override
            public void onChanged(ArrayList<Expense> expenses) {
                Log.d("ViewExpensesFragment", String.valueOf(expenses.size()));
                expenseList = expenses;
                if (expenseList.size() > 1) {
                    if (sortingPref == 0) {
                        expenseList.sort(new ExpenseAmountComparator());
                    } else if (sortingPref == 1) {
                        expenseList.sort(new ExpenseNameComparator());
                    } else if (sortingPref == 2) {
                        expenseList.sort(new ExpenseDateComparator());
                    }
                }
                expenseAdapter.setExpense(expenses);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.expense_add) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragContainer, new ExpenseFragAddEdit());
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        } else if (item.getItemId() == R.id.expense_sum) {
            double sum = 0;
            for (Expense expense : expenseList)
                sum += expense.getAmount();
            Toast.makeText(getContext(), "Sum of displayed expenses: " + String.format("%.2f", sum), Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.calendar) {
            showFilterDialog();
        } else if (item.getItemId() == R.id.expense_sort) {
            if (expenseList.size() > 1) {
                sortingPref = (sortingPref + 1) % 3;
                if (sortingPref == 0) {
                    expenseList.sort(new ExpenseAmountComparator());
                    Toast.makeText(getContext(), "Sort by amount", Toast.LENGTH_SHORT).show();
                } else if (sortingPref == 1) {
                    expenseList.sort(new ExpenseNameComparator());
                    Toast.makeText(getContext(), "Sort by name", Toast.LENGTH_SHORT).show();
                } else if (sortingPref == 2) {
                    expenseList.sort(new ExpenseDateComparator());
                    Toast.makeText(getContext(), "Sort by date", Toast.LENGTH_SHORT).show();
                }
                //Log.d("SortingCheck", expenseList.get(0).getName() + expenseList.get(0).getAmount() + expenseList.get(0).getDate() + "\n" + expenseList.get(1).getName() + expenseList.get(1).getAmount() + expenseList.get(1).getDate() + "\n" + expenseList.get(2).getName() + expenseList.get(2).getAmount() + expenseList.get(2).getDate());
                expenseViewModel.setExpense(expenseList);
            } else
                Toast.makeText(getContext(), "Must have at least two items to sort", Toast.LENGTH_SHORT).show();
            saveSortPreference(sortingPref);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSortPreference(int sortPreference) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SORT_PREFERENCE, sortPreference);
        editor.apply();
    }

    private int getSortPreference() {
        return prefs.getInt(SORT_PREFERENCE, 0);
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_expenses, null);

        final EditText monthEditText = dialogView.findViewById(R.id.etMonth);
        final EditText yearEditText = dialogView.findViewById(R.id.etYear);

        builder.setView(dialogView)
                .setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String monthStr = monthEditText.getText().toString();
                        String yearStr = yearEditText.getText().toString();
                        int month, year;
                        Log.d("showFilterDialogTest", monthStr + " " + yearStr);

                        if (yearStr.isEmpty() && monthStr.isEmpty()) {
                            year = -1;
                            month = -1;
                            expenseViewModel.filterExpensesDate(month, year);
                            dialog.dismiss();
                        } else if (!yearStr.isEmpty() && monthStr.isEmpty()) {
                            year = Integer.parseInt(yearStr);
                            if (year > 0) {
                                month = -1;
                                expenseViewModel.filterExpensesDate(month, year);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(), "Please follow the instructions. Enter a valid date.", Toast.LENGTH_SHORT).show();
                            }
                        } else if (!yearStr.isEmpty() && !monthStr.isEmpty()) {
                            year = Integer.parseInt(yearStr);
                            month = Integer.parseInt(monthStr);
                            if (year >= 0 && (month > 0 && month < 13)) {
                                expenseViewModel.filterExpensesDate(month, year);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(), "Please follow the instructions. Enter a valid date.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please follow the instructions. Enter a valid date.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }


    public static void editExpense() {
        expenseAdapter.editExpense(itemPos);
    }
}
