package com.samaya.myspendings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.samaya.myspendings.db.entity.Spendings;
import com.samaya.myspendings.db.repo.SpendingsRepo;

import java.util.List;

public class SpendingsViewModel  extends AndroidViewModel {
    private SpendingsRepo repo;
    private LiveData<List<Spendings>> allspendings;
    private LiveData<Integer> totalSpendings;
    public SpendingsViewModel(@NonNull Application application) {
        super(application);
        repo = new SpendingsRepo(application);
        allspendings = repo.getAllSpendings();
        totalSpendings = repo.getTotalSpendings();
    }

    LiveData<List<Spendings>> getAllspendings(){
        return allspendings;
    }

    LiveData<Integer> getTotalspendings(){
        return totalSpendings;
    }

    void insert(Spendings spending){
        repo.insert(spending);
    }
}
