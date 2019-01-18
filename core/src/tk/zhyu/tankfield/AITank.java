package tk.zhyu.tankfield;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

import tk.zhyu.tankfield.elements.HealthBar;

public class AITank extends Tank {
    public float[] direction = new float[]{0, 0, 0, 0};

    public AITank(float x, TankScreen screen, Array<Tank> target, String skin) {
        super(x, screen, skin, (int) (Math.random() * 4 + 1), (int) (Math.random() * 3 + 1), (int) (Math.random() * 4 + 1));
        this.target = target;
    }

    public void act(float delta) {
        super.act(delta);
        if (!screen.gameOver) {
            if (isTurn()) {
                for (int a = 0; a < 4; a++) {
                    if (Double.compare(Math.abs(direction[a]), 0) != 0 && fuel > 0) {
                        setLeft(!(setRight(direction[a] > 0)));
                        direction[a] += isLeft() ? delta : -delta;
                        boolean newD = direction[a] > 0;
                        if (isRight() != newD) {
                            direction[a] = 0;
                        }
                        return;
                    } else {
                        setRight(setLeft(false));
                    }
                }
                Audio.drive.stop(soundID);
                if (target.size > 0) {
                    float xDiff = target.get(target.size - 1).getX() - getX();
                    float yDiff = target.get(target.size - 1).getY() - getY();
                    float angle = angle(inventory.peek().maxPower, xDiff, yDiff, inventory.peek().gravity[Math.min(0, inventory.peek().gravity.length)]);
                    if (xDiff < 0) {
                        angle = (float) (Math.PI - angle);
                    } else {
                        angle -= Math.PI;
                        angle = (float) (Math.PI - angle);
                    }
                    if (angle == angle) {
                        angle += Math.random() * 0.2f - 0.1f;
                        setPower((float) Math.cos(angle), (float) Math.sin(angle));
                        shoot();
                    } else {
                        if (fuel > 0)
                            kickStart(xDiff);
                        else {
                            setPower((float) Math.cos(xDiff > 0 ? Math.toRadians(45) : Math.toRadians(135)), (float) Math.sin(xDiff > 0 ? Math.toRadians(45) : Math.toRadians(135)));
                            shoot();
                        }
                    }
                } else {
                    setPower((float) Math.cos(Math.toRadians(90)), (float) Math.sin(Math.toRadians(90)));
                    shoot();
                }
            }
        }
    }

    private void kickStart(float b) {
        super.kickStart();
        direction[0] = (float) (Math.random() * 3 - 1.5) + b / 30;
        direction[1] = (float) (Math.random() * 3 - 1.5) + b / 30;
        direction[2] = (float) (Math.random() * 3 - 1.5) + b / 30;
        direction[3] = (float) (Math.random() * 3 - 1.5) + b / 30;
    }

    public float angle(float v, float x, float y, float g) {
        float sqrt = (float) Math.sqrt(v * v * v * v - g * (g * x * x + 2 * y * v * v));
        float top = v * v + sqrt;
        try {
            return (float) Math.atan(top / (g * x));
        } catch (Exception e) {
            top = v * v - sqrt;
            return (float) Math.atan(top / (g * x));
        }
    }

    public void kickStart() {
        super.kickStart();
        direction[0] = (float) (Math.random() * 4 - 2);
        direction[1] = (float) (Math.random() * 4 - 2);
        direction[2] = (float) (Math.random() * 4 - 2);
        direction[3] = (float) (Math.random() * 4 - 2);
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (getHealth() < maxHealth) {
            batch.draw(HealthBar.h_bg, getX() - 7, getY() + 20, getWidth(), 2);
            batch.draw(HealthBar.h, getX() - 7, getY() + 20, getWidth() * (Math.max(Math.min(drawHealth * 1f / maxHealth, 1), 0)), 2);
        }
    }
}
