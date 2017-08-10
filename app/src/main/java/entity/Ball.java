package entity;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by Arthur on 8/4/2017.
 */

public class Ball extends Entity {
  private int radius;
  private RectF boundsRect;

  public Ball(int x, int y, int radius) {
    super(x,y);
    this.radius = radius;
  }

  public Ball(RectF boundsRect)
  {
    this.boundsRect = boundsRect;
    x = (int) boundsRect.left;
    y = (int) boundsRect.right;
    this.radius = EntityProperties.ballRadius;
//    this.radius = (int)(boundsRect.right - boundsRect.left)/2;
  }

  public void addXOffset(int xOffset)
  {
    boundsRect.left += xOffset;
    boundsRect.right += xOffset;
    x = (int)boundsRect.left;
  }
  public void addYOffset(int yOffset)
  {
    boundsRect.top += yOffset;
    boundsRect.bottom += yOffset;
    y = (int)boundsRect.top;
  }

  public int getRadius() { return radius; }

  //not used...
  public void setLeft(int l)
  {
    boundsRect.left = l;
    boundsRect.right = l + EntityProperties.ballRadius;
  }
  //this is used...
  public void setTop(int t)
  {
    boundsRect.top = t;
    boundsRect.bottom = t + EntityProperties.ballRadius;
  }

  @Override
  public int getX()
  {
    return (int)boundsRect.left;
  }

  @Override
  public int getY()
  {
    return (int)boundsRect.top;
  }

  public RectF getBoundsRect() { return boundsRect; }
}
