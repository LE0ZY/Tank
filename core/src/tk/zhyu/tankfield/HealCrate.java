package tk.zhyu.tankfield;

public class HealCrate extends Crate {
    public HealCrate(float x, float y, TankScreen screen) {
        super(x, y, Tank.atlas.findRegion("tanks_crateRepair"), screen);
    }

    public void received(Tank tank) {
        super.received(tank);
        tank.setHealth(Math.min(tank.maxHealth, tank.getHealth() + 80));
    }
}
