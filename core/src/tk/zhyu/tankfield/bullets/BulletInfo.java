package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public abstract class BulletInfo {
    public float maxPower;
    public float gravity[];
    public float push[];
    public float damage[];
    public float holeSize[];
    public float damageRadius[];
    public int icon_id;
    public int bullet_icon;

    public abstract void highPoint(Bullet b, TankScreen world);

    public abstract void hit(Bullet b, TankScreen world);

    public abstract void update(Bullet b, TankScreen world, float eTime);

    public void defaultHit(Bullet b, TankScreen world) {
        world.explosions.addExplosion(b.x, b.y);
        world.makeHole(b.x, b.y, holeSize[Math.min(b.variation, holeSize.length - 1)]);
        world.explode(damageRadius[Math.min(b.variation, damageRadius.length - 1)], push[Math.min(b.variation, push.length - 1)], b.x, b.y, damage[Math.min(b.variation, damage.length - 1)]);
        b.dead = true;
    }
}
