package tk.zhyu.tankfield;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Explosion extends Actor {
    public static Animation<TextureRegion> explosion;
    public float eTime = 0;
    public static float scale = 1 / 10f;

    public static void initAnime() {
        explosion = new Animation<TextureRegion>(0.1f, Tank.atlas.findRegion("tank_explosion2"), Tank.atlas.findRegion("tank_explosion3"), Tank.atlas.findRegion("tank_explosion4"));
    }

    public Explosion(float x, float y) {
        setPosition(x, y);
    }

    public void act(float delta) {
        eTime += delta;
        if (explosion.isAnimationFinished(eTime)) {
            remove();
        }
    }

    public void draw(Batch b, float pAlpha) {
        TextureRegion currentFrame = explosion.getKeyFrame(eTime);
        b.draw(currentFrame, getX() - currentFrame.getRegionWidth() * scale / 2, getY() - currentFrame.getRegionWidth() * scale / 2, 0, 0, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), scale, scale, 0);
    }
}
