package tk.zhyu.tankfield.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import tk.zhyu.tankfield.Buttons;
import tk.zhyu.tankfield.Tank;
import tk.zhyu.tankfield.TankField;

public class HealthBar extends Actor {
    private boolean direction;
    public Tank tank;
    public static Texture h_bg;
    public static Texture h;

    public HealthBar(boolean direction, float x, float y, Tank tank) {
        setPosition(x, y);
        this.direction = direction;
        this.tank = tank;
    }

    public void draw(Batch batch, float pAlpha) {
        batch.draw(h_bg, getX(), getY(), getWidth(), getHeight());
        batch.draw(h, direction ? getX() + 5 : getX() + getWidth() - 5, getY() + 5, (direction ? 1 : -1) * (getWidth() - 10) * (Math.max(Math.min(tank.drawHealth * 1f / tank.maxHealth, 1), 0)), getHeight() - 10);
        Buttons.font16.draw(batch, tank.getHealth() + "/" + tank.maxHealth, getX(), getY() + getHeight() / 3 * 2, getWidth(), Align.center, false);
        batch.draw(h_bg, getX(), getY() - getHeight(), getWidth(), getHeight());
        batch.draw(h, direction ? getX() + 5 : getX() + getWidth() - 5, getY() + 5 - getHeight(), (direction ? 1 : -1) * (getWidth() - 10) * (Math.max(Math.min(tank.fuel * 1f / tank.maxFuel, 1), 0)), getHeight() - 10);
        Buttons.font16.draw(batch, "Fuel: " + TankField.floatFormatter.getFormattedString(tank.fuel / tank.maxFuel * 100) + "%", getX(), getY() + getHeight() / 3 * 2 - getHeight(), getWidth(), Align.center, false);
    }

    public static void loadAssets() {
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        manager.load("health_background.png", Texture.class);
        manager.load("health.png", Texture.class);
        manager.finishLoading();
        h_bg = manager.get("health_background.png", Texture.class);
        h = manager.get("health.png", Texture.class);
    }
}
