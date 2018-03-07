package Simulation;

import Box.Coords;
import java.util.Observable;
import java.util.Observer;

public class NextGeneration extends Observable implements Observer {

    private final Coords coords;

    public NextGeneration(Coords coords) {
        this.coords = coords;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof NextTick) {
            setChanged();
            notifyObservers(coords);
        }
    }
}
