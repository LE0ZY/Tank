package tk.zhyu.tankfield.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;

import tk.zhyu.tankfield.Tank;
import tk.zhyu.tankfield.TankField;
import tk.zhyu.tankfield.TankScreen;
import tk.zhyu.tankfield.bullets.BulletInfo;

public class Fires extends Actor {
    private static boolean HTML = true;
    private ParticleEffectPool fireEffectPool;
    public static ParticleEffect FIRE;
    private TankScreen world;
    private Array<Fire> fireArray;
    private ParticleEffectPool.PooledEffect[] effects;
    private boolean[] onFire;

    public Fires(TankScreen screen) {
        world = screen;
        onFire = new boolean[world.groundLength / 5];
        Arrays.fill(onFire, false);
        fireArray = new Array<Fire>();
        effects = new ParticleEffectPool.PooledEffect[onFire.length];
        if (!HTML) {
            fireEffectPool = new ParticleEffectPool(FIRE, 1, 1000);
            for (int a = 0; a < onFire.length; a++) {
                effects[a] = fireEffectPool.obtain();
            }
        }
    }

    public void act(float delta) {
        Arrays.fill(onFire, false);
        for (Fire fire : fireArray) fire.update(onFire);
        if (!HTML) {
            for (int a = 0; a < onFire.length; a++) {
                if (!onFire[a]) effects[a].allowCompletion();
                effects[a].update(delta);
                effects[a].setPosition(a * 5, world.getY(a * 5));
                if (effects[a].isComplete() && onFire[a]) {
                    effects[a].start();
                }
            }
        }
    }

    public void draw(Batch batch, float pAlpha) {
        if (!HTML) {
            for (int a = 0; a < onFire.length; a++) {
                if (onFire[a]) effects[a].draw(batch);
            }
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    public void addFire(float x, float area, int round) {
        fireArray.add(new Fire(x, area, round));
    }

    public void updateRound() {
        for (Fire fire : fireArray) --fire.round;
        for (Tank t : world.tank)
            if (onFire(t.getX())) {
                t.setHealth(t.getHealth() - 20);
                world.messages.addMessage(t.getX(), t.getY(), "20", 2);
                System.out.println("Fire Damage 20");
            }
        for (Tank t : world.enemy)
            if (onFire(t.getX())) {
                t.setHealth(t.getHealth() - 20);
                world.messages.addMessage(t.getX(), t.getY(), "20", 2);
                System.out.println("Fire Damage 20");
            }
    }

    public boolean onFire(float x) {
        for (Fire fire : fireArray)
            if (fire.onFire(x))
                return true;
        return false;
    }

    public void reset() {
        Arrays.fill(onFire, false);
        for (Fire fire : fireArray) fire.round = -1;
        if (!HTML) {
            for (ParticleEffectPool.PooledEffect fire : effects) {
                fire.reset();
                fire.allowCompletion();
            }
        }
        if (fireArray.size > 0) fireArray.removeRange(0, fireArray.size - 1);
    }

    public class Fire {
        float x;
        private float area;
        int round;

        public Fire(float x, float area, int round) {
            this.x = x;
            this.area = area;
            this.round = round;
        }

        public boolean onFire(float x) {
            return this.x + area / 2 > x && x > this.x - area / 2;
        }

        public void update(boolean[] onFire) {
            for (int a = (int) ((x - area / 2) / 5); a < (x + area / 2) / 5; a++)
                onFire[Math.max(Math.min(a, onFire.length - 1), 0)] = true;
        }
    }

    public static void init() {
        if (((TankField) Gdx.app.getApplicationListener()).platform != TankField.Platform.HTML) {
            System.out.print("Loading Effects...");
            FIRE = new ParticleEffect();
            FIRE.load(Gdx.files.internal("effects/fire.p"), BulletInfo.atlas);
            FIRE.scaleEffect(1 / 8f);
            FIRE.setEmittersCleanUpBlendFunction(false);
            System.out.println("  Done.");
            HTML = false;
        } else
            System.out.println("Skipping Effect.");
    }
}
