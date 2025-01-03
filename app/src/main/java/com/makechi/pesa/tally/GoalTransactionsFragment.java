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
import com.makechi.pesa.tally.adapter.TransactionsAdapter;
import com.makechi.pesa.tally.entity.Goal;
import com.makechi.pesa.tally.entity.Transaction;
import com.makechi.pesa.tally.util.BetterActivityResult;
import com.makechi.pesa.tally.viewModel.GoalViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalTransactionsFragment extends Fragment {

    private final BetterActivityResult<Intent, ActivityResult> launcher = BetterActivityResult.registerForActivityResult(this);
    private GoalViewModel goalViewModel;
    private TransactionsAdapter transactionsAdapter;
    private Goal currentGoal;

    public GoalTransactionsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GoalTransactionsFragment.
     */
    public static GoalTransactionsFragment newInstance(int goalId) {
        GoalTransactionsFragment fragment = new GoalTransactionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_goal_transactions, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        int goalId = bundle.getInt("GOAL_ID", -1);

        RecyclerView transactionsRecyclerView = view.findViewById(R.id.recycler_view_goal_transactions);

        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        transactionsAdapter = new TransactionsAdapter();
        transactionsRecyclerView.setAdapter(transactionsAdapter);

        ExtendedFloatingActionButton extendedFloatingActionButton = view.findViewById(R.id.fab_add_contribution);
        extendedFloatingActionButton.setOnClickListener(v -> handleAddContribution());

        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
        goalViewModel.getGoalById(goalId).observe(this, this::populateGoalDetails);
        goalViewModel.getTransactionByGoal(goalId).observe(this, transactions -> transactionsAdapter.setTransactions(transactions));

        return view;
    }

    private void handleAddContribution() {
        Intent intent = new Intent(requireActivity(), AddContributionActivity.class);
        launcher.launch(intent, result -> {
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

    private void populateGoalDetails(Goal goal) {
        if (goal != null) {
            currentGoal = goal;
        } else {
            showErrorToast("Failed to fetch goal details");
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}