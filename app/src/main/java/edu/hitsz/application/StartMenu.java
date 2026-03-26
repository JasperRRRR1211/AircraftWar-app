package edu.hitsz.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JButton difficultyButton;
    private JButton scoreRecordButton;

    private JFrame mainFrame;

    public StartMenu(JFrame frame) {
        this.mainFrame = frame;

        difficultyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayoutDemo.cardPanel.add(new DifficultySelect().getMainPanel());
                CardLayoutDemo.cardLayout.last(CardLayoutDemo.cardPanel);
            }
        });
        scoreRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);

                // 2. 创建并显示分数表窗口
                // 注意：ScoreTable 构造函数需要知道主窗口的引用，以便返回时使用。
                ScoreTable scoreTable = new ScoreTable(mainFrame);

                // 因为 ScoreTable 继承自 JFrame，所以它是一个独立的窗口。
                scoreTable.setVisible(true);
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
