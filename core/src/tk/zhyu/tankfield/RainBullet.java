package tk.zhyu.tankfield;

public class RainBullet extends BulletInfo {
    public RainBullet() {
        gravity = new float[]{-98.1f, 0, -98.1f};
        maxPower = 160;
        push = new float[]{0.09f, 0.06f, 0.1f};
        damage = new float[]{20, 10, 5};
        holeSize = new float[]{10, 5, 0};
        icon_id = 53;
        bullet_icon = 3;
    }

    @Override
    public void highPoint(Bullet b, TankScreen world) {
        if (b.variation == 0) {
            b.dead = true;
            world.bullets.addBullet(b.x, b.y, b.equation.getVelocity(b.eTime).x > 0 ? 40 : -40, 0, this, 1);
        }
    }

    @Override
    public void hit(Bullet b, TankScreen world) {
        world.world.addActor(new Explosion(b.x, b.y));
        world.makeHole(b.x, b.y, holeSize[Math.min(b.variation, holeSize.length - 1)]);
        world.setFloor(world.y, (int) (b.x / 1000));
        world.explode(holeSize[Math.min(b.variation, holeSize.length - 1)] * 4, push[Math.min(b.variation, push.length - 1)], b.x, b.y);
        b.dead = true;
    }

    @Override
    public void update(Bullet b, TankScreen world, float eTime) {
        if (b.variation == 1 && Math.random() < 1 / 10f) {
            world.bullets.addBullet(b.x, b.y, (float) (Math.random() * 40 - 20), 0, this, 2);
        }
        if (b.variation == 1 && eTime > 5) {
            b.dead = true;
        }
    }
}
