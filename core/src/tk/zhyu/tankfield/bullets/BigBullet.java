package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class BigBullet extends BulletInfo {
    public BigBullet() {
        gravity = new float[]{-98.1f};
        maxPower = 160;
        push = new float[]{200};
        damage = new float[]{80};
        holeSize = new float[]{15};
        damageRadius = new float[]{30};
        icon_id = 63 + 5;
        bullet_icon = 1;
    }

    public void highPoint(Bullet b, TankScreen world) {

    }

    public void hit(Bullet b, TankScreen world) {
        defaultHit(b,world);
    }

    public void update(Bullet b, TankScreen world, float eTime) {

    }
}
