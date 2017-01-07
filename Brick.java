import javax.swing.ImageIcon;
import java.awt.*;

public class Brick extends GameActor {

    private boolean destroyed;    //true if the block is destroyed by the ball
    private int durability;
    private boolean hit;
    //constructor
    public Brick(int x, int y,int duability) {
        super(x,y);   //default for the brick location is -1,-1 (to avoid conflict)

        this.identity = GameUtils.UserDataType.BRICK;
        this.width = GameUtils.BRICK_WIDTH;
        this.height = GameUtils.BRICK_HEIGHT;
        this.durability = duability;
        hit = false;
        destroyed = false;        //just created, only the force can destroy this
        ImageIcon i_icon = new ImageIcon(getClass().getResource(imageSetter(y)));
        image = i_icon.getImage();
        image.getScaledInstance(width,height,image.SCALE_DEFAULT);
    }

    //this update method is called when the ball hit the brick
    //can only set the destroyed state to true, you cant resurrect from the death
    @Override
    public void update(Dimension d) {
        //stud has nothing
    }

    @Override
    public void onContact(GameActor b) {
        if (durability == 1) {
            //if the paddle is 1 hit destroy that
            durability = 0;
            on_screen = false;
            destroyed = true;
        }else {
            //if not decrease the durability
            durability -= 1;
            hit = true;
            changeImage();
        }
    }
    //get the image of the brick according to the row its located at.
    private String imageSetter(int y) {
        String temp_return = "";
        if (y <= GameUtils.BRICK_WALL_SPACE_Y) {
            //row 1
            if (!hit) {
                temp_return = "/Sprite/green_brick.png";
            }else {
                temp_return = "/Sprite/green_brick_cracked.png";
            }
        }else if(y <= GameUtils.BRICK_WALL_SPACE_Y + height + GameUtils.BRICK_SPACING) {
            //row 2
            if (!hit) {
                temp_return = "/Sprite/purple_brick.png";
            }else {
                temp_return = "/Sprite/purple_brick_cracked.png";
            }
        }else if(y <= GameUtils.BRICK_WALL_SPACE_Y + (height + GameUtils.BRICK_SPACING)*2) {
            //row 3
            if (!hit) {
                temp_return = "/Sprite/yellow_brick.png";
            }else {
                temp_return = "/Sprite/yellow_brick_cracked.png";
            }
        }else if(y <= GameUtils.BRICK_WALL_SPACE_Y + (height + GameUtils.BRICK_SPACING)*3) {
            //row 4
            if (!hit) {
                temp_return = "/Sprite/blue_brick.png";
            }else {
                temp_return = "/Sprite/blue_brick_cracked.png";
            }
        }else {
            //row 5
            if (!hit) {
                temp_return = "/Sprite/pink_brick.png";
            }else {
                temp_return = "/Sprite/pink_brick_cracked.png";
            }
        }
        return temp_return;
    }

    //change the image when the brick is hit
    private void changeImage() {
        ImageIcon i_icon_new = new ImageIcon(getClass().getResource(imageSetter(y)));
        image = i_icon_new.getImage();
        image.getScaledInstance(width,height,image.SCALE_DEFAULT);
    }
    //getter
    public boolean isDestroyed() {
        return destroyed;
    }

    public int getDuability() {
        return durability;
    }
}
