package main.java.view;

import main.java.controller.GameController;
import main.java.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class KenKenGridPanel extends JPanel {

    public enum Mode { DEFINE_BLOCKS, INSERT_NUMBERS }

    private final GameController controller;  // Controller logico del gioco
    private final int size;
    private final int cellSize = 100;         // Dimensione di ogni cella

    private final List<Cell> selectedCells = new ArrayList<>(); // Celle attualmente selezionate (per creare blocchi)
    private Mode currentMode = Mode.DEFINE_BLOCKS;              // Modalità corrente (default: definizione blocchi)

    public KenKenGridPanel(GameController controller) {
        this.controller = controller;
        this.size = controller.getGrid().getSize();  // Recupera dimensione della griglia

        setPreferredSize(new Dimension(size * cellSize, size * cellSize));// Imposta dimensione del pannello
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

        /* Sfondo */
        GradientPaint gp = new GradientPaint(
                0, 0, new Color(240, 240, 255),
                0, getHeight(), new Color(220, 220, 250));
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());

        /* Celle già coperte da un blocco → azzurro tenue */
        g2.setColor(new Color(180, 220, 255, 70));
        for (Block b : controller.getGrid().getBlocks())
            for (Cell c : b.getCells())
                g2.fillRect(c.getCol() * cellSize, c.getRow() * cellSize,
                        cellSize, cellSize);

        // Evidenzia celle selezionate in arancione semi-trasparente
        g2.setColor(new Color(255, 140, 0, 120));
        for (Cell c : selectedCells)
            g2.fillRect(c.getCol() * cellSize, c.getRow() * cellSize, cellSize, cellSize);

        /* Linee tratteggiate interne */
        Stroke dotted = new BasicStroke(
                2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1f, new float[]{6f, 6f}, 0f);
        g2.setStroke(dotted);
        g2.setColor(new Color(150, 150, 180));
        for (int i = 1; i < size; i++) {
            int x = i * cellSize;
            int y = i * cellSize;
            g2.drawLine(x, 0, x, size * cellSize);
            g2.drawLine(0, y, size * cellSize, y);
        }

        /* Bordo esterno spesso */
        g2.setStroke(new BasicStroke(4f));
        g2.setColor(Color.DARK_GRAY);
        g2.drawRect(0, 0, size * cellSize, size * cellSize);

        /*  Numeri inseriti dall'utente / solver */
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.setColor(new Color(40, 40, 60));
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++) {
                int val = controller.getGrid().getValue(r, c);
                if (val != 0) {
                    String s = String.valueOf(val);
                    int xx = c * cellSize + cellSize / 2 - 8;
                    int yy = r * cellSize + cellSize / 2 + 8;
                    g2.drawString(s, xx, yy);
                }
            }

        /*Bordi dei blocchi */
        g2.setStroke(new BasicStroke(3.5f));
        g2.setColor(new Color(30, 30, 50));
        for (Block b : controller.getGrid().getBlocks())
            drawBlockBorders(g2, b);

        /* Label target+operatore */
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        for (Block b : controller.getGrid().getBlocks())
            drawBlockLabel(g2, b);
    }
    /* =================== helper per disegno blocchi =================== */

    private void drawBlockBorders(Graphics2D g2, Block block) {
        for (Cell cell : block.getCells()) {
            int x = cell.getCol() * cellSize;
            int y = cell.getRow() * cellSize;

            Cell right = new Cell(cell.getRow(), cell.getCol() + 1);
            if (!isSameBlock(cell, right))
                g2.drawLine(x + cellSize, y, x + cellSize, y + cellSize);

            Cell down = new Cell(cell.getRow() + 1, cell.getCol());
            if (!isSameBlock(cell, down))
                g2.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
        }
    }

    private boolean isSameBlock(Cell c1, Cell c2) {
        if (c2.getRow() < 0 || c2.getRow() >= size || c2.getCol() < 0 || c2.getCol() >= size)
            return false;
        for (Block b : controller.getGrid().getBlocks())
            if (b.getCells().contains(c1) && b.getCells().contains(c2))
                return true;
        return false;
    }

    private void drawBlockLabel(Graphics2D g2, Block block) {
        Cell tl = getTopLeftCell(block.getCells());
        if (tl == null) return;
        int x = tl.getCol() * cellSize + 5;
        int y = tl.getRow() * cellSize + 18;
        g2.drawString(block.getTarget() + getOp(block), x, y);
    }

    private Cell getTopLeftCell(List<Cell> cells) {
        return cells.stream().min((a, b) ->
                        (a.getRow() != b.getRow())
                                ? Integer.compare(a.getRow(), b.getRow())
                                : Integer.compare(a.getCol(), b.getCol()))
                .orElse(null);
    }

    private String getOp(Block b) {
        if (b instanceof SumBlock) return "+";
        if (b instanceof SubBlock) return "-";
        if (b instanceof MulBlock) return "×";
        if (b instanceof DivBlock) return "÷";
        return "";
    }
}
