package entity;

import android.graphics.Color;

/**
 * Contains the parameters used by different actors of the game
 * such as Paddle, Brick, Ball, etc.
 */
public class EntityProperties
{
  public static int brickHeight = 50;
  public static int brickWidth = 100;
  public static int brickColor = Color.rgb(139,69,19);

  public static int paddleHeight = 100;
  public static int paddleLength = 300;
  public static int paddleColor = Color.rgb(255, 0, 0);

  public static int ballColor = Color.rgb(0,0,255);
  public static int ballRadius = 50;

}
