import bagel.*;
import bagel.util.Point;
import java.util.ArrayList;

public class javaTetris extends AbstractGame  {
    private final static int WINDOW_WIDTH = 500;
    private final static int WINDOW_HEIGHT = 600;
    private final static String GAME_TITLE = "javaTetris";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private static ArrayList<Block> backWall;


    public javaTetris(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * Method used to read file and create objects (you can change/move
     * this method as you wish).
     */
    private void readCSV() {

    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        javaTetris game = new javaTetris();
        backWall = initBackWall();
        game.run();
    }



    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        for (Block wall: backWall) {
            wall.render();
        }

    }

    private static ArrayList<Block> initBackWall() {
        ArrayList<Block> walls = new ArrayList<Block>();
        for (int width = 0; width < 10; width++) {
            for (int height = 0; height < 20; height++) {
                walls.add(new Block(new Point(50 + 25 * width, 50 + 25 * height), "TET"));
            }
        }
        return walls;
    }
}
