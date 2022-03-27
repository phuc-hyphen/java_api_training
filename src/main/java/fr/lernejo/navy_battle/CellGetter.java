package fr.lernejo.navy_battle;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class CellGetter {

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

    public Cell GetCellStander(Map<Cell, ResponseMessageFire> map, List<Cell> fired) {
        if (!map.isEmpty()) {
            Cell lastCell = GetLastHitCell(map);
            if (lastCell != null) {
                Cell nextCell = new Cell(lastCell.col(), lastCell.row() + 1);
                if (!fired.contains(nextCell) && 0 < nextCell.row() && nextCell.row() < 9) {
                    fired.add(nextCell);
                    return nextCell;
                }
            }
        }
        return GetRandomCell(fired);
    }
}
