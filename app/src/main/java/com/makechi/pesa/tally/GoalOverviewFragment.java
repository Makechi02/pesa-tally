package com.makechi.pesa.tally;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.makechi.pesa.tally.entity.Goal;
import com.makechi.pesa.tally.viewModel.GoalViewModel;

public class GoalOverviewFragment extends Fragment {

    private TextView descriptionTextView;
    private TextView progressTextView;
    private TextView savedTextView;
    private TextView targetTextView;
    private TextView remainingTextView;
    private TextView deadlineTextView;
    private TextView transactionCountTextView;
    private ProgressBar progressBar;
    private JarView jarView;

    public GoalOverviewFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GoalOverviewFragment.
     */
    public static GoalOverviewFragment newInstance(int goalId) {
        GoalOverviewFragment fragment = new GoalOverviewFragment();
        Bundle args = new Bundle();
        args.putInt("GOAL_ID", goalId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_overview, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        int goalId = bundle.getInt("GOAL_ID", -1);

        descriptionTextView = view.findViewById(R.id.text_view_goal_description);
//        savedTextView = view.findViewById(R.id.text_view_goal_saved);
//        targetTextView = view.findViewById(R.id.text_view_target);
//        remainingTextView = view.findViewById(R.id.text_view_remaining);
        deadlineTextView = view.findViewById(R.id.text_view_goal_deadline);
        transactionCountTextView = view.findViewById(R.id.text_view_goal_transactions);
//        progressBar = view.findViewById(R.id.progress_goal);
//        progressTextView = view.findViewById(R.id.text_progress);
        jarView = view.findViewById(R.id.jar_view);

        GoalViewModel goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
        goalViewModel.getGoalById(goalId).observe(this, this::populateGoalDetails);
        goalViewModel.getTransactionByGoal(goalId).observe(this, transactions -> {
            int count = transactions.size();

            transactionCountTextView.setText(String.valueOf(count));
        });

        return view;
    }

    private void populateGoalDetails(Goal goal) {
        if (goal != null) {
            double remainingAmount = goal.getTargetAmount() - goal.getAmountSaved();

            descriptionTextView.setText(goal.getDescription());
//            savedTextView.setText(Formatter.formatMoneyWithCurrency(goal.getAmountSaved()));
//            targetTextView.setText(Formatter.formatMoneyWithCurrency(goal.getTargetAmount()));
//            remainingTextView.setText(Formatter.formatMoneyWithCurrency(remainingAmount));
            deadlineTextView.setText(goal.getDeadline());

            float progress = (float) (goal.getAmountSaved() / goal.getTargetAmount());
            jarView.setProgress(progress);
//            progressBar.setProgress(progress);
//            progressTextView.setText(progress + "%");
        } else {
            showErrorToast("Failed to fetch goal details");
        }
    }

    private void showErrorToast(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show();
    }
}
