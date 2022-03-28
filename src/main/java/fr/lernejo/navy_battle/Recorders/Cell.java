package fr.lernejo.navy_battle.Recorders;

import java.util.List;

public record Cell(int col, int row) {
    public Cell getCell(Cell originCell, List<Cell> fired, int col_shift, int row_shift) {
        Cell nextCell = new Cell(originCell.col() + col_shift, originCell.row() + row_shift);
        if (!fired.contains(nextCell) && 0 <= nextCell.row() && nextCell.row() <= 9 && 0 <= nextCell.col() && nextCell.col() <= 9) {
            fired.add(nextCell);
            return nextCell;
        }
        return null;
    }
}
