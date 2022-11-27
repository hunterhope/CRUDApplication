package com.example.crudapplication.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.crudapplication.db.entity.NetWorkIOResult;

import java.time.LocalDateTime;
import java.util.Optional;

@Dao
public interface NetWorkIOResultDao {
    @Insert
    void insert(NetWorkIOResult netWorkIOResult);

    @Query("select * from networkioresult where hasShow==0 order by dataTime limit 1")
    LiveData<Optional<NetWorkIOResult>> latestResult();

    @Query("update networkioresult set hasShow=1 where dataTime==:key")
    void msgHasShow(LocalDateTime key);
}
