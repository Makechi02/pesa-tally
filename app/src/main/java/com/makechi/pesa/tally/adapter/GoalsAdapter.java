package com.makechi.pesa.tally.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.makechi.pesa.tally.R;
import com.makechi.pesa.tally.entity.Goal;
import com.makechi.pesa.tally.util.Formatter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalViewHolder> {

    private final OnGoalClickedListener listener;
    private List<Goal> goals = new ArrayList<>();

    public GoalsAdapter(OnGoalClickedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public GoalsAdapter.GoalViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_custom_goal, viewGroup, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GoalsAdapter.GoalViewHolder goalViewHolder, int i) {
        Goal goal = goals.get(i);
        int progress = (int) ((goal.getAmountSaved() / goal.getTargetAmount()) * 100);
        double percentageProgress = ((goal.getAmountSaved() / goal.getTargetAmount()) * 100);

        goalViewHolder.goalNameTextView.setText(goal.getName());
        goalViewHolder.amountSavedTextView.setText(Formatter.formatMoneyWithCurrency(goal.getAmountSaved()));
        goalViewHolder.targetAmountTextView.setText(Formatter.formatMoneyWithCurrency(goal.getTargetAmount()));
        goalViewHolder.progressBar.setProgress(progress);
        goalViewHolder.progressTextView.setText(Formatter.formatProgress(percentageProgress));

        goalViewHolder.itemView.setOnClickListener(view -> listener.onGoalClicked(goal));
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
        notifyDataSetChanged();
    }

    public interface OnGoalClickedListener {
        void onGoalClicked(Goal goal);
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {

        private final TextView goalNameTextView;
        private final TextView amountSavedTextView;
        private final TextView targetAmountTextView;
        private final ProgressBar progressBar;
        private final TextView progressTextView;

        public GoalViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            goalNameTextView = itemView.findViewById(R.id.text_goal_name);
            amountSavedTextView = itemView.findViewById(R.id.text_amount);
            targetAmountTextView = itemView.findViewById(R.id.text_target_amount);
            progressBar = itemView.findViewById(R.id.progress_goal);
            progressTextView = itemView.findViewById(R.id.text_progress);
        }
    }
}
