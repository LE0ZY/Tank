package tk.zhyu.tankfield.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import tk.zhyu.tankfield.TankField;
import tk.zhyu.tankfield.TankScreen;

public abstract class BulletInfo {
    public static TextureAtlas atlas;
    public float maxPower;
    public float gravity[];
    public float push[];
    public float damage[];
    public float holeSize[];
    public float damageRadius[];
    public int icon_id;
    public int fire_count = 1;
    public float fire_delay = 0.5f;

    public abstract void highPoint(Bullet b, TankScreen world);

    public abstract void hit(Bullet b, TankScreen world);

    public abstract void update(Bullet b, TankScreen world, float eTime);

    public void defaultHit(Bullet b, TankScreen world) {
        world.bullets.addExplosion(b.x, b.y, damageRadius[Math.min(b.getVariation(), damageRadius.length - 1)]);
        world.makeHole(b.x, b.y, holeSize[Math.min(b.getVariation(), holeSize.length - 1)]);
        world.explode(damageRadius[Math.min(b.getVariation(), damageRadius.length - 1)], push[Math.min(b.getVariation(), push.length - 1)], b.x, b.y, damage[Math.min(b.getVariation(), damage.length - 1)]);
        b.dead = true;
    }

    public TextureRegion getTexture(int variation) {
        int a = icon_id;
        int c = a % 9;
        a /= 9;
        TextureRegion r = atlas.findRegion(c + "-" + a + "-" + variation);
        if (r != null)
            return r;
        else
            return atlas.findRegion(c + "-" + a);
    }

    public static void defaultUpdate(Bullet b, TankScreen world, float eTime) {
        b.x = b.equation.getX(eTime);
        b.y = b.equation.getY(eTime);
        if (b.equation.getVelocity(eTime).y / b.equation.startVelocity.y < 0 && !b.high) {
            b.high = true;
            b.info.highPoint(b, world);
        }
        if (world.hit(b)) b.info.hit(b, world);
        if (b.x < 10 || b.x > world.groundLength - 10) b.dead = true;
        if (b.y > 3000) b.dead = true;
    }

    public static void init() {
        System.out.print("Loading Bullet Sprites...");
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        manager.load("bullets.txt", TextureAtlas.class);
        manager.finishLoading();
        atlas = manager.get("bullets.txt", TextureAtlas.class);
        System.out.println("  Done.");
    }
}
