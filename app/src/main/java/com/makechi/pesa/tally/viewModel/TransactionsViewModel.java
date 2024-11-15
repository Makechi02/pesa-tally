package com.makechi.pesa.tally.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.makechi.pesa.tally.entity.Transaction;
import com.makechi.pesa.tally.repository.TransactionsRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TransactionsViewModel extends AndroidViewModel {
    private final TransactionsRepository transactionsRepository;
    private final LiveData<List<Transaction>> allTransactions;

    public TransactionsViewModel(@NotNull Application application) {
        super(application);

        transactionsRepository = new TransactionsRepository(application);
        allTransactions = transactionsRepository.getAllTransactions();
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    public Transaction getDailyTransactionById(int id) {
        return transactionsRepository.getTransactionById(id);
    }

    public LiveData<List<Transaction>> getTransactionsByGoal(int goal) {
        return transactionsRepository.getTransactionsByGoal(goal);
    }
}
