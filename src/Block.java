import java.util.ArrayList;

import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Block extends GameObject{
    private final Image BLANK_TET = new Image("res/blocks/Tet.png");
    private final Image I_TET = new Image("res/blocks/ITet.png");
    private final Image J_TET = new Image("res/blocks/JTet.png");
    private final Image L_TET = new Image("res/blocks/LTet.png");
    private final Image O_TET = new Image("res/blocks/OTet.png");
    private final Image S_TET = new Image("res/blocks/STet.png");
    private final Image T_TET = new Image("res/blocks/TTet.png");
    private final Image Z_TET = new Image("res/blocks/ZTet.png");
    private final static int BLOCK_SIZE = 25;
    public Block(double x, double y, Tetromino type) {
        super(x, y, BLOCK_SIZE, BLOCK_SIZE);
        switch (type) {
            case BLANK:
                this.image = BLANK_TET;
                break;
            case I:
                this.image = I_TET;
                break;
            case J:
                this.image = J_TET;
                break;
            case L:
                this.image = L_TET;
                break;
            case O:
                this.image = O_TET;
                break;
            case S:
                this.image = S_TET;
                break;
            case T:
                this.image = T_TET;
                break;
            case Z:
                this.image = Z_TET;
                break;
        }
    }
}
