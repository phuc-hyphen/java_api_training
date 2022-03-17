package fr.lernejo.navy_battle;

import java.util.Map;

public record Ship(Cell startCell, int size, String orientation) {

    public void AddLocations(Map<Cell, Boolean> sea) {
        for (int i = 0; i < size; i++) {

            if (orientation.equals("Vertical")) {
                Cell cell = new Cell(startCell.x(), startCell.y() + i);
                sea.put(cell, false);
            } else if (orientation.equals("Horizontal")) {
                Cell cell = new Cell(startCell.x() + i, startCell.y());
                sea.put(cell, false);
            }
        }
    }
}
