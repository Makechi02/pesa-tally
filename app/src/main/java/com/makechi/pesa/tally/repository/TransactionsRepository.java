package com.makechi.pesa.tally.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.makechi.pesa.tally.dao.TransactionDao;
import com.makechi.pesa.tally.database.PesaTallyDatabase;
import com.makechi.pesa.tally.entity.Transaction;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TransactionsRepository {
    private final TransactionDao transactionDao;
    private final LiveData<List<Transaction>> allTransactions;

    public TransactionsRepository(Application application) {
        PesaTallyDatabase pesaTallyDatabase = PesaTallyDatabase.getDatabase(application);
        transactionDao = pesaTallyDatabase.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    public void saveTransaction(Transaction transaction) {
        PesaTallyDatabase.databaseWriteExecutor.execute(() -> transactionDao.insert(transaction));
    }

    public Transaction getTransactionById(int id) {
        AtomicReference<Transaction> transactionAtomicReference = new AtomicReference<>();
        PesaTallyDatabase.databaseWriteExecutor.execute(() -> transactionAtomicReference.set(transactionDao.getTransactionById(id)));
        return transactionAtomicReference.get();
    }

    public LiveData<List<Transaction>> getTransactionsByGoal(int goal) {
        AtomicReference<LiveData<List<Transaction>>> transactionsAtomicReference = new AtomicReference<>();
        PesaTallyDatabase.databaseWriteExecutor.execute(() -> transactionsAtomicReference.set(transactionDao.getTransactionsByGoal(goal)));
        return transactionsAtomicReference.get();
    }
}
