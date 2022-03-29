package fr.lernejo.navy_battle.Recorders;

import java.util.List;

public record Cell(int col, int row) {
    private Cell getCell(List<Cell> fired, int col_shift, int row_shift) {
        Cell nextCell = new Cell(col + col_shift, row + row_shift);
        if (!fired.contains(nextCell) && 0 <= nextCell.row() && nextCell.row() <= 9 && 0 <= nextCell.col() && nextCell.col() <= 9) {
            fired.add(nextCell);
            return nextCell;
        }
        return null;
    }

    public Cell LeftCell(List<Cell> fired) {
        return getCell(fired, -1, 0);
    }

    public Cell RightCell(List<Cell> fired) {
        return getCell(fired, 1, 0);
    }

    public Cell UpCell(List<Cell> fired) {
        return getCell(fired, 0, -1);
    }

    public Cell DownCell(List<Cell> fired) {
        return getCell(fired, 0, 1);
    }

    public Cell GetNearbyCell(List<Cell> fired) {
        Cell nextCellDown = DownCell(fired);
        if (nextCellDown != null) return nextCellDown;
        Cell nextCellRight = RightCell(fired);
        if (nextCellRight != null) return nextCellRight;
        Cell nextCellUp = UpCell(fired);
        if (nextCellUp != null) return nextCellUp;
        return LeftCell(fired);
    }

}
