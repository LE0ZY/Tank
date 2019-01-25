package tk.zhyu.tankfield.bullets;

import tk.zhyu.tankfield.TankScreen;

public class Grenade extends Antenna {
    public Grenade(){
        icon_id = (10 * 9) + 5;
    }
    @Override
    public void action(Bullet b, TankScreen world) {
        world.explode(30, 300, b.x, b.y, 0);
    }
}
