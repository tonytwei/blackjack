import java.util.ArrayList;

import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Block extends GameObject{
    private final Image BLANK_TET = new Image("res/blocks/-Tet.png");
    private final Image I_TET = new Image("res/blocks/ITet.png");
    private final Image J_TET = new Image("res/blocks/JTet.png");
    private final Image L_TET = new Image("res/blocks/LTet.png");
    private final Image O_TET = new Image("res/blocks/OTet.png");
    private final Image T_TET = new Image("res/blocks/TTet.png");
    private final Image Z_TET = new Image("res/blocks/ZTet.png");
    private final Image TET = new Image("res/blocks/Tet.png");
    
    protected ArrayList<Rectangle> blocks;

    public Block(Point topleft, String colour) {
        super(topleft, 12, 12);
        switch (colour) {
            case "BLANK_TET":
                this.image = BLANK_TET;
                break;
            case "I_TET":
                this.image = I_TET;
                break;
            case "J_TET":
                this.image = J_TET;
                break;
            case "L_TET":
                this.image = L_TET;
                break;
            case "O_TET":
                this.image = O_TET;
                break;
            case "T_TET":
                this.image = T_TET;
                break;
            case "Z_TET":
                this.image = Z_TET;
                break;
            case "TET":
                this.image = TET;
                break;
        }
    }
}
