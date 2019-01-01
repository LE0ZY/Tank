package tk.zhyu.tankfield;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Bullets extends Actor {

    private Array<Bullet> bullets;
    private TankScreen screen;

    public Bullets(TankScreen screen) {
        this.screen = screen;
        bullets = new Array<Bullet>();
    }

    public void draw(Batch batch, float parentAlpha) {
        for (Bullet bullet : bullets) {
            bullet.draw(batch);
        }
    }

    public void act(float delta) {
        Iterator<Bullet> i = bullets.iterator();
        while (i.hasNext()) {
            Bullet bullet = i.next();
            bullet.update(delta);
            if (bullet.dead) {
                i.remove();
            }
        }
    }

    public void addBullet(Bullet b) {
        bullets.add(b);
    }

    public void addBullet(float x, float y, float vx, float vy, BulletInfo info, int variation) {
        bullets.add(new Bullet(new ProjectileEquation(new Vector2(x, y), new Vector2(vx, vy), info, variation), screen, info));
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
}
