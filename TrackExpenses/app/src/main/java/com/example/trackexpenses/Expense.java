package com.example.trackexpenses;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

public class Expense implements Serializable {
    String name;
    double amount;
    String date;
    String description;
    int day, month, year;

    public Expense(String name, double amount, String date, String description) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        String[] temp = this.date.split("\\.");
        this.day = Integer.parseInt(temp[0]);
        this.month = Integer.parseInt(temp[1]);
        this.year = Integer.parseInt(temp[2]);
        this.description = description;
        Log.d("ExpenseObject", this.day + " " + this.month + " " + this.year);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        String[] temp = this.date.split("\\.");
        this.day = Integer.parseInt(temp[0]);
        this.month = Integer.parseInt(temp[1]);
        this.year = Integer.parseInt(temp[2]);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + " " + getAmount() + " " + getDay() + "." + getMonth() + "." + getYear() + " " + getDescription();
    }
}

class ExpenseAmountComparator implements Comparator<Expense> {
    @Override
    public int compare(Expense e1, Expense e2) {
        return Double.compare(e1.amount, e2.amount);
    }
}

class ExpenseNameComparator implements Comparator<Expense> {
    @Override
    public int compare(Expense e1, Expense e2) {
        return e1.name.compareTo(e2.name);
    }
}

class ExpenseDateComparator implements Comparator<Expense> {
    @Override
    public int compare(Expense e1, Expense e2) {
        if (e1.year != e2.year) {
            return Integer.compare(e1.year, e2.year);
        } else if (e1.month != e2.month) {
            return Integer.compare(e1.month, e2.month);
        } else {
            return Integer.compare(e1.day, e2.day);
        }
    }
}
