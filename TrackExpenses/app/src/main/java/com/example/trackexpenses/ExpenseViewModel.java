package com.example.trackexpenses;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ExpenseViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Expense>> expenseLiveData;
    private final String fileName = "data.txt";
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Expense> allExpenses;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        expenseLiveData = new MutableLiveData<>();
        allExpenses = loadExpenses();
        if (allExpenses != null)
            expenseLiveData.setValue(allExpenses);
        else
            expenseLiveData.setValue(new ArrayList<>());
    }

    public MutableLiveData<ArrayList<Expense>> getExpense() {
        if (expenseLiveData == null) {
            expenseLiveData = new MutableLiveData<>();
        }
        return expenseLiveData;
    }

    public void addExpense(Expense expense) {
        ArrayList<Expense> currExpenses = expenseLiveData.getValue();
        currExpenses.add(expense);
        allExpenses.add(expense);
        Log.d("ExpenseViewModelAdd", String.valueOf(currExpenses.size()));
        expenseLiveData.setValue(currExpenses);

        saveExpenses(allExpenses);
    }

    public void editTheExpense(Expense expense, int position) {
        ArrayList<Expense> currExpenses = expenseLiveData.getValue();
        Expense expenseToEdit = currExpenses.get(position);

        Log.d("editTheExpense", position + "");
        Log.d("editTheExpense", currExpenses.get(position).toString());
        Log.d("editTheExpense", expense.toString());
        for (Expense ex : currExpenses)
            Log.d("trythis", ex.toString());
        for (Expense e : allExpenses)
            Log.d("trythat", e.toString());

        for (int i = 0; i < allExpenses.size(); i++) {
            Log.d("checkinghere", allExpenses.get(i).toString());
            if (allExpenses.get(i).getName().equals(expenseToEdit.getName()) &&
                    allExpenses.get(i).getDescription().equals(expenseToEdit.getDescription()) &&
                    allExpenses.get(i).getAmount() == expenseToEdit.getAmount() &&
                    allExpenses.get(i).getDay() == expenseToEdit.getDay() &&
                    allExpenses.get(i).getMonth() == expenseToEdit.getMonth() &&
                    allExpenses.get(i).getYear() == expenseToEdit.getYear()) {
                allExpenses.get(i).setName(expense.getName());
                allExpenses.get(i).setAmount(expense.getAmount());
                allExpenses.get(i).setDate(expense.getDate());
                allExpenses.get(i).setDescription(expense.getDescription());
                Log.d("editTheExpense", allExpenses.get(i).toString() + "here?");
                break;
            }
        }

        currExpenses.get(position).setName(expense.getName());
        currExpenses.get(position).setAmount(expense.getAmount());
        currExpenses.get(position).setDate(expense.getDate());
        currExpenses.get(position).setDescription(expense.getDescription());
        Log.d("editTheExpense", currExpenses.get(position).toString());
        expenseLiveData.setValue(currExpenses);

        saveExpenses(allExpenses);
    }

    //Used in sorting
    public void setExpense(ArrayList<Expense> expenseList) {
        Log.d("ExpenseViewModelSet", String.valueOf(expenseList.size()));
        expenseLiveData.setValue(expenseList);
    }

    public void deleteExpense(int position) {
        ArrayList<Expense> currExpenses = expenseLiveData.getValue();
        Expense expenseToRemove = currExpenses.get(position);

        for (int i = 0; i < allExpenses.size(); i++) {
            if (allExpenses.get(i).getName().equals(expenseToRemove.getName()) &&
                    allExpenses.get(i).getDescription().equals(expenseToRemove.getDescription()) &&
                    allExpenses.get(i).getAmount() == expenseToRemove.getAmount() &&
                    allExpenses.get(i).getDay() == expenseToRemove.getDay() &&
                    allExpenses.get(i).getMonth() == expenseToRemove.getMonth() &&
                    allExpenses.get(i).getYear() == expenseToRemove.getYear()) {
                allExpenses.remove(i);
            }
        }

        currExpenses.remove(position);
        expenseLiveData.setValue(currExpenses);

        saveExpenses(allExpenses);
    }

    public void filterExpensesDate(int month, int year) {
        ArrayList<Expense> currExpenses = loadExpenses();
        ArrayList<Expense> filteredExpenses = new ArrayList<>();
        Log.d("filterExpenseDate", month + " " + year);
        if ((month > 0 && month < 13) || year >= 0) {
            for (Expense expense : currExpenses) {
                if (month == -1 && expense.getYear() == year) //All year's expenses
                    filteredExpenses.add(expense);
                else if (expense.getMonth() == month && expense.getYear() == year) {
                    Log.d("filterExpenseDate2", month + " " + year + " " + expense.getName() + " " + expense.getMonth() + " " + expense.getYear());
                    filteredExpenses.add(expense);
                }
            }
            expenseLiveData.setValue(filteredExpenses);
        } else //All of the expenses
            expenseLiveData.setValue(currExpenses);
    }

    private ArrayList<Expense> loadExpenses() {
        ArrayList<Expense> expenses = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            expenses = (ArrayList<Expense>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            expenses = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expenses;
    }

    private void saveExpenses(ArrayList<Expense> expenses) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(expenses);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
