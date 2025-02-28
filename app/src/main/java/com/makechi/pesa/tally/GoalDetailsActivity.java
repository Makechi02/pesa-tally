package com.makechi.pesa.tally;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.makechi.pesa.tally.adapter.GoalDetailsPagerAdapter;
import com.makechi.pesa.tally.entity.Goal;
import com.makechi.pesa.tally.util.BetterActivityResult;
import com.makechi.pesa.tally.viewModel.GoalViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GoalDetailsActivity extends BaseActivity {

    protected final BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerForActivityResult(this);
    private GoalViewModel goalViewModel;
    private Goal currentGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int goalId = getIntent().getIntExtra("GOAL_ID", -1);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        GoalDetailsPagerAdapter pagerAdapter = new GoalDetailsPagerAdapter(this, goalId);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(position == 0 ? R.string.goals : R.string.transactions))
                .attach();

        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
        goalViewModel.getGoalById(goalId).observe(this, this::populateGoalDetails);
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
            currentGoal = goal;
            Objects.requireNonNull(getSupportActionBar()).setTitle(currentGoal.getName());
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