package main.java.factory;

import java.util.Map;
import java.util.HashMap;

/** Registry che restituisce la factory corretta */
public final class BlockFactoryManager {

    private static final Map<String, BlockFactory> factories = new HashMap<>();

    /* registrazione di default  */
    static {
        factories.put("+", new SumBlockFactory());
        factories.put("-", new SubBlockFactory());
        factories.put("*", new MulBlockFactory());
        factories.put("/", new DivBlockFactory());
    }

    private BlockFactoryManager() {}   // utility class

    public static BlockFactory getFactory(String operator) {
        BlockFactory factory = factories.get(operator);
        if (factory == null) {
            throw new IllegalArgumentException("Operatore non supportato: " + operator);
        }
        return factory;
    }

    // Metodo per registrare dinamicamente nuove factory
    public static void register(String operator, BlockFactory factory) {
        if (factories.containsKey(operator)) { // metodo sicuro , per non sovrascrivere operazioi per sbaglio
            throw new IllegalArgumentException("Operatore già registrato: " + operator);
        }
        factories.put(operator, factory);
    }

    // Verifica se esiste già una factory per questo operatore
    public static boolean isRegistered(String operator) {
        return factories.containsKey(operator);
    }
}