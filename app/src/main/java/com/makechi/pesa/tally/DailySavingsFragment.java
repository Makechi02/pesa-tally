package com.makechi.pesa.tally;

import android.os.Bundle;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.makechi.pesa.tally.adapter.DailySavingsAdapter;
import com.makechi.pesa.tally.viewModel.DailySavingsViewModel;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailySavingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailySavingsFragment extends Fragment {

    private DailySavingsViewModel dailySavingsViewModel;
    private CalendarView calendarView;
    private RecyclerView savingsRecyclerView;
    private DailySavingsAdapter dailySavingsAdapter;

    public DailySavingsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DailySavingsFragment.
     */
    public static DailySavingsFragment newInstance() {
        DailySavingsFragment fragment = new DailySavingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_daily_savings, container, false);

        calendarView = view.findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            long dateInMillis = calendar.getTimeInMillis();
        });

        savingsRecyclerView = view.findViewById(R.id.savings_recycler_view);
        savingsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dailySavingsAdapter = new DailySavingsAdapter();
        savingsRecyclerView.setAdapter(dailySavingsAdapter);

        dailySavingsViewModel = new ViewModelProvider(this).get(DailySavingsViewModel.class);
        dailySavingsViewModel.getAllDailySavings().observe(getViewLifecycleOwner(), dailySavings -> dailySavingsAdapter.setDailySavingsList(dailySavings));

        return view;
    }
}