package tk.zhyu.tankfield.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
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

    public static boolean HTML = true;
    private Array<Bullet> bullets;
    private TankScreen screen;
    public static ParticleEffect EXPLOSION;
    public static ParticleEffect TRAIL;
    private ParticleEffectPool explosionEffectPool;
    private ParticleEffectPool trailEffectPool;
    private Array<ParticleEffectPool.PooledEffect> effects;

    public Bullets(TankScreen screen) {
        this.screen = screen;
        bullets = new Array<Bullet>();
        effects = new Array<ParticleEffectPool.PooledEffect>();
        if (!HTML) {
            explosionEffectPool = new ParticleEffectPool(EXPLOSION, 1, 1000);
            trailEffectPool = new ParticleEffectPool(TRAIL, 1, 1000);
        }
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
        }
        if (!HTML) {
            for (Bullet bullet : bullets) {
                bullet.trail.draw(batch);
            }
            for (int i = effects.size - 1; i >= 0; i--) {
                ParticleEffectPool.PooledEffect effect = effects.get(i);
                effect.draw(batch);
            }
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    public void act(float delta) {
        Iterator<Bullet> i = bullets.iterator();
        while (i.hasNext()) {
            Bullet bullet = i.next();
            bullet.update(delta);
            if (!HTML) {
                bullet.trail.setPosition(bullet.x, bullet.y);
                bullet.trail.update(delta);
            }
            if (bullet.dead) {
                if (!HTML) {
                    effects.add(bullet.trail);
                    bullet.trail.allowCompletion();
                }
                i.remove();
            }
        }
        if (!HTML) for (int a = effects.size - 1; a >= 0; a--) {
            ParticleEffectPool.PooledEffect effect = effects.get(a);
            effect.update(delta);
            if (effect.isComplete()) {
                effect.free();
                effects.removeIndex(a);
            }
        }
    }

    public Bullet addBullet(Bullet b) {
        bullets.add(b);
        if (!HTML) b.trail = trailEffectPool.obtain();
        return b;
    }

    public Bullet addBullet(float x, float y, float vx, float vy, BulletInfo info, int variation, Tank shooter) {
        return addBullet(new Bullet(new ProjectileEquation(new Vector2(x, y), new Vector2(vx, vy), info, variation), screen, info, shooter).withVariation(variation));
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
        if (!HTML) {
            ParticleEffectPool.PooledEffect pooledEffect = explosionEffectPool.obtain();
            pooledEffect.setPosition(x, y);
            pooledEffect.scaleEffect(radius / 20);
            pooledEffect.start();
            effects.add(pooledEffect);
        }
    }

    public static void init() {
        if (((TankField) Gdx.app.getApplicationListener()).platform != TankField.Platform.HTML) {
            System.out.print("Loading Effects...");
            EXPLOSION = new ParticleEffect();
            EXPLOSION.load(Gdx.files.internal("effects/explosion.particle"), BulletInfo.atlas);
            TRAIL = new ParticleEffect();
            TRAIL.load(Gdx.files.internal("effects/trail.particle"), BulletInfo.atlas);
            EXPLOSION.scaleEffect(1 / 4f);
            TRAIL.scaleEffect(1 / 8f);
            TRAIL.setEmittersCleanUpBlendFunction(false);
            EXPLOSION.setEmittersCleanUpBlendFunction(false);
            System.out.println("  Done.");
            HTML = false;
        } else
            System.out.println("Skipping Effect.");
        Fires.init();
    }

    public void reset() {
        if (!HTML) {
            for (int i = 0; i < bullets.size; i++) bullets.get(i).trail.free();
            for (int i = effects.size - 1; i >= 0; i--)
                effects.get(i).free(); //free all the effects back to the pool
            effects.clear();
        }
        if (bullets.size > 0) bullets.removeRange(0, bullets.size - 1);

    }
}
