package tk.zhyu.tankfield.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import tk.zhyu.tankfield.TankField;

public class Joystick extends Touchpad {

    public static Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();

    public Joystick() {
        super(10, style);
    }

    public Joystick(float dead) {
        super(dead, style);
    }

    public static void initStyle() {
        System.out.print("Loading Joystick Sprites...");
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        manager.load("knob.png", Texture.class);
        manager.load("bg.png", Texture.class);
        manager.finishLoading();
        Texture knob = manager.get("knob.png", Texture.class);
        Texture bg = manager.get("bg.png", Texture.class);
        style.background = new TextureRegionDrawable(new TextureRegion(bg));
        style.knob = new TextureRegionDrawable(new TextureRegion(knob));
        System.out.println("  Done.");
    }
}
