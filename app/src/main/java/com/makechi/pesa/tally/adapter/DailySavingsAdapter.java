package com.makechi.pesa.tally.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.makechi.pesa.tally.R;
import com.makechi.pesa.tally.entity.DailySavings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DailySavingsAdapter extends RecyclerView.Adapter<DailySavingsAdapter.DailySavingsViewHolder> {

    private List<DailySavings> dailySavingsList = new ArrayList<>();

    public void setDailySavingsList(List<DailySavings> dailySavingsList) {
        this.dailySavingsList = dailySavingsList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public DailySavingsAdapter.DailySavingsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transaction, viewGroup, false);
        return new DailySavingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DailySavingsAdapter.DailySavingsViewHolder dailySavingsViewHolder, int i) {
        DailySavings savings = dailySavingsList.get(i);

        dailySavingsViewHolder.dayTextView.setText("Date: " + savings.getDate());
        dailySavingsViewHolder.amountTextView.setText("Amount: " + savings.getAmount());
    }

    @Override
    public int getItemCount() {
        return dailySavingsList.size();
    }

    public static class DailySavingsViewHolder extends RecyclerView.ViewHolder {

        private final TextView dayTextView;
        private final TextView amountTextView;

        public DailySavingsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            dayTextView = itemView.findViewById(R.id.text_date);
            amountTextView = itemView.findViewById(R.id.text_amount);
        }
    }
}
