package com.samaya.myspendings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.samaya.myspendings.db.entity.Spendings;
import com.samaya.myspendings.db.repo.SpendingsRepo;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {

    private SpendingsRepo repo;
    private LiveData<List<Spendings>> allspendingsForReport;

    public ReportViewModel(@NonNull Application application) {
        super(application);
        repo = ((MySpendingsApp)application).getRepo();
        allspendingsForReport = repo.getAllSpendingsFoReport();
    }
    public  LiveData<List<Spendings>> getDailySpendingsForReport(){return  allspendingsForReport;}

}
