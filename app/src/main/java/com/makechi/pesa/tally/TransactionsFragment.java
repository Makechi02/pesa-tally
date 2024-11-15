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
import com.makechi.pesa.tally.adapter.TransactionsAdapter;
import com.makechi.pesa.tally.util.BetterActivityResult;
import com.makechi.pesa.tally.viewModel.TransactionsViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionsFragment extends Fragment {

    private final BetterActivityResult<Intent, ActivityResult> launcher = BetterActivityResult.registerForActivityResult(this);
    private TransactionsAdapter transactionsAdapter;

    public TransactionsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TransactionsFragment.
     */
    public static TransactionsFragment newInstance() {
        TransactionsFragment fragment = new TransactionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        requireActivity().setTitle(R.string.transactions);

        RecyclerView recyclerView = view.findViewById(R.id.transactions_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        transactionsAdapter = new TransactionsAdapter();
        recyclerView.setAdapter(transactionsAdapter);

        TransactionsViewModel transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
        transactionsViewModel.getAllTransactions().observe(getViewLifecycleOwner(), transactions -> transactionsAdapter.setTransactions(transactions));

        return view;
    }

    private void showToastMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}