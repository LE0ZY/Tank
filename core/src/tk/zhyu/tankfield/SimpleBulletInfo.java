package tk.zhyu.tankfield;

public class SimpleBulletInfo extends BulletInfo {
    public SimpleBulletInfo() {
        gravity = new float[]{-98.1f};
        maxPower = 160;
        push = new float[]{5};
        damage = new float[]{5};
        holeSize = new float[]{5};
    }
}
