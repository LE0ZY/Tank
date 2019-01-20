package tk.zhyu.tankfield.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import tk.zhyu.tankfield.ProjectileEquation;
import tk.zhyu.tankfield.Tank;
import tk.zhyu.tankfield.TankField;
import tk.zhyu.tankfield.TankScreen;
import tk.zhyu.tankfield.bullets.Bullet;
import tk.zhyu.tankfield.bullets.BulletInfo;

public class Bullets extends Actor {

    private Array<Bullet> bullets;
    private Array<ParticleEffect> explosions;
    private Array<ParticleEffect> fade_trails;
    private TankScreen screen;
    public static ParticleEffect EXPLOSION;
    public static ParticleEffect TRAIL;

    public Bullets(TankScreen screen) {
        this.screen = screen;
        bullets = new Array<Bullet>();
        explosions = new Array<ParticleEffect>();
        fade_trails = new Array<ParticleEffect>();
    }

    public static Vector2 getAveragePosition(Array<Tank> enemy) {
        Vector2 center = new Vector2(0, 0);
        float a = 0;
        for (Tank t : enemy) {
            center.add(t.getX(), t.getY());
            a += 1;
        }
        return center.scl(1f / a);
    }

    public void draw(Batch batch, float parentAlpha) {
        for (Bullet bullet : bullets) {
            bullet.draw(batch);
            bullet.trail.draw(batch);
        }
        for (ParticleEffect particleEffect : fade_trails) {
            particleEffect.draw(batch);
        }
        for (ParticleEffect particleEffect : explosions) {
            particleEffect.draw(batch);
        }
    }

    public void act(float delta) {
        Iterator<Bullet> i = bullets.iterator();
        while (i.hasNext()) {
            Bullet bullet = i.next();
            bullet.update(delta);
            bullet.trail.setPosition(bullet.x, bullet.y);
            bullet.trail.update(delta);
            if (bullet.dead) {
                fade_trails.add(bullet.trail);
                bullet.trail.allowCompletion();
                i.remove();
            }
        }
        for (ParticleEffect particleEffect : fade_trails) {
            particleEffect.update(delta);
            if (particleEffect.isComplete()) {
                fade_trails.removeValue(particleEffect, true);
                particleEffect.dispose();
            }
        }
        for (ParticleEffect particleEffect : explosions) {
            particleEffect.update(delta);
            if (particleEffect.isComplete()) {
                explosions.removeValue(particleEffect, true);
                particleEffect.reset();
                particleEffect.dispose();
            }
        }
    }

    public void addBullet(Bullet b) {
        bullets.add(b);
        b.trail = new ParticleEffect(TRAIL);
    }

    public void addBullet(float x, float y, float vx, float vy, BulletInfo info, int variation, Tank shooter) {
        addBullet(new Bullet(new ProjectileEquation(new Vector2(x, y), new Vector2(vx, vy), info, variation), screen, info, shooter).withVariation(variation));
    }

    public boolean empty() {
        return bullets.isEmpty();
    }

    public Array<Bullet> getBullets() {
        return bullets;
    }

    public Vector2 getAveragePosition() {
        Vector2 center = new Vector2(0, 0);
        float a = 0;
        for (Bullet bullet : bullets) {
            center.add(bullet.x, bullet.y);
            a += 1;
        }
        return center.scl(1f / a);
    }

    public void addExplosion(float x, float y, float radius) {
        ParticleEffect particleEffect = new ParticleEffect(EXPLOSION);
        particleEffect.setPosition(x, y);
        particleEffect.scaleEffect(radius / 20);
        particleEffect.start();
        explosions.add(particleEffect);
    }

    public static void init() {
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        ParticleEffectLoader.ParticleEffectParameter pep = new ParticleEffectLoader.ParticleEffectParameter();
        pep.imagesDir = Gdx.files.internal("images/");
        manager.load("effects/explosion.p", ParticleEffect.class, pep);
        manager.load("effects/trail.p", ParticleEffect.class, pep);
        manager.finishLoading();
        EXPLOSION = manager.get("effects/explosion.p", ParticleEffect.class);
        TRAIL = manager.get("effects/trail.p", ParticleEffect.class);
        EXPLOSION.scaleEffect(1 / 4f);
        TRAIL.scaleEffect(1 / 8f);
    }

    public void reset() {
        if (bullets.size > 0)
            bullets.removeRange(0, bullets.size - 1);
    }
}
