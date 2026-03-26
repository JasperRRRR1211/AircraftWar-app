package edu.hitsz.aircraft;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.shoot.DirectShoot;
import edu.hitsz.shoot.ShootStrategy;
import edu.hitsz.supply.BaseSupply;
import edu.hitsz.supply_factory.BloodSupplyFactory;
import edu.hitsz.supply_factory.BombSupplyFactory;
import edu.hitsz.supply_factory.FireSupplyFactory;
import edu.hitsz.supply_factory.SuperFireSupplyFactory;
import edu.hitsz.observer.BombObserver;

import java.util.Random;
import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends AbstractEnemy implements BombObserver{
    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    private int direction = 1;

    private ShootStrategy shootStrategy;
    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.shootStrategy = new DirectShoot();
    }


    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
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
        List<BaseSupply> list = new LinkedList<>();
        Random random = new Random();
        int prob = random.nextInt(100); // 0~99

        // 假设掉落概率：30% 不掉落，70% 掉落其中一种
        if (prob < 30) {
            return null;  // 不掉落
        }

        int type = random.nextInt(4); // 0,1,2,3
        int x = this.getLocationX();
        int y = this.getLocationY();
        SuperFireSupplyFactory superfireSuppplyFactory = new SuperFireSupplyFactory();
        BloodSupplyFactory bloodSupplyFactory = new BloodSupplyFactory();
        FireSupplyFactory fireSupplyFactory = new FireSupplyFactory();
        BombSupplyFactory bombSupplyFactory = new BombSupplyFactory();

        switch (type) {
            case 0:
                list.add(bloodSupplyFactory.createSupply(x, y)); // 血量补给
                break;
            case 1:
                list.add(fireSupplyFactory.createSupply(x, y)); // 火力补给
                break;
            case 2:
                list.add(bombSupplyFactory.createSupply(x, y)); // 炸弹补给
                break;
            case 3:
                list.add(superfireSuppplyFactory.createSupply(x, y));
                break;
            default:
                break;
        }
        return list;
    }

    @Override
    public void update() {
        this.vanish();
    }
}
