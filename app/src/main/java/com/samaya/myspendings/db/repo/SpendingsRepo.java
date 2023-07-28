package com.samaya.myspendings.db.repo;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.samaya.myspendings.db.AppDatabase;
import com.samaya.myspendings.db.dao.SpendingsDao;
import com.samaya.myspendings.db.entity.MonthlyOrYearlySpending;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpendingsRepo {

    private SpendingsDao dao;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    public SpendingsRepo(Application application){
        AppDatabase database = AppDatabase.getDatabase(application);
        dao = database.spendingsDao();
    }

    public void insert(Spendings spending) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertSpending(spending);
            }
        });
    }

    public void update(Spendings spending) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateSpending(spending);
            }
        });
    }

    public void delete(Spendings spending) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteSpending(spending);
            }
        });
    }
    public void purge(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.purge();
            }
        });
    }

    public  LiveData<List<Spendings>> getAllSpendings(){
        return dao.getAllSpendings();
    }

    public  LiveData<Integer> getTotalSpendings(){
        return dao.getTotalSpendings();
    }
    public LiveData<List<MonthlyOrYearlySpending>> getMonthlyTotal() { return dao.getMonthlyTotal();  }

    public LiveData<List<MonthlyOrYearlySpending>> getYearlyTotal() { return dao.getYearlyTotal();  }

}
