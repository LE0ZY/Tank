package tk.zhyu.tankfield;

import com.badlogic.gdx.math.Vector2;

public class ProjectileEquation {
    public Vector2 startVelocity;
    public Vector2 startPoint;
    private BulletInfo info;
    private int variation;

    public ProjectileEquation(Vector2 startPoint, Vector2 startVelocity, BulletInfo info, int variation) {
        this.startVelocity = startVelocity;
        this.startPoint = startPoint;
        this.info = info;
        this.variation = variation;
    }

    public float getX(float t) {
        return startVelocity.x * t + startPoint.x;
    }

    public float getY(float t) {
        return startVelocity.y * t + startPoint.y + 0.5f * info.gravity[Math.min(variation, info.gravity.length)] * t * t;
    }

    public Vector2 getVelocity(float t) {
        return new Vector2(startVelocity.x, startVelocity.y + info.gravity[Math.min(variation, info.gravity.length)] * t);
    }

    public ProjectileEquation clone() {
        return new ProjectileEquation(new Vector2(startPoint), new Vector2(startVelocity), info, variation);
    }
}