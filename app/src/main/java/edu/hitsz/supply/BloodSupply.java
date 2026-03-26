package edu.hitsz.supply;

import edu.hitsz.supply.BaseSupply;

public class BloodSupply extends BaseSupply {

    public static int hpIncrement = 10;

    public BloodSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public int getHpIncrement()
    {
        return hpIncrement;
    }
}
