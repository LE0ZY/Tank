package tk.zhyu.tankfield.elements;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import tk.zhyu.tankfield.Buttons;

public class Labels {
    public static Label getLabel(String str, float x, float y, float width, float height) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Buttons.blocks_font;
        Label label = new Label(str, labelStyle);
        label.setBounds(x, y, width, height);
        label.setAlignment(Align.center);
        return label;
    }
}
