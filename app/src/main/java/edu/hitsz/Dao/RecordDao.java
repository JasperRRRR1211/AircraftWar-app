package edu.hitsz.Dao;

import java.util.List;

public interface RecordDao {
    void saveRecord(Record record);
    List<Record> getAllRecords();
    void printRankList();
    void deleteRecord(String username, int score);
}