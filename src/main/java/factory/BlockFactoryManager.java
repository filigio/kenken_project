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


}
