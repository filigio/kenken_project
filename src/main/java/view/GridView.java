package main.java.view;

import main.java.controller.GameController;
import main.java.model.Block;
import main.java.model.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GridView extends JFrame {

    private GameController controller;
    private KenKenGridPanel gridPanel;
    private int size;

    public GridView() {
        super("KenKen GUI con Pannello Laterale"); // Titolo della finestra

        // Selezione della dimensione iniziale della griglia tramite dialogo
        Integer[] dims = {3, 4, 5, 6};
        Integer sel = (Integer) JOptionPane.showInputDialog(
                this,
                "Scegli la dimensione della griglia:",
                "Dimensione",
                JOptionPane.PLAIN_MESSAGE,
                null,
                dims,
                3); // valore predefinito

        if (sel == null) System.exit(0); // Se annulla, chiude il programma

        this.size = sel; // Imposta la dimensione scelta
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
        p.add(solve);p.add(Box.createVerticalStrut(10));

        /** Reset*/
        // Pulsante per resettare solo i valori (senza rimuovere blocchi)
        JButton resetVals = new JButton("Reset (solo valori)");
        resetVals.addActionListener(e -> {
            controller.resetGrid();      // Azzera solo i valori numerici
            gridPanel.repaint();         // Ridisegna la griglia aggiornata
        });
        p.add(resetVals); // Aggiunge il pulsante al pannello
        p.add(Box.createVerticalStrut(10)); // Spazio verticale

        // Pulsante per resettare tutto (valori + blocchi)
        JButton resetAll = new JButton("Reset Tutto");
        resetAll.addActionListener(e -> {
            controller.resetGrid();                          // Azzera i valori
            controller.getGrid().getBlocks().clear();        // Rimuove tutti i blocchi
            gridPanel.repaint();                             // Aggiorna visivamente
        });
        p.add(resetAll);
        p.add(Box.createVerticalStrut(10));

        /** Save e Load*/
        JButton save = new JButton("Save");
        save.addActionListener(e -> onSave());
        JButton load = new JButton("Load");
        load.addActionListener(e -> onLoad());
        p.add(save); p.add(Box.createVerticalStrut(10));
        p.add(load);
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

    /** Metodo che gestisce il salvataggio della partita su file
     Permette all’utente di salvare su file .ser (serializzato) e ricaricarlo */
    private void onSave() {
        JFileChooser fc = new JFileChooser(); // Crea selettore file
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                controller.saveGrid(fc.getSelectedFile()); // Salva la griglia nel file scelto
                JOptionPane.showMessageDialog(this, "Partita salvata con successo!");
            } catch (Exception ex) {
                // Mostra errore se qualcosa va storto
                JOptionPane.showMessageDialog(this, "Errore durante il salvataggio: " + ex.getMessage());
            }
        }
    }

    // Metodo che gestisce il caricamento della partita da file
    private void onLoad() {
        JFileChooser fc = new JFileChooser(); // Crea selettore file
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                controller.loadGrid(fc.getSelectedFile()); // Carica la griglia dal file scelto
                gridPanel.repaint(); // Ridisegna la griglia aggiornata
                JOptionPane.showMessageDialog(this, "Partita caricata con successo!");
            } catch (Exception ex) {
                // Mostra errore se qualcosa va storto
                JOptionPane.showMessageDialog(this, "Errore durante il caricamento: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GridView::new);
    }
}