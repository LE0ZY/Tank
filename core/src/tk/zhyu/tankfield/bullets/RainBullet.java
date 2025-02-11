package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class RainBullet extends BulletInfo {
    public RainBullet() {
        gravity = new float[]{-98.1f, 0, -98.1f};
        maxPower = 160;
        push = new float[]{50, 40, 30};
        damage = new float[]{50, 30, 20};
        holeSize = new float[]{10, 5, 4f};
        damageRadius = new float[]{20, 10, 10};
        icon_id = 53;
        fire_count = 3;
    }

    @Override
    public void highPoint(Bullet b, TankScreen world) {
        if (b.getVariation() == 0) {
            b.dead = true;
            world.bullets.addBullet(b.x, b.y, b.equation.getVelocity(b.eTime).x > 0 ? 90 : -90, 0, this, 1, b.owner);
        }
    }

    @Override
    public void hit(Bullet b, TankScreen world) {
        defaultHit(b, world);
    }

    @Override
    public void update(Bullet b, TankScreen world, float eTime) {
        defaultUpdate(b, world, eTime);
        if (b.getVariation() == 1 && Math.random() < 1 / 10f) {
            world.bullets.addBullet(b.x, b.y, (float) (Math.random() * 40 - 20), 0, this, 2, b.owner);
        }
        if (b.getVariation() == 1 && eTime > 5) {
            b.dead = true;
        }
    }
}
