import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Ball extends GameActor {
    private int dir_x;  //the direction for the balls: positive move right up,
    private int dir_y;  //negative move left down, also control the speed of the ball
    private Dimension dim;
    //constructor
    public Ball() {
        super(GameUtils.BALL_DEFAULT_X,GameUtils.BALL_DEFAULT_Y);

        this.identity = GameUtils.UserDataType.BALL;
        this.width = GameUtils.BALL_DIM;
        this.height = GameUtils.BALL_DIM;
        dir_y = GameUtils.BALL_FALL_Y;
        Random rand = new Random();

        //just a random ball direction left or right when initialized
        int seed = rand.nextInt(100) + 1;
        if (seed < 50) dir_x = -1;
        else dir_x = 1;
        ImageIcon i_icon = new ImageIcon(getClass().getResource("/Sprite/ball.png"));
        image = i_icon.getImage();
        image.getScaledInstance(width,height,image.SCALE_DEFAULT);
    }

    //other constructor for the power up
    public Ball(int x, int y,int dir_x,int dir_y) {
        super(x,y);

        this.identity = GameUtils.UserDataType.BALL;
        this.width = GameUtils.BALL_DIM;
        this.height = GameUtils.BALL_DIM;
        this.dir_x =dir_x;
        this.dir_y = dir_y;

        ImageIcon i_icon = new ImageIcon(getClass().getResource("/Sprite/ball.png"));
        image = i_icon.getImage();
        image.getScaledInstance(width,height,image.SCALE_DEFAULT);
    }

    @Override
    public void update(Dimension d) {
        dim = d;
        //guard so that ball cant fall horizontally or vertically
        if (on_screen && (dir_x == 0 || dir_y == 0)) {
            dir_x = 1;
            dir_y = 1;
        }
        if (x <= 0 || x >= GameUtils.GAME_WIDTH - width - 1) { //ball hit walls
            setDirCoor(-dir_x,dir_y);
        }else if((y >= GameUtils.GAME_HEIGHT - height - 1 && cheat )|| y<=0 ) {    //ball hit the floor
            setDirCoor(dir_x,-dir_y);
        }else if(y >= GameUtils.GAME_HEIGHT - height - 1 && !cheat) {
            on_screen = false;
            dir_y = 1;  //drop down to oblivion
        }
        //update the ball location

        x += dir_x;
        y += dir_y;
    }

    @Override
    public void onContact(GameActor b) {

        //Minkowski sum
        double w = 0.5 * (width + b.getWidth());
        double h = 0.5 * (height + b.getHeight());
        double dx = b.getRec().getCenterX() - getRec().getCenterX();
        double dy = b.getRec().getCenterY() - getRec().getCenterY();
        double wy = w * dy;
        double hx = h * dx;
        if (wy > hx) {
            if (wy > -hx) {
                //System.out.println("hit top");
                setDirCoor(dir_x,-dir_y);
            }else {
                // System.out.println("Hit right");
                setDirCoor(-dir_x,dir_y);
                //safe guard when the paddle pushes the ball to the edge of the screen
                int temp =  b.getX() + b.getWidth() + width;
                if (temp >= GameUtils.GAME_WIDTH - width - 1) {
                    y = b.getY()+ b.getHeight();
                    setDirCoor(-dir_x,dir_y); //revert to original direction
                }else {
                    x = temp - width +1 ;
                }
            }
        }else {
            if (wy > -hx) {
               // System.out.println("Hit left");
                setDirCoor(-dir_x, dir_y);
                //safe guard when the paddle pushes the ball to the edge of the screen
                int temp = b.getX() - width;
                if (temp < 0) {
                    y = b.getY()+ b.getHeight();
                    setDirCoor(-dir_x,dir_y); //revert to original direction
                }else {
                    x = temp - 1;
                }
            } else {
               // System.out.println("hit bottom");
                setDirCoor(dir_x, -dir_y);
            }
        }

        this.update(dim);
    }

    //getter and setter
    public void setDirCoor(int x,int y) {
        if (x  == 0) {
            dir_x = -dir_x;
        }
        if (y == 0) {
            dir_y = -dir_y;
        }
        dir_x = x;
        dir_y = y;
    }


    public int getDirX() {
        return dir_x;
    }

    public int getDirY() {
        return dir_y;
    }

    public Ellipse2D getCircle() {
        return new Ellipse2D.Float((float) x,(float) y, (float) width, (float) height);
    }

}
