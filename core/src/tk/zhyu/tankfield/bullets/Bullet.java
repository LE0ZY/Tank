package tk.zhyu.tankfield.bullets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import tk.zhyu.tankfield.ProjectileEquation;
import tk.zhyu.tankfield.Tank;
import tk.zhyu.tankfield.TankScreen;

public class Bullet {
    public TankScreen world;
    private BulletInfo info;
    public float x;
    public float y;
    private TextureRegion texture;
    public Tank owner;
    public boolean dead = false;
    public float eTime = 0;
    public int variation = 0;
    ProjectileEquation equation;
    public boolean high = false;

    public Bullet(ProjectileEquation equation, TankScreen world, BulletInfo info, Tank owner) {
        this.equation = equation;
        this.world = world;
        this.info = info;
        texture = Tank.atlas.findRegion("tank_bullet" + info.bullet_icon);
        this.owner = owner;
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
        if (y > 3000) {
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
