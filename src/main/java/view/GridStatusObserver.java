package main.java.view;

import main.java.observer.GridObserver;

import javax.swing.*;

public class GridStatusObserver implements GridObserver {
    private final JLabel statusLabel;

    public GridStatusObserver(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }

    @Override
    public void onGridChanged() {
        statusLabel.setText("Griglia aggiornata");

    }
}
