package tk.zhyu.tankfield;

public class AITank extends Tank {
    public float[] direction = new float[]{0, 0, 0, 0};
    private Tank target;

    public AITank(float x, TankScreen screen, Tank target) {
        super(x, screen);
        this.target = target;
    }

    public void act(float delta) {
        super.act(delta);
        if (isTurn()) {
            for (int a = 0; a < 4; a++) {
                if (Double.compare(Math.abs(direction[a]), 0) != 0) {
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
            float xDiff = target.getX() - getX();
            float yDiff = target.getY() - getY();
            float angle = angle(inventory.peek().maxPower, xDiff, yDiff, inventory.peek().gravity[Math.min(0, inventory.peek().gravity.length)]);
            if (xDiff < 0) {
                angle = (float) (Math.PI - angle);
            } else {
                angle -= Math.PI;
                angle = (float) (Math.PI - angle);
            }
            if (angle == angle) {
                setPower((float) Math.cos(angle), (float) Math.sin(angle));
                shoot();
                screen.next = target;
            } else {
                kickStart(xDiff);
            }
        }
    }

    private void kickStart(float b) {
        super.kickStart();
        direction[0] = (float) (Math.random() * 4 - 2) + b / 100;
        direction[1] = (float) (Math.random() * 4 - 2) + b / 100;
        direction[2] = (float) (Math.random() * 4 - 2) + b / 100;
        direction[3] = (float) (Math.random() * 4 - 2) + b / 100;
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
}
