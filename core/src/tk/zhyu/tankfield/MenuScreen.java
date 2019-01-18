package tk.zhyu.tankfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MenuScreen implements Screen {
    private TankField tankField;
    Stage stage;
    private OrthographicCamera cam;
    private ExtendViewport viewport;
    private Texture menu_bg;

    public MenuScreen(final TankField tankField) {
        this.tankField = tankField;
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        {
            manager.load("menu.png", Texture.class);
            manager.finishLoading();
            menu_bg = manager.get("menu.png", Texture.class);
        }
        stage = new Stage();
        cam = new OrthographicCamera(640, 480);
        viewport = new ExtendViewport(640, 480, cam);
        stage.setViewport(viewport);
        stage.addActor(new Image(menu_bg));
        ImageTextButton playButton = Buttons.getBlueButton(440, 380, "Play", 0, 1);
        playButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tankField.tankScreen.setDifficulty(2);
                tankField.goToScreen(tankField.tankScreen);
            }
        });
        stage.addActor(playButton);
        ImageTextButton hellButton = Buttons.getBlueButton(440, 300, "Hell", 0, 1);
        hellButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tankField.tankScreen.setDifficulty(10);
                tankField.goToScreen(tankField.tankScreen);
            }
        });
        stage.addActor(hellButton);
        ImageTextButton easyButton = Buttons.getBlueButton(440, 220, "Easy", 0, 1);
        easyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tankField.tankScreen.setDifficulty(1);
                tankField.goToScreen(tankField.tankScreen);
            }
        });
        stage.addActor(easyButton);
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
