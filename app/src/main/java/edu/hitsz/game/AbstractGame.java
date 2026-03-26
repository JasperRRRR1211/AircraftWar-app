package edu.hitsz.game;

import android.content.Context;

import android.os.Handler;

public abstract class AbstractGame extends Game {

    protected int enemyMaxNumber;   //最大敌机数量
    protected int heroShootCycle;   //英雄机射击周期
    protected int enemyShootCycle;     //敌机射击周期
    protected double eliteProb;        //精英机生成概率
    protected int enemySpawnCycle;     //敌机刷新周期
    protected int bossThreshold;       //boss生成阈值
    protected int bossHp;           //boss血量
    protected boolean hasBoss;         //是否出现boss
    protected boolean difficultyIncrease;

    protected int difficultyStep = 0;

    public AbstractGame(Context context, Handler handler) {
        super(context, handler);
        // 由子类初始化难度参数
        setInitialParams();
        // 将参数传给父类 Game
        super.initGameParams(enemyMaxNumber, heroShootCycle, enemyShootCycle,
                eliteProb, enemySpawnCycle, bossThreshold, bossHp,
                hasBoss, difficultyIncrease);
    }

    /**
     * 模板方法定义游戏流程，不可重写
     */
    @Override
    public void action() {
        System.out.println("== 当前模式: " + this.getClass().getSimpleName() + " ==");
        System.out.println("敌机上限：" + enemyMaxNumber + "，精英概率：" + eliteProb);

        // 启动父类游戏循环
        super.action();
    }

    /** 初始化难度参数（由子类实现） */
    protected abstract void setInitialParams();

    /** 难度随时间增加（普通、困难） */
    protected void applyDifficulty() {
        if (difficultyIncrease && time % 10000 == 0) { // 每10秒难度变化
            difficultyStep++;
            enemyMaxNumber = Math.min(enemyMaxNumber + 1, 10);
            eliteProb = Math.min(eliteProb + 0.05, 0.9);
            System.out.println("难度上升！当前敌机上限：" + enemyMaxNumber + " 精英概率：" + eliteProb);
        }
    }

    /** 获取 Boss 初始血量（困难模式可重写） */
    public int getBossHp() {
        return bossHp;
    }

    /** 是否有Boss */
    public boolean hasBoss() {
        return hasBoss;
    }

    /** Boss出现得分阈值 */
    public int getBossThreshold() {
        return bossThreshold;
    }

}