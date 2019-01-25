package tk.zhyu.tankfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import tk.zhyu.tankfield.TankField;

public class Audio {
    public static Sound click1;
    public static Sound background;
    public static Sound drive;
    public static Sound explosion;
    public static Sound tank_death;
    public static float VOLUME = 1.0f;
    public static Sound bomb_fall;
    public static Sound tomahawk_explosion;
    public static Sound ufo_laser;
    public static Sound underwater_explosion;
    public static Sound volcano;

    public static void loadAudio() {
        System.out.print("Loading Audio...");
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        manager.load("audio/click1.ogg", Sound.class);
        manager.load("audio/TanksShock_gameplay_music.mp3", Sound.class);
        manager.load("audio/varg_drive.mp3", Sound.class);
        manager.load("audio/explosion_bomb.mp3", Sound.class);
        manager.load("audio/Tank_death_sequence_1_.mp3", Sound.class);
        manager.load("audio/bomb_fall_3.mp3", Sound.class);
        manager.load("audio/tomahawk_explosion.mp3", Sound.class);
        manager.load("audio/ufo_laser.mp3", Sound.class);
        manager.load("audio/underwater_explosion.mp3", Sound.class);
        manager.load("audio/volcano.mp3", Sound.class);
        manager.finishLoading();
        click1 = manager.get("audio/click1.ogg");
        background = manager.get("audio/TanksShock_gameplay_music.mp3");
        drive = manager.get("audio/varg_drive.mp3");
        explosion = manager.get("audio/explosion_bomb.mp3");
        tank_death = manager.get("audio/Tank_death_sequence_1_.mp3");
        bomb_fall = manager.get("audio/bomb_fall_3.mp3", Sound.class);
        tomahawk_explosion = manager.get("audio/tomahawk_explosion.mp3", Sound.class);
        ufo_laser = manager.get("audio/ufo_laser.mp3", Sound.class);
        underwater_explosion = manager.get("audio/underwater_explosion.mp3", Sound.class);
        volcano = manager.get("audio/volcano.mp3", Sound.class);
        System.out.println("  Done.");
    }
}
