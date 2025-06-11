package main.java.view;

import main.java.controller.GameController;
import main.java.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class KenKenGridPanel extends JPanel {

    // ====== COSTANTI GRAFICHE ======
    private static final Color BACKGROUND_COLOR = new Color(220, 220, 255);    // Colore dello sfondo della griglia

    private static final Color COVERED_CELL_COLOR = new Color(180, 220, 255, 70);  // Celle coperte da blocchi definiti
    private static final Color SELECTED_CELL_COLOR = new Color(255, 140, 0, 120);    // Celle attualmente selezionate
    private static final Color GRID_LINE_COLOR = new Color(150, 150, 180);    // Linee tratteggiate della griglia
    private static final Color DIGIT_COLOR = new Color(40, 40, 60);    // Colore dei numeri inseriti
    private static final Color BLOCK_BORDER_COLOR = new Color(30, 30, 50);    // Bordo dei blocchi KenKen

    private static final float DASHED_LINE_THICKNESS = 2f;// Spessore delle linee tratteggiate (griglia)
    private static final float OUTER_BORDER_THICKNESS = 4f; // Spessore del bordo esterno della griglia
    private static final float BLOCK_BORDER_THICKNESS = 3.5f;// Spessore del bordo tra blocchi
    private static final float[] DASH_PATTERN = {6f, 6f};    // Pattern tratteggio per griglia: linea 6px, spazio 6px

    private static final Font DIGIT_FONT = new Font("SansSerif", Font.BOLD, 24);    // Font per i numeri dentro la griglia
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 16);    // Font per l’etichetta target+operatore nei blocchi


    public enum Mode { DEFINE_BLOCKS, INSERT_NUMBERS } // Modalità griglia: definizione blocchi o inserimento numeri

    private final GameController controller;  // Controller logico del gioco
    private final int size;
    private final int cellSize;         // Dimensione di ogni cella

    private final List<Cell> selectedCells = new ArrayList<>(); // Celle attualmente selezionate (per creare blocchi)
    private Mode currentMode = Mode.DEFINE_BLOCKS;              // Modalità corrente (default: definizione blocchi)


    private Supplier<Boolean> liveCheckSupplier = () -> true;  // campo per abilitare/disabilitare il live-check

    /** Imposta una funzione che indica se il controllo automatico della griglia (live check) è attivo.
     *  Usato per sapere dinamicamente, se il checkbox "Live Check" è selezionata, senza dover accedere direttamente alla checkbox. */
    public void setLiveCheckSupplier(Supplier<Boolean> supplier) {
        this.liveCheckSupplier = supplier;
    }   // memorizzo il Supplier per usarlo al volo

    public KenKenGridPanel(GameController controller) {
        this.controller = controller;
        this.size = controller.getGrid().getSize();  // Recupera dimensione della griglia
        this.cellSize = Math.max(50, 600 / size); // adatta la dimensione in base alla grid size

        setPreferredSize(new Dimension(size * cellSize, size * cellSize));// Imposta dimensione del pannello

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
                            if (v < 0 || v > size)
                                JOptionPane.showMessageDialog(KenKenGridPanel.this,
                                        "Valore non valido (0‥" + size + ")");
                            controller.setGridValue(row, col, v); // Imposta il valore nella griglia
                            if (liveCheckSupplier.get()) {                                         // se la checkbox è selezionata
                                String err = controller.validateCurrentGrid();                            // chiedo al controller di validare la griglia
                                if (err != null) {
                                    JOptionPane.showMessageDialog(  // mostro un warning se c’è un vincolo violato
                                            KenKenGridPanel.this,
                                            err,                      // messaggio di errore restituito dal controller
                                            "Vincolo violato",
                                            JOptionPane.WARNING_MESSAGE      // icona di warning
                                    );
                                }
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

    /** Preleva le celle selezionate per creare un blocco logico
     * e resetta la selezione per preparare la griglia al blocco successivo. */
    public List<Cell> consumeSelectedCells() { //restituisce la lista delle celle selezionate che creeranno il blocco
        List<Cell> res = new ArrayList<>(selectedCells); // Copia la lista delle celle selezionate
        selectedCells.clear(); // Svuota la selezione originale
        repaint(); // Ridisegna
        return res; // Ritorna la copia
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Chiama il metodo di disegno base [Pulisce il pannello prima di disegnare (fondamentale per evitare sovrapposizioni)]
        Graphics2D g2 = (Graphics2D) g;

        /* Sfondo */
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(0, 0, getWidth(), getHeight()); // Riempie l'intero pannello con quel colore

        /* Celle già coperte da un blocco → azzurro tenue */
        g2.setColor(COVERED_CELL_COLOR);// Colore trasparente per indicare visivamente le celle appartenenti a blocchi già definiti
        for (Block b : controller.getGrid().getBlocks())  // per ogni blocco
            for (Cell c : b.getCells()) // e per ogni cella
                g2.fillRect(c.getCol() * cellSize, c.getRow() * cellSize, // Riempie la cella corrispondente
                        cellSize, cellSize);

        // Evidenzia celle selezionate in arancione semi-trasparente
        g2.setColor(SELECTED_CELL_COLOR);
        for (Cell c : selectedCells)
            g2.fillRect(c.getCol() * cellSize, c.getRow() * cellSize, cellSize, cellSize);

        /* Linee tratteggiate interne */
        g2.setStroke(new BasicStroke(DASHED_LINE_THICKNESS, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, DASH_PATTERN, 0f));
        g2.setColor(GRID_LINE_COLOR);
        for (int i = 1; i < size; i++) {
            int x = i * cellSize;
            int y = i * cellSize;
            g2.drawLine(x, 0, x, size * cellSize);
            g2.drawLine(0, y, size * cellSize, y);
        }

        /* Bordo esterno spesso */
        g2.setStroke(new BasicStroke(OUTER_BORDER_THICKNESS));
        g2.setColor(Color.DARK_GRAY);
        g2.drawRect(0, 0, size * cellSize, size * cellSize);

        /*  Numeri inseriti dall'utente / solver */
        g2.setFont(DIGIT_FONT);
        g2.setColor(DIGIT_COLOR);
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
        g2.setStroke(new BasicStroke(BLOCK_BORDER_THICKNESS));
        g2.setColor(BLOCK_BORDER_COLOR);
        for (Block b : controller.getGrid().getBlocks())
            drawBlockBorders(g2, b);

        /* Label target+operatore */
        g2.setFont(LABEL_FONT);
        for (Block b : controller.getGrid().getBlocks())
            drawBlockLabel(g2, b);
    }
    /* =================== helper per disegno blocchi =================== */

    /** disegna i bordi dei blocchi sulla griglia, solo dove una cella confina con una cella esterna al blocco. */
    private void drawBlockBorders(Graphics2D g2, Block block) {
        for (Cell cell : block.getCells()) { // Per ogni cella del blocco da disegnare
            int x = cell.getCol() * cellSize; // Calcola la posizione X in pixel della cella
            int y = cell.getRow() * cellSize; // Calcola la posizione Y in pixel della cella

            // Controlla cella a destra
            Cell right = new Cell(cell.getRow(), cell.getCol() + 1);
            if (!isSameBlock(cell, right)) // Se non fa parte dello stesso blocco
                g2.drawLine(x + cellSize, y, x + cellSize, y + cellSize); // Disegna bordo destro

            // Controlla cella sotto
            Cell down = new Cell(cell.getRow() + 1, cell.getCol());
            if (!isSameBlock(cell, down))
                g2.drawLine(x, y + cellSize, x + cellSize, y + cellSize); // Disegna bordo inferiore

            // Controlla cella a sinistra
            Cell left = new Cell(cell.getRow(), cell.getCol() - 1);
            if (!isSameBlock(cell, left))
                g2.drawLine(x, y, x, y + cellSize); // Disegna bordo sinistro

            // Controlla cella sopra
            Cell up = new Cell(cell.getRow() - 1, cell.getCol());
            if (!isSameBlock(cell, up))
                g2.drawLine(x, y, x + cellSize, y); // Disegna bordo superiore
        }
    }

    /** Controlla se due celle fanno parte dello stesso blocco */
    private boolean isSameBlock(Cell c1, Cell c2) {
        if (c2.getRow() < 0 || c2.getRow() >= size || c2.getCol() < 0 || c2.getCol() >= size)
            return false;
        for (Block b : controller.getGrid().getBlocks())
            if (b.getCells().contains(c1) && b.getCells().contains(c2))
                return true;
        return false;
    }

    /** Disegna l’etichetta del blocco */
    private void drawBlockLabel(Graphics2D g2, Block block) {
        Cell tl = getTopLeftCell(block.getCells());
        if (tl == null) return;
        int x = tl.getCol() * cellSize + 5;
        int y = tl.getRow() * cellSize + 18;
        g2.drawString(block.getTarget() + getOp(block), x, y);
    }

    /** Trova la cella più in alto a sinistra tra quelle del blocco. */
    private Cell getTopLeftCell(List<Cell> cells) {
        return cells.stream().min((a, b) ->
                        (a.getRow() != b.getRow())
                                ? Integer.compare(a.getRow(), b.getRow()) // Confronta riga
                                : Integer.compare(a.getCol(), b.getCol())) // Se stessa riga, confronta colonna
                .orElse(null); // Se lista vuota, ritorna null
    }

    /** Restituisce il simbolo dell’operazione usata nel blocco */
    private String getOp(Block b) {
        if (b instanceof SumBlock) return "+";
        if (b instanceof SubBlock) return "-";
        if (b instanceof MulBlock) return "×";
        if (b instanceof DivBlock) return "÷";
        return "";
    }
}
