package tk.zhyu.tankfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MenuScreen implements Screen {
    private final Image bg;
    private final Image banner;
    private TankField tankField;
    Stage stage;
    private OrthographicCamera cam;
    private ExtendViewport viewport;
    private Texture menu_bg;
    private Texture menu_banner;
    private final Group ui;
    private final Group menu1;
    private final Group start;

    public MenuScreen(final TankField tankField) {
        this.tankField = tankField;
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        {
            manager.load("menu_bg.png", Texture.class);
            manager.load("menu_banner.png", Texture.class);
            manager.finishLoading();
            menu_bg = manager.get("menu_bg.png", Texture.class);
            menu_banner = manager.get("menu_banner.png", Texture.class);
        }
        stage = new Stage();
        cam = new OrthographicCamera(640, 480);
        viewport = new ExtendViewport(640, 480, cam);
        stage.setViewport(viewport);
        bg = new Image(menu_bg);
        stage.addActor(bg);
        Image tank = new Image(Tank.atlas.findRegion("tanks_tankDesert1"));
        tank.setPosition(30, 30);
        tank.setScale(1.2f);
        stage.addActor(tank);
        ui = new Group();
        banner = new Image(menu_banner);
        ui.addActor(banner);
        start = new Group();
        ImageTextButton playButton = Buttons.getBlueButton(10, 220, "Play", 0, 1);
        playButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tankField.tankScreen.hills = false;
                showPlayMenu();

            }
        });
        start.addActor(playButton);
        ImageTextButton groundButton = Buttons.getBlueButton(10, 140, "Hills", 0, 1);
        groundButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tankField.tankScreen.hills = true;
                showPlayMenu();
            }
        });
        start.addActor(groundButton);
        ui.addActor(start);

        menu1 = new Group();
        ImageTextButton easyButton = Buttons.getGreenButton(10, 380, "Easy", 0, 1);
        easyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tankField.tankScreen.setDifficulty(1);
                tankField.tankScreen.local = false;
                tankField.goToScreen(tankField.tankScreen);
            }
        });
        menu1.addActor(easyButton);
        ImageTextButton normalButton = Buttons.getBlueButton(10, 300, "Normal", 0, 1);
        normalButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tankField.tankScreen.setDifficulty(2);
                tankField.tankScreen.local = false;
                tankField.goToScreen(tankField.tankScreen);
            }
        });
        menu1.addActor(normalButton);
        ImageTextButton hellButton = Buttons.getYellowButton(10, 220, "Hard", 0, 1);
        hellButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tankField.tankScreen.setDifficulty(10);
                tankField.tankScreen.local = false;
                tankField.goToScreen(tankField.tankScreen);
            }
        });
        menu1.addActor(hellButton);
        ImageTextButton pvpButton = Buttons.getGreenButton(10, 140, "PVP", 0, 1);
        pvpButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tankField.tankScreen.setDifficulty(1);
                tankField.tankScreen.local = true;
                tankField.goToScreen(tankField.tankScreen);
            }
        });
        menu1.addActor(pvpButton);
        ImageTextButton backButton = Buttons.getGreenButton(10, 60, "Back", 0, 1);
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                showStartMenu();
            }
        });
        menu1.addActor(backButton);
        menu1.setPosition(210, 0);
        ui.addActor(menu1);
        stage.addActor(ui);
    }

    public void showStartMenu() {
        menu1.addAction(Actions.moveTo(210, 0, 0.5f, Interpolation.fade));
        start.addAction(Actions.moveTo(0, 0, 0.5f, Interpolation.fade));
    }

    public void showPlayMenu() {
        start.addAction(Actions.moveTo(210, 0, 0.5f, Interpolation.fade));
        menu1.addAction(Actions.moveTo(0, 0, 0.5f, Interpolation.fade));
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        bg.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        ui.setBounds(viewport.getWorldWidth() - 210, 0, 210, viewport.getWorldHeight());
        banner.setSize(210, viewport.getWorldHeight());
    }

    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {
        stage.dispose();
    }
}
