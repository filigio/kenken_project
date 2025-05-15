package main.java.view;

import main.java.controller.GameController;
import main.java.model.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class KenKenGridPanel extends JPanel {

    public enum Mode { DEFINE_BLOCKS, INSERT_NUMBERS }

    private final GameController controller;
    private final int size;
    private final int cellSize = 60;

    private final List<Cell> selectedCells = new ArrayList<>();
    private Mode currentMode = Mode.DEFINE_BLOCKS;

    public KenKenGridPanel(GameController controller) {
        this.controller = controller;
        this.size = controller.getGrid().getSize();

        setPreferredSize(new Dimension(size * cellSize, size * cellSize));
        setBackground(Color.WHITE);

        /** Listener per gestire i clic del mouse all'interno del pannello */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Determina la cella cliccata traducendo le coordinate del click. Questa scelta semplifica il mapping diretto tra clic utente e celle logiche.
                int col = e.getX() / cellSize; // Calcola la colonna cliccata
                int row = e.getY() / cellSize; // Calcola la riga cliccata
                if (row < 0 || row >= size || col < 0 || col >= size) return; // Ignora clic fuori dalla griglia

                Cell clicked = new Cell(row, col); // Crea un oggetto Cell con le coordinate cliccate

                if (currentMode == Mode.DEFINE_BLOCKS) { // Se in modalità definizione blocchi
                    //Permette la selezione multipla, e la deselezione con un secondo clic
                    if (selectedCells.contains(clicked)) {
                        selectedCells.remove(clicked); // Deseleziona se già selezionata
                    } else {
                        selectedCells.add(clicked); // Altrimenti aggiunge alla selezione
                    }
                } else { // Se in modalità inserimento numeri
                    String valStr = JOptionPane.showInputDialog(
                            KenKenGridPanel.this,
                            "Inserisci un numero (1.." + size + ") oppure 0 per cancellare:");

                    if (valStr != null) {
                        try {
                            int v = Integer.parseInt(valStr); // Converte il valore inserito
                            if (v < 0 || v > size) {
                                JOptionPane.showMessageDialog(KenKenGridPanel.this,
                                        "Valore non valido (0‥" + size + ")");
                            } else {
                                controller.setGridValue(row, col, v); // Imposta il valore nella griglia
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(KenKenGridPanel.this,
                                    "Inserimento non valido!");
                        }
                    }
                    selectedCells.clear(); // Cancella selezione corrente
                }
                // Dopo ogni interazione, il pannello viene ridisegnato.
                repaint(); // Ridisegna il pannello
            }
        });
    }

    public void setMode(Mode m) {
        currentMode = m; // Imposta la modalità corrente
        if (m == Mode.INSERT_NUMBERS) selectedCells.clear(); // Se cambio a inserimento numeri, svuoto selezione blocchi
        repaint(); // Ridisegna la griglia
    }

    public Mode getMode() {
        return currentMode; // Restituisce la modalità attuale
    }

    public List<Cell> consumeSelectedCells() { //restituisce la lista delle celle selezionate che creeranno il blocco
        List<Cell> res = new ArrayList<>(selectedCells); // Copia la lista delle celle selezionate
        selectedCells.clear(); // Svuota la selezione originale
        repaint(); // Ridisegna
        return res; // Ritorna la copia
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Chiama il metodo di disegno base
        Graphics2D g2 = (Graphics2D) g;

        // Evidenzia celle selezionate in arancione semi-trasparente
        g2.setColor(new Color(255, 140, 0, 120));
        for (Cell c : selectedCells)
            g2.fillRect(c.getCol() * cellSize, c.getRow() * cellSize, cellSize, cellSize);

        // Disegna la griglia
        g2.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= size; i++) {
            int coord = i * cellSize;
            g2.drawLine(coord, 0, coord, size * cellSize); // linee verticali
            g2.drawLine(0, coord, size * cellSize, coord); // linee orizzontali
        }

        // Disegna i numeri inseriti
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                int val = controller.getGrid().getValue(r, c); // Ottiene valore corrente della cella
                if (val != 0) {
                    String s = String.valueOf(val);
                    int xx = c * cellSize + cellSize / 2 - 8;
                    int yy = r * cellSize + cellSize / 2 + 8;
                    g2.drawString(s, xx, yy); // Disegna il numero centrato nella cella
                }
            }
        }
    }
}
