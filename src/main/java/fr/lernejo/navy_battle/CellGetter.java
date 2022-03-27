package fr.lernejo.navy_battle;

import java.util.*;

public class CellGetter {
    final List<Cell> goodPositions = new ArrayList<Cell>();

    public void AddGoodPositions(Map<Cell, Boolean> ship1, Map<Cell, Boolean> ship3, Map<Cell, Boolean> ship2, Map<Cell, Boolean> ship4, Map<Cell, Boolean> ship5) {
        for (Map.Entry<Cell, Boolean> it : ship1.entrySet()) {
            goodPositions.add(it.getKey());
        }
        for (Map.Entry<Cell, Boolean> it : ship2.entrySet()) {
            goodPositions.add(it.getKey());
        }
        for (Map.Entry<Cell, Boolean> it : ship3.entrySet()) {
            goodPositions.add(it.getKey());
        }
        for (Map.Entry<Cell, Boolean> it : ship4.entrySet()) {
            goodPositions.add(it.getKey());
        }
        for (Map.Entry<Cell, Boolean> it : ship5.entrySet()) {
            goodPositions.add(it.getKey());
        }
    }

    public Cell GetRandomCell(List<Cell> fired) { // get random cell
        Random rand = new Random();
        int x, y;
        Cell cell;
        do {
            x = rand.nextInt(0, 10);
            y = rand.nextInt(0, 10);
            cell = new Cell(x, y);
        } while (fired.contains(cell) || x < 0 || y < 0);
        fired.add(cell);
        return cell;
    }

    public Cell GetLastHitCell(Map<Cell, ResponseMessageFire> map) {
        Cell cell = null;
        if (!map.isEmpty()) {
            for (Map.Entry<Cell, ResponseMessageFire> it : map.entrySet()) {
                if (Objects.equals(it.getValue().consequence(), "hit")) {
                    cell = it.getKey();
                }
            }
        }
        return cell;
    }

    public Cell GetCellStanderWay(Map<Cell, ResponseMessageFire> map, List<Cell> fired) {
        if (!map.isEmpty()) {
            Cell lastCell = GetLastHitCell(map);
            if (lastCell != null) {
                Cell nextCell = GetNearbyCell(lastCell, fired);
                if (nextCell != null) {
                    return nextCell;
                }
            }
        }
        return GetRandomCell(fired);
    }

    public Cell GetNearbyCell(Cell originCell, List<Cell> fired) {
        Cell nextCellDown = getCell(originCell, fired, 0, 1);
        if (nextCellDown != null) return nextCellDown;
        Cell nextCellUp = getCell(originCell, fired, 0, -1);
        if (nextCellUp != null) return nextCellUp;
        Cell nextCellRight = getCell(originCell, fired, 1, 0);
        if (nextCellRight != null) return nextCellRight;
        return getCell(originCell, fired, -1, 0);
//        return null;
    }

    private Cell getCell(Cell originCell, List<Cell> fired, int col_shift, int row_shift) {
        Cell nextCell = new Cell(originCell.col() + col_shift, originCell.row() + row_shift);
        if (!fired.contains(nextCell) && 0 < nextCell.row() && nextCell.row() < 9) {
            fired.add(nextCell);
            return nextCell;
        }
        return null;
    }

    public Cell GetCellTactic(List<Cell> fired) {
        for (Cell position : goodPositions) {
            if (!fired.contains(position)) {
                fired.add(position);
                return position;
            }
        }
        return GetRandomCell(fired);
    }

}
