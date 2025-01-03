package com.makechi.pesa.tally.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.makechi.pesa.tally.GoalOverviewFragment;
import com.makechi.pesa.tally.GoalTransactionsFragment;
import org.jetbrains.annotations.NotNull;

public class GoalDetailsPagerAdapter extends FragmentStateAdapter {

    private final int goalId;

    public GoalDetailsPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity, int goalId) {
        super(fragmentActivity);
        this.goalId = goalId;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int i) {
        return i == 0 ? GoalOverviewFragment.newInstance(goalId) : GoalTransactionsFragment.newInstance(goalId);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
