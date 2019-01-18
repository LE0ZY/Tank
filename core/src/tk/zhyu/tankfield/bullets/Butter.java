package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class Butter extends BulletInfo {
    public Butter() {
        gravity = new float[]{-98.1f};
        maxPower = 160;
        push = new float[]{2};
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

    }

    @Override
    public void update(Bullet b, TankScreen world, float eTime) {

    }
}
