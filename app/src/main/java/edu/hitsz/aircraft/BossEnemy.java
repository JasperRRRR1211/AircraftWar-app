package edu.hitsz.aircraft;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.observer.BombObserver;
import edu.hitsz.shoot.RingShoot;
import edu.hitsz.shoot.ShootStrategy;
import edu.hitsz.supply.BaseSupply;
import edu.hitsz.supply_factory.BloodSupplyFactory;
import edu.hitsz.supply_factory.BombSupplyFactory;
import edu.hitsz.supply_factory.FireSupplyFactory;
import edu.hitsz.supply_factory.SuperFireSupplyFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BossEnemy extends AbstractEnemy implements BombObserver {

    //20 bullets
    private int shootNum = 20;

    //power of every bullet
    private int power = 30;

    private int horizontalDirection = 1;

    private int direction = 1;

    private ShootStrategy shootStrategy;

    public void setHp(int hp) {
        this.hp = hp;
    }

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.shootStrategy = new RingShoot();
    }

    @Override
    public void forward() {
        // 水平移动
        locationX += speedX * horizontalDirection;

        // 悬浮于上方，不下降（或缓慢下降）
        if (locationY < 100) {
            locationY += 0.5; // 微弱上下漂浮感
        }

        // 碰到左右边界反向移动
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            horizontalDirection = -horizontalDirection;
        }
    }

    public void setShootStrategy(ShootStrategy strategy) {
        this.shootStrategy = strategy;
    }

    public List<BaseBullet> shoot() {
        return shootStrategy.shoot(getLocationX(), getLocationY(), 0, getSpeedY(),
                shootNum, power, direction);
    }

    public List<BaseSupply> GenSupply() {
        List<BaseSupply> supplies = new LinkedList<>();
        Random random = new Random();

        // 掉落上限为3个
        int dropCount = random.nextInt(4); // 0~3

        BloodSupplyFactory bloodFactory = new BloodSupplyFactory();
        FireSupplyFactory fireFactory = new FireSupplyFactory();
        BombSupplyFactory bombFactory = new BombSupplyFactory();
        SuperFireSupplyFactory superfireFactory = new SuperFireSupplyFactory();

        for (int i = 0; i < dropCount; i++) {
            int type = random.nextInt(4); // 随机选择补给类型
            int x = this.getLocationX() + random.nextInt(50) - 25; // 偏移散落
            int y = this.getLocationY() + random.nextInt(30) - 15;

            switch (type) {
                case 0:
                    supplies.add(bloodFactory.createSupply(x, y));
                    break;
                case 1:
                    supplies.add(fireFactory.createSupply(x, y));
                    break;
                case 2:
                    supplies.add(bombFactory.createSupply(x, y));
                    break;
                case 3:
                    supplies.add(superfireFactory.createSupply(x, y));
                    break;
                default:
                    break;
            }
        }
        return supplies;
    }

    @Override
    public void update(){}
}
