import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class GameStage extends JComponent {
    private GameUtils.Stage game_stage;

    private BackGround bg;
    private Ball ball,ball2,ball3;
    private Paddle paddle;
    private List<Brick> brick_arr;
    private List<GameActor> world_actor;
    private List<Ball> active_ball;
    private PowerUp power_up;

    private int power_timer = 5000,game_time = 0;
    private int score,num_brick;
    private boolean power_flag,empowered;
    private GameUtils.Power active_power;   //power_flag: power is on the screen
                                                                 //active_power: power is destroyed but the effect still

    private Timer fps_thread, ball_control, power_control;
    private int mouse_x,mouse_y;        //mouse listener
    private int fps,ball_speed;
    private double scale_width, scale_height;
    private Dimension dim;

    //Constructor, not initiate the game
    GameStage(int fps, int ball_speed) {
        this.fps = fps;
        this.ball_speed = ball_speed;
        this.addMouseListener(new MouseControlListener());
        this.addMouseMotionListener(new MouseControlListener());
        this.addKeyListener(new KeyControlListener());
        this.setFocusable(true);

        //resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                dim = e.getComponent().getBounds().getSize();
                scale_width = (double) dim.width/ (double)GameUtils.GAME_WIDTH;
                scale_height = (double) dim.height/(double) GameUtils.GAME_HEIGHT;

            }
        });

        bg = new BackGround();
        gameInit();

        //------------------------------TIME CONTROL----------------------------//
        //timer thread for the FPS
        fps_thread = new Timer();
        fps_thread.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                repaint();
            }
        }, GameUtils.DELAY,1000/this.fps);


        //timer thread for ball
        ball_control = new Timer();
        ball_control.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (game_stage == GameUtils.Stage.RUNNING) {
                    checkCollision();
                    updateBall();
                    if (active_ball.size() == 0) game_stage = GameUtils.Stage.END;
                }
            }
        },GameUtils.DELAY, 10/this.ball_speed);

        //time counter for the game
        //use this to control the power effect
        power_control = new Timer();
        power_control.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                game_time += 10;
                if (game_time == 5000) game_stage = GameUtils.Stage.SPLASHSCREEN;
                if (game_stage == GameUtils.Stage.RUNNING) {
                    if (power_flag && power_up != null) power_up.update(dim);
                    if (empowered == true) {
                        power_timer -= 10;
                        if (power_timer <= 0) deactivatePower();   //5 sec
                    }
                }
            }
        },GameUtils.DELAY,10);
        //----------------------------------------------------------------------//
    }

    //------------------------------------PAINT------------------------------------//
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;    //cast to get the 2d graphic method

        //draw background
        g2d.drawImage(bg.getImage(),1,1,bg.getWidth(),bg.getHeight(),this);
        if (game_stage != GameUtils.Stage.SPLASHSCREEN &&
            game_stage != GameUtils.Stage.OPENSCREEN) {
            //allow the image to be resize able
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.scale(scale_width, scale_height);

            for (int i = 0; i < world_actor.size(); ++i) {
                drawActor(g2d, world_actor.get(i));
            }
            drawText(g2d);
            if(game_stage == GameUtils.Stage.END){
                gameEnd(g2d);
            }
        }
        if (game_stage == GameUtils.Stage.OPENSCREEN) {
            openScreen(g2d);
        }
        if (game_stage == GameUtils.Stage.SPLASHSCREEN ||
            game_stage == GameUtils.Stage.PAUSED) {
            splashScreen(g2d);
            if (game_stage == GameUtils.Stage.SPLASHSCREEN && (game_time % 60)== 0) {
                g2d.setColor(new Color(112,0,74));
                g2d.setFont(new Font("Verdana", Font.PLAIN, 15));
                g2d.drawString("----Press ENTER to start----", 200, 600);
            }
        }
        Toolkit.getDefaultToolkit().sync();
    }
    //----------------------------------------------------------------------------//

    //---------------------MOUSE,KEYBOARD INPUT-----------------------------------------//
    private class MouseControlListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            if(game_stage == GameUtils.Stage.RUNNING) {
                mouse_x = e.getX();
                mouse_y = e.getY();
            }
        }

        public void mouseDragged(MouseEvent e) {

            int dx = e.getX() - mouse_x;
            int dy = e.getY() - mouse_y;
            if (game_stage == GameUtils.Stage.RUNNING) {
//          if (paddle.getRec().getBounds2D().contains(mouse_x,mouse_y)) {
                paddle.setCoor(paddle.getX() + dx, paddle.getY());
                checkCollision();
                repaint();
//          }
                mouse_x += dx;
                mouse_y += dy;
            }
        }
    }

    private class KeyControlListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int ke = e.getKeyCode();
            if(game_stage == GameUtils.Stage.SPLASHSCREEN) {
                if (ke == KeyEvent.VK_ENTER)
                    game_stage = GameUtils.Stage.RUNNING;
            }
            if (game_stage == GameUtils.Stage.END) {
                gameInit();
                if (ke == KeyEvent.VK_ENTER)
                    game_stage = GameUtils.Stage.RUNNING;
            }
            switch (ke) {
                case KeyEvent.VK_P: //press P to pause
                    game_stage = GameUtils.Stage.PAUSED;
                    break;
                case KeyEvent.VK_R: //press R to resume
                    game_stage = GameUtils.Stage.RUNNING;
                    break;
                case KeyEvent.VK_C: //press C to enter cheat mode
                    for(int i=0;i < world_actor.size();++i) {
                        world_actor.get(i).setCheat();
                    }
                    score += 1000000000;
                    break;
                default:
                    break;
            }

        }
    }
    //-----------------------------------------------------------------------//


    //---------------------------GAME HELPER-----------------------------------//
    private void checkCollision() {
        //loop through all the ball in the world( if more than 1 exist)
        boolean spawn_power = false;
        for (int l = 0; l < active_ball.size(); ++l) {
            boolean hit_flag = false;        //to avoid hit more than 1 object at the same time
            if (active_ball.get(l).isOnScreen()) {
                //check the ball and paddle
                if (active_ball.get(l).getRec().intersects(paddle.getRec()) ||
                        paddle.getRec().intersects(active_ball.get(l).getRec())) {
                    active_ball.get(l).onContact(paddle);
                    active_ball.get(l).update(dim);
                    hit_flag = true;
                    score -= 10;
                }

                //check the ball and the bricks
                if (!brick_arr.isEmpty()) {
                    for (int i = 0; i < brick_arr.size(); ++i) {
                        if (active_ball.get(l) != null) {
                            if (active_ball.get(l).getRec().intersects(brick_arr.get(i).getRec()) &&
                                    !hit_flag && brick_arr.get(i).isOnScreen()) {

                                if (!brick_arr.get(i).isDestroyed()) {
                                    hit_flag = true;
                                    active_ball.get(l).onContact(brick_arr.get(i));
                                    brick_arr.get(i).onContact(active_ball.get(l));
                                    score += 100;

                                    //if power one spawn the power with a probability
                                    if (!power_flag && !spawn_power) {
                                        Random rand = new Random();
                                        int seed = rand.nextInt(100) + 1;   //get the number from 1 to 100
                                        if (seed <= 100) {   //100% for now
                                            power_up = new PowerUp(brick_arr.get(i).getX() + brick_arr.get(i).getWidth() / 2,
                                                    brick_arr.get(i).getY() + brick_arr.get(i).getHeight() + 1);
                                            world_actor.add(power_up);
                                            power_flag = true;
                                            spawn_power = true;
                                        }
                                    }
                                    if (brick_arr.get(i).getDuability() == 0) {
                                        world_actor.remove(brick_arr.get(i));
                                        num_brick--;
                                        score += 50;
                                    }

                                }
                            }
                        }
                    }
                }

                //if the power hit the paddle, destroy it and activate the effect
                if (power_up != null) {
                    if (paddle.getRec().intersects(power_up.getRec())) {
                        power_up.onContact(paddle);
                        world_actor.remove(power_up);
                        activatePower();
                    }
                }

                //check if we win
                if (num_brick == 0) {
                    game_stage = GameUtils.Stage.END;
                }
            }else {
                active_ball.remove(l);
            }
        }
    }
    private void drawActor(Graphics2D g2d,GameActor actor) {
        if (actor.isOnScreen()) {
            //draw images
            g2d.drawImage(actor.getImage(), actor.getX(), actor.getY(),
                    actor.getWidth(), actor.getHeight(), this);

            //DEBUG Renderer    TODO:turn off when done
//            if (actor.getIdentity() != GameUtils.UserDataType.BALL) {
//                g2d.setColor(Color.GREEN);
//                g2d.drawRect(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
//                g2d.setColor(Color.RED);
//                g2d.drawRect(actor.getX(), actor.getY(), 1, 1);
//            } else {
//                g2d.setColor(Color.GREEN);
//                g2d.drawOval(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
//                g2d.setColor(Color.RED);
//                g2d.drawRect((int) actor.getRec().getCenterX(), (int) actor.getRec().getCenterY(), 1, 1);
//            }
        }
    }

    private void drawText(Graphics2D g2d) {
        Font font = new Font("Comic Sans MS", Font.PLAIN, 15);
        g2d.setFont(font);
        g2d.setColor(new Color(0,15,8));
        g2d.drawString("FPS: " + fps,20, 20);
        g2d.drawString("Score: " + score,20, 40);
        if (empowered) {
            g2d.drawString("Power: " + (power_timer/1000 + 1)+ "s",20, 60);
        }
    }
    private void gameEnd(Graphics2D g2d) {
        String msg = "Game Over";
        Font font = new Font("Verdana", Font.BOLD, 30);
        FontMetrics fontm = this.getFontMetrics(font);

        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        g2d.drawString(msg,
                (GameUtils.GAME_WIDTH - fontm.stringWidth(msg)) / 2,
                GameUtils.GAME_WIDTH / 2);
        msg = "Your Score: " + score;
        g2d.drawString(msg,
                (GameUtils.GAME_WIDTH - fontm.stringWidth(msg)) / 2,
                GameUtils.GAME_WIDTH/2 +50);
        msg = "press ENTER to restart";
        g2d.setFont(new Font("verdana", Font.PLAIN, 12));
        g2d.drawString(msg,
                (GameUtils.GAME_WIDTH - 150) / 2,
                GameUtils.GAME_WIDTH/2 + 140);

    }

    private void openScreen(Graphics2D g2d){
        String msg = "Long Nguyen";
        Font font = new Font("Comic Sans MS", Font.PLAIN, 23);
        FontMetrics fontm = this.getFontMetrics(font);
        g2d.setColor(new Color(33,27,34));  //dark grey
        g2d.setFont(font);
        int temp_y = GameUtils.GAME_WIDTH;
        g2d.drawString(msg,(GameUtils.GAME_WIDTH - fontm.stringWidth(msg)) / 2,temp_y / 2);

        msg = "20455912";
        temp_y += 60;
        g2d.drawString(msg,(GameUtils.GAME_WIDTH - fontm.stringWidth(msg)) / 2,temp_y / 2);

        msg = "cs349(W16) - a1";
        temp_y += 90;
        g2d.drawString(msg,(GameUtils.GAME_WIDTH - fontm.stringWidth(msg)) / 2,temp_y / 2);

    }

    private void splashScreen(Graphics2D g2d) {
        Font font = new Font("Verdana", Font.PLAIN, 20);
        g2d.setColor(new Color(33,27,34));
        g2d.setFont(font);
        g2d.drawString("Hold left Mouse to move the paddle",150,350);
        g2d.drawString("P : pause the game",150,400);
        g2d.drawString("R : resume the game",150,450);
        g2d.drawString("C : enter cheat mode",150,500);

    }
    private void activatePower() {
        power_flag = false;
        active_power =power_up.myPower();
        switch (active_power) {
            case TRIPLE:
                if (active_ball.size() == 1) {
                    int temp = -2;
                    if (!ball.isOnScreen()) {
                        ball = new Ball(active_ball.get(0).getX(),active_ball.get(0).getY(),
                                temp,active_ball.get(0).getDirY());
                        world_actor.add(ball);
                        active_ball.add(ball);
                        temp = 2;
                    }
                    if (ball2 == null || !ball2.isOnScreen()) {
                        ball2 = new Ball(active_ball.get(0).getX(), active_ball.get(0).getY(),
                                temp, active_ball.get(0).getDirY());
                        world_actor.add(ball2);
                        active_ball.add(ball2);
                        temp = -temp;
                    }
                    if (ball3 == null || !ball3.isOnScreen()) {
                        ball3 = new Ball(active_ball.get(0).getX(), active_ball.get(0).getY(),
                                temp, active_ball.get(0).getDirY());
                        world_actor.add(ball3);
                        active_ball.add(ball3);
                    }
                }
                empowered = false;
                break;
            case LONG:
                paddle.changeSize(600);
                empowered = true;
                break;
        }
    }

    private void deactivatePower() {
        power_timer = 5000;
        empowered = false;
        switch (active_power) {
            case TRIPLE:
                break;
            case LONG:
                paddle.changeSize(2606);
                break;
        }
    }

    private void gameInit() {
        score = 0;
        num_brick = GameUtils.NUM_MAX_BRICK;
        empowered = false;
        power_flag = false;
        game_stage = GameUtils.Stage.OPENSCREEN;
        brick_arr = new ArrayList<>(); //get an array of bricks not actually have the bricks
        active_ball = new ArrayList<>();
        world_actor = new ArrayList<>();
        setDoubleBuffered(true);
        //setting up the actors
        paddle = new Paddle();
        ball = new Ball();
        ball2 = null;
        ball3 = null;
        world_actor.add(paddle);
        world_actor.add(ball);
        active_ball.add(ball);
        power_up = null;
        int row_spacing = 0;
        int block_count = 0;
        for (int i = 0; i < 5; ++i) {                                            //row
            int col_spacing = 0;
            for (int j = 0; j < (GameUtils.NUM_MAX_BRICK/ 5); ++j) {             //col
                int temp_durability = 1;
                if (i == 2 || j == 2) {temp_durability = 2; }
                brick_arr.add(new Brick(j * GameUtils.BRICK_WIDTH + GameUtils.BRICK_WALL_SPACE_X + col_spacing,
                        i * GameUtils.BRICK_HEIGHT + GameUtils.BRICK_WALL_SPACE_Y + row_spacing,
                        temp_durability));
                world_actor.add(brick_arr.get(block_count));
                ++block_count;
                col_spacing += GameUtils.BRICK_SPACING;
            }
            row_spacing += GameUtils.BRICK_SPACING;
        }
    }

    private void updateBall() {
        for (int i=0;i < active_ball.size();++i) {
            active_ball.get(i).update(dim);
        }
    }
    //---------------------------------------------------------------------------------------//
}
