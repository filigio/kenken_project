package main.java.factory;

import main.java.model.Block;
import main.java.model.Cell;
import main.java.model.SumBlock;
import java.util.List;

/** ConcreteCreator per '+' */
public class SumBlockFactory implements BlockFactory {
    @Override
    public Block createBlock(int target, List<Cell> cells) {
        return new SumBlock(target, cells);
    }
}
