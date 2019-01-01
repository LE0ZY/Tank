package tk.zhyu.tankfield.transition;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TransitionScreen implements Screen {
	Game game;

	Screen current;
	Screen next;
	SpriteBatch b;

	int currentTransitionEffect;
	ArrayList<TransitionEffect> transitionEffects;

	public TransitionScreen(Game game, Screen current, Screen next, ArrayList<TransitionEffect> transitionEffects) {
		b = new SpriteBatch();
		this.current = current;
		this.next = next;
		this.transitionEffects = transitionEffects;
		this.currentTransitionEffect = 0;
		this.game = game;
	}

	public void render(float delta) {
		if (currentTransitionEffect >= transitionEffects.size()) {
			game.setScreen(next);
			return;
		}
		b.begin();
		transitionEffects.get(currentTransitionEffect).update(delta);
		transitionEffects.get(currentTransitionEffect).render(current, next, delta, b);
		b.end();
		if (transitionEffects.get(currentTransitionEffect).isFinished())
			currentTransitionEffect++;
	}

	public void show() {

	}

	public void resize(int width, int height) {

	}

	public void pause() {

	}

	public void resume() {

	}

	public void hide() {

	}

	public void dispose() {

	}
}