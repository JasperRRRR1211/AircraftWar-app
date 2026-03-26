package edu.hitsz.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import edu.hitsz.game.Game;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.NormalGame;
import edu.hitsz.game.HardGame;
import edu.hitsz.application.ImageManager;

/**
 * 专门用来承载游戏界面的 Activity
 */
public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 同样需要设置全屏，保证游戏体验
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 1. 【新增】创建一个主线程的 Handler，准备接收游戏结束的信号
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // 对暗号：1 代表玩家已经输完名字并保存了
                if (msg.what == 1) {
                    // 发起跳转
                    Intent intent = new Intent(GameActivity.this, RankingActivity.class);
                    startActivity(intent);
                    // 销毁当前的游戏页面
                    finish();
                }
            }
        };

        Intent intent = getIntent();
        // 取出 MainActivity 传过来的 "difficulty"
        String difficulty = intent.getStringExtra("difficulty");

        // 万一没传过来，默认给 easy
        if (difficulty == null) {
            difficulty = "easy";
        }

        System.out.println("GameActivity received difficulty: " + difficulty);

        // 3. 根据难度设置背景
        ImageManager.setBackground(difficulty);

        // 4. 根据难度创建对应的游戏逻辑
        Game game;
        switch (difficulty) {
            case "normal":
                game = new NormalGame(this, handler);
                break;
            case "hard":
                game = new HardGame(this, handler);
                break;
            case "easy":
            default:
                game = new EasyGame(this, handler);
                break;
        }

        // 5. 把这个 Activity 的外衣，换成我们的游戏画板
        setContentView(game);
    }
}