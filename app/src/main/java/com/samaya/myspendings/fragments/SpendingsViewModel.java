package com.samaya.myspendings.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.samaya.myspendings.MySpendingsApp;
import com.samaya.myspendings.db.entity.DMYSpending;
import com.samaya.myspendings.db.entity.Spendings;
import com.samaya.myspendings.db.repo.SpendingsRepo;

import java.util.Date;
import java.util.List;

public class SpendingsViewModel  extends AndroidViewModel {

    private SpendingsRepo repo;

    private LiveData<List<Spendings>> allspendings;
    private LiveData<Integer> totalSpendings;
    private LiveData<List<DMYSpending>> monthlySpendings;

    private LiveData<List<DMYSpending>> yearlySpendings;
    private LiveData<List<DMYSpending>> dailySpendings;


    public SpendingsViewModel(@NonNull Application application) {
        super(application);
        repo = ((MySpendingsApp)application).getRepo();
        allspendings = repo.getAllSpendings();
        totalSpendings = repo.getTotalSpendings();
        monthlySpendings = repo.getMonthlyTotal();
        yearlySpendings = repo.getYearlyTotal();
        dailySpendings = repo.getDailyTotal();

    }

    public LiveData<List<Spendings>> getAllspendings(){
        return allspendings;
    }

    public LiveData<Integer> getTotalspendings(){
        return totalSpendings;
    }

    public  LiveData<List<DMYSpending>> getMonthlySpendings(){return  monthlySpendings;}

    public  LiveData<List<DMYSpending>> getYearlySpendings(){return  yearlySpendings;}

    public  LiveData<List<DMYSpending>> getDailySpendings(){return  dailySpendings;}



    public void insert(Spendings spending){
        spending.createdDt = new Date(System.currentTimeMillis());
        spending.updatedDt = spending.createdDt;
        spending.state = Spendings.STATE_CREATED;
        repo.insert(spending);
    }

    public void setCandelete(Spendings spending){
        spending.updatedDt = new Date(System.currentTimeMillis());
        spending.state = Spendings.STATE_READY_TO_DELETED;
        repo.update(spending);
    }
    public void delete(Spendings spending){
        spending.updatedDt = new Date(System.currentTimeMillis());
        spending.state = Spendings.STATE_DELETED;
        repo.update(spending);
    }

    public void undoDelete(Spendings spending){
        spending.updatedDt = new Date(System.currentTimeMillis());
        spending.state = Spendings.STATE_CREATED;
        repo.update(spending);
    }
    public void update(Spendings spending){
        spending.updatedDt = new Date(System.currentTimeMillis());
        repo.update(spending);
    }

    public void purge(){
        repo.purge();
    }
}
