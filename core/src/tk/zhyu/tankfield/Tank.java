package tk.zhyu.tankfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Stack;

public class Tank extends Body {
    public static TextureAtlas atlas;
    public static Texture traj;
    private ProjectileEquation equation;
    private float targetAngle;
    protected TankScreen screen;

    private boolean left;
    private boolean right;
    private boolean offGround;

    private Vector2 velocity;

    private Vector2 power;
    public Stack<BulletInfo> inventory;
    private float speed = 0.5f;
    private static int trajectoryPointCount = 20;

    protected boolean turn = true;
    protected boolean face = true;


    public long soundID = -1;

    private final TextureAtlas.AtlasRegion body;
    private final TextureAtlas.AtlasRegion track;

    public Tank(float x, TankScreen screen) {
        super(x, screen.getY(x), 16, 11, screen.getCurve(x));
        targetAngle = angle;
        inventory = new Stack<BulletInfo>();
        addSimpleShells();
        power = new Vector2(0.5f, 0.5f);
        velocity = new Vector2(0, 0);
        this.screen = screen;
        body = atlas.findRegion("tanks_tankDesert_body1");
        track = atlas.findRegion("tanks_tankTracks1");
        equation = new ProjectileEquation(new Vector2(getX(), getY()), new Vector2(power.x * inventory.peek().maxPower, power.y * inventory.peek().maxPower), inventory.peek(), 0);
        left = false;
        right = false;
        offGround = false;
    }

    public void addSimpleShells() {
        for (int a = 0; a < 10; a++) {
            inventory.add(new SimpleBulletInfo());
        }
    }

    public void act(float delta) {
        if (turn) {
            if (left && !right) setX((float) (getX() - speed * Math.cos(targetAngle)));
            if (right && !left) setX((float) (getX() + speed * Math.cos(targetAngle)));
            if (left)
                face = false;
            if (right)
                face = true;
            if ((left || right) && soundID == -1) {
                soundID = Audio.drive.loop();
                System.out.println("Playing Sound, " + soundID);
            } else if (!(left || right)) {
                Audio.drive.stop(soundID);
                soundID = -1;
            }
            if (screen.getY(getX()) - screen.getY(getX() + 1) > 10) {
                screen.y[screen.getX(getX())] = screen.getY(getX() + 1) + 7;
                screen.setFloor(screen.y, (int) (getX() / 1000));
            } else if (screen.getY(getX()) - screen.getY(getX() + 1) < -10) {
                screen.y[screen.getX(getX())] = screen.getY(getX() + 1) - 7;
                screen.setFloor(screen.y, (int) (getX() / 1000));
            }
        }
        if (getX() < 10) setX(10);
        if (getX() > screen.groundLength - 10) setX(screen.groundLength - 10);
        if (offGround) {
            velocity.add(0, -0.1f);
            setY(getY() + velocity.y);
            setX(getX() + velocity.x);
            if (getY() < screen.getY(getX())) {
                offGround = false;
            }
        } else {
            targetAngle = screen.getCurve(getX());
            setY(screen.getY(getX()));
            velocity.set(0, 0);
            angle = (angle * 5 + targetAngle) / 6f;
        }
        if (turn) {
            equation.startPoint.set(getBulletPosition());
        }
        collisionBox.setRotation(angle * MathUtils.radiansToDegrees);
        collisionBox.setPosition((float) (getX() - Math.cos(angle) * width / 2), (float) (getY() - Math.sin(angle) * width / 2));
    }

    public void setCurrent(int n) {
        inventory.push(inventory.remove(n));
        equation = new ProjectileEquation(new Vector2(getX(), getY()), new Vector2(power.x * inventory.peek().maxPower, power.y * inventory.peek().maxPower), inventory.peek(), 0);
    }

    public void setPower(float x, float y) {
        power.set(x, y);
        equation.startVelocity.set(power.x * inventory.peek().maxPower, power.y * inventory.peek().maxPower);
    }

    public void draw(Batch b, float pAlpha) {
        b.draw(track, (float) (getX() - Math.cos(targetAngle) * track.originalWidth / 20), (float) (getY() - Math.sin(targetAngle) * track.originalWidth / 20), 0, 0, track.originalWidth, track.originalHeight, 1 / 10f, 1 / 10f, targetAngle * MathUtils.radiansToDegrees);
        b.draw(body, (float) (getX() - (face ? 1 : -1) * Math.cos(angle) * body.originalWidth / 20 - Math.sin(targetAngle) * 3), (float) (getY() - (face ? 1 : -1) * Math.sin(angle) * body.originalWidth / 20 + Math.cos(targetAngle) * 3), 0, 0, (face ? 1 : -1) * body.originalWidth, body.originalHeight, 1 / 10f, 1 / 10f, angle * MathUtils.radiansToDegrees);
        if (turn && !left && !right) {
            float t = 0f;
            float timeSeparation = 0.1f;
            for (int i = 0; i < trajectoryPointCount; i++) {
                float x = equation.getX(t);
                float y = equation.getY(t);
                b.draw(traj, x, y, 2, 2);
                t += timeSeparation;
            }
        }
    }

    public Vector2 getBulletPosition() {
        return new Vector2((float) (getX() + Math.cos(angle + Math.PI / 2) * 10), (float) (getY() + Math.sin(angle + Math.PI / 2) * 10));
    }

    public static void loadAtlas() {
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        manager.load("sprites.atlas", TextureAtlas.class);
        manager.finishLoading();
        atlas = manager.get("sprites.atlas", TextureAtlas.class);
        Explosion.initAnime();
        ShellSelector.loadAssets();
    }

    public void shoot() {
        Bullet b = new Bullet(equation.clone(), screen, inventory.peek());
        float finalTime = 0f;
        while (collisionBox.contains(equation.getX(finalTime), equation.getY(finalTime)))
            finalTime += 0.05f;
        finalTime += 0.05f;
        b.eTime = finalTime;
        float angle = power.angleRad();
        Explosion explosion = new Explosion((float) (Math.cos(angle) * 2), (float) (Math.sin(angle) * 2));
        Vector2 bP = getBulletPosition();
        explosion.setPosition(bP.x + explosion.getX(), bP.y + explosion.getY());
        screen.world.addActor(explosion);
        screen.bullets.addBullet(b);
        turn = false;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean setLeft(boolean left) {
        return this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean setRight(boolean right) {
        return this.right = right;
    }

    public boolean isOffGround() {
        return offGround;
    }

    public boolean isTurn() {
        return turn;
    }

    public Vector2 getPower() {
        return power;
    }

    public void kickStart() {
        turn = true;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void setOffGround(boolean offGround) {
        this.offGround = offGround;
    }
}
