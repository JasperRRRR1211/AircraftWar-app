package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.observer.BombObserver;
import edu.hitsz.shoot.ScatterShoot;
import edu.hitsz.shoot.ShootStrategy;
import edu.hitsz.supply.BaseSupply;
import edu.hitsz.supply.BloodSupply;
import edu.hitsz.supply_factory.BloodSupplyFactory;
import edu.hitsz.supply_factory.BombSupplyFactory;
import edu.hitsz.supply_factory.FireSupplyFactory;
import edu.hitsz.supply_factory.SuperFireSupplyFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class EliteEnemyPro extends AbstractEnemy implements BombObserver {

    //3 shots
    private int shootNum = 3;

    private int power = 30;

    private int direction = 1;

    //move to left or right, 1->right
    private int horizontalDirection = 1;

    private ShootStrategy shootStrategy;

    public EliteEnemyPro(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.shootStrategy = new ScatterShoot();
    }

    @Override
    public void forward() {
        // 水平摆动控制
        locationX += speedX * horizontalDirection;
        locationY += speedY;

        // 碰到边界就反向移动
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            horizontalDirection = -horizontalDirection;
        }

        // 飞出屏幕下方则消失
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    public void setShootStrategy(ShootStrategy strategy) {
        this.shootStrategy = strategy;
    }

    @Override
    public List<BaseBullet> shoot() {
        return shootStrategy.shoot(getLocationX(), getLocationY(), 0, getSpeedY(),
                shootNum, power, direction);
    }

    public List<BaseSupply> GenSupply() {
        List<BaseSupply> list = new LinkedList<>();
        Random random = new Random();
        int prob = random.nextInt(100); // 0~99

        // 50% 概率不掉落
        if (prob < 50) {
            return null;
        }

        // 掉落一个随机类型补给
        int type = random.nextInt(4); // 0,1,2
        int x = this.getLocationX();
        int y = this.getLocationY();

        BloodSupplyFactory bloodSupplyFactory = new BloodSupplyFactory();
        FireSupplyFactory fireSupplyFactory = new FireSupplyFactory();
        BombSupplyFactory bombSupplyFactory = new BombSupplyFactory();
        SuperFireSupplyFactory superfireSupplyFactory = new SuperFireSupplyFactory();

        switch (type) {
            case 0:
                list.add(bloodSupplyFactory.createSupply(x, y));
                break;
            case 1:
                list.add(fireSupplyFactory.createSupply(x, y));
                break;
            case 2:
                list.add(bombSupplyFactory.createSupply(x, y));
                break;
            case 3:
                list.add(superfireSupplyFactory.createSupply(x, y));
                break;
            default:
                break;
        }
        return list;
    }

    @Override
    public void update() {
        this.decreaseHp(40);
    }
}