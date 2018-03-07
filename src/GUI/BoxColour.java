package GUI;

import java.awt.Color;
import javax.swing.JLabel;

public final class BoxColour extends JLabel {

    private final int x, y;

    public BoxColour(int x, int y, Color colour) {
        this.x = x;
        this.y = y;
        setState(colour);
    }
    
    public int getRow() {
        return x;
    }
    
    public int getCol() {
        return y;
    }

    public void setState(Color colour) {
        if (colour.equals(Color.YELLOW)) {
            setBackground(colour);
        } else if (colour.equals(Color.RED)) {
            setBackground(colour);
        } else {
            setBackground(colour);
        }
    }
    
    @Override
    public String toString() {
        return "P( " + x + ", " + y + ") ";
    }
}
