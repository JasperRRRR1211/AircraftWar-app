package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.shoot.DirectShoot;
import edu.hitsz.shoot.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    private static HeroAircraft instance;

    private int shootNum = 1;   //子弹的数量
    private int power = 60;     //子弹的伤害
    private int direction = -1; //飞机飞行方向，-1代表向上飞行
    private ShootStrategy shootStrategy;

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.shootStrategy = new DirectShoot();
    }

    // 公共静态方法来获取唯一的实例
    public static HeroAircraft getInstance(int locationX, int locationY, int speedX, int speedY, int hp) {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
                }
            }
        }
        return instance;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    public void setShootStrategy(ShootStrategy strategy) {
        this.shootStrategy = strategy;
    }

    @Override
    public List<BaseBullet> shoot() {
        return shootStrategy.shoot(getLocationX(), getLocationY(), 0, getSpeedY(),
                shootNum, power, direction);
    }
}
