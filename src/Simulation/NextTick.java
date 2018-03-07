package Simulation;

import java.util.Observable;

public class NextTick extends Observable {

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }
}
