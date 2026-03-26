package edu.hitsz.shoot;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class ScatterShoot implements ShootStrategy{
    @Override
    public List<BaseBullet> shoot(int x, int y, int speedX, int speedY, int shootNum, int power, int direction) {
        List<BaseBullet> res = new LinkedList<>();
        int baseSpeedY;
        if (direction == 1) {
            baseSpeedY = speedY + direction * 4;
        }else {
            baseSpeedY = speedY + direction * 14;
        }
        // 三发子弹：-15°, 0°, +15°
        double[] angleOffset = {-15.0, 0.0, 15.0};
        for (double angle : angleOffset) {
            double rad = Math.toRadians(angle);
            int bulletSpeedX = (int) (5 * Math.sin(rad));
            int bulletSpeedY = (int) (baseSpeedY + 5 * Math.cos(rad));

            if (direction == -1) {
                BaseBullet bullet = new HeroBullet(x, y, bulletSpeedX, bulletSpeedY, power);
                res.add(bullet);
            }else if (direction == 1){
                BaseBullet bullet = new EnemyBullet(x, y, bulletSpeedX, bulletSpeedY, power);
                res.add(bullet);
            }
        }
        return res;
    }
}
