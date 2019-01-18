package tk.zhyu.tankfield.elements;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import tk.zhyu.tankfield.Tank;

public class Explosions extends Actor {
    public static Animation<TextureRegion> animation;
    public Array<Explosion> explosionArray;
    public static float scale = 1 / 10f;

    public static void initAnime() {
        animation = new Animation<TextureRegion>(0.1f, Tank.atlas.findRegion("tank_explosion2"), Tank.atlas.findRegion("tank_explosion3"), Tank.atlas.findRegion("tank_explosion4"));
    }

    public Explosions() {
        explosionArray = new Array<Explosion>();
    }

    public void act(float delta) {
        for (Explosion explosion : explosionArray) {
            if (animation.isAnimationFinished(explosion.eTime)) {
                explosionArray.removeValue(explosion, true);
            } else
                explosion.eTime += delta;
        }
    }

    public void draw(Batch b, float pAlpha) {
        for (Explosion explosion : explosionArray) {
            TextureRegion currentFrame = animation.getKeyFrame(explosion.eTime);
            b.draw(currentFrame, explosion.position.x + getX() - currentFrame.getRegionWidth() * scale / 2, explosion.position.y + getY() - currentFrame.getRegionWidth() * scale / 2, 0, 0, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), scale, scale, 0);
        }
    }

    public void addExplosion(float x, float y) {
        explosionArray.add(new Explosion(x, y));
    }

    public class Explosion {
        public float eTime = 0;
        public Vector2 position;

        public Explosion(float x, float y) {
            position = new Vector2(x, y);
        }
    }
}
