package main.java.view;

import main.java.controller.GameController;
import main.java.model.Block;
import main.java.model.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GridView extends JFrame {

    private final int size = 3;
    private final JLabel[][] cellLabels = new JLabel[size][size];
    private final GameController controller = new GameController(size);
    private final boolean[][] selected = new boolean[size][size];  // tiene traccia delle celle selezionate

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
                cell.setOpaque(true);
                // Listener per la selezione delle celle
                int finalR = r;
                int finalC = c;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selected[finalR][finalC] = !selected[finalR][finalC];
                        cell.setBackground(selected[finalR][finalC] ? Color.ORANGE : null);
                    }
                });

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

        /** Azione per creare un blocco*/
        createBlockBtn.addActionListener(e -> onAddBlock());

        bottomPanel.add(createBlockBtn);
        bottomPanel.add(solveBtn);

        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    // Metodo per creare un blocco a partire dalle celle selezionate
    private void onAddBlock() {
        List<Cell> cells = new ArrayList<>();

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (selected[r][c]) {
                    cells.add(new Cell(r, c));
                    selected[r][c] = false;
                    cellLabels[r][c].setBackground(null); // reset selezione
                }
            }
        }

        if (cells.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessuna cella selezionata!");
            return;
        }

        String op = JOptionPane.showInputDialog(this, "Operatore (+, -, *, /):");
        if (op == null) return;

        String tgt = JOptionPane.showInputDialog(this, "Risultato del blocco:");
        if (tgt == null) return;

        try {
            int target = Integer.parseInt(tgt);
            Block b = Block.createBlock(op.trim(), target, cells);
            controller.addBlock(b);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Target non valido!");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GridView::new);
    }
}
