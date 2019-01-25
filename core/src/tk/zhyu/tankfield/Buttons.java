package tk.zhyu.tankfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import tk.zhyu.tankfield.TankField;

public class Buttons {
    public static TextureAtlas blue;
    public static TextureAtlas green;
    public static TextureAtlas grey;
    public static TextureAtlas red;
    public static TextureAtlas yellow;
    public static Skin blueSkin;
    public static Skin greenSkin;
    public static Skin greySkin;
    public static Skin redSkin;
    public static Skin yellowSkin;
    public static BitmapFont font4;
    public static BitmapFont font16;
    public static BitmapFont font32;
    public static BitmapFont font64;
    public static BitmapFont blocks_font;

    public static void loadAtlas() {
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        manager.load("button/blueSheet.atlas", TextureAtlas.class);
        manager.load("button/greenSheet.atlas", TextureAtlas.class);
        manager.load("button/greySheet.atlas", TextureAtlas.class);
        manager.load("button/redSheet.atlas", TextureAtlas.class);
        manager.load("button/yellowSheet.atlas", TextureAtlas.class);
        manager.load("font_4.fnt", BitmapFont.class);
        manager.load("font_16.fnt", BitmapFont.class);
        manager.load("font_32.fnt", BitmapFont.class);
        manager.load("font_64.fnt", BitmapFont.class);
        manager.load("blocks.fnt", BitmapFont.class);
        manager.finishLoading();
        blue = manager.get("button/blueSheet.atlas", TextureAtlas.class);
        green = manager.get("button/greenSheet.atlas", TextureAtlas.class);
        grey = manager.get("button/greySheet.atlas", TextureAtlas.class);
        red = manager.get("button/redSheet.atlas", TextureAtlas.class);
        yellow = manager.get("button/yellowSheet.atlas", TextureAtlas.class);
        font4 = manager.get("font_4.fnt", BitmapFont.class);
        font4.getData().setScale(1 / 4f);
        font16 = manager.get("font_16.fnt", BitmapFont.class);
        font32 = manager.get("font_32.fnt", BitmapFont.class);
        font64 = manager.get("font_64.fnt", BitmapFont.class);
        blocks_font = manager.get("blocks.fnt", BitmapFont.class);
        blueSkin = new Skin();
        blueSkin.addRegions(blue);
        greenSkin = new Skin();
        greenSkin.addRegions(green);
        greySkin = new Skin();
        greySkin.addRegions(grey);
        redSkin = new Skin();
        redSkin.addRegions(red);
        yellowSkin = new Skin();
        yellowSkin.addRegions(yellow);
    }

    public static ImageTextButton getButton(Skin s, float x, float y, String text, String bu, String bd) {
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.font = new BitmapFont(font32.getData(), font32.getRegion(), font32.usesIntegerPositions());
        style.up = s.getDrawable(bu);
        style.down = s.getDrawable(bd);
        style.disabled = greySkin.getDrawable(bu.replaceAll(".+_(.+)", "grey_$1"));
        style.disabledFontColor = Color.LIGHT_GRAY;
        style.fontColor = Color.DARK_GRAY;
        style.downFontColor = Color.GRAY;
        ImageTextButton button = new ImageTextButton(text, style);
        button.getLabelCell().padBottom(10);
        button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Audio.click1.play(Audio.VOLUME);
            }
        });
        button.setX(x);
        button.setY(y);
        return button;
    }

    public static ImageTextButton getBlueButton(float x, float y, String text, int i, int j) {
        return getButton(blueSkin, x, y, text, "blue_button" + addLeadingZero(i),
                "blue_button" + addLeadingZero(j));
    }

    public static ImageTextButton getBlueButton(float x, float y, String text, String bu, String bd) {
        return getButton(blueSkin, x, y, text, bu, bd);
    }

    public static ImageTextButton getGreenButton(float x, float y, String text, int i, int j) {
        return getButton(greenSkin, x, y, text, "green_button" + addLeadingZero(i),
                "green_button" + addLeadingZero(j));
    }

    public static ImageTextButton getGreenButton(float x, float y, String text, String bu, String bd) {
        return getButton(greenSkin, x, y, text, bu, bd);
    }

    public static ImageTextButton getYellowButton(float x, float y, String text, int i, int j) {
        return getButton(yellowSkin, x, y, text, "yellow_button" + addLeadingZero(i),
                "yellow_button" + addLeadingZero(j));
    }

    public static ImageTextButton getYellowButton(float x, float y, String text, String bu, String bd) {
        return getButton(yellowSkin, x, y, text, bu, bd);
    }

    public static ImageTextButton getGreyButton(float x, float y, String text, int i, int j) {
        return getButton(greySkin, x, y, text, "grey_button" + addLeadingZero(i),
                "grey_button" + addLeadingZero(j));
    }

    public static ImageTextButton getGreyButton(float x, float y, String text, String bu, String bd) {
        return getButton(greySkin, x, y, text, bu, bd);
    }

    public static String addLeadingZero(int number) {
        if (number >= 10) {
            return number + "";
        } else {
            return "0" + number;
        }
    }
}
