package edu.hitsz.application;

import edu.hitsz.Music.SoundManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DifficultySelect {
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel CheckBoxPanel;
    private JButton easyButton;
    private JButton normalButton;
    private JButton hardButton;
    private JPanel CheckBoxWithText;
    private JComboBox SoundcomboBox;


    public DifficultySelect() {
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame("easy");
            }
        });

        normalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame("normal");
            }
        });

        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame("hard");
            }
        });

        SoundcomboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) SoundcomboBox.getSelectedItem();
                if ("on".equalsIgnoreCase(selected)) {
                    System.out.println("music on");
                    SoundManager.setMusicOn(true);
                    SoundManager.playBackground("src/videos/bgm.wav", true);
                } else {
                    System.out.println("music off");
                    SoundManager.setMusicOn(false);
                    SoundManager.stopBackground();
                }
            }
        });
    }

    private void startGame(String difficulty) {
        // 使用 Main.startGame 启动游戏
        Main.startGame(CardLayoutDemo.frame, difficulty);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    /*
    private void startGame(String difficulty) {
        System.out.println("select difficulty：" + difficulty);
        JFrame frame = new JFrame("Aircraft War");
        Game game;
        switch (difficulty) {
            case "medium":
                //game = new MediumGame();
                break;
            case "hard":
                //game = new HardGame();
                break;
            default:
                //game = new EasyGame();
                break;
        }
        //frame.setContentPane(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //game.action();  // 启动游戏逻辑
    }

     */
    /*
    public static void main(String[] args) {
        JFrame frame = new JFrame("选择难度");
        frame.setContentPane(new DifficultySelect().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

     */
}
