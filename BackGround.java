import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

public class BackGround {
    private int width;
    private int height;
    private Image[] image;
    private int tracker;
    private int tracker_add;
    private Timer image_timer;
    public BackGround() {
        width = GameUtils.GAME_WIDTH;
        height = GameUtils.GAME_HEIGHT;
        image = new Image[17];
        tracker = 0;
        tracker_add = 1;
        for (int i=0;i< 17;++i) {
            ImageIcon i_icon = new ImageIcon(getClass().getResource("Sprite/bg/background" + i +".jpg"));
            image[i] = i_icon.getImage();
            image[i].getScaledInstance(width,height,image[i].SCALE_DEFAULT);
        }

        image_timer = new Timer();
        image_timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (tracker >= 16) tracker_add = -1;
                else if (tracker <= 0) tracker_add = 1;
                tracker += tracker_add;
            }
        }, GameUtils.DELAY,2500);
    }

    //getter
    public int getWidth(){
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getImage() {

        return image[tracker];
    }
}
