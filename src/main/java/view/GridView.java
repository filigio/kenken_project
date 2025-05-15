package main.java.view;

import main.java.controller.GameController;
import main.java.model.Block;
import main.java.model.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GridView extends JFrame {

    private final GameController controller;
    private final KenKenGridPanel gridPanel;
    private final int size;

    public GridView() {
        super("KenKen GUI con Pannello Laterale"); // Titolo della finestra

        this.size = 3; // Dimensione fissa iniziale della griglia [da implementare successivmante in modo tale da poter scegliere la grandezza]
        this.controller = new GameController(size); // Inizializza il controller con la dimensione

        setDefaultCloseOperation(EXIT_ON_CLOSE); // Chiude il programma alla chiusura della finestra
        setLayout(new BorderLayout()); // Layout principale

        gridPanel = new KenKenGridPanel(controller); // Inizializza il pannello della griglia

        add(gridPanel, BorderLayout.CENTER); // Aggiunge la griglia al centro
        add(buildSidePanel(), BorderLayout.EAST); // Aggiunge il pannello laterale a destra

        pack(); // Dimensionamento automatico della finestra
        setLocationRelativeTo(null); // Centra la finestra sullo schermo
        setVisible(true); // Rende visibile la GUI
    }

    /* ======================= pannello laterale ====================== */

    private JPanel buildSidePanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createTitledBorder("Operazioni"));

        /* modalità */
        // Permette di cambiare modalità
        JRadioButton rbDef = new JRadioButton("Definisci blocchi", true);
        JRadioButton rbIns = new JRadioButton("Inserisci numeri");

        ButtonGroup group = new ButtonGroup(); // Raggruppa i radio button
        group.add(rbDef);
        group.add(rbIns);
        // Gestione cambio modalità
        rbDef.addActionListener(e -> gridPanel.setMode(KenKenGridPanel.Mode.DEFINE_BLOCKS));
        rbIns.addActionListener(e -> gridPanel.setMode(KenKenGridPanel.Mode.INSERT_NUMBERS));

        p.add(rbDef);
        p.add(rbIns);
        p.add(Box.createVerticalStrut(10));

        /** Aggiungi blocco*/
        JButton addBlock = new JButton("Aggiungi Blocco");
        addBlock.addActionListener(e -> onAddBlock());
        p.add(addBlock);
        p.add(Box.createVerticalStrut(10));
        /** solve → inserisce sempre la prima soluzione */
        JButton solve = new JButton("Solve");
        solve.addActionListener(e -> onSolve());
        p.add(solve);

        return p;
    }

    private void onAddBlock() {
        List<Cell> cells = gridPanel.consumeSelectedCells(); // Ottiene e svuota selezione
        if (cells.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessuna cella selezionata!");
            return;
        }

        // Chiede all’utente l’operatore e il target
        String op = JOptionPane.showInputDialog(this, "Operatore (+, -, *, /):");
        if (op == null) return;

        String tgt = JOptionPane.showInputDialog(this, "Risultato del blocco:");
        if (tgt == null) return;

        try {
            int target = Integer.parseInt(tgt); // Converte il target
            Block b = Block.createBlock(op.trim(), target, cells); // Crea il blocco
            controller.addBlock(b); // Aggiunge alla griglia
            gridPanel.repaint(); // Ridisegna la griglia
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Target non valido!");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }

    /** Inserisce la prima soluzione se esiste, altrimenti mostra un messaggio. */
    private void onSolve() {
        var solutions = controller.solvePuzzle(); // Risolve la griglia
        if (solutions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessuna soluzione trovata.");
            return;
        }

        // Mostra la prima soluzione trovata
        int[][] sol = solutions.get(0);
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                controller.setGridValue(r, c, sol[r][c]); // Applica la soluzione

        gridPanel.repaint(); // Ridisegna con la soluzione
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(GridView::new);
    }
}
