package main.java.view;

import main.java.controller.GameController;
import main.java.observer.GridObserver;

import javax.swing.*;
import java.util.List;

public class GridStatusObserver implements GridObserver {

    private final GameController controller;
    private final JLabel statusLabel;
    private final KenKenGridPanel gridPanel;


    public GridStatusObserver(GameController controller,
                              JLabel statusLabel,
                              KenKenGridPanel gridPanel) {
        this.controller = controller;
        this.statusLabel = statusLabel;
        this.gridPanel = gridPanel;
    }

    @Override
    public void onGridChanged() {
        if (controller.isGridComplete()) {
            String msg = controller.validateCurrentGrid();
            statusLabel.setText(msg == null
                    ? "Griglia completa e corretta!"
                    : "Griglia completa ma NON corretta: " + msg);
        } else {
            statusLabel.setText("Griglia incompleta. Continua a giocare!");
        }

        gridPanel.repaint(); // ridisegna sempre la griglia
    }
}
