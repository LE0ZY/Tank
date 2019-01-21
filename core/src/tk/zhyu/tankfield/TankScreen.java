package tk.zhyu.tankfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import tk.zhyu.tankfield.bullets.Bullet;
import tk.zhyu.tankfield.bullets.BulletInfo;
import tk.zhyu.tankfield.bullets.Nuke;
import tk.zhyu.tankfield.elements.Bullets;
import tk.zhyu.tankfield.elements.HealthBar;
import tk.zhyu.tankfield.elements.Joystick;
import tk.zhyu.tankfield.elements.Labels;
import tk.zhyu.tankfield.elements.ShellSelector;


public class TankScreen implements Screen, GestureDetector.GestureListener {
    public static Texture greyOverlay;
    private final Label gameOverLabel;
    private final ImageTextButton goBackButton;
    private final Image pauseOverlay;
    private ImageTextButton pauseButton;
    private Group pauseMenu;
    public RoundState roundState = RoundState.SELF;
    private int difficulty = 1;
    private boolean paused = false;
    public int seed;

    enum RoundState {
        SELF, ENEMY, BULLETS_OF_SELF, BULLETS_OF_ENEMY
    }

    private Group ui;
    private Texture sky;
    public Bullets bullets;
    private Joystick shootStick;
    private Joystick movementStick;
    private ImageTextButton fireButton;
    ShellSelector shellSelector;
    private TankField tankField;
    Stage stage;
    Group world;
    Group gameOverScreen;
    private OrthographicCamera cam;
    private ExtendViewport viewport;
    HealthBar hb;
    Image overlay;

    ShapeRenderer debug;

    public int groundLength = 1000;
    public float y[] = new float[groundLength];
    Array<Tank> tank;
    Array<Tank> enemy;
    Texture dirt;
    public float scale = 2;
    InputMultiplexer multiplexer;
    public Vector2 offset = new Vector2();
    public Vector2 renderDiff;
    TextureRegion bedrocks;

    public Array<Body> bodies = new Array<Body>();

    public PolygonActor ground;
    private boolean update = false;
    private boolean won = false;
    public boolean gameOver = false;

    public TankScreen(final TankField tankField) {
        this.tankField = tankField;
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        {
            manager.load("terrain.png", Texture.class);
            manager.load("sky.png", Texture.class);
            manager.load("traj.png", Texture.class);
            manager.load("bedrock.png", Texture.class);
            manager.load("gameOver_overlay.png", Texture.class);
            manager.finishLoading();
            dirt = manager.get("terrain.png", Texture.class);
            sky = manager.get("sky.png", Texture.class);
            Tank.traj = manager.get("traj.png", Texture.class);
            Texture bedrock = manager.get("bedrock.png", Texture.class);
            bedrock.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            bedrocks = new TextureRegion(bedrock);
            bedrocks.setRegion(0, 0, groundLength, 100);
            greyOverlay = manager.get("gameOver_overlay.png", Texture.class);
        }
        cam = new OrthographicCamera(640, 480);
        viewport = new ExtendViewport(640, 480, cam);
        stage = new Stage(viewport, new PolygonSpriteBatch());
        stage.setViewport(viewport);
        world = new Group();
        debug = new ShapeRenderer();
        TextureRegionDrawable imgTextureRegionDrawable = new TextureRegionDrawable(bedrocks);
        Image img = new Image();
        img.setDrawable(imgTextureRegionDrawable);
        img.setSize(groundLength, 100);
        img.setPosition(0, -10100);
        world.addActor(img);
        ui = new Group();
        makeFloor();
        renderDiff = new Vector2(0, 0);
        tank = new Array<Tank>();
        enemy = new Array<Tank>();
        world.setScale(scale);
        bullets = new Bullets(this);
        world.addActor(bullets);
        movementStick = new Joystick();
        ui.addActor(movementStick);
        shootStick = new Joystick(2);
        shootStick.setPosition(640 - 256, 0);
        ui.addActor(shootStick);
        fireButton = Buttons.getYellowButton(640 - 356, 0, "Fire", 0, 1);
        fireButton.setSize(100, 50);
        fireButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (tank.get(0).turn) {
                    tank.get(0).shoot();
                }
            }
        });
        ui.addActor(fireButton);
        addTanks();
        shellSelector = new ShellSelector(640 - 356, 60, tank.get(0), ui);
        ui.addActor(shellSelector);
        hb = new HealthBar(true, 0, 480 - 40, tank.get(0));
        hb.setSize(300, 40);
        ui.addActor(hb);
        if (Bullets.HTML) {
            Label noYou = Labels.getLabel("HTML Do not support Effects", 0, 480 - 100, 640, 24);
            ui.addActor(noYou);
        }
        gameOverScreen = new Group();
        overlay = new Image(greyOverlay);
        gameOverScreen.addActor(overlay);
        gameOverLabel = Labels.getLabel("Troll Lol Lol", 0, 0, 640, 480);
        gameOverScreen.addActor(gameOverLabel);
        goBackButton = Buttons.getGreenButton(640 / 2 - 100, 480 / 2 - 100, "Back", 2, 3);
        ImageTextButton.ImageTextButtonStyle style = goBackButton.getStyle();
        style.font = Buttons.blocks_font;
        goBackButton.setStyle(style);
        goBackButton.setSize(200, 50);
        goBackButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tankField.goToScreen(tankField.sc);
            }
        });
        gameOverScreen.addActor(goBackButton);
        pauseMenu = new Group();
        pauseMenu.setBounds(0, 0, 640, 480);
        pauseButton = Buttons.getGreenButton(0, 0, "Pause", 0, 1);
        pauseMenu = new Group();
        pauseMenu.setZIndex(100);
        pauseMenu.setVisible(false);
        pauseOverlay = new Image(greyOverlay);
        pauseOverlay.setBounds(0, 0, 640, 480);
        pauseMenu.addActor(pauseOverlay);
        pauseButton.setZIndex(101);
        pauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (paused) resume();
                else pause();
            }
        });
        ImageTextButton restart = Buttons.getYellowButton(0, 0, "Restart", 2, 3);
        restart.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                show();
            }
        });
        pauseMenu.addActor(restart);
        ImageTextButton quit = Buttons.getYellowButton(0, 0, "Exit", 0, 1);
        quit.setPosition(0, restart.getHeight());
        quit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tankField.goToScreen(tankField.sc);
            }
        });
        pauseMenu.addActor(quit);
        ui.addActor(pauseButton);
        ui.addActor(pauseMenu);
        stage.addActor(world);
        stage.addActor(ui);
        stage.addActor(gameOverScreen);
        stage.addListener(new InputListener() {
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys
                            .EQUALS:
                        scale *= 1.1;
                        world.setScale(scale);
                        break;
                    case Input.Keys
                            .MINUS:
                        scale /= 1.1;
                        world.setScale(scale);
                        break;
                }
                return false;
            }
        });
        GestureDetector gestureDetector = new GestureDetector(this);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gestureDetector);
    }

    public TankScreen getThis() {
        return this;
    }

    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
        Audio.background.stop();
        Audio.background.loop(0.4f);
        ui.setVisible(true);
        gameOver = false;
        for (Body b : bodies) {
            b.remove();
        }
        bullets.reset();
        bodies.removeRange(0, bodies.size - 1);
        seed = (int) (Math.random() * 1000);
        makeFloor();
        addTanks();
        bullets.toFront();
        ground.toFront();
        gameOverScreen.setVisible(false);
        hb.tank = tank.get(0);
        shellSelector.tank = tank.get(0);
        roundState = RoundState.SELF;
        Audio.drive.stop();
        resume();
    }


    public void setDifficulty(int d) {
        difficulty = d;
    }

    public void addTanks() {
        for (Tank t : enemy) {
            t.remove();
            bodies.removeValue(t, true);
        }
        for (Tank t : tank) {
            t.remove();
            bodies.removeValue(t, true);
        }
        if (enemy.size > 0)
            enemy.removeRange(0, enemy.size - 1);
        if (tank.size > 0)
            tank.removeRange(0, tank.size - 1);
        for (int a = 0; a < difficulty; a++) {
            enemy.add(new AITank(groundLength / 2 + groundLength / 16 + 30 * a, this, tank, "Grey"));
        }
        tank.add(new Tank(groundLength / 2 - groundLength / 16, this, "Desert", 1, 1, 1));
        float x = tank.get(0).getX();
        float y = tank.get(0).getY();
        renderDiff.set(x, y);
        for (Tank t : enemy) {
            t.turn = false;
            world.addActor(t);
            bodies.add(t);
            t.setHealth(t.maxHealth = 300 - difficulty * 8);
            t.fuel = t.maxFuel = 200 - difficulty * 5;
        }
        for (Tank t : tank) {
            t.target = enemy;
            world.addActor(t);
            bodies.add(t);
            t.setHealth(t.maxHealth = 300 + difficulty * 20);
            t.fuel = t.maxFuel = 200 + difficulty * 10;
            if (difficulty > 2)
                t.shouldExplMove = true;
        }
    }

    public void render(float delta) {
        if (!paused) {
            if (!gameOver) {
                if (!bullets.empty()) {
                    if (roundState == RoundState.SELF) {
                        boolean allDone = true;
                        for (Tank t : tank)
                            if (t.turn) allDone = false;
                        if (allDone) {
                            roundState = RoundState.BULLETS_OF_SELF;
                            System.out.println("Waiting for SELF's bullets to finish.");
                        }
                    } else if (roundState == RoundState.ENEMY) {
                        boolean allDone = true;
                        for (Tank t : enemy)
                            if (t.turn) allDone = false;
                        if (allDone) {
                            roundState = RoundState.BULLETS_OF_ENEMY;
                            System.out.println("Waiting for ENEMY's bullets to finish.");
                        }
                    }
                }
                for (Body b : bodies)
                    if (b instanceof Crate) {
                        for (Tank t : tank)
                            if (isOverlap(t.collisionBox, b.collisionBox)) {
                                ((Crate) b).received(t);
                                break;
                            }
                        for (Tank t : enemy)
                            if (isOverlap(t.collisionBox, b.collisionBox)) {
                                ((Crate) b).received(t);
                                break;
                            }
                    }
            }
            if (update) {
                update = false;
                smoothie();
                setFloor(y);
            }
        }
        checkStl();
        if (reFocus) {
            offset.scl(0.9f);
            if (offset.len() < 0.1) {
                reFocus = false;
            }
        }
        if (roundState == RoundState.SELF && tank.size > 0) {
            float x = (renderDiff.x * 7 - tank.get(0).getX()) / 8f;
            float y = (renderDiff.y * 7 - tank.get(0).getY()) / 8f;
            renderDiff.set(x, y);
        } else if (roundState == RoundState.ENEMY && enemy.size > 0) {
            Vector2 center = Bullets.getAveragePosition(enemy);
            if ((int) center.len() != 0) {
                float x = (renderDiff.x * 7 - center.x) / 8f;
                float y = (renderDiff.y * 7 - center.y) / 8f;
                renderDiff.set(x, y);
            }
        } else if (roundState == RoundState.BULLETS_OF_ENEMY || roundState == RoundState.BULLETS_OF_SELF) {
            Vector2 center = bullets.getAveragePosition();
            if ((int) center.len() != 0) {
                float x = (renderDiff.x * 7 - center.x) / 8f;
                float y = (renderDiff.y * 7 - center.y) / 8f;
                renderDiff.set(x, y);
            }
            if (bullets.empty())
                roundState = roundState == RoundState.BULLETS_OF_SELF ? RoundState.ENEMY : RoundState.SELF;
        }
        if (tank.size > 0) {
            if (!(tank.get(0) instanceof AITank)) {
                tank.get(0).setLeft(movementStick.getKnobPercentX() < -0.1);
                tank.get(0).setRight(movementStick.getKnobPercentX() > 0.1);
                tank.get(0).setSpeed(Math.abs(movementStick.getKnobPercentX()));
                if (Math.abs(shootStick.getKnobPercentX()) > 0 || Math.abs(shootStick.getKnobPercentY()) > 0)
                    tank.get(0).setPower(shootStick.getKnobPercentX(), shootStick.getKnobPercentY());
            }
        }
        world.setPosition(renderDiff.x * scale + 320 + offset.x * scale, renderDiff.y * scale + 240 + offset.y * scale);
        if (paused)
            ui.act(delta);
        else
            stage.act(delta);
        stage.getBatch().begin();
        stage.getBatch().setColor(1, 1, 1, 1);
        stage.getBatch().draw(sky, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        stage.getBatch().end();
        stage.draw();
        if (!paused) {
            if (!gameOver) {
                for (Tank t : enemy)
                    if (t.getHealth() <= 0) {
                        enemy.removeValue(t, true);
                        t.remove();
                        bodies.removeValue(t, true);
                    }
                for (Tank t : tank)
                    if (t.getHealth() <= 0) {
                        tank.removeValue(t, true);
                        t.remove();
                        bodies.removeValue(t, true);
                    }
            }
            if (!gameOver) {
                if (enemy.size == 0) {
                    won = true;
                    gameOverLabel.setText("You Won");
                    System.out.println("Player Won.");
                    gameOver();
                }
                if (tank.size == 0) {
                    won = false;
                    gameOverLabel.setText("You Lost");
                    System.out.println("AI Won.");
                    gameOver();
                }
            }
            if (!gameOver && (roundState == RoundState.BULLETS_OF_ENEMY || roundState == RoundState.BULLETS_OF_SELF) && bullets.empty()) {
                if (roundState == RoundState.BULLETS_OF_ENEMY) {
                    for (Tank t : tank)
                        if (t.skipTurn <= 0)
                            t.kickStart();
                        else
                            t.skipTurn--;
                    roundState = RoundState.SELF;
                    System.out.println("Player's turn to fire.");
                    reFocus = true;
                } else if (roundState == RoundState.BULLETS_OF_SELF) {
                    for (Tank t : enemy)
                        if (t.skipTurn <= 0)
                            t.kickStart();
                        else
                            t.skipTurn--;
                    roundState = RoundState.ENEMY;
                    System.out.println("Enemy's turn to fire.");
                    reFocus = true;
                }
                if (Math.random() > 0.8) {
                    BulletInfo[] bullets = {new Nuke()};
                    WeaponCrate c = new WeaponCrate((float) (groundLength / 2 + 200 - 400 * Math.random()), 100000, this, bullets[(int) (bullets.length * Math.random())]);
                    bodies.add(c);
                    world.addActor(c);
                    System.out.println("Added Weapon Crate.");
                }
                if (Math.random() > 0.5) {
                    FuelCrate c = new FuelCrate((float) (groundLength / 2 + 200 - 400 * Math.random()), 100000, this);
                    bodies.add(c);
                    world.addActor(c);
                    System.out.println("Added Fuel Crate.");
                }
                if (Math.random() > 0.7) {
                    HealCrate c = new HealCrate((float) (groundLength / 2 + 200 - 400 * Math.random()), 100000, this);
                    bodies.add(c);
                    world.addActor(c);
                    System.out.println("Added Health Crate.");
                }
            }
        }
    }

    boolean reFocus = false;

    private void checkStl() {
        if (checkSelfStl()) {
            roundState = RoundState.BULLETS_OF_SELF;
            System.out.println("SELF staling.");
        } else if (checkEnemyStl()) {
            roundState = RoundState.BULLETS_OF_ENEMY;
            System.out.println("ENEMY staling.");
        }
    }

    private boolean checkSelfStl() {
        if (roundState == RoundState.SELF) {
            for (Tank t : tank)
                if (t.turn)
                    return false;
            return true;
        } else return false;
    }

    private boolean checkEnemyStl() {
        if (roundState == RoundState.ENEMY) {
            for (Tank t : enemy)
                if (t.turn)
                    return false;
            return true;
        } else return false;
    }

    public static boolean isOverlap(Polygon A, Polygon B) {
        return A.getBoundingRectangle().overlaps(B.getBoundingRectangle());
    }

    public void gameOver() {
        gameOver = true;
        ui.setVisible(false);
        gameOverScreen.setVisible(true);
        Audio.drive.stop();
        Audio.background.stop();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        debug.setProjectionMatrix(cam.combined);
        fireButton.setPosition(viewport.getWorldWidth() - 356, 0);
        shootStick.setPosition(viewport.getWorldWidth() - 256, 0);
        shellSelector.setPosition(viewport.getWorldWidth() - 356, 60);
        hb.setPosition(0, viewport.getWorldHeight() - 40);
        gameOverScreen.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        overlay.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        gameOverLabel.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        goBackButton.setPosition(viewport.getWorldWidth() / 2 - 100, viewport.getWorldHeight() / 2 - 100);
        pauseMenu.setBounds(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        pauseOverlay.setBounds(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        pauseButton.setPosition(viewport.getWorldWidth() - pauseButton.getWidth(), viewport.getWorldHeight() - pauseButton.getHeight());
    }

    public void pause() {
        pauseMenu.setVisible(true);
        pauseButton.setText("Resume");
        pauseButton.toFront();
        paused = true;
    }

    public void resume() {
        pauseMenu.setVisible(false);
        pauseButton.setText("Pause");
        paused = false;
    }

    public void hide() {

    }

    public void dispose() {
        stage.dispose();
    }

    private float sScale = 5;

    public void makeFloor() {
        PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator(seed);
        for (int a = 0; a < groundLength; a++) {
            y[a] = perlinNoiseGenerator.noise1(a * 0.005f) * 200 + 500;
        }
        float[] spriteVer = new float[groundLength * 2 + 4];
        spriteVer[0] = 0;
        spriteVer[1] = -100000;
        for (int x = 0; x < groundLength; x++) {
            spriteVer[x * 2 + 2] = x * sScale;
            spriteVer[x * 2 + 3] = y[x] * sScale;
        }
        spriteVer[groundLength * 2 + 2] = groundLength * sScale;
        spriteVer[groundLength * 2 + 3] = -100000;
        dirt.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        PolygonRegion region = new PolygonRegion(new TextureRegion(dirt), spriteVer, new EarClippingTriangulator().computeTriangles(spriteVer).items);
        float b = 204.81f;
        if (ground != null)
            ground.remove();
        ground = new PolygonActor(region, b, b / 2f);
        ground.setPosition(0, 0);
        world.addActor(ground);
    }

    public void setFloor(float[] y) {
        int groundLength = 1000;
        float[] spriteVer = new float[groundLength * 2 + 4];
        spriteVer[0] = 0;
        spriteVer[1] = -100000;
        for (int x = 0; x < groundLength; x++) {
            spriteVer[x * 2 + 2] = x * sScale;
            spriteVer[x * 2 + 3] = y[x] * sScale;
        }
        spriteVer[groundLength * 2 + 2] = groundLength * sScale;
        spriteVer[groundLength * 2 + 3] = -100000;
        dirt.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        PolygonRegion region = new PolygonRegion(new TextureRegion(dirt), spriteVer, new EarClippingTriangulator().computeTriangles(spriteVer).items);
        ground.setRegion(region);
    }

    public void makeHole(float x, float y, float radius) {
        if (Double.compare(radius, 0) == 0)
            return;
        float leftX = x - Math.abs(radius);
        float rightX = x + Math.abs(radius);
        for (int a = (int) Math.max(leftX, 0); a < groundLength && a < rightX; a++) {
            double targetY = y + Math.sin(Math.acos((a - x) / radius) + Math.PI) * radius;
            if ((this.y[a] > targetY && radius > 0) || (this.y[a] < targetY && radius < 0)) {
                this.y[a] = (float) targetY;
                this.y[a] = Math.max(this.y[a], -10000);
            }
        }
        update = true;
    }

    public void smoothie() {
        boolean again = false;
        for (int a = 0; a < groundLength; a++) {
            float average = (getY(a + 1) + getY(a - 1)) / 2;
            float diff = average - getY(a);
            if (Math.abs(diff) > 1) {
                y[a] = diff > 0 ? average + 0.5f : average - 0.5f;
                again = true;
            }
        }
        if (again)
            smoothie();
    }

    public float getY(float x) {
        x = Math.max(Math.min(groundLength - 1, x), 0);
        float leftX = y[(int) x];
        float rightX = y[(int) Math.ceil(x)];
        float diff = rightX - leftX;
        return leftX + diff * (x - (int) x);
    }

    public int getX(float x) {
        x = Math.max(Math.min(groundLength - 1, x), 0);
        return (int) x;
    }

    public float getCurve(float x) {
        float leftX = y[(int) x];
        float rightX = y[(int) (x + 1)];
        return (float) Math.atan2(rightX - leftX, (int) (x + 1) - (int) x);
    }

    private float initialScale;

    public boolean touchDown(float x, float y, int pointer, int button) {
        initialScale = scale;
        return false;
    }

    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    public boolean longPress(float x, float y) {
        return false;
    }

    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    public boolean pan(float x, float y, float deltaX, float deltaY) {
        offset.add(deltaX / 2 / scale, -deltaY / 2 / scale);
        return true;
    }

    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    public boolean zoom(float initialDistance, float distance) {
        float pointerScale = distance / initialDistance;
        float targetScale = initialScale * pointerScale;
        scale = targetScale;
        world.setScale(scale);
        return false;
    }

    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2
            pointer1, Vector2 pointer2) {
        return false;
    }

    public void pinchStop() {

    }

    public boolean hit(Bullet bullet) {
        if (bullet.y < getY(bullet.x))
            return true;
        for (Body b : bodies) {
            if (b.hit(bullet.x, bullet.y))
                return true;
        }
        return false;
    }

    public void explode(float radius, float push, float x, float y, float damage) {
        Circle circle = new Circle(x, y, radius);
        for (Body b : bodies) {
            if (b instanceof Tank) {
                if (overlaps(b.collisionBox, circle)) {
                    float angle = (float) Math.atan2(b.getY() + Math.sin(b.angle + Math.PI / 2) * 12 - y, b.getX() + Math.cos(b.angle + Math.PI / 2) * 12 - x);
                    ((Tank) b).getVelocity().add((float) (push * Math.cos(angle)), (float) (push * Math.sin(angle)));
                    ((Tank) b).setOffGround(true);
                    ((Tank) b).setHealth((int) Math.max(((Tank) b).getHealth() - damage, 0));
                    System.out.println("Push Event: push-∠=" + angle + ", tank-∠=" + b.angle + ", force=" + push);
                }
            }
            if (b instanceof Crate) {
                if (overlaps(b.collisionBox, circle)) {
                    b.remove();
                    bodies.removeValue(b, true);
                    if (b instanceof FuelCrate) {
                        ((FuelCrate) b).exploding = true;
                    }
                }
            }
        }
    }

    public boolean overlaps(Polygon polygon, Circle circle) {
        float[] vertices = polygon.getTransformedVertices();
        Vector2 center = new Vector2(circle.x, circle.y);
        float squareRadius = circle.radius * circle.radius;
        for (int i = 0; i < vertices.length; i += 2) {
            if (i == 0) {
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
                    return true;
            } else {
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[i - 2], vertices[i - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
                    return true;
            }
        }
        return false;
    }
}
