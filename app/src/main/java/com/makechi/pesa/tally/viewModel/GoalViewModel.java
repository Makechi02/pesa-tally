package com.makechi.pesa.tally.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.makechi.pesa.tally.entity.Goal;
import com.makechi.pesa.tally.entity.Transaction;
import com.makechi.pesa.tally.repository.GoalRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GoalViewModel extends AndroidViewModel {

    private final GoalRepository goalRepository;
    private final LiveData<List<Goal>> allGoals;

    public GoalViewModel(@NotNull Application application) {
        super(application);

        goalRepository = new GoalRepository(application);
        allGoals = goalRepository.getAllGoals();
    }

    public LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }

    public LiveData<List<Transaction>> getTransactionByGoal(int goalId) {
        return goalRepository.getTransactionsByGoal(goalId);
    }

    public LiveData<Goal> getGoalById(int id) {
        return goalRepository.getGoalById(id);
    }

    public void saveGoal(Goal goal) {
        goalRepository.saveGoal(goal);
    }

    public void updateGoal(Goal goal) {
        goalRepository.updateGoal(goal);
    }

    public void deleteGoal(Goal goal) {
        goalRepository.deleteGoal(goal);
    }

    public void addTransaction(Transaction transaction, Goal goal) {
        goalRepository.addTransaction(transaction, goal);
    }
}
