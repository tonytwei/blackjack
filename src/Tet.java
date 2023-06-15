import bagel.Input;
import bagel.util.Point;

import java.util.ArrayList;

public class Tet extends GameObject{
    private final static int LEFT_BOUNDARY = 50;
    private final static int RIGHT_BOUNDARY = 300;

    private ArrayList<Block> blocks;
    public Tet(double x, double y, Tetromino type) {
        super(x, y, 0, 0);
        this.blocks = initBlocks(type);
    }

    @Override
    public void render() {
        for (Block block: blocks) {
            block.render();
        }
    }
    public void move(Move move) {
        boolean validMove = true;
        for (Block block: blocks) {
            if (((move == Move.MOVE_LEFT) && (block.left() == LEFT_BOUNDARY)) ||
                ((move == Move.MOVE_RIGHT) && (block.right() == RIGHT_BOUNDARY))) {
                validMove = false;
            }
        }
        if (!validMove) {
            return;
        }

        for (Block block: blocks) {
            Point point;
            switch (move) {
                case MOVE_LEFT:
                    point = new Point(block.left() - 25, block.top());
                    block.moveTo(point);
                    break;
                case MOVE_RIGHT:
                    point = new Point(block.left() + 25, block.top());
                    block.moveTo(point);
                    break;
            }
        }
    }
    private ArrayList<Block> initBlocks(Tetromino type) {
        ArrayList<Block> tempBlocks = new ArrayList<>();
        double left = this.left();
        double top = this.top();
        switch (type) {
            case I:
                tempBlocks.add(new Block(left          ,top, Tetromino.I));
                tempBlocks.add(new Block(left + 25  ,top, Tetromino.I));
                tempBlocks.add(new Block(left + 50  ,top, Tetromino.I));
                tempBlocks.add(new Block(left + 75  ,top, Tetromino.I));
                break;
            case J:
                tempBlocks.add(new Block(left + 25, top, Tetromino.J));
                tempBlocks.add(new Block(left + 25, top + 25, Tetromino.J));
                tempBlocks.add(new Block(left, top + 50, Tetromino.J));
                tempBlocks.add(new Block(left + 25,top + 50, Tetromino.J));
                break;
            case L:
                tempBlocks.add(new Block(left, top, Tetromino.L));
                tempBlocks.add(new Block(left, top + 25, Tetromino.L));
                tempBlocks.add(new Block(left, top + 50, Tetromino.L));
                tempBlocks.add(new Block(left + 25,top + 50, Tetromino.L));
                break;
            case O:
                tempBlocks.add(new Block(left, top, Tetromino.O));
                tempBlocks.add(new Block(left + 25, top, Tetromino.O));
                tempBlocks.add(new Block(left, top + 25, Tetromino.O));
                tempBlocks.add(new Block(left + 25,top + 25, Tetromino.O));
                break;
            case S:
                tempBlocks.add(new Block(left + 25, top, Tetromino.S));
                tempBlocks.add(new Block(left + 50, top, Tetromino.S));
                tempBlocks.add(new Block(left, top + 25, Tetromino.S));
                tempBlocks.add(new Block(left + 25,top + 25, Tetromino.S));
                break;
            case T:
                tempBlocks.add(new Block(left, top, Tetromino.T));
                tempBlocks.add(new Block(left + 25, top, Tetromino.T));
                tempBlocks.add(new Block(left + 50, top, Tetromino.T));
                tempBlocks.add(new Block(left + 25,top + 25, Tetromino.T));
                break;
            case Z:
                tempBlocks.add(new Block(left, top, Tetromino.Z));
                tempBlocks.add(new Block(left + 25, top, Tetromino.Z));
                tempBlocks.add(new Block(left+ 25, top + 25, Tetromino.Z));
                tempBlocks.add(new Block(left + 50,top + 25, Tetromino.Z));
                break;
        }
        return tempBlocks;
    }
}
