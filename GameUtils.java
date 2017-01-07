

//THIS CLASS CONTAINS:
//          global constants used
//          enum for the Actors
//          helpers for GameStage

public class GameUtils {
    public static final int GAME_WIDTH = 600;
    public static final int GAME_HEIGHT = 800;
    public static final int DELAY = 0; //ms

    //paddle global variables
    public static final int PADDLE_WIDTH = 140;
    public static final int PADDLE_HEIGHT = 20;
    public static final int PADDLE_DEFAULT_X = GAME_WIDTH/2 - PADDLE_WIDTH/2;
    public static final int PADDLE_DEFAULT_Y = GAME_HEIGHT - 80;

    //ball global variables
    public static final int BALL_DIM = 20;   //for easier implementation, we set the ball as a square( the irony)
    public static final int BALL_DEFAULT_X = PADDLE_DEFAULT_X + PADDLE_WIDTH/2 - BALL_DIM/2;
    public static final int BALL_DEFAULT_Y = PADDLE_DEFAULT_Y - GAME_HEIGHT/12;
    public static final int BALL_FALL_X = 1;    //direction of the ball falling
    public static final int BALL_FALL_Y = -1;   //default is falling down





    //brick global variables
    public static final int BRICK_SPACING = 3;
    public static final int BRICK_WIDTH = 90;
    public static final int BRICK_HEIGHT = 25;
    public static final int NUM_MAX_BRICK = 25;      //25 bricks in the game no more, no less
    public static final int BRICK_WALL_SPACE_X = (GAME_WIDTH - (BRICK_WIDTH + BRICK_SPACING -1)*NUM_MAX_BRICK/5 ) /2;
    public static final int BRICK_WALL_SPACE_Y = 120;


    //powerup global variables
    public static final int POWER_DIM = 25;
    //user data type for the actors in the game
    public enum UserDataType {
        BALL,
        PADDLE,
        BRICK,
        POWERUP
    }

    //enum for power up effect
    public enum Power {
        TRIPLE,
        LONG,
    }

    //enum for game stage
    public enum Stage {
        RUNNING,
        PAUSED,
        END,
        SPLASHSCREEN,
        OPENSCREEN
    }


    //Helper for drawing compoments

}
