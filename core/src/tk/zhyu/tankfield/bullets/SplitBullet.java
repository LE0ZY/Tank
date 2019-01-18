package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class SplitBullet extends BulletInfo {
    public SplitBullet() {
        gravity = new float[]{-98.1f};
        maxPower = 160;
        push = new float[]{50, 25, 12.5f};
        damage = new float[]{40, 20, 10};
        holeSize = new float[]{10, 4, 2};
        damageRadius = new float[]{20};
        icon_id = 99;
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
        if (eTime > 1.2f) {
            float speed = b.equation.getVelocity(b.eTime).len();
            float angle = b.equation.getVelocity(b.eTime).angleRad();
            world.bullets.addBullet(b.x, b.y, (float) (speed * Math.cos(angle + 0.1)), (float) (speed * Math.sin(angle + 0.1)), this, b.variation + 1, b.owner);
            world.bullets.addBullet(b.x, b.y, (float) (speed * Math.cos(angle - 0.1)), (float) (speed * Math.sin(angle - 0.1)), this, b.variation + 1, b.owner);
            b.dead = true;
        }
    }
}
