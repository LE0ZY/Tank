package tk.zhyu.tankfield;

public class BigBullet extends BulletInfo {
    public BigBullet() {
        gravity = new float[]{-98.1f};
        maxPower = 160;
        push = new float[]{0.1f};
        damage = new float[]{50};
        holeSize = new float[]{15};
        icon_id = 63 + 5;
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
    }

    public void update(Bullet b, TankScreen world, float eTime) {

    }
}
