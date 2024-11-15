package com.makechi.pesa.tally.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.makechi.pesa.tally.dao.DailySavingsDao;
import com.makechi.pesa.tally.dao.GoalDao;
import com.makechi.pesa.tally.dao.TransactionDao;
import com.makechi.pesa.tally.entity.DailySavings;
import com.makechi.pesa.tally.entity.Goal;
import com.makechi.pesa.tally.entity.Transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DailySavings.class, Goal.class, Transaction.class}, version = 1, exportSchema = false)
public abstract class PesaTallyDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile PesaTallyDatabase INSTANCE;

    public static PesaTallyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PesaTallyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), PesaTallyDatabase.class, "pesa_tally_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DailySavingsDao dailySavingsDao();

    public abstract GoalDao goalDao();

    public abstract TransactionDao transactionDao();
}
