package tk.zhyu.tankfield.bullets;

import com.badlogic.gdx.math.Vector2;

import tk.zhyu.tankfield.TankScreen;

public class ScatterStorm extends BulletInfo {
    public ScatterStorm() {
        gravity = new float[]{-98.1f, -98.1f, 0};
        maxPower = 160;
        push = new float[]{15, 10, 10};
        damage = new float[]{60, 6, 6};
        holeSize = new float[]{10, 10, 10};
        damageRadius = new float[]{20};
        icon_id = 12 * 9 - 1;
    }

    public void highPoint(Bullet b, TankScreen world) {
        if (b.getVariation() == 0) {
            Vector2 velocity = b.equation.getVelocity(b.eTime);
            for (int a = 0; a < 8; a++) {
                float angle = (float) (Math.random() * Math.PI * 2);
                world.bullets.addBullet(b.x, b.y, (float) (Math.cos(angle) * 30) + velocity.x, (float) (Math.sin(angle) * 30) + velocity.y, this, 1, b.owner);
            }
            b.dead = true;
        }
    }

    public void hit(Bullet b, TankScreen world) {
        defaultHit(b, world);
    }

    public void update(Bullet b, TankScreen world, float eTime) {
        defaultUpdate(b, world, eTime);
        if (b.getVariation() == 1 && eTime > 1) {
            if (b.owner.target.size > 0) {
                float angle = (float) Math.atan2(b.owner.target.get(0).getY() - b.y, b.owner.target.get(0).getX() - b.x);
                world.bullets.addBullet(b.x, b.y, (float) (Math.cos(angle) * 400), (float) (Math.sin(angle) * 400), this, 2, b.owner);
                b.dead = true;
            }
        }
    }
}
