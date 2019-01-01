package tk.zhyu.tankfield;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PolygonActor extends Actor {
    private PolygonRegion region;
    private float width = 0;
    private float height = 0;

    public PolygonActor(PolygonRegion region, float width, float height) {
        this.region = region;
        this.width = width;
        this.height = height;
    }

    public void draw(Batch batch, float parentAlpha) {
        if (width != 0)
            ((PolygonSpriteBatch) batch).draw(region, getX(), getY(), width, height);
        else
            ((PolygonSpriteBatch) batch).draw(region, getX(), getY());
    }

    public void setRegion(PolygonRegion region) {
        this.region = region;
    }
}
