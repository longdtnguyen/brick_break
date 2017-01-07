//----------------------------------------------------------//
//------------------------Breakout game---------------------//
//----------------------CS349 Winter 2016-------------------//
//------------------------Assignment 1----------------------//
//----------------------------------------------------------//

Created by:

||--------------------------------------------------------||
||                    Long Nguyen                         ||
||                     l34nguye                           ||
||                     20455912                           ||
||--------------------------------------------------------||


//----------------------IMPLEMENTATION-------------------------//
- The main file (Breakout.java) is a jframe that handle drawing the frame and the canvas

- The file GameStage.java is where everything happens:
	+ collision detection
	+ timers class which FPS and ball speed run separatedly
	+ drawing

- The class GameActor is the superclass of all the actors in the game
(ball, paddle, bricks and power up). Background is not counted as the game actor.

- the GameUtils class provide all the Enums and Constants for the game



//-----------------------GAME INFO-----------------------------//
run makefile to compile the game
- run "make run" to run the game with default FPS and ball speed
- run "java Breakout FPS ball_speed" where 
			+ FPS is an Int
			+ ball_speed is an Int from 1-10 where 1 is the slowest speed
			  (NOTE: do NOT put the ballspeed out of range otherwise the 
			  game will crash to OBLIVION)
- run "make clean to clear the project"
 e.g: java Breakout 60 5

- 4 is a recomnended ball_speed for a noob-friendly game



//----------------------GAME CONTROL----------------------------//
- To move the paddle: how down left button and drag horizontally 
                      to move left or right. Release the button to stop moving
- Press "P" during the game to pause the game
- Press "R" to resume the game when its paused.
         (NOTE: no guarantee behavour when pressed while the game is running)
- Press "C" to enter cheat mode where the MAIN ball cannot drop below the screen
  (WARING: if you press C you will be added 1 million score and marked as a cheater)



//-------------------ADDITIONAL FEATURES-------------------------//
- there will be power up spawn when  the ball breaks the brick
	+ Yellow star: spawn 2 additional balls with higher speed than the 
				   main ball
	+ Red star: The paddle becomes longer by a factor of 1.5 and will 
	            return back to normal after 5 second.
- Some of the bricks will require 2 hits to completely break.
	the pattern for the 2 hits bricks is like this:
	           ----2----
	           ----2----
	           222222222
	           ----2----
	           ----2----
- ADDED: visual upgrade for bricks,ball, paddle
- ADDED: background is animated (KINDA!- photoshop skill is too low sir)

all the Spites are from UnLuckY Studio at www.unluckystudio.com
(KINDA: needed alot of photoshop)


