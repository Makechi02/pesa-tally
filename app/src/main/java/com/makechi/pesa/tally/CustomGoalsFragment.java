package com.makechi.pesa.tally;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.makechi.pesa.tally.adapter.GoalsAdapter;
import com.makechi.pesa.tally.entity.Goal;
import com.makechi.pesa.tally.util.BetterActivityResult;
import com.makechi.pesa.tally.viewModel.GoalViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomGoalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomGoalsFragment extends Fragment {

    private final BetterActivityResult<Intent, ActivityResult> launcher = BetterActivityResult.registerForActivityResult(this);
    private GoalViewModel goalViewModel;
    private GoalsAdapter goalsAdapter;

    public CustomGoalsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CustomGoalsFragment.
     */
    public static CustomGoalsFragment newInstance() {
        CustomGoalsFragment fragment = new CustomGoalsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_goals, container, false);

        goalsAdapter = new GoalsAdapter(goal -> {
            Intent intent = new Intent(requireContext(), GoalDetailsActivity.class);
            intent.putExtra("GOAL_ID", goal.getId());
            startActivity(intent);
        });

        RecyclerView goalsRecyclerView = view.findViewById(R.id.custom_goals_recycler_view);
        goalsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        goalsRecyclerView.setAdapter(goalsAdapter);

        ExtendedFloatingActionButton extendedFloatingActionButton = view.findViewById(R.id.fab_add_goal);
        extendedFloatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddGoalActivity.class);
            launcher.launch(intent, result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();

                    String name = data.getStringExtra("name");
                    String description = data.getStringExtra("description");
                    String targetAmount = data.getStringExtra("targetAmount");
                    String deadline = data.getStringExtra("deadline");

                    Goal newGoal = new Goal(name, description, Double.parseDouble(targetAmount), deadline);
                    goalViewModel.saveGoal(newGoal);

                    showToastMessage(getString(R.string.goal_add_success));
                } else {
                    showToastMessage(getString(R.string.goal_not_saved));
                }
            });
        });

        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
        goalViewModel.getAllGoals().observe(getViewLifecycleOwner(), goals -> goalsAdapter.setGoals(goals));

        return view;
    }

    private void showToastMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}