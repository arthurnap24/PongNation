package entity;

import android.graphics.RectF;

public class Brick extends Entity
{
  private boolean isDestroyed;
  private int width;
  private int height;
  private RectF boundsRect;

  /**
   * Set the left and top corner property of this Entity implementation to locate its
   * position in the GameView. As of right now, this constructor is not used. The current
   * constructor used is Brick(RectF boundsRect).
   *
   * @param x - left coordinate of the brick
   * @param y - top coordinate of the brick
   */
  public Brick(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  /**
   * Fit the brick sprite image into the rectangle passed into this contstructor.
   * @param boundsRect - the RectF object that describes the bounds of this Entity implementation
   */
  public Brick(RectF boundsRect)
  {
    this.boundsRect = boundsRect;
    this.x = (int) boundsRect.left; //NOTE the cast here...
    this.y = (int) boundsRect.top;
  }

  /**
   * Used to denote that the Brick is destroyed and should disappear in the game
   * screen.
   */
  public void destroy() { isDestroyed = true; }

  /**
   * Revives the brick whenever possible.
   */
  public void unDestroy() { isDestroyed = false; }

  /**
   * Returns the destroyed field of this Brick object.
   * @return - destroyed field of this Brick object
   */
  public boolean getDestroyed() { return isDestroyed; }

  /**
   * Returns the bounding rectangle that defines the Brick's surface area.
   * @return - bounding rectangle of the Brick
   */
  public RectF getBoundsRect() { return boundsRect; }
}
