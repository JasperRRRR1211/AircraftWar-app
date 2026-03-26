package edu.hitsz.shoot;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class RingShoot implements ShootStrategy{
    @Override
    public List<BaseBullet> shoot(int x, int y, int speedX, int speedY, int shootNum, int power, int direction) {
        List<BaseBullet> res = new LinkedList<>();
        int radiusSpeed = 10; // 子弹的飞行半径速度分量
        shootNum = 20;

        for (int i = 0; i < shootNum; i++) {
            double angle = 2 * Math.PI * i / shootNum;
            int vx = (int) (radiusSpeed * Math.cos(angle));
            int vy = (int) (radiusSpeed * Math.sin(angle));

            BaseBullet bullet;
            // direction == -1 表示英雄机，1 表示敌机
            if (direction == -1) {
                bullet = new HeroBullet(x, y, vx, vy, power);
            } else {
                bullet = new EnemyBullet(x, y, vx, vy, power);
            }
            res.add(bullet);
        }
        return res;
    }
}
