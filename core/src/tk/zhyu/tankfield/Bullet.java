package tk.zhyu.tankfield;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bullet {
    public TankScreen world;
    private BulletInfo info;
    public float x;
    public float y;
    private TextureRegion texture;
    public boolean dead = false;
    public float eTime = 0;
    public int variation = 0;
    ProjectileEquation equation;

    public Bullet(ProjectileEquation equation, TankScreen world, BulletInfo info) {
        this.equation = equation;
        this.world = world;
        this.info = info;
        texture = Tank.atlas.findRegion("tank_bullet1");
    }

    public void update(float delta) {
        eTime += delta;
        x = equation.getX(eTime);
        y = equation.getY(eTime);
        if (world.hit(this)) {
            dead = true;
            hit();
        }
        if (x < 10 || x > world.groundLength - 10) {
            dead = true;
        }
    }

    public Bullet withVariation(int variation) {
        this.variation = variation;
        return this;
    }

    public void hit() {
        world.world.addActor(new Explosion(x, y));
        world.makeHole(x, y, info.holeSize[Math.min(variation, info.holeSize.length - 1)]);
        world.setFloor(world.y, (int) (x / 1000));
        world.explode(info.holeSize[Math.min(variation, info.holeSize.length - 1)], info.push[Math.min(variation, info.push.length - 1)], x, y);
    }

    public void draw(Batch b) {
        float angle = equation.getVelocity(eTime).angleRad();
        float hypotd2 = (float) (Math.hypot(texture.getRegionWidth(), texture.getRegionHeight()) / 20f);
        b.draw(texture, (float) (x - Math.cos(angle) * hypotd2), (float) (y - Math.sin(angle) * hypotd2), 0, 0, texture.getRegionWidth(), texture.getRegionHeight(), 1 / 10f, 1 / 10f, (float) (equation.getVelocity(eTime).angle()));
    }
}
