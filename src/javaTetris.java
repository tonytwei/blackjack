import bagel.*;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class javaTetris extends AbstractGame  {
    private final static String GAME_TITLE = "javaTetris";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Font TEXT_40 = new Font("res/FSO8BITR.TTF", 40);
    private final Font TEXT_24 = new Font("res/FSO8BITR.TTF", 24);
    private final static int WINDOW_WIDTH = 500;
    private final static int WINDOW_HEIGHT = 600;
    private final static int LEFT_BOUNDARY = 50;
    private final static int RIGHT_BOUNDARY = 300;
    private final static int BOTTOM_BOUNDARY = 550;
    private final static int BOARD_BLOCK_WIDTH = 10;
    private final static int BOARD_BLOCK_HEIGHT = 20;
    private final static int BLOCK_SIZE = 25;
    private final static int NOT_STARTED = 0;
    private final static int RUNNING = 1;
    private final static int PAUSED = 2;
    private final static int COUNTDOWN = 3;
    private static int GAME_STATE = NOT_STARTED;
    private static ArrayList<Block> backWall = new ArrayList<>();
    private static ArrayList<Block> pauseWall = new ArrayList<>();
    private ArrayList<Tet> tets = new ArrayList<>(); // TODO DELETE LATER
    private Queue<Tet> nextTets = new LinkedList<>();
    private Tet curTet;
    private Tet heldTet;
    private ArrayList<Block> placedBlocks = new ArrayList<>();
    boolean fileRead = false;
    private int gameScore = 0;
    private int frameCount = 0;
    private int speedLevel = 0;
    private Integer countdown;
    private boolean bottomCollision = false;
    private final static int[] speed = new int[]{59, 47, 37, 28, 21, 15, 11, 8, 5, 3, 2, 1};


    public javaTetris(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    public static void main(String[] args) {
        javaTetris game = new javaTetris();
        game.run();
    }

    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        switch (GAME_STATE) {
            case NOT_STARTED:
                initialiseTetris();
                drawStart();
                checkStart(str(input));
                break;
            case RUNNING:
                drawGameBackground();
                checkPause(str(input));
                renderPlacedBlocks();
                frameCounter();
                updateCurTet();
                drawShadowTet();
                drawQueue();
                checkForHold(str(input));
                drawHold();
                moveRenderCurTet(str(input));
                clearLines();
                drawScore();
                break;
            case PAUSED:
                checkPause(str(input));
                drawPaused();
                break;
            case COUNTDOWN:
                frameCounter();
                drawCountdown();
                break;
        }
    }

    public void drawScore() {
        TEXT_24.drawString("SCORE " + gameScore, 335, 50);
    }

    public void drawCountdown() {
        if (countdown == 0) {
            GAME_STATE = RUNNING;
            return;
        }
        TEXT_40.drawString(countdown.toString(), 225, 200);
        if (frameCount == 59) {
            countdown--;
        }
    }
    public void checkStart(Move move) {
        if (move == Move.HARD_DROP) {
            GAME_STATE = COUNTDOWN;
            frameCount = 0;
            countdown = 3;
        }
    }

    public void drawStart() {
        TEXT_40.drawString("JAVA TETRIS", 90, 200);
        TEXT_24.drawString("PRESS SPACE TO START", 80, 300);
    }

    public void drawPaused() {
        TEXT_40.drawString("GAME PAUSED", 90, 200);
        TEXT_24.drawString("PRESS SPACE TO CONTINUE", 50, 300);
    }
    public void checkPause(Move move) {
        if (move == Move.PAUSE) {
            GAME_STATE = PAUSED;
        } else if (GAME_STATE == PAUSED && move == Move.HARD_DROP) {
            GAME_STATE = COUNTDOWN;
        }
    }
    public void drawHold() {
        if (heldTet != null) {
            heldTet.render();
        }
    }

    public void moveRenderCurTet(Move move) {
        if (curTet == null) {
            return;
        }
        curTet.move(move, placedBlocks);
        bottomCollision = false;
        handleCollision(curTet, move);
        if (!bottomCollision && move != Move.SOFT_DROP) {
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
    }

    public void checkForHold(Move move) {
        if (move != Move.HOLD) {
            return;
        }
        curTet.moveTo(new Point(350, 125));
        if (heldTet != null) {
            heldTet.moveTo(new Point(125, 50));
        }
        Tet temp = curTet;
        curTet = heldTet;
        heldTet = temp;
    }

    public void drawShadowTet() {
        if (curTet == null) {
            return;
        }
        curTet.drawShadow(placedBlocks);
    }
    public void drawQueue() {
        int newX = 350;
        int newY = 275;
        int i = 0;
        for (Tet tet: nextTets) {
            tet.moveTo(new Point(newX, newY + i * 100));
            tet.render();
            i++;
        }
    }

    public void clearLines() {
        ArrayList<Integer> linesToClear = new ArrayList<>();
        // find lines that need to be cleared
        for (int row = 0; row < BOARD_BLOCK_HEIGHT; row++) {
            int blockCount = 0;
            for (Block block: placedBlocks) {
                if (block.top() == 50 + row * BLOCK_SIZE) {
                    blockCount++;
                }
            }
            if (blockCount == BOARD_BLOCK_WIDTH) {
                linesToClear.add(row);
            }
        }

        switch (linesToClear.size()) {
            case 1:
                gameScore += 40;
                break;
            case 2:
                gameScore += 100;
                break;
            case 3:
                gameScore += 300;
                break;
            case 4:
                gameScore += 1200;
                break;
        }

        // remove blocks in cleared lines
        for (int line:linesToClear) {
            ArrayList<Block> blocksToRemove = new ArrayList<>();
            for(Block block: placedBlocks) {
                if (block.top() == 50 + line * BLOCK_SIZE) {
                    blocksToRemove.add(block);
                } else if (block.top() < 50 + line * BLOCK_SIZE) {
                    Point point = new Point(block.left(), block.top() + BLOCK_SIZE);
                    block.moveTo(point);
                }
            }
            for (Block block: blocksToRemove) {
                placedBlocks.remove(block);
            }
        }
    }


    public void updateCurTet() {
        if (curTet != null) {
            return;
        }
        frameCount = 0;
        curTet = nextTets.poll();
        curTet.moveTo(new Point(125, 50));
        nextTets.add(generateTet());
    }

    public void handleCollision(Tet tet, Move move) {
        if ((!collisionCheck(tet) && inBounds(tet)) || (move == null)) {
            return;
        }
        switch (move) {
            case MOVE_LEFT:
                tet.move(Move.MOVE_RIGHT, null);
                break;
            case MOVE_RIGHT:
                tet.move(Move.MOVE_LEFT, null);
                break;
            case SOFT_DROP:
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
            addBlocks();
            initNextTets();
            initBackWall();
            initPauseWall();
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
        if (input.wasPressed(Keys.LEFT) ||
                (input.isDown(Keys.J) && frameCount % 10 == 0)) {
            return Move.MOVE_LEFT;
        } else if (input.wasPressed(Keys.RIGHT) ||
                (input.isDown(Keys.L) && frameCount % 10 == 0)){
            return Move.MOVE_RIGHT;
        } else if (input.wasPressed(Keys.UP)){
            return Move.ROTATE_RIGHT;
        } else if (input.wasPressed(Keys.Z)){
            return Move.ROTATE_LEFT;
        } else if (input.wasPressed(Keys.DOWN) ||
                (input.isDown(Keys.K) && frameCount % 5 == 0)){
            return Move.SOFT_DROP;
        } else if (input.wasPressed(Keys.SPACE)){
            return Move.HARD_DROP;
        } else if (input.wasPressed(Keys.C)){
            return Move.HOLD;
        } else if (input.wasPressed(Keys.P)){
            return Move.PAUSE;
        }
        return null;
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

    private void drawGameBackground() {
        for (Block wall: backWall) {
            wall.render();
        }
        TEXT_24.drawString("HOLD", 360, 90);
        TEXT_24.drawString("QUEUE", 350, 240);
    }

    private void initBackWall() {
        for (int width = 0; width < 10; width++) {
            for (int height = 0; height < 20; height++) {
                backWall.add(new Block(50 + 25 * width, 50 + 25 * height, Tetromino.BLANK));
            }
        }
        for (int width = 0; width < 4; width++) {
            for (int height = 0; height < 4; height++) {
                backWall.add(new Block(350 + 25 * width, 100 + 25 * height, Tetromino.BLANK));
            }
        }
        for (int width = 0; width < 4; width++) {
            for (int height = 0; height < 12; height++) {
                backWall.add(new Block(350 + 25 * width, 250 + 25 * height, Tetromino.BLANK));
            }
        }
    }

    private void initPauseWall() {
        Block pauseBackground = new Block(0, 0, Tetromino.I);
        for (int width = 0; width < 12; width++) {
            for (int height = 0; height < 20; height++) {
                Point newPoint = new Point(100 + width * BLOCK_SIZE, 100 + height * BLOCK_SIZE);
                pauseBackground.moveTo(newPoint);
                pauseWall.add(pauseBackground);
            }
        }
    }
}
