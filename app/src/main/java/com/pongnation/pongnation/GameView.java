package com.pongnation.pongnation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import entity.Ball;
import entity.Brick;
import entity.EntityProperties;
import entity.Paddle;
import gameproperty.GameProperties;

/**
 * Created by Arthur on 8/3/2017.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
  private MainThread thread;

  private Paint wallPaint;

  private Paddle p1;
  private Ball ball;

  private int paddleX;
  private int newX;

  private ArrayList<Brick> bricks = new ArrayList<>(); //contains the bricks

  private Paint ballPaint;
  private Paint paddlePaint;
  private Paint brickPaint;
  private Paint pointsPaint;

  public GameView(Context context)
  {
    super(context);
    getHolder().addCallback(this);

    thread = new MainThread(getHolder(), this);
    setFocusable(true);

    wallPaint = new Paint();
    wallPaint.setColor(Color.rgb(0,255,0));

    paddlePaint = new Paint();
    paddlePaint.setColor(EntityProperties.paddleColor);

    ballPaint = new Paint();
    ballPaint.setColor(EntityProperties.ballColor);

    brickPaint = new Paint();
    brickPaint.setColor(EntityProperties.brickColor);

    pointsPaint = new Paint();
    pointsPaint.setColor(Color.BLACK);
    pointsPaint.setTextSize(200);
    pointsPaint.setStyle(Paint.Style.FILL);

    initEntities();
    initSprites();
  }

  private void initEntities()
  {
    initBricks();
    p1= new Paddle(100);
    ball = new Ball(new RectF(300, 1300, 300+ EntityProperties.ballRadius, 1300+EntityProperties.ballRadius));

//    ball = new Ball(300, 1300, 50);
  }

  boolean fingerPlaced = false;
  @Override
  public boolean onTouchEvent(MotionEvent e)
  {
    //if it's the first time that the user pressed on the screen, make paddle follow finger
    newX = (int)e.getX();
    if (e.getAction() == MotionEvent.ACTION_DOWN)
    {
      fingerPlaced = true;
    }
    else if (e.getAction() == MotionEvent.ACTION_UP)
    {
      fingerPlaced = false;
    }
    return true;
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder)
  {
    thread.setRunning(true);
    thread.start();
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

  @Override
  public void surfaceDestroyed(SurfaceHolder holder)
  {
    boolean retry = true;
    while (retry)
    {
      try
      {
        thread.setRunning(false);
        thread.join();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      retry = false;
    }
  }

  private void initBricks()
  {
    int w = EntityProperties.brickWidth;
    int h = EntityProperties.brickHeight;
    int wallH = GameProperties.wallHeight;

    for (int i=0; i< GameProperties.brickRows; i++)
    {
      for (int j=0; j< GameProperties.brickCols; j++)
      {
        Brick b = new Brick(new RectF(j*w, i*h+wallH, (j*w)+w,(i*h)+h+wallH));
        bricks.add(b);
      }
    }
  }

  private int ballXOffset = 5;
  private int ballYOffset = 5;
  /**
   * maybe update the ball here and shit...
   */
  public void update()
  {
    movePaddle();
    //check for ball going out of bounds (in paddleX)...
    if (ball.getX() >= this.getWidth()-ball.getRadius())
    {
      ballXOffset = -ballXOffset;
    }
    else if (ball.getX() <= 0)
    {
      ballXOffset = -ballXOffset;
    }

    //check for ball going out of bounds 1(in y)...
    if (ball.getY() <= GameProperties.wallHeight-ball.getRadius())
    {
      ballYOffset = -ballYOffset;
    }
    else if (ball.getY() >= this.getHeight())
    {
      ballYOffset = -ballYOffset;
      ball.setTop(GameProperties.ballRespawnY);
    }
    //paddle just makes the ball movement negative...
    if (paddleHitsBall() || paddleHitsBrick())
    {
      ballYOffset = -ballYOffset;
//      ballXOffset = -ballXOffset;
    }

    ball.addXOffset(ballXOffset);
    ball.addYOffset(ballYOffset);
  }

  /**
   * Make the paddle object chase the finger location.
   */
  private void paddleChaseFinger()
  {
    int paddleVel = 20;
    if (Math.abs(p1.getX()-newX) >= 20)
    {
      if (newX < paddleX)
      {
        paddleX = p1.getX() - paddleVel;
        p1.setX(paddleX);
      }
      else if (newX > paddleX)
      {
        paddleX = p1.getX() + paddleVel;
        p1.setX(paddleX);
      }
    }
  }

  /**
   * Performs a call to paddleChaseFinger() whenever needed. The need for the paddle
   * to chase the finger is determined by the user applying pressure on the screen.
   */
  private void movePaddle()
  {
    if (fingerPlaced)
    {
      paddleChaseFinger();
    }
  }

  /**
   *
   * @return
   */
  private boolean paddleHitsBall()
  {
    int ballPaddleDist = (this.getHeight()-ball.getY())-GameProperties.paddleYMin-ball.getRadius()/2;
    if (ball.getX()+ball.getRadius() >= p1.getX() - EntityProperties.paddleLength/2 &&
        ball.getX()+ball.getRadius() <= p1.getX() + EntityProperties.paddleLength/2 &&
        ballPaddleDist <= 0)
    {
      return true;
    }
    return false;
  }

  //if ball hits any one of the bricks
  private boolean paddleHitsBrick()
  {
    boolean hasHit = false;
    for (Brick b : bricks)
    {
      int xDist = Math.abs(b.getX() - ball.getX());
      int yDist = Math.abs(b.getY() - ball.getY());

      if (xDist <= ball.getRadius() && yDist <= ball.getRadius())
      {
        b.destroy();
        points += 100;
        System.out.println("Collision occured");
        hasHit = true; //may collide to more than 1 brick at a time...
      }
    }
    //...so we wait till the end of the method to return true if something is hit...
    if (hasHit)
    {
      return true;
    }
    return false;
  }


  // Where all the sprites are created..
  private Bitmap paddleBitMap;
  private Bitmap brickBitMap;
  private Bitmap ballBitMap;
  private Bitmap bgBitMap;
  private void initSprites()
  {
    bgBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    paddleBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.test_paddle);
    brickBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.brick_res);
    ballBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ball_res);
  }


  private boolean firstTime = true;
  /**
   * Put graphics manipulation code here...
   * @param canvas
   */
  @Override
  public void draw(Canvas canvas)
  {
    super.draw(canvas);
    if (canvas != null)
    {
      //FUCKING BUGGY
//      if (firstTime)
//      {
        drawBackground(canvas);
        firstTime = false;
//
      drawPaddle(canvas);
      drawBricks(canvas);
      drawBall(canvas);
      drawPoints(canvas);
    }
  }

  private int points=0;
  /**
   * Draw the background walls.
   * @param canvas - Current canvas held by the View object.
   */
  private void drawPoints(Canvas canvas)
  {
    String pointsStr = "" + points;
    canvas.drawText(pointsStr, 0, 220, pointsPaint);
  }

  /**
   * Draw the destroyable bricks.
   * @param canvas - Current canvas held by the View object.
   */
  private void drawBricks(Canvas canvas)
  {
    for (Brick b : bricks)
    {
      if (!b.getDestroyed())
      {
        canvas.drawBitmap(brickBitMap, null, b.getBoundsRect(), null);
      }
      else
      {
        bricks.remove(bricks.indexOf(b));
      }
    }
  }

  //Can't draw from outside the GameView?
  private void drawPaddle(Canvas canvas)
  {
    int paddleYMin = this.getHeight()-GameProperties.paddleYMin;
    canvas.drawBitmap(paddleBitMap,p1.getX()-EntityProperties.paddleLength/2, paddleYMin , paddlePaint);
  }

  //BUGGY AS FUCK
  private void drawBall(Canvas canvas)
  {
    ballPaint.setColor(EntityProperties.ballColor);
    canvas.drawBitmap(ballBitMap, null, ball.getBoundsRect(), ballPaint);
  }

  private void drawBackground(Canvas canvas)
  {
    canvas.drawBitmap(bgBitMap, null, new Rect(0,0,this.getWidth(),this.getHeight()), new Paint());
  }
}