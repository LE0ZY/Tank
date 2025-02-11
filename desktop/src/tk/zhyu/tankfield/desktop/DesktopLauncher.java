package tk.zhyu.tankfield.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import tk.zhyu.tankfield.TankField;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new TankField(new DesktopFloatFormatter(), TankField.Platform.DESKTOP), config);
    }
}
