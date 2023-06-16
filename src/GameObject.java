import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * GameObject class which represents a rectangle in the game
 * Stores rectangle position, image of object, original point of object
 * Extends Rectangle class
 */
public class GameObject extends Rectangle{
    protected Image image;

    public GameObject(double x, double y, double width, double height) {super(x, y, width, height);}
    public void render() {
        this.image.drawFromTopLeft(this.left(), this.top());
    }

}
