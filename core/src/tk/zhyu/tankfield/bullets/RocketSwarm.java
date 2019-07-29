package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class RocketSwarm extends BulletInfo {
    public RocketSwarm() {
        gravity = new float[]{-98.1f};
        maxPower = 160;
        push = new float[]{40};
        damage = new float[]{20};
        holeSize = new float[]{10};
        damageRadius = new float[]{25};
        icon_id = 4 * 9 + 0;
        fire_count = 11;
        fire_delay = 0.2f;
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
                world.bullets.addBullet(b.x, b.y, (float) (Math.cos(angle) * 100), (float) (Math.sin(angle) * 100), this, 1, b.owner);
                b.dead = true;
            }
        }
    }
}
