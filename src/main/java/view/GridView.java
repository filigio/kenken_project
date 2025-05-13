package main.java.view;

import javax.swing.*;
import java.awt.*;

public class GridView extends JFrame {

    private final int size = 3;
    private final JLabel[][] cellLabels = new JLabel[size][size];

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

        JButton solveBtn = new JButton("Solve");
        // Nessun actionListener ancora

        add(gridPanel, BorderLayout.CENTER);
        add(solveBtn, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GridView::new);
    }
}
