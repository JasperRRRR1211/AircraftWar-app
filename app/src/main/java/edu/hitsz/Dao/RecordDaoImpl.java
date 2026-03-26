package edu.hitsz.Dao;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecordDaoImpl implements RecordDao {
    private final String filename = "scoreboard.txt";

    @Override
    public void saveRecord(Record record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(record.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    records.add(new Record(parts[0],
                            Integer.parseInt(parts[1]),
                            parts[2],
                            parts[3]));
                }
            }
        } catch (IOException e) {
            // 如果文件不存在，则返回空列表
        }
        return records;
    }

    @Override
    public void printRankList() {
        List<Record> records = getAllRecords();
        if (records.isEmpty()) {
            System.out.println("暂无排行榜数据。");
            return;
        }

        records.sort(Comparator.comparingInt(Record::getScore).reversed());

        System.out.println("========== 当前排行榜 ==========");
        for (int i = 0; i < records.size(); i++) {
            Record r = records.get(i);
            System.out.printf("第%d名：%s，得分 %d，日期 %s，时间 %s%n",
                    i + 1, r.getUsername(), r.getScore(), r.getDate(), r.getTime());
        }
        System.out.println("================================");
    }

    @Override
    public void deleteRecord(String username, int score) {
        List<Record> records = getAllRecords();
        records.removeIf(r -> r.getUsername().equals(username) && r.getScore() == score);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Record r : records) {
                writer.write(r.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}