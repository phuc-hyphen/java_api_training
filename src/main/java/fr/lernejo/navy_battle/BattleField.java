package fr.lernejo.navy_battle;

import java.util.*;


public class BattleField {
    final Map<Cell, Boolean> porteAvionMap = new HashMap<Cell, Boolean>();
    final Map<Cell, Boolean> croiseurMap = new HashMap<Cell, Boolean>();
    final Map<Cell, Boolean> torpilleurMap = new HashMap<Cell, Boolean>();
    final Map<Cell, Boolean> contreTorpilleurMap = new HashMap<Cell, Boolean>();
    final Map<Cell, Boolean> contreTorpilleur2Map = new HashMap<Cell, Boolean>();
    final List<Cell> fired = new ArrayList<Cell>();
    final List<Cell> received = new ArrayList<Cell>();
    final List<Map<Cell, Boolean>> sunkShips = new ArrayList<Map<Cell, Boolean>>();

    public void InitialSea() {
        Ship porteAvion = new Ship(new Cell(1, 3), 5, "Vertical");
        porteAvion.AddLocations(porteAvionMap);
        Ship croiseur = new Ship(new Cell(1, 1), 4, "Vertical");
        croiseur.AddLocations(croiseurMap);
        Ship torpilleur = new Ship(new Cell(8, 8), 2, "Vertical");
        torpilleur.AddLocations(torpilleurMap);
        Ship contreTorpilleur = new Ship(new Cell(0, 9), 3, "Horizontal");
        contreTorpilleur.AddLocations(contreTorpilleurMap);
        Ship contreTorpilleur2 = new Ship(new Cell(5, 5), 3, "Horizontal");
        contreTorpilleur2.AddLocations(contreTorpilleur2Map);
    }

    public boolean ShipLeft() {
        return sunkShips.size() != 5;
//        return !porteAvionMap.containsValue(false) && !croiseurMap.containsValue(false) && !torpilleurMap.containsValue(false)
//            && !contreTorpilleurMap.containsValue(false) && !contreTorpilleur2Map.containsValue(false);
    }

    public Cell RandomShot() { // get random cell
        Random rand = new Random();
        Cell cell;
        do {
            int x = rand.nextInt(10);
            int y = rand.nextInt(10);
            cell = new Cell(x, y);
        } while (fired.contains(cell));
        fired.add(cell);
        return cell;
    }

    public boolean HitCheck(Cell cell) { // check if the cell hit any ship
        return IfCellHit(cell, porteAvionMap) || IfCellHit(cell, croiseurMap) || IfCellHit(cell, torpilleurMap) ||
            IfCellHit(cell, contreTorpilleurMap) || IfCellHit(cell, contreTorpilleur2Map);
    }

    private boolean IfCellHit(Cell cell, Map<Cell, Boolean> ship) { // check if the cell hit the ship and get the received cell
        boolean check = false;
        if (ship.containsKey(cell) && !received.contains(cell)) {
            ship.replace(cell, false, true);
            received.add(cell);
            check = true;
        }
        return check;
    }

    public boolean SunkCheck() { // check if any ship is sunk
        return IfShipSunk(porteAvionMap) || IfShipSunk(croiseurMap) ||
            IfShipSunk(torpilleurMap) || IfShipSunk(contreTorpilleurMap) || IfShipSunk(contreTorpilleur2Map);
    }

    private boolean IfShipSunk(Map<Cell, Boolean> ship) { // check if the ship is sunk
        boolean sunk = false;
        if (!sunkShips.contains(ship) && !ship.containsValue(false)) {
            sunkShips.add(ship);
            sunk = true;
        }
        return sunk;
    }
}