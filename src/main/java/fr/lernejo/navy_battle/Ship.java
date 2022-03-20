package fr.lernejo.navy_battle;

import java.util.Map;

public class Ship {
    private final Cell startCell;
    private final int size;
    private final String orientation;

    public Ship(Cell startCell, int size, String orientation) {
        this.startCell = startCell;
        this.size = size;
        this.orientation = orientation;
    }

    public void AddLocations(Map<Cell, Boolean> sea) {
        for (int i = 0; i < size; i++) {
            if (orientation.equals("Vertical")) {
                Cell cell = new Cell(startCell.col(), startCell.row() + i);
                sea.put(cell, false);
            } else if (orientation.equals("Horizontal")) {
                Cell cell = new Cell(startCell.col() + i, startCell.row());
                sea.put(cell, false);
            }
        }
    }
}
