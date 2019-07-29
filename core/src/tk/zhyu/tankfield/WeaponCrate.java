package tk.zhyu.tankfield;

import tk.zhyu.tankfield.bullets.BulletInfo;

public class WeaponCrate extends Crate {
    private BulletInfo bulletInfo;
    private String text;

    public WeaponCrate(float x, float y, TankScreen screen, BulletInfo bulletInfo, String text) {
        super(x, y, Tank.atlas.findRegion("tanks_crateAmmo"), screen);
        this.bulletInfo = bulletInfo;
        this.text = text;
    }

    public void received(Tank tank) {
        super.received(tank);
        screen.messages.addMessage(tank.getX(), tank.getY() + 10, text, 1);
        tank.inventory.add(bulletInfo);
    }
}
