package com.example.trackexpenses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class MyAlertDialogFragment extends DialogFragment {
    private ExpenseViewModel expenseViewModel;

    public static MyAlertDialogFragment newInstance(String title, String msg, String pos, String neg) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msg", msg);
        args.putString("pos", pos);
        args.putString("neg", neg);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        expenseViewModel = new ViewModelProvider(getActivity()).get(ExpenseViewModel.class);

        String title = getArguments().getString("title");
        String msg = getArguments().getString("msg");
        String pos = getArguments().getString("pos");
        String neg = getArguments().getString("neg");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton(pos, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (title.contains("Delete"))
                    expenseViewModel.deleteExpense(ExpenseFragView.itemPos);
                else if (title.contains("Edit"))
                    ExpenseFragView.editExpense();
            }
        });

        alertDialogBuilder.setNegativeButton(neg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }
}
