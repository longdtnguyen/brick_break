import javax.swing.*;
import java.awt.*;

//----------------------------------------------------------------------//

public class Breakout extends JFrame {
    static private int game_init_fps = 60; //Default FPS
    static private int ball_speed = 3;         //Default ball speed
    private GameStage  game_stage;
    //constructor
    Breakout(int FPS, int ball_speed) {

        super();
        game_stage = new GameStage(FPS,ball_speed);
        add(game_stage);

        setTitle("Breakout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setSize(GameUtils.GAME_WIDTH,GameUtils.GAME_HEIGHT);
        setLocationRelativeTo(null);                                //set the game screen in the center
        setResizable(true);
        setVisible(true);

    }

    //main method
    public static void main(String[] args) {
        //this is command line argument
        //will be tested later
        if (args.length > 0) {
            game_init_fps = Integer.valueOf(args[0]);
            ball_speed = Integer.valueOf(args[1]);
            if (ball_speed < 1)
                ball_speed = 1;
                System.out.println("I TOLD YOU NOT TO DO IT");
            if (ball_speed > 10)
                ball_speed = 10;
                System.out.println("I TOLD YOU NOT TO DO IT");
        }else {

        }
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run(){
                Breakout game = new Breakout(game_init_fps,ball_speed);
                game.setVisible(true);
            }
        });
    }
}
