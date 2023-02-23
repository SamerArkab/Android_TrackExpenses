package com.example.trackexpenses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ExpenseDateChangeReceiver extends BroadcastReceiver {
    private ExpenseViewModel expenseViewModel;

    public ExpenseDateChangeReceiver() {
        // Default constructor
        //The purpose of the default constructor is to provide a public constructor with no arguments that can be called by the Android system when creating an instance of the class
    }

    public ExpenseDateChangeReceiver(ExpenseViewModel expenseViewModel) {
        this.expenseViewModel = expenseViewModel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("Custom_Date_BroadcastReceiver")) {
            int currMonth = intent.getIntExtra("Current_Month", 0);
            int currYear = intent.getIntExtra("Current_Year", 0);

            Log.d("DateBroadcastReceiver", currYear + " " + currMonth);

            expenseViewModel.filterExpensesDate(currMonth, currYear);
        }
    }
}
