package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import edu.hitsz.game.Game;

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author hitsz
 */
public class HeroController implements View.OnTouchListener{
    private Game game;
    private HeroAircraft heroAircraft;

    public HeroController(Game game, HeroAircraft heroAircraft){
        this.game = game;
        this.heroAircraft = heroAircraft;
    }

    /**
     * 触碰屏幕时的操作
     * @return true：view继续响应Touch操作；
     *         false：view不再响应Touch操作，此后再也接收不到MotionEvent
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v.performClick(); // 消除编译器警告
            heroAircraft.setLocation(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            heroAircraft.setLocation(event.getX(), event.getY());
        }
        return true; // 必须返回 true
    }
}

