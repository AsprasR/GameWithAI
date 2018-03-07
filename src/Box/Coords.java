package Box;

import GUI.BoxColour;
import java.util.ArrayList;
import java.util.Objects;

public class Coords {

    private final ArrayList<BoxColour> point;

    public Coords() {
        point = new ArrayList<>();
    }

    public Coords(ArrayList<BoxColour> point) {
        ArrayList<BoxColour> tmp = new ArrayList<>();
        tmp.addAll(point);
        this.point = tmp;
    }
    
    public int size() {
        return point.size();
    }

    public void setCoords(BoxColour point) {
        this.point.add(point);
    }

    public void removeCoords(BoxColour point) {
        this.point.remove(point);
    }

    public boolean contains(BoxColour point) {
        return this.point.contains(point);
    }

    public ArrayList<BoxColour> getCoords() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Coords) {
            Coords that = (Coords) o;
            if (point == that.point) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 79 * Objects.hashCode(this.point);
    }

    @Override
    public String toString() {
        StringBuilder word = new StringBuilder();
        point.forEach((pair) -> {
            word.append(pair).append("\n");
        });
        return word.toString();
    }

}
