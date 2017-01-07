import java.awt.*;


public abstract class GameActor {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean on_screen;
    protected GameUtils.UserDataType identity;
    //this is just the image, the region of the actors is defined by the rectangle
    protected Image image;
    protected Dimension dim;
    protected boolean cheat;

    //constructor
    public GameActor(int x, int y) {
        this.x = x;
        this.y = y;
        on_screen = true;
        cheat = false;
        dim = new Dimension(GameUtils.GAME_WIDTH,GameUtils.GAME_HEIGHT);
    }

    //update the next stage of the actor
    abstract void update(Dimension d);
    abstract void onContact(GameActor b);

    //getter and setter
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GameUtils.UserDataType getIdentity() {
        return this.identity;
    }

    public Image getImage() { return image; }

    public void setCheat() {
        this.cheat = true;
    }
    public boolean isOnScreen() {
        return on_screen;
    }
    public Rectangle getRec() {
        return new Rectangle(x,y,width,height);
    }
    public void setCoor(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
