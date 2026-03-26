package edu.hitsz.game;

import android.content.Context;

import android.os.Handler;

public class EasyGame extends AbstractGame {

    public EasyGame(Context context, Handler handler) {
        super(context, handler);
    }

    @Override
    protected void setInitialParams() {
        this.enemyMaxNumber = 4;
        this.heroShootCycle = 400;
        this.enemyShootCycle = 900;
        this.eliteProb = 0.7;
        this.enemySpawnCycle = 800;
        this.bossThreshold = Integer.MAX_VALUE; // 不出现Boss
        this.bossHp = 0;
        this.hasBoss = false;
        this.difficultyIncrease = false;
    }
}