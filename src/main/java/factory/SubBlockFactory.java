package main.java.factory;

import main.java.model.Block;
import main.java.model.Cell;
import main.java.model.SubBlock;
import java.util.List;

public class SubBlockFactory implements BlockFactory {
    @Override
    public Block createBlock(int target, List<Cell> cells) {
        return new SubBlock(target, cells);
    }
}
