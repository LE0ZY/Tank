package tk.zhyu.tankfield.bullets;

import com.badlogic.gdx.math.Vector2;

import tk.zhyu.tankfield.TankScreen;

public class ABulletTooOP extends BulletInfo {
    public ABulletTooOP() {
        gravity = new float[]{-98.1f};
        maxPower = 400;
        push = new float[]{50};
        damage = new float[]{Float.MAX_VALUE / 4};
        holeSize = new float[]{200};
        damageRadius = new float[]{400};
        icon_id = 44;
        bullet_icon = 4;
    }

    @Override
    public void highPoint(Bullet b, TankScreen world) {
        if (b.variation == 0) {
            world.bullets.addBullet(b.x, b.y, b.equation.startVelocity.x, b.equation.startVelocity.y, this, 0, b.owner);
        }
    }

    @Override
    public void hit(Bullet b, TankScreen world) {
        if (b.variation < 5) {
            defaultHit(b, world);
            Vector2 velocity = b.equation.getVelocity(b.eTime);
            world.bullets.addBullet(b.x, b.y, velocity.x, -velocity.y / 1.2f, this, b.variation + 1, b.owner);
        }
    }

    @Override
    public void update(Bullet b, TankScreen world, float eTime) {
    }
}
