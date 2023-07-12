package com.samaya.myspendings.db.repo;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import com.samaya.myspendings.db.AppDatabase;
import com.samaya.myspendings.db.dao.SpendingsDao;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpendingsRepo {

    private SpendingsDao dao;
    LiveData<List<Spendings>> allSpendings;

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

    public  LiveData<List<Spendings>> getAllSpendings(){
        return dao.getAllSpendings();
    }


}
