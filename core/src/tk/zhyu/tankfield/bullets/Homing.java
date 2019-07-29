package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class Homing extends BulletInfo {
    public Homing() {
        gravity = new float[]{-98.1f};
        maxPower = 160;
        push = new float[]{50};
        damage = new float[]{50};
        holeSize = new float[]{20};
        damageRadius = new float[]{25};
        icon_id = 90 + 6;
    }

    @Override
    public void highPoint(Bullet b, TankScreen world) {
        if (b.getVariation() == 0) {
            if (b.owner.target.size > 0) {
                float angle = (float) Math.atan2(b.owner.target.get(0).getY() - b.y, b.owner.target.get(0).getX() - b.x);
                world.bullets.addBullet(b.x, b.y, (float) (Math.cos(angle) * 400), (float) (Math.sin(angle) * 400), this, 1, b.owner);
                b.dead = true;
            }
        }
    }

    @Override
    public void hit(Bullet b, TankScreen world) {
        defaultHit(b, world);
    }

    @Override
    public void update(Bullet b, TankScreen world, float eTime) {
        defaultUpdate(b, world, eTime);
        if (b.eTime > 2 && b.getVariation() == 0) {
            if (b.owner.target.size > 0) {
                float angle = (float) Math.atan2(b.owner.target.get(0).getY() - b.y, b.owner.target.get(0).getX() - b.x);
                world.bullets.addBullet(b.x, b.y, (float) (Math.cos(angle) * 400), (float) (Math.sin(angle) * 400), this, 1, b.owner);
                b.dead = true;
            }
        }
    }
}
