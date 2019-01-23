package tk.zhyu.tankfield.bullets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import tk.zhyu.tankfield.ProjectileEquation;
import tk.zhyu.tankfield.Tank;
import tk.zhyu.tankfield.TankScreen;

public class Bullet {
    public TankScreen world;
    BulletInfo info;
    public float x;
    public float y;
    private TextureRegion texture;
    public Tank owner;
    public boolean dead = false;
    public float eTime = 0;
    private int variation = 0;
    ProjectileEquation equation;
    public boolean high = false;
    public ParticleEffectPool.PooledEffect trail;
    public float denot;
    public boolean dir;
    // FOR DRAWING
    double hypot;
    double baseAngle;

    public Bullet(ProjectileEquation equation, TankScreen world, BulletInfo info, Tank owner) {
        this.equation = equation;
        this.world = world;
        this.info = info;
        this.owner = owner;
        texture = info.getTexture(0);
        hypot = Math.hypot(texture.getRegionWidth(), texture.getRegionHeight());
        baseAngle = Math.asin(texture.getRegionHeight() / hypot);
    }


    public void update(float delta) {
        boolean old = eTime < 0.1f;
        info.update(this, world, eTime += delta);
        if (getVariation() == 0 && ((old && eTime >= 0.1f))) {
            float angle = equation.startVelocity.angleRad();
            Vector2 bP = owner.getBulletPosition();
            world.bullets.addExplosion((float) (Math.cos(angle) * 2 + bP.x), (float) (Math.sin(angle) * 2 + bP.y), 10);
        }
    }

    public void draw(Batch b) {
        Vector2 v = equation.getVelocity(eTime);
        float vAngle = v.angleRad();
        b.draw(texture, (float) (x - hypot * Math.cos(vAngle + baseAngle * (v.x > 0 ? 1 : -1)) / 60f), (float) (y - hypot * Math.sin(vAngle + baseAngle * (v.x > 0 ? 1 : -1)) / 60f), 0, 0, texture.getRegionWidth() / 30f, texture.getRegionHeight() / 30f, 1, v.x > 0 ? 1 : -1, vAngle * MathUtils.radiansToDegrees);
    }

    public int getVariation() {
        return variation;
    }

    public Bullet withVariation(int variation) {
        texture = info.getTexture(variation);
        this.variation = variation;
        hypot = Math.hypot(texture.getRegionWidth(), texture.getRegionHeight());
        baseAngle = Math.asin(texture.getRegionHeight() / hypot);
        return this;
    }
}
