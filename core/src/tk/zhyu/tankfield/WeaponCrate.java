package tk.zhyu.tankfield;

import tk.zhyu.tankfield.bullets.BulletInfo;

public class WeaponCrate extends Crate {
    private BulletInfo bulletInfo;

    public WeaponCrate(float x, float y, TankScreen screen, BulletInfo bulletInfo) {
        super(x, y, Tank.atlas.findRegion("tanks_crateAmmo"), screen);
        this.bulletInfo = bulletInfo;
    }

    public void received(Tank tank) {
        super.received(tank);
        tank.inventory.add(bulletInfo);
    }
}
