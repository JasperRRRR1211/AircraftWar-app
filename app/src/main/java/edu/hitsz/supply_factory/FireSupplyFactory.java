package edu.hitsz.supply_factory;
import edu.hitsz.supply.FireSupply;

public class FireSupplyFactory implements SupplyFactory{
    public FireSupply createSupply(int x, int y){
        return new FireSupply(x, y, 0, 30);
    }
}
