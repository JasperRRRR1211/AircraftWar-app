package edu.hitsz.aircraft_factory;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class BossEnemyFactory implements AircraftFactory{
    public BossEnemy createAircraft() {
        return new BossEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                (Math.random() > 0.5 ? 3 : -3),
                0,
                0);
    }
}
