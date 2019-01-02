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
    public boolean high = false;

    public Bullet(ProjectileEquation equation, TankScreen world, BulletInfo info) {
        this.equation = equation;
        this.world = world;
        this.info = info;
        texture = Tank.atlas.findRegion("tank_bullet" + info.bullet_icon);
    }

    public void update(float delta) {
        eTime += delta;
        x = equation.getX(eTime);
        y = equation.getY(eTime);
        info.update(this, world, eTime);
        if (equation.getVelocity(eTime).y / equation.startVelocity.y < 0 && !high) {
            high = true;
            info.highPoint(this, world);
        }
        if (world.hit(this)) {
            info.hit(this, world);
        }
        if (x < 10 || x > world.groundLength - 10) {
            dead = true;
        }
    }

    public Bullet withVariation(int variation) {
        this.variation = variation;
        return this;
    }

    public void draw(Batch b) {
        b.draw(texture, x, y, 0, texture.getRegionHeight() / 20f, texture.getRegionWidth() / 10f, texture.getRegionHeight() / 10f, 1, 1, equation.getVelocity(eTime).angle());
    }
}
