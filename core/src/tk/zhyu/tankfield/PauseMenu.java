package tk.zhyu.tankfield;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class PauseMenu extends Actor {
    public Button button;
    public Group pauseMenu;

    public PauseMenu() {
        button = Buttons.getGreenButton(0, 0, "||", 9, 8);
        pauseMenu = new Group();
        pauseMenu.setZIndex(9999);
        button.setZIndex(9998);
        getParent().addActor(button);
        getParent().addActor(pauseMenu);
    }

    public void act(float delta) {
        if (pauseMenu.isVisible()) {

        }
    }
}
