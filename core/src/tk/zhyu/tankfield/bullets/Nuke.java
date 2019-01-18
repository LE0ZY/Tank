package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class Nuke extends BulletInfo {
    public Nuke() {
        gravity = new float[]{-98.1f};
        maxPower = 200;
        push = new float[]{20};
        damage = new float[]{150};
        holeSize = new float[]{50};
        damageRadius = new float[]{55};
        icon_id = 27 + 5;
        bullet_icon = 4;
    }

    @Override
    public void highPoint(Bullet b, TankScreen world) {

    }

    @Override
    public void hit(Bullet b, TankScreen world) {
        defaultHit(b, world);
    }

    @Override
    public void update(Bullet b, TankScreen world, float eTime) {

    }
}
