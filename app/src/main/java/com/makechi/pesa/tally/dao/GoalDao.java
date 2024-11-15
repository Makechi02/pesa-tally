package com.makechi.pesa.tally.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.makechi.pesa.tally.entity.Goal;
import com.makechi.pesa.tally.entity.Transaction;

import java.util.List;

@Dao
public interface GoalDao {
    @Insert
    void insert(Goal goal);

    @Query("SELECT * FROM goals WHERE id = :id")
    LiveData<Goal> getGoalById(int id);

    @Query("SELECT * FROM goals ORDER BY deadline ASC")
    LiveData<List<Goal>> getAllGoals();

    @Update
    void update(Goal goal);

    @Delete
    void delete(Goal goal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTransaction(Transaction transaction);
}
