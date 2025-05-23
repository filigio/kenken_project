package main.java.factory;

import main.java.model.Block;
import main.java.model.Cell;
import java.util.List;

/** Creator astratto del Factory Method */
public interface BlockFactory {
    Block createBlock(int target, List<Cell> cells);
}
