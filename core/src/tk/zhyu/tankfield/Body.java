package tk.zhyu.tankfield;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Body extends Actor {
    protected float width;
    protected float height;
    protected float angle;
    protected TankScreen screen;
    public Polygon collisionBox;

    public Body(float x, float y, float width, float height, float angle, TankScreen screen) {
        this.width = width;
        this.height = height;
        setBounds(x, y, width, height);
        this.angle = angle;
        setPosition(x, y);
        collisionBox = new Polygon(new float[]{0, 0, width, 0, width, height, 0, height});
        collisionBox.setRotation(angle * MathUtils.radiansToDegrees);
        collisionBox.setPosition((float) (x - Math.cos(angle) * width / 2), (float) (y - Math.sin(angle) * width / 2));
        this.screen = screen;
    }

    public boolean hit(float x, float y) {
        return collisionBox.contains(x, y);
    }
}
