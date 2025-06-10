package main.java.view;

import main.java.controller.GameController;
import main.java.observer.GridObserver;

import javax.swing.*;
import java.util.List;

public class GridStatusObserver implements GridObserver {

    private final GameController controller;
    private final JLabel statusLabel;
    private final JButton prevBtn;
    private final JButton nextBtn;
    private final KenKenGridPanel gridPanel;
    private List<int[][]> solutions;
    private int currentSolIdx;

    public GridStatusObserver(GameController controller,
                              JLabel statusLabel,
                              JButton prevBtn,
                              JButton nextBtn,
                              KenKenGridPanel gridPanel) {
        this.controller = controller;
        this.statusLabel = statusLabel;
        this.prevBtn = prevBtn;
        this.nextBtn = nextBtn;
        this.gridPanel = gridPanel;
        this.solutions = List.of(); // inizialmente vuota
        this.currentSolIdx = -1;
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

        prevBtn.setEnabled(solutions != null && currentSolIdx > 0);
        nextBtn.setEnabled(solutions != null && currentSolIdx < solutions.size() - 1);

        gridPanel.repaint(); // ridisegna sempre la griglia
    }

    public void setSolutions(List<int[][]> sols) {
        this.solutions = sols;
        this.currentSolIdx = 0;
    }
    public void clearSolutions() {
        this.solutions = List.of();
        this.currentSolIdx = -1;
    }
}
