package edu.hitsz.supply_factory;
import edu.hitsz.supply.BloodSupply;


public class BloodSupplyFactory implements SupplyFactory{
    public BloodSupply createSupply(int x, int y){
        return new BloodSupply(x, y, 0, 30);
    }
}
