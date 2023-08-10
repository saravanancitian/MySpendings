package com.samaya.myspendings.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.samaya.myspendings.db.entity.DMYSpending;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.Date;
import java.util.List;

@Dao
public interface SpendingsDao {

    @Insert
    void insertSpending(Spendings spending);

    @Update
    void updateSpending(Spendings spending);

    @Delete
    void deleteSpending(Spendings spending);

    @Query("DELETE FROM spendings  where state = 2")
    void purge();

    @Query("SELECT * FROM spendings where state = 1 ORDER BY whendt desc ")
    LiveData<List<Spendings>> getAllSpendings();

    @Query("SELECT sum(sp.amount) FROM spendings as sp where sp.state = 1")
    LiveData<Integer> getTotalSpendings();

    @Query("SELECT sum(sp.amount) as amount , strftime('%m-%Y', sp.whendt) as dmyDate FROM spendings as sp  where sp.state = 1 group by dmyDate")
    LiveData<List<DMYSpending>> getMonthlyTotal();

    @Query("SELECT sum(sp.amount) as amount , strftime('%Y', sp.whendt) as dmyDate FROM spendings as sp where sp.state = 1 group by dmyDate")
    LiveData<List<DMYSpending>> getYearlyTotal();

    @Query("SELECT sum(sp.amount) as amount , strftime('%d-%m-%Y', sp.whendt) as dmyDate FROM spendings as sp  where sp.state = 1 group by dmyDate")
    LiveData<List<DMYSpending>> getDailyTotal();


    @Query("SELECT * FROM spendings where state = 1 ORDER BY whendt asc ")
    LiveData<List<Spendings>> getAllSpendingsForReport();

    @Query("SELECT strftime('%m-%Y', sp.whendt) as dmyDate FROM spendings as sp  where sp.state = 1 group by dmyDate")
    LiveData<List<String>> getAllMonths();

    @Query("SELECT strftime('%Y', sp.whendt) as dmyDate FROM spendings as sp where sp.state = 1 group by dmyDate")
    LiveData<List<String>> getAllYears();


    //TODO
    @Query("SELECT sum(sp.amount) as amount , strftime('%d-%m-%Y', sp.whendt) as dmyDate FROM spendings as sp  where sp.state = 1 and strftime('%m-%Y', sp.whendt) = :monthyear  group by dmyDate")
    LiveData<List<DMYSpending>> getDailyTotalForMonthForReport(String monthyear);

    //TODO
    @Query("SELECT sum(sp.amount) as amount , strftime('%m-%Y', sp.whendt) as dmyDate FROM spendings as sp  where sp.state = 1 and strftime('%Y', sp.whendt) = :year group by dmyDate")
    LiveData<List<DMYSpending>> getMonthlyTotalForYearForReport(String year);

    @Query("SELECT * FROM spendings where whendt between :start and :end and state = 1 ORDER BY whendt asc ")
    LiveData<List<Spendings>> getAllSpendingsInRangeForReport(Date start, Date end);
}
