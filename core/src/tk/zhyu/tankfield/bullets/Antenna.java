package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public abstract class Antenna extends BulletInfo {

    public Antenna() {
        gravity = new float[]{-98.1f, 0};
        maxPower = 160;
        push = new float[]{0, 0};
        damage = new float[]{0, 0};
        holeSize = new float[]{0, 0};
        damageRadius = new float[]{0, 0};
        icon_id = (11 * 9) + 2;
    }

    @Override
    public void highPoint(Bullet b, TankScreen world) {

    }

    @Override
    public void hit(Bullet b, TankScreen world) {
        if (b.getVariation() == 0) {
            world.bullets.addBullet(b.x, b.y, 0, 0, this, 1, b.owner).dir = b.equation.startVelocity.x > 0;
            b.dead = true;
        } else defaultHit(b, world);
    }

    @Override
    public void update(Bullet b, TankScreen world, float eTime) {
        if (b.getVariation() != 1)
            defaultUpdate(b, world, eTime);
        else if (b.getVariation() == 1) {
            b.x = b.equation.getX(b.eTime);
            b.y = b.equation.getY(b.eTime);
            if (b.eTime > 1) {
                b.dead = true;
                action(b, world);
            }
        }
    }

    public abstract void action(Bullet b, TankScreen world);
}
