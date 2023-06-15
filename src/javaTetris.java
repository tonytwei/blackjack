import bagel.*;
import bagel.util.Point;
import java.util.ArrayList;

public class javaTetris extends AbstractGame  {
    private final static String GAME_TITLE = "javaTetris";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final static int WINDOW_WIDTH = 500;
    private final static int WINDOW_HEIGHT = 600;;
    private static ArrayList<Block> backWall;
    private ArrayList<Tet> tets = new ArrayList<>();
    boolean fileRead = false;


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
        if (!fileRead) {
            addBlocks();
            fileRead = true;
        }
        tets.get(0).move(str(input));
        drawBackground();
        renderTets();

    }

    public Move str(Input input) {
        if (input.wasPressed(Keys.J)) {
            return Move.MOVE_LEFT;
        } else if (input.wasPressed(Keys.L)){
            return Move.MOVE_RIGHT;
        } else if (input.wasPressed(Keys.I)){
            return Move.ROTATE_RIGHT;
        } else if (input.wasPressed(Keys.Z)){
            return Move.ROTATE_LEFT;
        } else if (input.wasPressed(Keys.K)){
            return Move.SOFT_DROP;
        } else if (input.wasPressed(Keys.SPACE)){
            return Move.HARD_DROP;
        } else if (input.wasPressed(Keys.C)){
            return Move.HOLD;
        } else {
            return Move.PAUSE;
        }

    }

    private void renderTets() {
        for (Tet tet: tets) {
            tet.render();
        }
    }

    private void addBlocks() {
        tets.add(new Tet(100,100, Tetromino.I));
        tets.add(new Tet(100,200, Tetromino.J));
        tets.add(new Tet(100,300, Tetromino.L));;
        tets.add(new Tet(100,400, Tetromino.O));
        tets.add(new Tet(200,100, Tetromino.S));
        tets.add(new Tet(200,200, Tetromino.T));
        tets.add(new Tet(200,300, Tetromino.Z));
    }

    private void drawBackground() {
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        for (Block wall: backWall) {
            wall.render();
        }
    }

    private static ArrayList<Block> initBackWall() {
        ArrayList<Block> walls = new ArrayList<Block>();
        for (int width = 0; width < 10; width++) {
            for (int height = 0; height < 20; height++) {
                walls.add(new Block(50 + 25 * width, 50 + 25 * height, Tetromino.BLANK));
            }
        }
        return walls;
    }
}
