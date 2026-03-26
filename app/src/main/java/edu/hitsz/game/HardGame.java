package edu.hitsz.game;

import android.content.Context;

import android.os.Handler;

public class HardGame extends AbstractGame {

    public HardGame(Context context, Handler handler) {
        super(context, handler);
    }

    @Override
    protected void setInitialParams() {
        this.enemyMaxNumber = 8;
        this.heroShootCycle = 600;
        this.enemyShootCycle = 800;
        this.eliteProb = 0.8;
        this.enemySpawnCycle = 500;
        this.bossThreshold = 150;
        this.bossHp = 500;
        this.hasBoss = true;
        this.difficultyIncrease = true;
    }

    @Override
    protected void applyDifficulty() {
        super.applyDifficulty();
        // 每次召唤Boss血量上升
        this.bossHp += 100;
        System.out.println("Boss血量提升到：" + bossHp);
    }
}