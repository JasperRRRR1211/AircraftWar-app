package edu.hitsz.shoot;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class DirectShoot implements ShootStrategy{
    public List<BaseBullet> shoot(int x, int y, int speedX, int speedY, int shootNum, int power, int direction){
        List<BaseBullet> res = new LinkedList<>();
        BaseBullet bullet;
        for (int i = 0; i < shootNum; i++) {
            if (direction == 1) {
                bullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10,
                                         y + direction*2,
                                         speedX, speedY + direction*5,
                                         power);
                res.add(bullet);
            } else if (direction == -1) {
                bullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10,
                                        y + direction*2,
                                        speedX, speedY + direction*15,
                                        power);
                res.add(bullet);
            }
        }
        return res;
    }
}
