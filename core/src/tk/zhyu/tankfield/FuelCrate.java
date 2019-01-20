package tk.zhyu.tankfield;

public class FuelCrate extends Crate {
    public boolean exploding = false;

    public FuelCrate(float x, float y, TankScreen screen) {
        super(x, y, Tank.atlas.findRegion("tanks_barrelGrey"), screen);
    }

    public void received(Tank tank) {
        super.received(tank);
        tank.fuel = tank.maxFuel;
    }

    public void act(float delta) {
        super.act(delta);
        if (exploding) {
            screen.bullets.addExplosion(getX(), getY(), 15);
            screen.makeHole(getX(), getY(), 10);
            screen.explode(15, 20, getX(), getY(), 10);
        }
    }
}
