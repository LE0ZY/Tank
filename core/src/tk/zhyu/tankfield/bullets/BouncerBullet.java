package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class BouncerBullet extends BulletInfo {
    public BouncerBullet() {
        gravity = new float[]{-98.1f};
        maxPower = 120;
        push = new float[]{50, 25, 12.5f};
        damage = new float[]{20, 10, 5};
        holeSize = new float[]{10, 5, 2.5f, 0};
        damageRadius = new float[]{15, 7.5f, 3.5f};
        icon_id = 27;
    }

    public void highPoint(Bullet b, TankScreen world) {
    }

    public void hit(Bullet b, TankScreen world) {
        defaultHit(b, world);
        if (b.getVariation() < 2) {
            world.bullets.addBullet(b.x, b.y + 0.4f, 0, 60, this, b.getVariation() + 1, b.owner);
            world.bullets.addBullet(b.x, b.y + 0.4f, 10, 60, this, b.getVariation() + 1, b.owner);
            world.bullets.addBullet(b.x, b.y + 0.4f, -10, 60, this, b.getVariation() + 1, b.owner);
        }
    }

    public void update(Bullet b, TankScreen world, float eTime) {
        defaultUpdate(b, world, eTime);
    }
}