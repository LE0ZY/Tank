package tk.zhyu.tankfield.transition;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TransitionEffect {

	private float alpha = 0;
	private float duration;
	private float totalDuration;

	protected float getAlpha() {
		return alpha;
	}

	public void update(float delta) {
		duration += delta;
		alpha = duration / totalDuration;
	}

	public void render(Screen current, Screen next, float delta, SpriteBatch b) {

	}

	boolean isFinished() {
		return alpha >= 1;
	}

	public TransitionEffect(float duration) {
		this.duration = 0;
		this.totalDuration = duration;
	}
}