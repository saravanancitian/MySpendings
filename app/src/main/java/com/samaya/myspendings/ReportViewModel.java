package com.samaya.myspendings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.samaya.myspendings.db.entity.Spendings;
import com.samaya.myspendings.db.repo.SpendingsRepo;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    private SpendingsRepo repo;
    private LiveData<List<Spendings>> allspendingsForReport;

    public ReportViewModel(@NonNull  Application application){
        super(application);
        repo = new SpendingsRepo(application);
        allspendingsForReport = repo.getAllSpendingsFoReport();


    }

    public  LiveData<List<Spendings>> getDailySpendingsForReport(){return  allspendingsForReport;}


}