package edu.hitsz.supply_factory;
import edu.hitsz.supply.BaseSupply;

public interface SupplyFactory {
    public abstract BaseSupply createSupply(int x, int y);
}
