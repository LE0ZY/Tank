package tk.zhyu.tankfield;

public abstract class BulletInfo {
    public float maxPower;
    public float gravity[];
    public float push[];
    public float damage[];
    public float holeSize[];
    public int icon_id;
    public int bullet_icon;

    public abstract void highPoint(Bullet b, TankScreen world);

    public abstract void hit(Bullet b, TankScreen world);

    public abstract void update(Bullet b, TankScreen world, float eTime);
}
