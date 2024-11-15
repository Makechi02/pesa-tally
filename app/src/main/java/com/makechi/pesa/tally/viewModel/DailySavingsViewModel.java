package com.makechi.pesa.tally.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.makechi.pesa.tally.entity.DailySavings;
import com.makechi.pesa.tally.repository.DailySavingsRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DailySavingsViewModel extends AndroidViewModel {
    private final DailySavingsRepository dailySavingsRepository;
    private final LiveData<List<DailySavings>> allDailySavings;

    public DailySavingsViewModel(@NotNull Application application) {
        super(application);
        dailySavingsRepository = new DailySavingsRepository(application);
        this.allDailySavings = dailySavingsRepository.getAllDailySavings();
    }

    public LiveData<List<DailySavings>> getAllDailySavings() {
        return allDailySavings;
    }

    public void saveDailySavings(DailySavings dailySavings) {
        dailySavingsRepository.saveDailySaving(dailySavings);
    }

    public DailySavings getDailySavingById(int id) {
        return dailySavingsRepository.getDailySavingsById(id);
    }

    public LiveData<List<DailySavings>> getSavingsByDate(String date) {
        return dailySavingsRepository.getDailySavingsByDate(date);
    }
}
