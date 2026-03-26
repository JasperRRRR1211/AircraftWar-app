package edu.hitsz.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;
import android.content.Intent;

import edu.hitsz.R;
import edu.hitsz.application.ImageManager;

/**
 * 程序的 Android 入口
 * 替代了原本基于 JFrame 的 Main.java
 */
public class MainActivity extends Activity {

    // 保留这两个静态常量，以便你的其他类（如 HeroAircraft）
    // 依然可以通过 MainActivity.WINDOW_WIDTH 访问屏幕边界。
    // （注意：把类名从 Main 改为 MainActivity 后，其他类里的调用也要跟着改）
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;

    //是否播放音乐
    public static boolean isMusicOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 设置全屏显示，隐藏 Android 的标题栏和状态栏（沉浸式游戏体验）
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 2. 动态获取当前手机屏幕的分辨率，并赋值给静态变量
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        WINDOW_WIDTH = metrics.widthPixels;
        WINDOW_HEIGHT = metrics.heightPixels;

        ImageManager.init(this);

        setContentView(R.layout.start);

        Button btnEasy = findViewById(R.id.SelectButton1);
        Button btnNormal = findViewById(R.id.selectButton2);
        Button btnHard = findViewById(R.id.selectButton3);
        SwitchCompat musicSwitch = findViewById(R.id.musicButton);


        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isMusicOn = isChecked;
                System.out.println("Music state changed to: " + isChecked);
            }
        });

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity("easy");
            }
        });

        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity("normal");
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity("hard");
            }
        });
    }

    /**
     * 发送船票，跳转到游戏专属页面
     */
    private void startGameActivity(String difficulty) {
        // 创建一张从当前页面 (MainActivity) 到目标页面 (GameActivity) 的船票
        Intent intent = new Intent(MainActivity.this, GameActivity.class);

        // 往包裹里塞入难度参数
        intent.putExtra("difficulty", difficulty);

        startActivity(intent);
    }
}