package tk.zhyu.tankfield.elements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Messages extends Actor {
    Array<Message> messageArray;

    public Messages() {
        messageArray = new Array<Message>();
    }

    public void addMessage(float x, float y, String text, float time) {
        messageArray.add(new Message(x, y, text, time));
    }

    public void act(float delta) {
        for (Message message : messageArray) {
            message.update(delta);
            if (message.isDead()) messageArray.removeValue(message, true);
        }
    }

    public void draw(Batch batch, float pAlpha) {
        for (Message message : messageArray) {
            message.draw(batch);
        }
    }
}
