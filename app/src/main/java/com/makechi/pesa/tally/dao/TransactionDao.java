package com.makechi.pesa.tally.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.makechi.pesa.tally.entity.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {
    @Insert
    void insert(Transaction transaction);

    @Query("SELECT * FROM transactions WHERE id = :id")
    Transaction getTransactionById(int id);

    @Query("SELECT * FROM transactions WHERE goal = :goal ORDER BY date ASC")
    LiveData<List<Transaction>> getTransactionsByGoal(int goal);

    @Query("SELECT * FROM transactions ORDER BY date ASC")
    LiveData<List<Transaction>> getAllTransactions();

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

}
