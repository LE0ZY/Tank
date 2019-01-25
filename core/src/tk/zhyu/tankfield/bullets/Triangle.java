package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class Triangle extends BulletInfo {
    public Triangle() {
        gravity = new float[]{-98.1f, 0, 0};
        maxPower = 160;
        push = new float[]{20};
        damage = new float[]{0, 10, 10};
        holeSize = new float[]{0, 5, 5};
        damageRadius = new float[]{12};
        icon_id = 9 + 4;
    }

    @Override
    public void highPoint(Bullet b, TankScreen world) {

    }

    @Override
    public void hit(Bullet b, TankScreen world) {
        if (b.getVariation() == 0) {
            for (int a = 0; a < 10; a++) {
                double angle = Math.random() * Math.PI;
                world.bullets.addBullet(b.x, b.y, (float) (100 * Math.cos(angle)), (float) (100 * Math.sin(angle)), this, 1, b.owner);
            }
            b.dead = true;
        } else
            defaultHit(b, world);
    }

    @Override
    public void update(Bullet b, TankScreen world, float eTime) {
        defaultUpdate(b, world, eTime);
        if (b.getVariation() == 1 && eTime > 1) {
            world.bullets.addBullet(b.x, b.y, -b.equation.startVelocity.x * 1.2f, -b.equation.startVelocity.y * 1.2f, this, 2, b.owner);
            b.dead = true;
        }
    }
}
