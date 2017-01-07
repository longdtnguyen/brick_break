import javax.swing.ImageIcon;
import java.awt.*;

public class Paddle extends GameActor {

    //constructor
    public Paddle() {
        super(GameUtils.PADDLE_DEFAULT_X,GameUtils.PADDLE_DEFAULT_Y);

        this.identity = GameUtils.UserDataType.PADDLE;
        this.width = GameUtils.PADDLE_WIDTH;
        this.height = GameUtils.PADDLE_HEIGHT;

        ImageIcon i_icon = new ImageIcon(getClass().getResource("/Sprite/paddle.png"));
        image = i_icon.getImage();
        image.getScaledInstance(width,height,image.SCALE_DEFAULT);
    }

    @Override
    public void update(Dimension d) {
        //paddle cant move on it own
    }

    @Override
    public void onContact(GameActor b) {
        //stud for now... have no effect on paddle
    }

    @Override
    public void setCoor(int x, int y) {
        if (x <= 0) {
            this.x = 0;
        }else if (x >= GameUtils.GAME_WIDTH - width - 1) {
            this.x = GameUtils.GAME_WIDTH - width  - 1 ;
        }else {
            this.x = x;
            this.y = y;
        }

    }

    public void changeSize(int factor) {
        if (factor == 600) {
            this.width = 210;
            image.getScaledInstance(width, height, image.SCALE_DEFAULT);
        }else {
            this.width = GameUtils.PADDLE_WIDTH;
            image.getScaledInstance(width, height, image.SCALE_DEFAULT);
        }
    }
}
