package main.java.factory;

import main.java.model.Block;
import main.java.model.Cell;
import main.java.model.DivBlock;
import java.util.List;

public class DivBlockFactory implements BlockFactory {
    @Override
    public Block createBlock(int target, List<Cell> cells) {
        return new DivBlock(target, cells);
    }
}
