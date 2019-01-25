package tk.zhyu.tankfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Stack;

import tk.zhyu.tankfield.bullets.BigBullet;
import tk.zhyu.tankfield.bullets.BombPlane;
import tk.zhyu.tankfield.bullets.BouncerBullet;
import tk.zhyu.tankfield.bullets.Bullet;
import tk.zhyu.tankfield.bullets.BulletInfo;
import tk.zhyu.tankfield.bullets.Grenade;
import tk.zhyu.tankfield.bullets.Homing;
import tk.zhyu.tankfield.bullets.ScatterStorm;
import tk.zhyu.tankfield.bullets.ShootGun;
import tk.zhyu.tankfield.bullets.SkyFire;
import tk.zhyu.tankfield.bullets.SplitBullet;
import tk.zhyu.tankfield.elements.Bullets;
import tk.zhyu.tankfield.elements.HealthBar;
import tk.zhyu.tankfield.elements.ShellSelector;

public class Tank extends Body {
    public static TextureAtlas atlas;
    static Texture traj;
    public float maxFuel = 200;
    private ProjectileEquation equation;
    private float targetAngle;
    public int maxHealth = 300;
    private int currentHealth = maxHealth;
    private int drawHealth = currentHealth;
    private float fuel = 200;

    private boolean left;
    private boolean right;
    private boolean offGround;

    private Vector2 velocity;
    public Array<Tank> target;

    private Vector2 power;
    public Stack<BulletInfo> inventory;
    private float speed = 0.5f;
    private static final int trajectoryPointCount = 5;

    boolean turn = true;
    protected boolean face = true;
    int skipTurn = 0;


    long soundID = -1;

    private final TextureAtlas.AtlasRegion body;
    private final TextureAtlas.AtlasRegion track;
    private final TextureAtlas.AtlasRegion turret;
    private float eTime = 0;

    public Tank(float x, TankScreen screen, String skin, int bodyimage, int trackimage, int turretimage) {
        super(x, screen.getY(x), 16, 11, screen.getCurve(x), screen);
        targetAngle = angle;
        inventory = new Stack<BulletInfo>();
        addSimpleShells();
        power = new Vector2(0.5f, 0.5f);
        velocity = new Vector2(0, 0);
        body = atlas.findRegion("tanks_tank" + skin + "_body" + bodyimage);
        track = atlas.findRegion("tanks_tankTracks" + trackimage);
        turret = atlas.findRegion("tanks_turret" + turretimage);
        equation = new ProjectileEquation(new Vector2(getX(), getY()), new Vector2(power.x * inventory.peek().maxPower, power.y * inventory.peek().maxPower), inventory.peek(), 0);
        left = false;
        right = false;
        offGround = false;
    }

    private void addSimpleShells() {
        inventory.add(new BigBullet());
        inventory.add(new BouncerBullet());
        //inventory.add(new RainBullet());
        inventory.add(new SplitBullet());
        inventory.add(new ScatterStorm());
        inventory.add(new ShootGun());
        inventory.add(new Homing());
        inventory.add(new BombPlane());
        //inventory.add(new Triangle());
        inventory.add(new SkyFire());
        inventory.add(new Grenade());
        //inventory.add(new RocketSwarm());
        //inventory.add(new ABulletTooOP());
    }

    public boolean shouldExplicitMove = false;

    public void act(float delta) {
        eTime += delta;
        if (!screen.gameOver) {
            if (turn || shouldExplicitMove) {
                if (left && !right && fuel > 0 && !offGround) {
                    setX((float) (getX() - speed * Math.cos(targetAngle)));
                    fuel -= speed;
                    setPower(power.x, power.y);
                }
                if (right && !left && fuel > 0 && !offGround) {
                    setX((float) (getX() + speed * Math.cos(targetAngle)));
                    fuel -= speed;
                    setPower(power.x, power.y);
                }
                if (left)
                    face = false;
                if (right)
                    face = true;
                if ((left || right) && soundID == -1) {
                    soundID = Audio.drive.loop(Audio.VOLUME);
                    System.out.println("Playing Sound, " + soundID);
                } else if (!(left || right)) {
                    Audio.drive.stop(soundID);
                    soundID = -1;
                }
            }
            if (getX() < 20) setX(20);
            if (getX() > screen.groundLength - 20) setX(screen.groundLength - 20);
            if (offGround) {
                velocity.x = Math.max(Math.min(velocity.x, 1000), -1000);
                velocity.y = Math.max(Math.min(velocity.y, 1000), -1000);
                velocity.add(0, -98.1f * 2 * delta);
                setY(getY() + velocity.y * delta);
                setX(getX() + velocity.x * delta);
                if (getY() < screen.getY(getX())) {
                    offGround = false;
                    if (velocity.len() > 150) {
                        screen.makeHole(getX(), getY(), 3);
                        screen.explode(3, 5, getX(), getY(), 10);
                        velocity.scl(2 / 3f);
                        offGround = true;
                    }
                }
            } else {
                targetAngle = (targetAngle * 5 + screen.getCurve(getX())) / 6f;
                velocity.set(0, 0);
                angle = (angle * 5 + targetAngle) / 6f;
                if (getY() > screen.getY(getX()) + 0.8f) {
                    offGround = true;
                    velocity.set((float) (speed * Math.cos(targetAngle) / delta * (left ? -1 : 1)), (float) (speed * Math.sin(targetAngle) / delta * (left ? -1 : 1)));
                } else setY(screen.getY(getX()));
            }
            if (turn) {
                equation.startPoint.set(getBulletPosition());
            }
            collisionBox.setRotation(angle * MathUtils.radiansToDegrees);
            collisionBox.setPosition((float) (getX() - Math.cos(angle) * width / 2), (float) (getY() - Math.sin(angle) * width / 2));
            drawHealth = (drawHealth * 7 + currentHealth) / 8;
        }
    }

    public void setCurrent(int n) {
        inventory.push(inventory.remove(n));
        equation = new ProjectileEquation(new Vector2(getX(), getY()), new Vector2(power.x * inventory.peek().maxPower, power.y * inventory.peek().maxPower), inventory.peek(), 0);
    }

    private static float distance(float alpha, float beta) {
        float a = alpha - beta;
        a = (float) ((a + Math.PI) % (Math.PI * 2) - Math.PI);
        return a;
    }

    public void setPower(float x, float y) {
        float diffAngle = distance((float) Math.atan2(y, x), angle);
        float mag = (float) Math.hypot(x, y);
        if (diffAngle < 0 && diffAngle > -Math.PI / 2) {
            setPower((float) (mag * Math.cos(angle + 0.01)), (float) (mag * Math.sin(angle + 0.01)));
        } else if (diffAngle > Math.PI || (diffAngle < -Math.PI / 2 && diffAngle >= -Math.PI)) {
            System.out.println(diffAngle);
            setPower((float) (mag * Math.cos(angle + Math.PI - 0.01)), (float) (mag * Math.sin(angle + Math.PI - 0.01)));
        } else {
            power.set(x, y);
            equation.startVelocity.set(power.x * inventory.peek().maxPower, power.y * inventory.peek().maxPower);
            diffAngle = distance((float) (power.angleRad() + Math.PI / 2), angle);
            face = diffAngle > 0;
        }
    }

    public void draw(Batch b, float pAlpha) {
        b.draw(track, (float) (getX() - Math.cos(targetAngle) * track.originalWidth / 20), (float) (getY() - Math.sin(targetAngle) * track.originalWidth / 20), 0, 0, track.originalWidth, track.originalHeight, 1 / 10f, 1 / 10f, targetAngle * MathUtils.radiansToDegrees);
        Vector2 barrelPosition = getBarrelPosition();
        b.draw(turret, barrelPosition.x, barrelPosition.y, 0, turret.getRegionHeight() / 20f, turret.getRegionWidth() / 10f, turret.getRegionHeight() / 10f, 1, 1, power.angle());
        b.draw(body, (float) (getX() - (face ? 1 : -1) * Math.cos(angle) * body.originalWidth / 20 - Math.sin(targetAngle) * 3), (float) (getY() - (face ? 1 : -1) * Math.sin(angle) * body.originalWidth / 20 + Math.cos(targetAngle) * 3), 0, 0, (face ? 1 : -1) * body.originalWidth, body.originalHeight, 1 / 10f, 1 / 10f, angle * MathUtils.radiansToDegrees);
        if (turn && !left && !right && !screen.gameOver) {
            float start = eTime / 5 % 0.2f;
            float timeSeparation = 0.2f;
            for (int i = 0; i < trajectoryPointCount; i++) {
                float x = equation.getX(start);
                float y = equation.getY(start);
                b.draw(traj, x, y, 2, 2);
                start += timeSeparation;
            }
        }
        if (getHealth() < maxHealth) {
            b.draw(HealthBar.h_bg, getX() - 7, getY() + 20, getWidth(), 2);
            b.draw(HealthBar.h, getX() - 7, getY() + 20, getWidth() * (Math.max(Math.min(getDrawHealth() * 1f / maxHealth, 1), 0)), 2);
        }
    }

    public Vector2 getBulletPosition() {
        float barrelRad = power.angleRad();
        Vector2 b = getBarrelPosition();
        return new Vector2((float) (b.x + Math.cos(barrelRad) * 8), (float) (b.y + Math.sin(barrelRad) * 8));
    }

    public Vector2 getBarrelPosition() {
        return new Vector2((float) (getX() + Math.cos(angle + Math.PI / 2) * 10), (float) (getY() + Math.sin(angle + Math.PI / 2) * 10));
    }

    public static void loadAtlas() {
        System.out.print("Loading Tank Sprites...");
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        manager.load("sprites.atlas", TextureAtlas.class);
        manager.finishLoading();
        atlas = manager.get("sprites.atlas", TextureAtlas.class);
        System.out.println("  Done.");
        ShellSelector.loadAssets();
        HealthBar.loadAssets();
        Bullets.init();
    }

    public void shoot() {
        int count = inventory.peek().fire_count;
        equation = new ProjectileEquation(getBulletPosition(), new Vector2(power.x * inventory.peek().maxPower, power.y * inventory.peek().maxPower), inventory.peek(), 0);
        for (int a = 0; a < count; a++) {
            Bullet b = new Bullet(equation.clone(), screen, inventory.peek(), this);
            b.eTime = -a * inventory.peek().fire_delay;
            screen.bullets.addBullet(b);
        }
        turn = false;
        inventory.pop();
        if (inventory.size() == 0) {
            addSimpleShells();
        }
        System.out.println("Shooting " + inventory.peek().icon_id + ", START: " + equation.startPoint + ", V: " + equation.startVelocity);
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
        fuel = maxFuel;
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

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int setHealth(int health) {
        this.currentHealth = health;
        if (health <= 0) {
            Audio.drive.stop(soundID);
        }
        return health;
    }

    public int getHealth() {
        return currentHealth;
    }

    public float setFuel(float fuel) {
        this.fuel = fuel;
        return fuel;
    }

    public float getFuel() {
        return fuel;
    }

    public float getDrawHealth() {
        return drawHealth;
    }
}
