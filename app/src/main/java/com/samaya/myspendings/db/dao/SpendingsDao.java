package com.samaya.myspendings.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.samaya.myspendings.db.entity.DMYSpending;
import com.samaya.myspendings.db.entity.Spendings;

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
}
