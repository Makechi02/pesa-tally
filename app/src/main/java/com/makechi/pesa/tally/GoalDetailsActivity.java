package com.makechi.pesa.tally;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.makechi.pesa.tally.adapter.TransactionsAdapter;
import com.makechi.pesa.tally.entity.Goal;
import com.makechi.pesa.tally.entity.Transaction;
import com.makechi.pesa.tally.util.BetterActivityResult;
import com.makechi.pesa.tally.util.Formatter;
import com.makechi.pesa.tally.viewModel.GoalViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GoalDetailsActivity extends BaseActivity {

    protected final BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerForActivityResult(this);
    private GoalViewModel goalViewModel;
    private TextView descriptionTextView, progressTextView, savedTextView, targetTextView, remainingTextView,
            deadlineTextView, transactionCountTextView;
    private ProgressBar progressBar;
    private LinearLayout transactionsLayout;
    private TransactionsAdapter transactionsAdapter;
    private Goal currentGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int goalId = getIntent().getIntExtra("GOAL_ID", -1);
        descriptionTextView = findViewById(R.id.text_view_goal_description);
        savedTextView = findViewById(R.id.text_view_goal_saved);
        targetTextView = findViewById(R.id.text_view_target);
        remainingTextView = findViewById(R.id.text_view_remaining);
        deadlineTextView = findViewById(R.id.text_view_goal_deadline);
        transactionCountTextView = findViewById(R.id.text_view_goal_transactions);
        progressBar = findViewById(R.id.progress_goal);
        progressTextView = findViewById(R.id.text_progress);
        transactionsLayout = findViewById(R.id.layout_transactions);
        RecyclerView transactionsRecyclerView = findViewById(R.id.recycler_view_goal_transactions);

        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionsAdapter = new TransactionsAdapter();
        transactionsRecyclerView.setAdapter(transactionsAdapter);

        ExtendedFloatingActionButton extendedFloatingActionButton = findViewById(R.id.fab_add_contribution);
        extendedFloatingActionButton.setOnClickListener(view -> handleAddContribution());

        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
        goalViewModel.getGoalById(goalId).observe(this, this::populateGoalDetails);
        goalViewModel.getTransactionByGoal(goalId).observe(this, transactions -> {
            int count = transactions.size();

            if (transactions.isEmpty()) {
                transactionsLayout.setVisibility(View.GONE);
            }

            transactionCountTextView.setText(String.valueOf(count));
            transactionsAdapter.setTransactions(transactions);
        });
    }

    private void handleAddContribution() {
        Intent intent = new Intent(this, AddContributionActivity.class);
        activityLauncher.launch(intent, result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                String date = data.getStringExtra("date");
                String type = data.getStringExtra("type");
                double amount = data.getDoubleExtra("amount", 0);

                Transaction transaction = new Transaction(currentGoal.getId(), date, amount, type);
                goalViewModel.addTransaction(transaction, currentGoal);
                showToastMessage(getString(R.string.contribution_add_success));
            } else {
                showToastMessage(getString(R.string.contribution_not_saved));
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goal_details;
    }

    @Override
    protected void inflateAdditionalMenuItems(Menu menu) {
        getMenuInflater().inflate(R.menu.goal_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (currentGoal != null) {
            if (item.getItemId() == R.id.action_delete) {
                promptDeleteGoal(currentGoal);
                return true;
            } else if (item.getItemId() == R.id.action_edit) {
                startEditActivity(currentGoal);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateGoalDetails(Goal goal) {
        if (goal != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(goal.getName());

            currentGoal = goal;

            double remainingAmount = goal.getTargetAmount() - goal.getAmountSaved();

            descriptionTextView.setText(goal.getDescription());
            savedTextView.setText(Formatter.formatMoneyWithCurrency(goal.getAmountSaved()));
            targetTextView.setText(Formatter.formatMoneyWithCurrency(goal.getTargetAmount()));
            remainingTextView.setText(Formatter.formatMoneyWithCurrency(remainingAmount));
            deadlineTextView.setText(goal.getDeadline());

            int progress = (int) ((goal.getAmountSaved() / goal.getTargetAmount()) * 100);
            progressBar.setProgress(progress);
            progressTextView.setText(progress + "%");
        } else {
            showErrorToast("Failed to fetch goal details");
        }
    }

    private void promptDeleteGoal(Goal goal) {
        DeleteConfirmationBottomSheet bottomSheet = DeleteConfirmationBottomSheet.newInstance(
                getString(R.string.delete_goal),
                getString(R.string.confirm_delete_goal),
                goal.getName()
        );
        bottomSheet.setOnDeleteConfirmListener(() -> deleteGoal(goal));
        bottomSheet.show(getSupportFragmentManager(), "DeleteGoalBottomSheet");
    }

    private void deleteGoal(Goal goal) {
        goalViewModel.deleteGoal(goal);
        getOnBackPressedDispatcher().onBackPressed();
        showToastMessage("Goal deleted successfully");
    }

    private void startEditActivity(Goal goal) {
        Intent intent = new Intent(this, EditGoalActivity.class);
        intent.putExtra("GOAL_ID", goal.getId());

        activityLauncher.launch(intent, result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Intent data = result.getData();

                String name = data.getStringExtra("name");
                String description = data.getStringExtra("description");
                double targetAmount = data.getDoubleExtra("targetAmount", 0);
                String deadline = data.getStringExtra("deadline");

                goal.setName(name);
                goal.setDescription(description);
                goal.setTargetAmount(targetAmount);
                goal.setDeadline(deadline);

                goalViewModel.updateGoal(goal);
                showToastMessage("Goal Updated successfully");
            } else {
                showToastMessage("Goal not updated");
            }
        });
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}