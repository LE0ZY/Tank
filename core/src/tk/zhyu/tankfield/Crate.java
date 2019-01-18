package tk.zhyu.tankfield;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Crate extends Body {
    private TextureRegion crateImage;

    public Crate(float x, float y, TextureRegion crateImage, TankScreen screen) {
        super(x - crateImage.getRegionWidth() / 20f, y, crateImage.getRegionWidth() / 10f, crateImage.getRegionHeight() / 10f, 0, screen);
        this.crateImage = crateImage;
    }

    public void act(float delta) {
        if (getY() > screen.getY(getX())) {
            setY(screen.getY(getX()));
            angle = screen.getCurve(getX());
            collisionBox.setRotation(angle * MathUtils.radiansToDegrees);
            collisionBox.setPosition((float) (getX() - Math.cos(angle) * width / 2), (float) (getY() - Math.sin(angle) * width / 2));
        }
    }

    public void draw(Batch b, float pAlpha) {
        b.draw(crateImage, (float) (getX() - Math.cos(angle) * crateImage.getRegionWidth() / 20), (float) (getY() - Math.sin(angle) * crateImage.getRegionWidth() / 20), 0, 0, crateImage.getRegionWidth(), crateImage.getRegionHeight(), 1 / 10f, 1 / 10f, angle * MathUtils.radiansToDegrees);
    }

    public void received(Tank tank) {
        remove();
        screen.bodies.removeValue(this, true);
    }
}
