package com.samaya.myspendings.db.repo;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.samaya.myspendings.db.AppDatabase;
import com.samaya.myspendings.db.dao.SpendingsDao;
import com.samaya.myspendings.db.entity.DMYSpending;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.Date;
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
        executor.execute(() -> dao.insertSpending(spending));
    }

    public void update(Spendings spending) {
        executor.execute(() -> dao.updateSpending(spending));
    }

    public void delete(Spendings spending) {
        executor.execute(() -> dao.deleteSpending(spending));
    }
    public void purge(){
        executor.execute(() -> dao.purge());
    }

    public  LiveData<List<Spendings>> getAllSpendings(){
        return dao.getAllSpendings();
    }

    public  LiveData<Integer> getTotalSpendings(){
        return dao.getTotalSpendings();
    }
    public LiveData<List<DMYSpending>> getMonthlyTotal() { return dao.getMonthlyTotal();  }

    public LiveData<List<DMYSpending>> getYearlyTotal() { return dao.getYearlyTotal();  }
    public LiveData<List<DMYSpending>> getDailyTotal() { return dao.getDailyTotal();  }


    public  LiveData<List<Spendings>> getAllSpendingsFoReport(){
        return dao.getAllSpendingsForReport();
    }

    public  LiveData<List<String>> getAllMonths(){
        return dao.getAllMonths();
    }

    public  LiveData<List<String>> getAllYears(){
        return dao.getAllYears();
    }

    public LiveData<List<DMYSpending>> getDailyTotalForMonthForReport(String monthyear){
        return dao.getDailyTotalForMonthForReport(monthyear);
    }
    public LiveData<List<DMYSpending>> getMonthlyTotalForYearForReport(String year){
        return dao.getMonthlyTotalForYearForReport(year);
    }

    public LiveData<List<Spendings>> getAllSpendingsInRangeForReport(Date start, Date end){
        return dao.getAllSpendingsInRangeForReport(start, end);
    }


}
