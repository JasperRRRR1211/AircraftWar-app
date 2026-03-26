package edu.hitsz.shoot;

import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public interface ShootStrategy {
    public List<BaseBullet> shoot(int x, int y, int speedX, int speedY, int shootNum, int power, int direction);
}
