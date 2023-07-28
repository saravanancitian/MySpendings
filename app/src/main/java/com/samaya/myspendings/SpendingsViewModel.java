package com.samaya.myspendings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.samaya.myspendings.db.entity.MonthlyOrYearlySpending;
import com.samaya.myspendings.db.entity.Spendings;
import com.samaya.myspendings.db.repo.SpendingsRepo;

import java.util.Date;
import java.util.List;

public class SpendingsViewModel  extends AndroidViewModel {
    private SpendingsRepo repo;
    private LiveData<List<Spendings>> allspendings;
    private LiveData<Integer> totalSpendings;
    private LiveData<List<MonthlyOrYearlySpending>> monthlySpendings;

    private LiveData<List<MonthlyOrYearlySpending>> yearlySpendings;

    public SpendingsViewModel(@NonNull Application application) {
        super(application);
        repo = new SpendingsRepo(application);
        allspendings = repo.getAllSpendings();
        totalSpendings = repo.getTotalSpendings();
        monthlySpendings = repo.getMonthlyTotal();
        yearlySpendings = repo.getYearlyTotal();
    }

    public LiveData<List<Spendings>> getAllspendings(){
        return allspendings;
    }

    public LiveData<Integer> getTotalspendings(){
        return totalSpendings;
    }

    public  LiveData<List<MonthlyOrYearlySpending>> getMonthlySpendings(){return  monthlySpendings;}

    public  LiveData<List<MonthlyOrYearlySpending>> getYearlySpendings(){return  yearlySpendings;}

    void insert(Spendings spending){
        spending.createdDt = new Date(System.currentTimeMillis());
        spending.updatedDt = spending.createdDt;
        spending.state = Spendings.STATE_CREATED;
        repo.insert(spending);
    }

    void setCandelete(Spendings spending){
        spending.updatedDt = new Date(System.currentTimeMillis());
        spending.state = Spendings.STATE_READY_TO_DELETED;
        repo.update(spending);
    }
    void delete(Spendings spending){
        spending.updatedDt = new Date(System.currentTimeMillis());
        spending.state = Spendings.STATE_DELETED;
        repo.update(spending);
    }

    void undoDelete(Spendings spending){
        spending.updatedDt = new Date(System.currentTimeMillis());
        spending.state = Spendings.STATE_CREATED;
        repo.update(spending);
    }
    void update(Spendings spending){
        spending.updatedDt = new Date(System.currentTimeMillis());
        repo.update(spending);
    }

    void purge(){
        repo.purge();
    }
}
