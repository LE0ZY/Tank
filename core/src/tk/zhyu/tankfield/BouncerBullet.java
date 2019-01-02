package tk.zhyu.tankfield;

public class BouncerBullet extends BulletInfo {
    public BouncerBullet() {
        gravity = new float[]{-98.1f};
        maxPower = 80;
        push = new float[]{0.09f, 0.06f, 0.03f};
        damage = new float[]{20, 10, 5};
        holeSize = new float[]{10, 5, 2.5f};
        icon_id = 27;
        bullet_icon = 2;
    }

    public void highPoint(Bullet b, TankScreen world) {
    }

    public void hit(Bullet b, TankScreen world) {
        world.world.addActor(new Explosion(b.x, b.y));
        world.makeHole(b.x, b.y, holeSize[Math.min(b.variation, holeSize.length - 1)]);
        world.setFloor(world.y, (int) (b.x / 1000));
        world.explode(holeSize[Math.min(b.variation, holeSize.length - 1)] * 4, push[Math.min(b.variation, push.length - 1)], b.x, b.y);
        b.dead = true;
        if (b.variation < 2) {
            world.bullets.addBullet(b.x, b.y + 0.4f, 0, 60, this, b.variation + 1);
            world.bullets.addBullet(b.x, b.y + 0.4f, 10, 60, this, b.variation + 1);
            world.bullets.addBullet(b.x, b.y + 0.4f, -10, 60, this, b.variation + 1);
        }
    }

    public void update(Bullet b, TankScreen world, float eTime) {

    }
}