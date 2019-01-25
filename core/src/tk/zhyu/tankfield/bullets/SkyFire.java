package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class SkyFire extends Antenna {
    public SkyFire() {
        gravity = new float[]{-98.1f, 0, -98.1f, -98.1f};
        maxPower = 160;
        push = new float[]{0, 0, 20, 10};
        damage = new float[]{0, 0, 20, 5};
        holeSize = new float[]{0, 0, 10, 5};
        damageRadius = new float[]{0, 0, 30, 20};
        icon_id = (9 * 9) + 4;
    }

    public void action(Bullet b, TankScreen world) {
        for (int a = 0; a < 5; a++)
            world.bullets.addBullet(b.x - 30 + a * 10, b.y + 80 - a * 10, 0, -10, this, b.getVariation() + 1, b.owner);
    }

    public void update(Bullet b, TankScreen world, float eTime) {
        super.update(b, world, eTime);
        if (b.getVariation() == 2 && eTime > 0.5f) {
            b.dead = true;
            for (float a = (float) (Math.PI / 2 - 0.2f); a < Math.PI / 2 + 0.21f; a += 0.1f) {
                world.bullets.addBullet(b.x, b.y, (float) (60 * Math.cos(a)), (float) (60 * Math.sin(a)), this, b.getVariation() + 1, b.owner);
            }
        }
    }

    public void hit(Bullet b, TankScreen world) {
        super.hit(b, world);
        if (b.getVariation() == 3) {
            world.fires.addFire(b.x, 5, 3);
        }
    }
}
