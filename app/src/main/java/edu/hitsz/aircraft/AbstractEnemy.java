package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.observer.BombObserver;
import edu.hitsz.shoot.ShootStrategy;
import edu.hitsz.supply.BaseSupply;

import java.util.List;

/**
 * 抽象敌机类：
 * 所有敌机（普通敌机、精英敌机、BOSS敌机）的共同父类
 * 封装了敌机的通用属性和行为
 *
 * @author hitsz
 */
public abstract class AbstractEnemy extends AbstractAircraft implements BombObserver {

    /** 子弹一次发射数量 */
    protected int shootNum = 1;

    /** 子弹伤害 */
    protected int power = 10;

    /** 子弹射击方向 (向上发射：-1，向下发射：1) */
    protected int direction = 1;

    /** 射击策略 */
    protected ShootStrategy shootStrategy;

    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    /**
     * 敌机移动逻辑
     * 出界则消失
     */
    @Override
    public void forward() {
        super.forward();
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    /**
     * 设置射击策略（可在不同难度或敌机类型中动态切换）
     */
    public void setShootStrategy(ShootStrategy strategy) {
        this.shootStrategy = strategy;
    }

    /**
     * 发射子弹，由子类根据具体策略实现
     */
    @Override
    public abstract List<BaseBullet> shoot();

    /**
     * 生成补给，由子类具体实现（普通敌机不掉落，精英/BOSS掉落）
     */
    @Override
    public List<BaseSupply> GenSupply() {
        return null;
    }

    /**
     * 实现炸弹观察者：当炸弹生效时敌机消失
     */
    @Override
    public void update() {
        this.vanish();
    }
}