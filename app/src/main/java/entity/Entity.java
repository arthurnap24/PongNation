package entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * This class describes the skeleton of what a game actor should be.
 */
public abstract class Entity
{
  protected int x,y;

  public Entity()
  {
    x = 0;
    y = 0;
  }

  public Entity(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public int getX() { return x; }
  public int getY() { return y; }

  public void setX(int x) { this.x = x; }
  public void setY(int y) { this.y = y; }
}
