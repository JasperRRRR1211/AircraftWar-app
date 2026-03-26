package edu.hitsz.supply_factory;

import edu.hitsz.supply.FireSupply;
import edu.hitsz.supply.SuperFireSupply;

public class SuperFireSupplyFactory implements SupplyFactory {
    public SuperFireSupply createSupply(int x, int y){
        return new SuperFireSupply(x, y, 0, 30);
    }
}
