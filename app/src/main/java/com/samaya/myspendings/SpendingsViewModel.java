package com.samaya.myspendings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.samaya.myspendings.db.entity.Spendings;
import com.samaya.myspendings.db.repo.SpendingsRepo;
import com.samaya.myspendings.db.entity.MonthlySpending;

import java.util.List;

public class SpendingsViewModel  extends AndroidViewModel {
    private SpendingsRepo repo;
    private LiveData<List<Spendings>> allspendings;
    private LiveData<Integer> totalSpendings;
    private LiveData<List<MonthlySpending>> monthlySpendings;
    public SpendingsViewModel(@NonNull Application application) {
        super(application);
        repo = new SpendingsRepo(application);
        allspendings = repo.getAllSpendings();
        totalSpendings = repo.getTotalSpendings();
        monthlySpendings = repo.getMonthlyTotal();
    }

    public LiveData<List<Spendings>> getAllspendings(){
        return allspendings;
    }

    public LiveData<Integer> getTotalspendings(){
        return totalSpendings;
    }

    public  LiveData<List<MonthlySpending>> getMonthlySpendings(){return  monthlySpendings;}

    void insert(Spendings spending){
        repo.insert(spending);
    }
    void delete(Spendings spending){ repo.delete(spending);}
}
