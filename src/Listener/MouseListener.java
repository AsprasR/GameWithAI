/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import GUI.BoxColour;
import GUI.BoxGUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Nightspire
 */
public class MouseListener extends MouseAdapter {

    private final BoxGUI map;
    private boolean buttonPressed = false;
    private BoxColour point;

    public MouseListener(BoxGUI map) {
        this.map = map;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttonPressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (buttonPressed) {
            point = (BoxColour) e.getSource();
            map.labelPressed(point, 0, 1);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        point = (BoxColour) e.getSource();
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                if (buttonPressed == false) {
                    map.labelPressed(point, 0, 1);
                    buttonPressed = true;
                }
                break;
            case MouseEvent.BUTTON3:
                if (buttonPressed == false) {
                    map.labelPressed(point, 1, 0);
                    buttonPressed = true;
                }
                break;
            case MouseEvent.BUTTON2:
                map.Tick();
                buttonPressed = false;
                break;
        }
    }
}
