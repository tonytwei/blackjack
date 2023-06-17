import bagel.*;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class javaTetris extends AbstractGame  {
    private final static String GAME_TITLE = "javaTetris";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final static int WINDOW_WIDTH = 500;
    private final static int WINDOW_HEIGHT = 600;;
    private final static int LEFT_BOUNDARY = 50;
    private final static int RIGHT_BOUNDARY = 300;
    private final static int BOTTOM_BOUNDARY = 550;
    private final static int BLOCK_SIZE = 25;
    private static ArrayList<Block> backWall;
    private ArrayList<Tet> tets = new ArrayList<>(); // TODO DELETE LATER
    private Queue<Tet> nextTets = new LinkedList<>();
    private Tet curTet = null;
    private ArrayList<Block> placedBlocks = new ArrayList<>();
    boolean fileRead = false;
    private int frameCount = 0;
    private int speedLevel = 0;
    private boolean bottomCollision = false;
    private final static int[] speed = new int[]{59, 47, 37, 28, 21, 15, 11, 8, 5, 3, 2, 1};


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
        initialiseTetris();
        drawBackground();
        renderPlacedBlocks();
        frameCounter();
        updateCurTet();



        curTet.move(str(input), placedBlocks);
        bottomCollision = false;
        handleCollision(curTet, str(input));
        if (!bottomCollision) {
            autoMove(curTet);
            handleCollision(curTet, Move.SOFT_DROP);
        }
        if (bottomCollision) {
            curTet.move(Move.MOVE_UP, null);
            curTet.render();
            for (Block block: curTet.blocks) {
                placedBlocks.add(block);
            }
            curTet = null;
        } else {
            curTet.render();
        }




        // TODO remove later
        //renderTets();

    }


    public void updateCurTet() {
        if (curTet != null) {
            return;
        }
        curTet = nextTets.poll();
        assert curTet != null;
        System.out.println(curTet.toString());
        nextTets.add(generateTet());
    }

    public void handleCollision(Tet tet, Move move) {
        if ((!collisionCheck(tet) && inBounds(tet)) || (move == null)) {
            return;
        }
        System.out.println("collision");
        switch (move) {
            case MOVE_LEFT:
                tet.move(Move.MOVE_RIGHT, null);
                break;
            case MOVE_RIGHT:
                tet.move(Move.MOVE_LEFT, null);
                break;
            case SOFT_DROP:
                System.out.println("bottomCollision flag");
                bottomCollision = true;
                break;
        }
    }

    public boolean inBounds(Tet tet) {
        for (Block block: tet.blocks) {
            if ((block.left() == LEFT_BOUNDARY - BLOCK_SIZE) ||
                (block.right() == RIGHT_BOUNDARY + BLOCK_SIZE) ||
                (block.bottom() == BOTTOM_BOUNDARY + BLOCK_SIZE)) {
                return false;
            }
        }
        return true;
    }

    public boolean collisionCheck(Tet tet) {
        // tet and placedBlocks
        for (Block block: tet.blocks) {
            for (Block placedBlock: placedBlocks) {
                if (block.intersects(placedBlock)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void initialiseTetris() {
        if (!fileRead) {
            System.out.println("initialisation");
            addBlocks();
            initNextTets();
            fileRead = true;
        }
    }

    public void renderPlacedBlocks() {
        for (Block block: placedBlocks) {
            block.render();
        }
    }

    public Tet generateTet() {
        int randomNum = (int)(Math.random() * (7));
        return new Tet(100, 100, Tetromino.values()[randomNum]);
    }

    public void initNextTets() {
        for (int i = 0; i < 3; i++) {
            nextTets.add(generateTet());
        }
    }

    public void autoMove(Tet tet) {
        if (frameCount == speed[speedLevel]) {
            tet.move(Move.SOFT_DROP, null);
        }
    }

    public void frameCounter() {
        frameCount++;
        if (frameCount == 60) {
            frameCount = 0;
        }
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
        } else if (input.wasPressed(Keys.ESCAPE)){
            return Move.PAUSE;
        }
        return null;

    }

    private void renderTets() {
        for (Tet tet: tets) {
            tet.render();
        }
    }

    private void addBlocks() {
        tets.add(generateTet());
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
