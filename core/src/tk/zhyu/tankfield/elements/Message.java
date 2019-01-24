package tk.zhyu.tankfield.elements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import tk.zhyu.tankfield.Buttons;

public class Message {
    private float x;
    private float y;
    private String text;
    private float time;
    final GlyphLayout layout;
    final float fontX;
    final float fontY;


    public Message(float x, float y, String text, float time) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.time = time;
        layout = new GlyphLayout(Buttons.font4, text);
        fontX = x + (50 - layout.width) / 2;
        fontY = y + (10 + layout.height) / 2;
    }

    public void update(float delta) {
        time -= delta;
    }

    public boolean isDead() {
        return time <= 0;
    }

    public void draw(Batch batch) {
        Buttons.font4.draw(batch, text, fontX, fontY);
    }
}
