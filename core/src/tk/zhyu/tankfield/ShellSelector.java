package tk.zhyu.tankfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ShellSelector extends Actor {
    public static Texture socket;
    public static TextureRegion[][] weapons;
    private Tank tank;
    float eTime = 0;
    public Group selector;
    final ScrollPane sp;

    public ShellSelector(float x, float y, final Tank tank, Group parent) {
        setPosition(x, y);
        setSize(66, 66);
        this.tank = tank;
        selector = new Group();
        sp = new ScrollPane(selector);
        sp.setVisible(false);
        sp.setBounds(x, y + 66, 66, 66 * 5);
        selector.setSize(66, 66 * tank.inventory.size());
        for (int a = 0; a < tank.inventory.size(); a++) {
            int t = tank.inventory.get(a).icon_id;
            int u = t % 9;
            t /= 9;
            Image i = new Image(weapons[t][u]);
            i.setBounds(0, a * 66, 66, 66);
            final int finalA = a;
            i.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    tank.setCurrent(finalA);
                    sp.setVisible(false);
                }
            });
            selector.addActor(i);
        }
        addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                sp.setVisible(!sp.isVisible());
            }
        });
        parent.addActor(sp);
    }

    public void act(float delta) {
        eTime += delta;
        sp.setPosition(getX(), getY() + 66);
    }

    public void draw(Batch b, float pAlpha) {
        b.draw(socket, getX(), getY(), 66, 66);
        int a = tank.inventory.peek().icon_id;
        int c = a % 9;
        a /= 9;
        b.draw(weapons[a][c], getX() + 1, getY() + 1, 64, 64);
    }

    public static void loadAssets() {
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        manager.load("socket.png", Texture.class);
        manager.load("SpriteCollection_WeaponIcons@4x0.png", Texture.class);
        manager.finishLoading();
        socket = manager.get("socket.png", Texture.class);
        weapons = new TextureRegion(manager.get("SpriteCollection_WeaponIcons@4x0.png", Texture.class)).split(208, 208);
    }
}
