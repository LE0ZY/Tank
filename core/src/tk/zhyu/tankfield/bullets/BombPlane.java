package tk.zhyu.tankfield.bullets;

import com.badlogic.gdx.Gdx;

import tk.zhyu.tankfield.TankScreen;

public class BombPlane extends Antenna {
    public BombPlane() {
        gravity = new float[]{-98.1f, 0, 0, -98.1f};
        maxPower = 160;
        push = new float[]{0, 0, 100, 50};
        damage = new float[]{0, 0, 100, 25};
        holeSize = new float[]{0, 0, 30, 15};
        damageRadius = new float[]{0, 0, 35, 30};
        icon_id = (7 * 9) + 8;
    }

    @Override
    public void action(Bullet b, TankScreen world) {
        world.bullets.addBullet(b.x - (b.dir ? 100 : -100), b.y + 100, b.dir ? 100 : -100, 0, this, 2, b.owner).denot = 0.2f;
    }

    public void update(Bullet b, TankScreen world, float eTime) {
        super.update(b, world, eTime);
        if (b.getVariation() == 2) {
            b.denot -= Gdx.graphics.getDeltaTime();
            if (b.denot <= 0) {
                world.bullets.addBullet(b.x, b.y - 10, b.equation.startVelocity.x / 2, 0, this, 3, b.owner);
                b.denot = 0.2f;
            }
            if (eTime > 1.2f)
                b.dead = true;
        }
    }
}
