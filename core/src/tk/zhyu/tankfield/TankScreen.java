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
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class TankScreen implements Screen, GestureDetector.GestureListener {
    public Tank next;
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
    private OrthographicCamera cam;
    private ExtendViewport viewport;

    ShapeRenderer debug;

    public int groundLength = 10000;
    public int segmentCount = 10;
    public float y[] = new float[groundLength];
    Tank tank;
    AITank enemy;
    Texture dirt;
    public float scale = 2;
    InputMultiplexer multiplexer;
    public Vector2 offset = new Vector2();
    public Vector2 renderDiff;
    TextureRegion bedrocks;

    public Array<Body> bodies = new Array<Body>();

    public PolygonActor[] grounds = new PolygonActor[segmentCount];

    public TankScreen(final TankField tankField) {
        this.tankField = tankField;
        AssetManager manager = ((TankField) Gdx.app.getApplicationListener()).manager;
        {
            manager.load("terrain.png", Texture.class);
            manager.load("sky.png", Texture.class);
            manager.load("traj.png", Texture.class);
            manager.load("bedrock.png", Texture.class);
            manager.finishLoading();
            dirt = manager.get("terrain.png", Texture.class);
            sky = manager.get("sky.png", Texture.class);
            Tank.traj = manager.get("traj.png", Texture.class);
            Texture bedrock = manager.get("bedrock.png", Texture.class);
            bedrock.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            bedrocks = new TextureRegion(bedrock);
            bedrocks.setRegion(0, 0, groundLength, 100);
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
        img.setPosition(0, -100);
        world.addActor(img);
        ui = new Group();
        makeFloor();
        tank = new Tank(20, this);
        renderDiff = new Vector2(20, tank.getY());
        enemy = new AITank(150, this, tank);
        enemy.turn = false;
        next = enemy;
        world.addActor(tank);
        world.addActor(enemy);
        bodies.add(tank);
        bodies.add(enemy);
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
                if (tank.turn) {
                    tank.shoot();
                    next = enemy;
                }
            }
        });
        ui.addActor(fireButton);
        shellSelector = new ShellSelector(640 - 356, 60, tank, ui);
        ui.addActor(shellSelector);

        stage.addActor(world);
        stage.addActor(ui);
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
        Audio.background.loop(0.4f);
    }


    public void render(float delta) {
        if (tank.turn) {
            float x = (renderDiff.x * 7 - tank.getX()) / 8f;
            float y = (renderDiff.y * 7 - tank.getY()) / 8f;
            renderDiff.set(x, y);
        } else if (enemy.turn) {
            float x = (renderDiff.x * 7 - enemy.getX()) / 8f;
            float y = (renderDiff.y * 7 - enemy.getY()) / 8f;
            renderDiff.set(x, y);
        } else if (!bullets.empty()) {
            Vector2 center = bullets.getAveragePosition();
            if ((int) center.len() != 0) {
                float x = (renderDiff.x * 7 - center.x) / 8f;
                float y = (renderDiff.y * 7 - center.y) / 8f;
                renderDiff.set(x, y);
            }
        }
        world.setPosition(renderDiff.x * scale + 320 + offset.x * scale, renderDiff.y * scale + 240 + offset.y * scale);
        tank.setLeft(movementStick.getKnobPercentX() < -0.2);
        tank.setRight(movementStick.getKnobPercentX() > 0.2);
        if (Math.abs(shootStick.getKnobPercentX()) > 0 || Math.abs(shootStick.getKnobPercentY()) > 0) {
            tank.setPower(shootStick.getKnobPercentX(), shootStick.getKnobPercentY());
        }
        stage.act(delta);
        stage.getBatch().begin();
        stage.getBatch().draw(sky, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        stage.getBatch().end();
        stage.draw();
        if (!tank.turn && !enemy.turn && bullets.empty()) {
            if (next.skipTurn > 0) {
                next.skipTurn--;
                if (next == tank)
                    next = enemy;
                else
                    next = tank;
            } else
                next.kickStart();
        }
//        debug.begin(ShapeRenderer.ShapeType.Line);
//        debug.setColor(0, 0, 0, 1);
//        for (Body b : bodies) {
//            debug.polygon(b.collisionBox.getTransformedVertices());
//        }
//        for (Bullet b : bullets.getBullets()) {
//            debug.circle(b.x, b.y, 2);
//        }
//        debug.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        debug.setProjectionMatrix(cam.combined);
        fireButton.setPosition(viewport.getWorldWidth() - 356, 0);
        shootStick.setPosition(viewport.getWorldWidth() - 256, 0);
        shellSelector.setPosition(viewport.getWorldWidth() - 356, 60);
    }

    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {
        stage.dispose();
    }

    private float sScale = 5;

    public void makeFloor() {
        PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator((int) (Math.random() * 1000));
        for (int n = 0; n < segmentCount; n++) {
            int groundLength = 1000;
            for (int a = 0; a < groundLength; a++) {
                y[a + groundLength * n] = perlinNoiseGenerator.noise1(a * 0.005f + n * groundLength * 0.005f) * 200 + 500;
            }
            float[] spriteVer = new float[groundLength * 2 + 4];
            spriteVer[0] = 0;
            spriteVer[1] = 0;
            for (int x = 0; x < groundLength; x++) {
                spriteVer[x * 2 + 2] = x * sScale;
                spriteVer[x * 2 + 3] = y[x + groundLength * n] * sScale;
            }
            spriteVer[groundLength * 2 + 2] = groundLength * sScale;
            spriteVer[groundLength * 2 + 3] = 0;
            dirt.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            PolygonRegion region = new PolygonRegion(new TextureRegion(dirt), spriteVer, new EarClippingTriangulator().computeTriangles(spriteVer).items);
            float b = 204.81f;
            grounds[n] = new PolygonActor(region, b, b / 2f);
            grounds[n].setPosition(groundLength * n, 0);
            world.addActor(grounds[n]);
        }
    }

    public void setFloor(float[] y, int n) {
        n = Math.max(Math.min(9, n), 0);
        int groundLength = 1000;
        float[] spriteVer = new float[groundLength * 2 + 4];
        spriteVer[0] = groundLength * n * sScale;
        spriteVer[1] = 0;
        for (int x = 0; x < groundLength; x++) {
            y[x + groundLength * n] = Math.max(y[x + groundLength * n], 0);
            spriteVer[x * 2 + 2] = x * sScale;
            spriteVer[x * 2 + 3] = y[x + groundLength * n] * sScale;
        }
        spriteVer[groundLength * 2 + 2] = groundLength * sScale;
        spriteVer[groundLength * 2 + 3] = 0;
        dirt.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        PolygonRegion region = new PolygonRegion(new TextureRegion(dirt), spriteVer, new EarClippingTriangulator().computeTriangles(spriteVer).items);
        grounds[n].setRegion(region);
    }

    public void makeHole(float x, float y, float radius) {
        float leftX = x - Math.abs(radius);
        float rightX = x + Math.abs(radius);
        for (int a = (int) Math.max(leftX, 0); a < groundLength && a < rightX; a++) {
            double targetY = y + Math.sin(Math.acos((a - x) / radius) + Math.PI) * radius;
            if ((this.y[a] > targetY && radius > 0) || (this.y[a] < targetY && radius < 0)) {
                this.y[a] = (float) targetY;
            }
        }
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

    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    public void pinchStop() {

    }

    public boolean hit(Bullet bullet) {
        if (bullet.y < getY(bullet.x))
            return true;
        for (Body b : bodies) {
            if (b.collisionBox.contains(bullet.x, bullet.y))
                return true;
        }
        return false;
    }

    public void explode(float radius, float push, float x, float y) {
        for (Body b : bodies) {
            if (b instanceof Tank) {
                float distance = (float) Math.hypot(b.getX() - x, b.getY() - y);
                if (distance < radius) {
                    float angle = (float) Math.atan2(b.getY() - y, b.getX() - x);
                    float fP = (float) (push * radius / Math.sqrt(Math.sqrt(distance)));
                    ((Tank) b).getVelocity().add((float) (fP * Math.cos(angle)), (float) (fP * Math.sin(angle) + fP / 3f));
                    ((Tank) b).setOffGround(true);
                }
            }
        }
    }
}
