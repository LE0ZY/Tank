package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class SplitBullet extends BulletInfo {
    public SplitBullet() {
        gravity = new float[]{-98.1f};
        maxPower = 160;
        push = new float[]{50, 25, 12.5f};
        damage = new float[]{80, 60, 40};
        holeSize = new float[]{12, 8, 5};
        damageRadius = new float[]{20};
        icon_id = 99;
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
        defaultUpdate(b, world, eTime);
        if (eTime > 1f) {
            float speed = b.equation.getVelocity(b.eTime).len();
            float angle = b.equation.getVelocity(b.eTime).angleRad();
            world.bullets.addBullet(b.x, b.y, (float) (speed * Math.cos(angle + 0.1)), (float) (speed * Math.sin(angle + 0.1)), this, b.getVariation() + 1, b.owner);
            world.bullets.addBullet(b.x, b.y, (float) (speed * Math.cos(angle - 0.1)), (float) (speed * Math.sin(angle - 0.1)), this, b.getVariation() + 1, b.owner);
            b.dead = true;
        }
    }
}
