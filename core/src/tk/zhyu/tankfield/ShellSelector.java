package tk.zhyu.tankfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ShellSelector extends Actor {
    public static Texture socket;
    private Tank tank;

    public ShellSelector(float x, float y, Tank tank) {
        setPosition(x, y);
        setSize(130, 130);
        this.tank = tank;
    }

    public static void loadAssets() {
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        manager.load("socket.png", Texture.class);
        manager.finishLoading();
        socket = manager.get("socket.png", Texture.class);
    }
}
