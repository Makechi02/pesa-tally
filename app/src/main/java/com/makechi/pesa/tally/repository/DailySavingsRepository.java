package com.makechi.pesa.tally.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.makechi.pesa.tally.dao.DailySavingsDao;
import com.makechi.pesa.tally.database.PesaTallyDatabase;
import com.makechi.pesa.tally.entity.DailySavings;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DailySavingsRepository {
    private final DailySavingsDao dailySavingsDao;
    private final LiveData<List<DailySavings>> allDailySavings;

    public DailySavingsRepository(Application application) {
        PesaTallyDatabase pesaTallyDatabase = PesaTallyDatabase.getDatabase(application);
        this.dailySavingsDao = pesaTallyDatabase.dailySavingsDao();
        this.allDailySavings = dailySavingsDao.getAllDailySavings();
    }

    public LiveData<List<DailySavings>> getAllDailySavings() {
        return allDailySavings;
    }

    public void saveDailySaving(DailySavings dailySavings) {
        PesaTallyDatabase.databaseWriteExecutor.execute(() -> dailySavingsDao.insert(dailySavings));
    }

    public DailySavings getDailySavingsById(int id) {
        AtomicReference<DailySavings> dailySavingsAtomicReference = new AtomicReference<>();
        PesaTallyDatabase.databaseWriteExecutor.execute(() -> dailySavingsAtomicReference.set(dailySavingsDao.getSavingById(id)));
        return dailySavingsAtomicReference.get();
    }

    public LiveData<List<DailySavings>> getDailySavingsByDate(String date) {
        AtomicReference<LiveData<List<DailySavings>>> dailySavingsAtomicReference = new AtomicReference<>();
        PesaTallyDatabase.databaseWriteExecutor.execute(() -> dailySavingsAtomicReference.set(dailySavingsDao.getSavingsByDate(date)));
        return dailySavingsAtomicReference.get();
    }
}
