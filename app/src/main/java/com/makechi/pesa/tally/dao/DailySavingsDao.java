package com.makechi.pesa.tally.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.makechi.pesa.tally.entity.DailySavings;

import java.util.List;

@Dao
public interface DailySavingsDao {
    @Insert
    void insert(DailySavings dailySavings);

    @Query("SELECT * FROM daily_savings WHERE date = :date")
    LiveData<List<DailySavings>> getSavingsByDate(String date);

    @Query("SELECT * FROM daily_savings WHERE id = :id")
    DailySavings getSavingById(int id);

    @Query("SELECT * FROM daily_savings ORDER BY date ASC")
    LiveData<List<DailySavings>> getAllDailySavings();

    @Update
    void update(DailySavings dailySavings);
}
