package com.example.crudapplication.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NetWorkIOResult {

    @PrimaryKey
    @NonNull
    public LocalDateTime dataTime;
    public Integer msgId;//自己給訊息一個分類,將來可以國際化
    public Integer hasShow;//是不是顯示過
    public String msg;//原始IO回來訊息

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetWorkIOResult that = (NetWorkIOResult) o;
        return dataTime.equals(that.dataTime) && Objects.equals(msgId, that.msgId) && Objects.equals(hasShow, that.hasShow) && Objects.equals(msg, that.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataTime, msgId, hasShow, msg);
    }

    @Override
    public String toString() {
        return "NetWorkIOResult{" +
                "dataTime=" + dataTime +
                ", msgId=" + msgId +
                ", hasShow=" + hasShow +
                ", msg='" + msg + '\'' +
                '}';
    }
}
