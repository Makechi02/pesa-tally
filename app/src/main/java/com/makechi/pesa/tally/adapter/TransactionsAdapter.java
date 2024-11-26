package com.makechi.pesa.tally.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.makechi.pesa.tally.R;
import com.makechi.pesa.tally.entity.Transaction;
import com.makechi.pesa.tally.util.Formatter;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transaction, viewGroup, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TransactionViewHolder viewHolder, int i) {
        Transaction transaction = transactions.get(i);

        viewHolder.dateTextView.setText(transaction.getDate());

        if (transaction.getType().equals("DEPOSIT")) {
            viewHolder.amountTextView.setText(Formatter.formatDepositWithCurrency(transaction.getAmount()));
            viewHolder.amountTextView.setTextColor(viewHolder.amountTextView.getContext().getColor(R.color.deposit_color));
        } else {
            viewHolder.amountTextView.setText(Formatter.formatWithdrawalWithCurrency(transaction.getAmount()));
            viewHolder.amountTextView.setTextColor(viewHolder.amountTextView.getContext().getColor(R.color.withdrawal_color));
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        private final TextView dateTextView;
        private final TextView amountTextView;

        public TransactionViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.text_date);
            amountTextView = itemView.findViewById(R.id.text_amount);
        }
    }
}
