import javax.swing.*;
import java.awt.*;
import java.util.Random;
public class PowerUp extends GameActor {
    private boolean destroyed;
    private GameUtils.Power power;

    public PowerUp(int x, int y) {
        super(x,y);
        this.height = GameUtils.POWER_DIM;
        this.width = GameUtils.POWER_DIM;
        this.identity = GameUtils.UserDataType.POWERUP;
        this.power = randomEnum(GameUtils.Power.class);
        destroyed = false;
        if (power == GameUtils.Power.TRIPLE) {
            ImageIcon i_icon = new ImageIcon(getClass().getResource("/Sprite/star.png"));
            image = i_icon.getImage();
        }else if (power == GameUtils.Power.LONG) {
            ImageIcon i_icon = new ImageIcon(getClass().getResource("/Sprite/star_red.png"));
            image = i_icon.getImage();
        }

        image.getScaledInstance(width,height,image.SCALE_DEFAULT);

    }

    @Override
    public void update(Dimension d) {
        //power up only go down and go through unit
        if (y - height < d.getHeight()) {
            y += 1;
        }
    }

    @Override
    public void onContact(GameActor b) {
        destroyed = true;
        on_screen = false;
    }

    //getter and setter
    public boolean isDestroyed() {
        return destroyed;
    }

    public GameUtils.Power myPower() {
        return power;
    }


    //get a random enum
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}

