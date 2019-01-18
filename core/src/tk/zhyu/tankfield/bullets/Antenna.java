package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public abstract class Antenna extends BulletInfo {

    public Antenna() {
        gravity = new float[]{-98.1f, 0};
        maxPower = 160;
        push = new float[]{0};
        damage = new float[]{20};
        holeSize = new float[]{10};
        damageRadius = new float[]{30};
        icon_id = (11 * 9) + 2;
        bullet_icon = 2;
    }

    @Override
    public void highPoint(Bullet b, TankScreen world) {

    }

    @Override
    public void hit(Bullet b, TankScreen world) {
        world.bullets.addBullet(b.x, b.y, 0, 0, this, b.variation + 1, b.owner);
    }

    @Override
    public void update(Bullet b, TankScreen world, float eTime) {

    }
}
