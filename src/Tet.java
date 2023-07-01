import bagel.util.Point;

import java.util.ArrayList;

public class Tet extends GameObject{
    private final static int LEFT_BOUNDARY = 50;
    private final static int RIGHT_BOUNDARY = 300;
    private final static int BOTTOM_BOUNDARY = 550;
    private final static int BLOCK_SIZE = 25;
    private Tetromino type;
    public ArrayList<Block> blocks;
    public ArrayList<Block> blocksOrigPos;
    private Block rotationBlock;
    private int rotationIndex = 0;
    int[][][] offsetData;
    public Tet(double x, double y, Tetromino type) {
        super(x, y, 0, 0);
        this.blocks = initBlocks(type);
        initOffsetData(type);
    }

    @Override
    public void render() {
        for (Block block: blocks) {
            block.render();
        }
    }

    @Override
    public String toString() {
        return ("tet is of type: " + this.type);
    }

    @Override
    public void moveTo(Point point) {
        for (int i = 0; i < blocks.size(); i++) {
            double newX = blocksOrigPos.get(i).left() + point.x;
            double newY = blocksOrigPos.get(i).top() + point.y;
            blocks.get(i).moveTo(new Point(newX, newY));
        }
    }

    public void drawShadow(ArrayList<Block> placedBlocks) {
        ArrayList<Block> shadowBlocks = new ArrayList<>();
        for (Block block: blocks) {
            shadowBlocks.add(new Block(block.left(), block.top(), Tetromino.SHADOW));
        }

        boolean intersect = false;
        while (!intersect) {
            for (Block shadowBlock: shadowBlocks) {
                shadowBlock.moveTo(new Point(shadowBlock.left(), shadowBlock.top() + 25));
            }
            if (placedBlocks.size() == 0) {
                for (Block shadowBlock: shadowBlocks) {
                    if (shadowBlock.top() == 550) {
                        intersect = true;
                        break;
                    }
                }
            } else {
                for (Block placedBlock: placedBlocks) {
                    for (Block shadowBlock: shadowBlocks) {
                        if ((placedBlock.left() == shadowBlock.left()) &&
                                (placedBlock.top() == shadowBlock.top())) {
                            intersect = true;
                            break;
                        } else
                        if (shadowBlock.top() == 550) {
                            intersect = true;
                            break;
                        }
                    }
                    if (intersect) {
                        break;
                    }
                }
            }
        }
        for (Block shadowBlock: shadowBlocks) {
            shadowBlock.moveTo(new Point(shadowBlock.left(), shadowBlock.top() - 25));
            shadowBlock.render();
        }
    }

    public boolean canMovePiece(ArrayList<Block> placedBlocks, double posX, double posY) {
        for (Block block : placedBlocks) {
            if ((block.left() == posX) && (block.top() == posY)) {
                return false;
            }
        }
        // TODO consider placed shadow blocks as well

        return true;
    }
    public boolean canMovePiece(int[] offset, ArrayList<Block> placedBlocks) {
        for (Block block: blocks) {
            double newX = block.left() + BLOCK_SIZE * offset[0];
            double newY = block.top() + BLOCK_SIZE * offset[1];
            // in bounds
            if ((newX <= LEFT_BOUNDARY - BLOCK_SIZE) ||
                    (newX >= RIGHT_BOUNDARY) ||
                    (newY >= BOTTOM_BOUNDARY + BLOCK_SIZE)) {
                return false;
            }
            // overlaps a placed block
            for (Block placedBlock: placedBlocks) {
                if ((placedBlock.left() == newX) &&
                        (placedBlock.top() == newY)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void offsetBy(int[] offset) {
        for (Block block: blocks) {
            double newX = block.left() + BLOCK_SIZE * offset[0];
            double newY = block.top() + BLOCK_SIZE * offset[1];
            block.moveTo(new Point(newX, newY));
        }
    }

    public boolean Offset(int oldRotIndex, int newRotIndex, ArrayList<Block> placedBlocks) {
        int[] offsetVal1;
        int[] offsetVal2;
        int[] endOffset = new int[2];
        int[][][] offset = offsetData;
        boolean movePossible = false;
        for (int testIndex = 0; testIndex < 5; testIndex++) {
            offsetVal1 = offsetData[testIndex][oldRotIndex];
            offsetVal2 = offsetData[testIndex][newRotIndex];
            endOffset[0] = offsetVal1[0] - offsetVal2[0];
            endOffset[1] = offsetVal1[1] - offsetVal2[1];
            if (canMovePiece(endOffset, placedBlocks)) {
                movePossible = true;
                break;
            }
        }

        if (movePossible) {
            offsetBy(endOffset);
        }
        return movePossible;
    }

    public void rotateBlock(Block block, boolean clockwise) {
        double relX = block.left() - rotationBlock.left();
        double relY = block.top() - rotationBlock.top();
        double newX, newY;
        if (clockwise) {
            newX = -1 * relY;
            newY = relX;
        } else {
            newX = relY;
            newY = -1 * relX;
        }
        newX += rotationBlock.left();
        newY += rotationBlock.top();
        block.moveTo(new Point(newX, newY));
    }

    public void rotateTet(boolean clockwise, boolean shouldOffset, ArrayList<Block> placedBlocks) {
        int oldRotationIndex = rotationIndex;
        rotationIndex += clockwise ? 1: -1;
        rotationIndex = Mod(rotationIndex, 4);

        for (Block block: blocks) {
            rotateBlock(block, clockwise);
        }

        if (!shouldOffset) {
            return;
        }

        boolean canOffset = Offset(oldRotationIndex, rotationIndex, placedBlocks);
        if (!canOffset) {
            rotateTet(!clockwise, false, placedBlocks);
        }
    }

    public int Mod(int x, int m) {
        return (x % m + m) % m;
    }

    public void initOffsetData(Tetromino type) {
        if (type == Tetromino.BLANK) {
            return;
        }
        int[][][] offsets;
        if (type == Tetromino.O) {
            offsets = new int[1][4][2];
            offsets[0][0][0] = 0; offsets[0][0][1] = 0;
            offsets[0][1][0] = 0; offsets[0][1][1] = -1;
            offsets[0][2][0] = -1; offsets[0][2][1] = -1;
            offsets[0][3][0] = -1; offsets[0][3][1] = 0;
        } else if (type == Tetromino.I) {
            offsets = new int[5][4][2];
            offsets[0][0][0] = 0; offsets[0][0][1] = 0;
            offsets[0][1][0] = -1; offsets[0][1][1] = 0;
            offsets[0][2][0] = -1; offsets[0][2][1] = 1;
            offsets[0][3][0] = 0; offsets[0][3][1] = 1;

            offsets[1][0][0] = -1; offsets[1][0][1] = 0;
            offsets[1][1][0] = 0; offsets[1][1][1] = 0;
            offsets[1][2][0] = 1; offsets[1][2][1] = 1;
            offsets[1][3][0] = 0; offsets[1][3][1] = 1;

            offsets[2][0][0] = 2; offsets[2][0][1] = 0;
            offsets[2][1][0] = 0; offsets[2][1][1] = 0;
            offsets[2][2][0] = -2; offsets[2][2][1] = 1;
            offsets[2][3][0] = 0; offsets[2][3][1] = 1;

            offsets[3][0][0] = -1; offsets[3][0][1] = 0;
            offsets[3][1][0] = 0; offsets[3][1][1] = 1;
            offsets[3][2][0] = 1; offsets[3][2][1] = 0;
            offsets[3][3][0] = 0; offsets[3][3][1] = -1;

            offsets[4][0][0] = 2; offsets[4][0][1] = 0;
            offsets[4][1][0] = 0; offsets[4][1][1] = -2;
            offsets[4][2][0] = -2; offsets[4][2][1] = 0;
            offsets[4][3][0] = 0; offsets[4][3][1] = 2;
        } else {
            offsets = new int[5][4][2];
            offsets[0][0][0] = 0; offsets[0][0][1] = 0;
            offsets[0][1][0] = 0; offsets[0][1][1] = 0;
            offsets[0][2][0] = 0; offsets[0][2][1] = 0;
            offsets[0][3][0] = 0; offsets[0][3][1] = 0;

            offsets[1][0][0] = 0; offsets[1][0][1] = 0;
            offsets[1][1][0] = 1; offsets[1][1][1] = 0;
            offsets[1][2][0] = 0; offsets[1][2][1] = 0;
            offsets[1][3][0] = -1; offsets[1][3][1] = 0;

            offsets[2][0][0] = 0; offsets[2][0][1] = 0;
            offsets[2][1][0] = 1; offsets[2][1][1] = -1;
            offsets[2][2][0] = 0; offsets[2][2][1] = 0;
            offsets[2][3][0] = -1; offsets[2][3][1] = -1;

            offsets[3][0][0] = 0; offsets[3][0][1] = 0;
            offsets[3][1][0] = 0; offsets[3][1][1] = 2;
            offsets[3][2][0] = 0; offsets[3][2][1] = 0;
            offsets[3][3][0] = 0; offsets[3][3][1] = 2;

            offsets[4][0][0] = 0; offsets[4][0][1] = 0;
            offsets[4][1][0] = 1; offsets[4][1][1] = 2;
            offsets[4][2][0] = 0; offsets[4][2][1] = 0;
            offsets[4][3][0] = -1; offsets[4][3][1] = 2;
        }
        offsetData = offsets;
    }

    public void move(Move move, ArrayList<Block> placedBlocks) {
        if (move == null) {
            return;
        }
        if (move == Move.ROTATE_RIGHT) {
            rotateTet(true, true, placedBlocks);
            return;
        } else if (move == Move.ROTATE_LEFT) {
            rotateTet(false, true, placedBlocks);
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
                case MOVE_UP:
                    point = new Point(block.left(), block.top() - 25);
                    block.moveTo(point);
                    break;
                case SOFT_DROP:
                    point = new Point(block.left(), block.top() + 25);
                    block.moveTo(point);
                    break;
                case HARD_DROP:
                    hardDrop(placedBlocks);
                    break;
            }
        }
    }

    public void hardDrop(ArrayList<Block> placedBlocks) {
        while (!overlapsPlacedBlocks(placedBlocks) && inBounds()) {
            move(Move.SOFT_DROP, placedBlocks);
        }
    }

    public boolean inBounds() {
        for (Block block: blocks) {
            if ((block.left() <= LEFT_BOUNDARY - BLOCK_SIZE) ||
                    (block.left() >= RIGHT_BOUNDARY) ||
                    (block.bottom() >= BOTTOM_BOUNDARY + BLOCK_SIZE)) {
                return false;
            }
        }
        return true;
    }

    public boolean overlapsPlacedBlocks(ArrayList<Block> placedBlocks) {
        for (Block block: blocks) {
            for (Block placedBlock: placedBlocks) {
                if ((block.left() == placedBlock.left()) &&
                        (block.top() == placedBlock.top())) {
                    return true;
                }
            }

        }
        return false;
    }
    private ArrayList<Block> initBlocks(Tetromino tetType) {
        type = tetType;
        ArrayList<Block> tempBlocks = new ArrayList<>();
        ArrayList<Block> tempOrigPos = new ArrayList<>();
        double left = this.left();
        double top = this.top();
        switch (tetType) {
            case I:
                rotationBlock = new Block(left + 25, top + 25, Tetromino.I);
                tempBlocks.add(new Block(left, top + 25, Tetromino.I));
                tempBlocks.add(rotationBlock);
                tempBlocks.add(new Block(left + 50, top + 25, Tetromino.I));
                tempBlocks.add(new Block(left + 75, top + 25, Tetromino.I));
                tempOrigPos.add(new Block(0, 25, Tetromino.I));
                tempOrigPos.add(new Block(25, 25, Tetromino.I));
                tempOrigPos.add(new Block(50, 25, Tetromino.I));
                tempOrigPos.add(new Block(75, 25, Tetromino.I));
                break;
            case J:
                rotationBlock = new Block(left + 25, top + 25, Tetromino.J);
                tempBlocks.add(new Block(left, top, Tetromino.J));
                tempBlocks.add(new Block(left, top + 25, Tetromino.J));
                tempBlocks.add(rotationBlock);
                tempBlocks.add(new Block(left + 50, top + 25, Tetromino.J));
                tempOrigPos.add(new Block(0, 0, Tetromino.J));
                tempOrigPos.add(new Block(0, 25, Tetromino.J));
                tempOrigPos.add(new Block(25, 25, Tetromino.J));
                tempOrigPos.add(new Block(50, 25, Tetromino.J));
                break;
            case L:
                rotationBlock = new Block(left + 25, top + 25, Tetromino.L);
                tempBlocks.add(new Block(left + 50, top, Tetromino.L));
                tempBlocks.add(new Block(left, top + 25, Tetromino.L));
                tempBlocks.add(rotationBlock);
                tempBlocks.add(new Block(left + 50, top + 25, Tetromino.L));
                tempOrigPos.add(new Block(50, 0, Tetromino.L));
                tempOrigPos.add(new Block(0, 25, Tetromino.L));
                tempOrigPos.add(new Block(25, 25, Tetromino.L));
                tempOrigPos.add(new Block(50, 25, Tetromino.L));
                break;
            case O:
                rotationBlock = new Block(left + 25, top + 25, Tetromino.O);
                tempBlocks.add(new Block(left + 25, top, Tetromino.O));
                tempBlocks.add(new Block(left + 50, top, Tetromino.O));
                tempBlocks.add(rotationBlock);
                tempBlocks.add(new Block(left + 50,top + 25, Tetromino.O));
                tempOrigPos.add(new Block(25, 0, Tetromino.O));
                tempOrigPos.add(new Block(50, 0, Tetromino.O));
                tempOrigPos.add(new Block(25, 25, Tetromino.O));
                tempOrigPos.add(new Block(50, 25, Tetromino.O));
                break;
            case S:
                rotationBlock = new Block(left + 25,top + 25, Tetromino.S);
                tempBlocks.add(new Block(left + 25, top, Tetromino.S));
                tempBlocks.add(new Block(left + 50, top, Tetromino.S));
                tempBlocks.add(new Block(left, top + 25, Tetromino.S));
                tempBlocks.add(rotationBlock);
                tempOrigPos.add(new Block(25, 0, Tetromino.S));
                tempOrigPos.add(new Block(50, 0, Tetromino.S));
                tempOrigPos.add(new Block(0, 25, Tetromino.S));
                tempOrigPos.add(new Block(25, 25, Tetromino.S));
                break;
            case T:
                rotationBlock = new Block(left + 25, top + 25, Tetromino.T);
                tempBlocks.add(new Block(left + 25, top, Tetromino.T));
                tempBlocks.add(new Block(left, top + 25, Tetromino.T));
                tempBlocks.add(rotationBlock);
                tempBlocks.add(new Block(left + 50,top + 25, Tetromino.T));
                tempOrigPos.add(new Block(25, 0, Tetromino.T));
                tempOrigPos.add(new Block(0, 25, Tetromino.T));
                tempOrigPos.add(new Block(25, 25, Tetromino.T));
                tempOrigPos.add(new Block(50, 25, Tetromino.T));
                break;
            case Z:
                rotationBlock = new Block(left+ 25, top + 25, Tetromino.Z);
                tempBlocks.add(new Block(left, top, Tetromino.Z));
                tempBlocks.add(new Block(left + 25, top, Tetromino.Z));
                tempBlocks.add(rotationBlock);
                tempBlocks.add(new Block(left + 50,top + 25, Tetromino.Z));
                tempOrigPos.add(new Block(0, 0, Tetromino.Z));
                tempOrigPos.add(new Block(25, 0, Tetromino.Z));
                tempOrigPos.add(new Block(25, 25, Tetromino.Z));
                tempOrigPos.add(new Block(50, 25, Tetromino.Z));
                break;
        }
        blocksOrigPos = tempOrigPos;
        return tempBlocks;
    }
}
