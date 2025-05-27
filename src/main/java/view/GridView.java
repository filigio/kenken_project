package main.java.view;

import main.java.controller.GameController;
import main.java.factory.BlockFactoryManager;
import main.java.model.Block;
import main.java.model.Cell;
import main.java.model.Grid;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
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

        JLabel statusLabel = new JLabel("Griglia pronta");    // Etichetta che mostra lo stato
        add(statusLabel, BorderLayout.NORTH);                 // Aggiungila nella parte superiore della GUI

        controller.addObserver(new GridStatusObserver(controller, statusLabel, gridPanel));
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

        /** Change Size*/
        // Pulsante per cambiare la dimensione della griglia
        JButton changeSize = new JButton("Cambia dimensione");
        changeSize.addActionListener(e -> {// Azione associata al clic sul pulsante
            dispose();      // Chiude la finestra attuale (rimuove tutto)
            new GridView(); // Crea una nuova GUI KenKen, partendo da zero (chiede nuova dimensione)
        });
        p.add(changeSize);
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

    /**
     * Metodo chiamato quando l'utente clicca su "Aggiungi Blocco".
     * Permette di creare un blocco matematico (vincolo) sulle celle selezionate.
     */
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
            Block b = BlockFactoryManager.getFactory(op.trim())
                    .createBlock(target, cells);   // Crea il blocco
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

    private void onLoad() {
        JFileChooser fc = new JFileChooser();// Crea selettore file
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                // Carica la griglia da file
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()));
                Grid loadedGrid = (Grid) ois.readObject(); //prendendo un oggetto Grid salvato precedentemente su file

                // Recupera la dimensione della griglia caricata
                int newSize = loadedGrid.getSize();

                // Chiedi conferma se la dimensione è diversa da quella attuale
                if (newSize != size) {
                    int ans = JOptionPane.showConfirmDialog(this,
                            "La griglia salvata è di dimensione " + newSize + "x" + newSize +
                                    ". Vuoi caricarla comunque?",
                            "Cambio dimensione",
                            JOptionPane.YES_NO_OPTION);
                    if (ans != JOptionPane.YES_OPTION) return;
                }

                // Crea un nuovo controller con la griglia caricata
                GameController newController = new GameController(newSize);
                newController.getGrid().setValues(loadedGrid.getValuesCopy());
                newController.getGrid().getBlocks().addAll(loadedGrid.getBlocks());

                // Ricostruisci dinamicamente la GUI
                reloadFromController(newController);
                JOptionPane.showMessageDialog(this, "Partita caricata con successo!");
            } catch (Exception ex) {
                // Mostra errore se qualcosa va storto
                JOptionPane.showMessageDialog(this, "Errore durante il caricamento: " + ex.getMessage());
            }
        }
    }
    /**
     * Ricrea dinamicamente controller e griglia dopo il caricamento.
     */

    private void reloadFromController(GameController newController) {
        this.controller = newController; //  Sostituisce il vecchio GameController con quello caricato da file
        this.size = newController.getGrid().getSize(); //Aggiorna la dimensione interna (size) in base alla griglia appena caricata

        if (gridPanel != null) {
            remove(gridPanel); // Rimuove il vecchio pannello griglia
        }

        this.gridPanel = new KenKenGridPanel(controller); // Crea nuova griglia
        add(gridPanel, BorderLayout.CENTER);              // Aggiunge la nuova griglia

        revalidate(); // Aggiorna il layout della finestra
        repaint();    // Ridisegna visivamente la GUI
        pack();       // Ridimensiona la finestra alla nuova griglia
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GridView::new);
    }
}