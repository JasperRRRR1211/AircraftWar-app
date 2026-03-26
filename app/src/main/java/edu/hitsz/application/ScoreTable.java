package edu.hitsz.application;

import Dao.Record;
import Dao.RecordDao;
import Dao.RecordDaoImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

public class ScoreTable extends JFrame {
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JButton deleteButton;
    private JButton returnButton;
    private JTable ScoreTable;
    private JScrollPane ScrollPanel;
    private DefaultTableModel tableModel;
    private RecordDao recordDao;


    public ScoreTable(JFrame frame) {
        this.mainFrame = frame;
        recordDao = new RecordDaoImpl();

        // 定义表格列名
        String[] columnNames = {"排名", "用户名", "分数", "日期", "时间"};
        String[][] tableData = {{"1", "penny", "300", "25.1.1", "10:10"}};
        tableModel = new DefaultTableModel(columnNames, 0);
        ScoreTable.setModel(tableModel);
        ScrollPanel.setViewportView(ScoreTable);

        refreshTable();
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ScoreTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(
                            deleteButton,
                            "确定要删除该条记录吗？",
                            "确认删除",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        // 从表格模型取出 username 与 score（列索引按你表格定义）
                        String username = (String) tableModel.getValueAt(selectedRow, 1); // 列1：用户名
                        Object scoreObj = tableModel.getValueAt(selectedRow, 2);          // 列2：分数
                        int score;
                        try {
                            score = Integer.parseInt(String.valueOf(scoreObj));
                        } catch (NumberFormatException ex) {
                            // 防御：若解析失败，提示并返回
                            JOptionPane.showMessageDialog(deleteButton, "分数格式错误，无法删除。");
                            return;
                        }

                        // 调用 DAO（符合你现有接口）
                        recordDao.deleteRecord(username, score);

                        // 刷新表格
                        refreshTable();
                    }
                } else {JOptionPane.showMessageDialog(ScoreTable.this, "请先选中一条记录！");}
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. 关闭当前的分数表窗口 (ScoreTable)
                dispose();

                // 2. 显示主窗口 (StartMenu 所在的窗口)
                mainFrame.setVisible(true);
            }
        });

        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 关闭时只关闭此窗口
        frame.setSize(500, 400);
        this.pack();
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // 清空表格

        List<Record> records = recordDao.getAllRecords();
        // 按分数降序排列
        records.sort(Comparator.comparingInt(Record::getScore).reversed());

        int rank = 1;
        for (Record record : records) {
            tableModel.addRow(new Object[]{
                    rank++,
                    record.getUsername(),
                    record.getScore(),
                    record.getDate(),
                    record.getTime()
            });
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    /*
    public static void main(String[] args) {
        // 创建 ScoreTable 实例
        ScoreTable scoreTableInstance = new ScoreTable(mainFrame);

        JFrame frame = new JFrame("ScoreTable");

        // **修正：将 ScoreTable 实例的根面板设置为 JFrame 的内容面板**
        frame.setContentPane(scoreTableInstance.mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 建议先pack再设置大小，或者只用pack，让组件自己决定最佳大小
        frame.pack(); // 让窗口自动调整大小以适应内部组件
        // 如果需要固定大小，再使用 setSize
        // frame.setSize(500, 400);

        frame.setVisible(true);
    }
     */
}
