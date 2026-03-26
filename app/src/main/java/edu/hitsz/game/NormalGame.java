package edu.hitsz.game;

import android.content.Context;

import android.os.Handler;

public class NormalGame extends AbstractGame {


    public NormalGame(Context context, Handler handler) {
        super(context, handler);
    }

    @Override
    protected void setInitialParams() {
        this.enemyMaxNumber = 6;
        this.heroShootCycle = 500;
        this.enemyShootCycle = 850;
        this.eliteProb = 0.75;
        this.enemySpawnCycle = 600;
        this.bossThreshold = 150;
        this.bossHp = 400;
        this.hasBoss = true;
        this.difficultyIncrease = true;
    }
}