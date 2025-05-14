package main.java.view;

import main.java.controller.GameController;

import javax.swing.*;
import java.awt.*;

public class GridView extends JFrame {

    private final int size = 3;
    private final JLabel[][] cellLabels = new JLabel[size][size];
    private final GameController controller = new GameController(size);


    public GridView() {
        super("KenKen - GUI Base");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(size, size));
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                JLabel cell = new JLabel("", SwingConstants.CENTER);
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                cell.setPreferredSize(new Dimension(60, 60));
                cell.setFont(new Font("SansSerif", Font.BOLD, 20));
                gridPanel.add(cell);
                cellLabels[r][c] = cell;
            }
        }
        // Pannello con bottoni
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton createBlockBtn = new JButton("Crea Blocco");
        JButton solveBtn = new JButton("Solve");
        solveBtn.addActionListener(e -> {
            var solutions = controller.solvePuzzle();
            if (solutions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessuna soluzione trovata.");
                return;
            }

            int[][] sol = solutions.get(0); // prende la prima (e unica) soluzione

            for (int r = 0; r < size; r++)
                for (int c = 0; c < size; c++)
                    cellLabels[r][c].setText(String.valueOf(sol[r][c])); // aggiorna le celle grafiche
        });


        bottomPanel.add(createBlockBtn);
        bottomPanel.add(solveBtn);

        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GridView::new);
    }
}
