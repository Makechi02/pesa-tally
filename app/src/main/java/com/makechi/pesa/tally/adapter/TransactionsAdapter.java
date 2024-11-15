package com.makechi.pesa.tally.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.makechi.pesa.tally.R;
import com.makechi.pesa.tally.entity.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    private List<Transaction> transactions = new ArrayList<>();

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_daily_savings, viewGroup, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TransactionViewHolder transactionViewHolder, int i) {
        Transaction transaction = transactions.get(i);

        transactionViewHolder.dayTextView.setText("Date: " + transaction.getDate());
        transactionViewHolder.amountTextView.setText("Amount: " + transaction.getAmount());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        private final TextView dayTextView;
        private final TextView amountTextView;

        public TransactionViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            dayTextView = itemView.findViewById(R.id.text_day);
            amountTextView = itemView.findViewById(R.id.text_amount_saved);
        }
    }
}
