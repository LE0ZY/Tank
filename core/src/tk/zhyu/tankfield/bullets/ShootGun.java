package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class ShootGun extends BulletInfo {
    public ShootGun() {
        gravity = new float[]{-98.1f};
        maxPower = 120;
        push = new float[]{25};
        damage = new float[]{20};
        holeSize = new float[]{10};
        damageRadius = new float[]{30};
        icon_id = (11 * 9) + 2;
    }

    public void highPoint(Bullet b, TankScreen world) {

    }

    @Override
    public void hit(Bullet b, TankScreen world) {
        defaultHit(b, world);
    }

    @Override
    public void update(Bullet b, TankScreen world, float eTime) {
        defaultUpdate(b, world, eTime);
        if (b.getVariation() == 0) {
            float speed = b.equation.getVelocity(b.eTime).len();
            float angle = b.equation.getVelocity(b.eTime).angleRad();
            world.bullets.addBullet(b.x, b.y, (float) (speed * Math.cos(angle + 0.05)), (float) (speed * Math.sin(angle + 0.05)), this, b.getVariation() + 1, b.owner);
            world.bullets.addBullet(b.x, b.y, (float) (speed * Math.cos(angle - 0.05)), (float) (speed * Math.sin(angle - 0.05)), this, b.getVariation() + 1, b.owner);
            world.bullets.addBullet(b.x, b.y, (float) (speed * Math.cos(angle + 0.15)), (float) (speed * Math.sin(angle + 0.15)), this, b.getVariation() + 1, b.owner);
            world.bullets.addBullet(b.x, b.y, (float) (speed * Math.cos(angle - 0.15)), (float) (speed * Math.sin(angle - 0.15)), this, b.getVariation() + 1, b.owner);
            b.dead = true;
        }
    }
}
