package com.makechi.pesa.tally.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.makechi.pesa.tally.CustomGoalsFragment;
import com.makechi.pesa.tally.DailySavingsFragment;
import org.jetbrains.annotations.NotNull;

public class GoalsPagerAdapter extends FragmentStateAdapter {

    public GoalsPagerAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int i) {
        return i == 0 ? DailySavingsFragment.newInstance() : CustomGoalsFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
