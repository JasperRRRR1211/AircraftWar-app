package edu.hitsz.supply_factory;
import edu.hitsz.supply.BombSupply;

public class BombSupplyFactory implements SupplyFactory{
    public BombSupply createSupply(int x, int y) {
        return new BombSupply(x, y, 0, 30);
    }
}
