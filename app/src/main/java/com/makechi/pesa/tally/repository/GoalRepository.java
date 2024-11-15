package com.makechi.pesa.tally.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.makechi.pesa.tally.dao.GoalDao;
import com.makechi.pesa.tally.dao.TransactionDao;
import com.makechi.pesa.tally.database.PesaTallyDatabase;
import com.makechi.pesa.tally.entity.Goal;
import com.makechi.pesa.tally.entity.Transaction;
import com.makechi.pesa.tally.entity.TransactionType;

import java.util.List;

public class GoalRepository {
    private final GoalDao goalDao;
    private final TransactionDao transactionDao;
    private final LiveData<List<Goal>> allGoals;

    public GoalRepository(Application application) {
        PesaTallyDatabase pesaTallyDatabase = PesaTallyDatabase.getDatabase(application);
        goalDao = pesaTallyDatabase.goalDao();
        transactionDao = pesaTallyDatabase.transactionDao();
        allGoals = goalDao.getAllGoals();
    }

    public LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }

    public LiveData<List<Transaction>> getTransactionsByGoal(int goalId) {
        return transactionDao.getTransactionsByGoal(goalId);
    }

    public void saveGoal(Goal goal) {
        PesaTallyDatabase.databaseWriteExecutor.execute(() -> goalDao.insert(goal));
    }

    public LiveData<Goal> getGoalById(int id) {
        return goalDao.getGoalById(id);
    }

    public void updateGoal(Goal goal) {
        PesaTallyDatabase.databaseWriteExecutor.execute(() -> goalDao.update(goal));
    }

    public void deleteGoal(Goal goal) {
        PesaTallyDatabase.databaseWriteExecutor.execute(() -> goalDao.delete(goal));
    }

    public void addTransaction(Transaction transaction, Goal goal) {
        TransactionType transactionType = TransactionType.valueOf(transaction.getType());

        PesaTallyDatabase.databaseWriteExecutor.execute(() -> {
            if (goal != null) {
                double updatedAmount = switch (transactionType) {
                    case WITHDRAWAL -> goal.getAmountSaved() - transaction.getAmount();
                    case DEPOSIT -> goal.getAmountSaved() + transaction.getAmount();
                };
                goal.setAmountSaved(updatedAmount);
                goalDao.update(goal);
                transactionDao.insert(transaction);
            }
        });
    }
}
