/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Box.Coords;
import Box.CountingTree;
import Listener.MouseListener;
import Simulation.NextTick;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author dev
 */
public final class BoxGUI extends JPanel implements Observer {

    private final BoxColour[][] points;
    private final int col, row, size;
    private final Dimension boxSize;
    private final NextTick tick;
    private final Coords coords;
    private final int sizeOfLine = 1;
    private CountingTree countingTree;
    private final MouseListener mouseListener;

    public BoxGUI(Coords coords, int col, int row, int size) {
        this.col = col;
        this.row = row;
        this.coords = coords;
        this.size = size;
        tick = new NextTick();
        points = new BoxColour[row][col];

        boxSize = new Dimension(this.size, this.size);
        mouseListener = new MouseListener(this);

        setLayout(new GridLayout(row, col));
        setBox();
    }

    public NextTick getTick() {
        return tick;
    }

    public void Tick() {
        tick.setChanged();
        tick.notifyObservers(coords);
    }

    private void setBox() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
               /* if (i % 2 == 0 && j % 2 == 0 || i % 2 == 0 && j % 2 == 1 || j % 5 == 0 || i > row - 5 || j > col - 5) {
                    points[i][j] = new BoxColour(i, j, Color.YELLOW);
                    points[i][j].setOpaque(true);
                    points[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, sizeOfLine));
                    points[i][j].setPreferredSize(boxSize);
                    points[i][j].addMouseListener(mouseListener);
                    add(points[i][j]);
                } else {*/
                    points[i][j] = new BoxColour(i, j, Color.BLACK);
                    points[i][j].setOpaque(true);
                    points[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, sizeOfLine));
                    points[i][j].setPreferredSize(boxSize);
                    points[i][j].addMouseListener(mouseListener);
                    coords.setCoords(points[i][j]);
                    add(points[i][j]);

                }
            //}
        }
        countingTree = new CountingTree(row, col, points, coords);
    }

    public void labelPressed(BoxColour point, int a, int b) {
        point.setState(Color.RED);
        coords.removeCoords(point);
        int x = point.getRow() + a;
        int y = point.getCol() + b;
        if (x < 0 || y < 0 || x + 1 > row || y + 1 > col) {

        } else {
            points[x][y].setState(Color.RED);
            coords.removeCoords(points[x][y]);
            System.out.println("Usunięto punkt o x= " + x + " i y= " + y);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Coords) {
            countingTree.findTheBestPoints();
        }
    }
}

/*
    private int check(Coords coords) {
        int wynik = 0;
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < rows; j++) {
                if (coords.contains(points[i][j])) {
                    if (i + 1 < col && coords.contains(points[i + 1][j])) {
                        wynik++;
                    }
                    if (j + 1 < rows && coords.contains(points[i][j + 1])) {
                        wynik++;
                    }
                }
            }
        }
        System.out.println("Wynik " + wynik);
        System.out.println(coords);
        return wynik;
    }

    
    private PosPoints bettercheck(BoxColour point, Coords coords, PosPoints tmp, int suma) {
        PosPoints posPoints = new PosPoints(tmp.getList());
        for (int i = point.getCol() - 1; i <= point.getCol() + 1; i++) {
            for (int j = point.getRow() - 1; j <= point.getRow() + 1; j++) {
                if (i < 0 || j < 0 || i + 1 > col || j + 1 > rows) {
                } else {
                    if (coords.contains(points[i][j])) {
                        if (i + 1 < col && !coords.contains(points[i + 1][j])
                                && !posPoints.contains(points[i][j], points[i + 1][j])) {
                            odejmij++;
                            xd++;
                            posPoints.add(points[i][j], points[i + 1][j]);
                        }
                        if (j + 1 < rows && !coords.contains(points[i][j + 1])
                                && !posPoints.contains(points[i][j], points[i][j + 1])) {
                            odejmij++;
                            xd++;
                            posPoints.add(points[i][j], points[i][j + 1]);
                        }
                    } else {
                        if (i + 1 < col && !posPoints.contains(points[i][j], points[i + 1][j])) {
                            odejmij++;
                            xd++;
                            posPoints.add(points[i][j], points[i + 1][j]);
                        }
                        if (j + 1 < rows && !posPoints.contains(points[i][j], points[i][j + 1])) {
                            odejmij++;
                            xd++;
                            posPoints.add(points[i][j], points[i][j + 1]);
                        }
                    }

                    //                  System.out.println(odejmij);
                }
            }
        }
        System.out.println("first " + (suma - odejmij));
        //  System.out.println(coords);

        return posPoints;
    }

    private void loop(Coords tmp, int k, PosPoints posPoints, int suma) {
        Coords coords = new Coords(tmp.getCoords());
        PosPoints posTmp = new PosPoints(posPoints.getList());
        for (BoxColour point : this.coords.getCoords()) {
            if (coords.contains(point)) {
                coords.removeCoords(point);
                int i = point.getCol();
                int j = point.getRow();
                if (i + 1 < col && coords.contains(points[i + 1][j])) {
                    coords.removeCoords(points[i + 1][j]);
                    odejmij = 0;
                    posPoints = bettercheck(point, coords, posPoints, suma);
              //      check(coords);
                    if (k < 3) {
                        loop(coords, k + 1, posPoints, suma - odejmij);
                    }
                    coords.setCoords(points[i + 1][j]);
                }
                posPoints = posTmp;
                if (j + 1 < rows && coords.contains(points[i][j + 1])) {
                    coords.removeCoords(points[i][j + 1]);
                    odejmij = 0;
                    posPoints = bettercheck(point, coords, posPoints, suma);
               //     check(coords);
                    if (k < 3) {
                        loop(coords, k + 1, posPoints, suma - odejmij);
                    }
                    coords.setCoords(points[i][j + 1]);
                }
                coords.setCoords(points[i][j]);
                posPoints = posTmp;
            }
        }
    }

    private void setRandomPoints() {
        xd = 0;
        odejmij = 0;
        posPoints = bettercheck(point, coords, posPoints, suma);
        suma -= odejmij;
     //   check(coords);
        //   System.out.println(posPoints);
        loop(coords, 0, posPoints, suma);
        //  System.out.println("suma " + xd);
    }
 */
