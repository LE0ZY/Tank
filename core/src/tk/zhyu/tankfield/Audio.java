package tk.zhyu.tankfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import tk.zhyu.tankfield.TankField;

public class Audio {
    public static Sound click1;
    public static Sound background;
    public static Sound drive;

    public static void loadAudio() {
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        manager.load("audio/click1.ogg", Sound.class);
        manager.load("audio/TanksShock_gameplay_music.mp3", Sound.class);
        manager.load("audio/varg_drive.mp3", Sound.class);
        manager.finishLoading();
        click1 = manager.get("audio/click1.ogg");
        background = manager.get("audio/TanksShock_gameplay_music.mp3");
        drive = manager.get("audio/varg_drive.mp3");
    }
}
