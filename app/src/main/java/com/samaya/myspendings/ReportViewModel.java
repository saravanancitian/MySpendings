package com.samaya.myspendings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.samaya.myspendings.db.entity.DMYSpending;
import com.samaya.myspendings.db.entity.Spendings;
import com.samaya.myspendings.db.repo.SpendingsRepo;

import java.util.Calendar;
import java.util.List;

public class ReportViewModel extends AndroidViewModel {

    private SpendingsRepo repo;
    private LiveData<List<Spendings>> allspendingsForReport;
    private LiveData<List<String>> allmonths;
    private LiveData<List<String>> allyears;

    public ReportViewModel(@NonNull Application application) {
        super(application);
        repo = ((MySpendingsApp)application).getRepo();
        allspendingsForReport = repo.getAllSpendingsFoReport();
        allmonths = repo.getAllMonths();
        allyears = repo.getAllYears();
    }
    public  LiveData<List<Spendings>> getDailySpendingsForReport(){return  allspendingsForReport;}

    public LiveData<List<String>> getAllmonths(){
        return allmonths;
    }

    public LiveData<List<String>> getAllyears(){
        return allyears;
    }

    public LiveData<List<DMYSpending>> getDailyTotalForMonth(String monthyear){
        return repo.getDailyTotalForMonthForReport(monthyear);
    }

    public LiveData<List<DMYSpending>> getMonthlyTotalForYearForReport(String year){
        return repo.getMonthlyTotalForYearForReport(year);
    }

}
