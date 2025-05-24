package main.java.factory;

import main.java.model.Block;
import main.java.model.Cell;
import main.java.model.MulBlock;
import java.util.List;

public class MulBlockFactory implements BlockFactory {
    @Override
    public Block createBlock(int target, List<Cell> cells) {
        return new MulBlock(target, cells);
    }
}
