package com.samaya.myspendings;

import android.app.Application;

import com.google.android.material.color.DynamicColors;
import com.samaya.myspendings.db.repo.SpendingsRepo;

public class MySpendingsApp extends Application {



    private SpendingsRepo repo;
    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
        repo = new SpendingsRepo(this);
    }

    public SpendingsRepo getRepo() {
        return repo;
    }
}
