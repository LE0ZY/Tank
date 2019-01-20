package tk.zhyu.tankfield;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;

import tk.zhyu.tankfield.elements.Joystick;
import tk.zhyu.tankfield.transition.FadeOutTransitionEffect;
import tk.zhyu.tankfield.transition.TransitionEffect;
import tk.zhyu.tankfield.transition.TransitionScreen;


public class TankField extends Game {

    public static FloatFormatter floatFormatter;
    public AssetManager manager;
    public MenuScreen sc;
    public TankScreen tankScreen;

    public TankField(FloatFormatter floatFormatter) {
        this.floatFormatter = floatFormatter;
    }

    @Override
    public void create() {
        manager = new AssetManager();
        Audio.loadAudio();
        Tank.loadAtlas();
        Joystick.initStyle();
        Buttons.loadAtlas();
        sc = new MenuScreen(this);
        tankScreen = new TankScreen(this);
        setScreen(sc);
        manager.clear();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        super.render();
    }

    @Override
    public void dispose() {
        sc.dispose();
    }

    public void goToScreen(Screen s) {
        ArrayList<TransitionEffect> transitionEffects = new ArrayList<TransitionEffect>();
        transitionEffects.add(new FadeOutTransitionEffect(0.5f));
        TransitionScreen trans = new TransitionScreen(this, getScreen(), s, transitionEffects);
        setScreen(trans);
    }
}
