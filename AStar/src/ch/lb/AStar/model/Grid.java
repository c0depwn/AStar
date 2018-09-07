/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.lb.AStar.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author LB
 */
public class Grid extends Group {
        
    private final Cell[][] cells;
    private boolean diagonalAllowed = false;

    public Grid(double x, double y, int columns, int rows, int cellSize,  double strokeWidth, boolean diagonalAllowed) {
        
        cells = new Cell[columns][rows];
        
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                
                Cell cell = new Cell(this, x+i*cellSize, y+j*cellSize, cellSize, cellSize, strokeWidth);
                
                cells[i][j] = cell;
                cell.setX(i);
                cell.setY(j);
                
                this.gCost.put(cell, Integer.MAX_VALUE);
                this.fCost.put(cell, Integer.MAX_VALUE);
                
                super.getChildren().add(cell);
            }
        }
        
        this.diagonalAllowed = diagonalAllowed;
        
        findCellNeighbours(columns, rows);
    }
    
    private void findCellNeighbours(int columns, int rows) {
        for (int i=0; i<columns; i++) {
            for (int j=0; j<rows; j++) {
                
                if (i > 0)
                    cells[i][j].putNeighbour(Direction.W, cells[i-1][j]); // LEFT
                if (i < columns-1)
                    cells[i][j].putNeighbour(Direction.E, cells[i+1][j]); // RIGHT
                if (j > 0)
                    cells[i][j].putNeighbour(Direction.N, cells[i][j-1]); // TOP
                if (j < rows-1)
                    cells[i][j].putNeighbour(Direction.S, cells[i][j+1]); // BOTTOM
                
                if (diagonalAllowed) {
                    if (i > 0 && j > 0)
                        cells[i][j].putNeighbour(Direction.NW, cells[i-1][j-1]); // LEFT TOP
                    if ( i > 0 && j < rows-1)
                        cells[i][j].putNeighbour(Direction.SW, cells[i-1][j+1]); // LEFT BOTTOM
                    if (i < columns-1 && j > 0)
                        cells[i][j].putNeighbour(Direction.NE, cells[i+1][j-1]); // RIGHT TOP
                    if (i < columns-1 && j < rows-1)
                        cells[i][j].putNeighbour(Direction.SE, cells[i+1][j+1]); // RIGHT BOTTOM
                }
                
            }
        }
    }
    
    public void setDiagonalAllowed(boolean diag) {
        this.diagonalAllowed = diag;
    }
    
    public void setStart(Cell start) {
        this.start = start;
    }
    
    public Cell getStart() {
        return this.start;
    }
    
    public void setGoal(Cell goal) {
        this.goal = goal;
    }
    
    public Cell getGoal() {
        return this.goal;
    }
    
    private final int H_V_COST = 10;
    private final int D_COST = 14;
    
    private Cell start = null;
    private Cell goal = null;

    ArrayList<Cell> closed = new ArrayList();
    ArrayList<Cell> open = new ArrayList();
    HashMap<Cell, Integer> gCost = new HashMap();
    HashMap<Cell, Integer> fCost = new HashMap();
    HashMap<Cell, Cell> cameFrom = new HashMap();
    
    public void runAStar4D() {
                
        open.add(start);
        gCost.put(start, 0);
        fCost.put(start, calcHeuristicCost4D(start));
                
        while (!open.isEmpty()) {
         
            Cell current = null;
            
            for (Cell c : open) {
                if (current == null) {
                    current = c;
                }
                if (fCost.get(c) < fCost.get(current)) {
                    current = c;
                }
            }    
            
            if (current.isGoal) {
                
                open.forEach((c) -> {
                    if (c != start && c != goal)
                        c.setFill(Color.BROWN); // opened set
                });
        
                closed.forEach((c) -> {
                    if (c != start && c != goal)
                        c.setFill(Color.AQUA); // closed set
                });
                
                for (Cell c : reconstructPath(current)) {
                    if (c != start && c != goal) {
                        c.setFill(Color.PINK);
                    }  
                } 
            }
            
            if (current.isStart) {
                current.setFill(Color.GREEN);
            }
            
            open.remove(current);
            closed.add(current);
            
            for (Cell neighbor : current.getNeighbours().values()) {
                
                if (neighbor.isTraversable() && !closed.contains(neighbor)) {
                    
                    int tentativeGCost = gCost.get(current) + H_V_COST;
                    
                    if (!open.contains(neighbor)) {
                        open.add(neighbor);
                    } else if (tentativeGCost < gCost.get(neighbor)) {
                        break;
                    }
                  
                    cameFrom.put(neighbor, current);
                    gCost.put(neighbor, tentativeGCost);
                    fCost.put(neighbor, gCost.get(neighbor) + calcHeuristicCost4D(neighbor));
                  
                }
            }
        }     
    }
    
    private int calcHeuristicCost4D(Cell cell) {
        int dx = Math.abs(cell.getX() - goal.getX());
        int dy = Math.abs(cell.getY() - goal.getY());
        return H_V_COST * (dx + dy);
    }
    
    public void runAStar8D() {
                
        open.add(start);
        gCost.put(start, 0);
        fCost.put(start, calcHeuristicCost8D(start));
                
        while (!open.isEmpty()) {
         
            Cell current = null;
            
            for (Cell c : open) {
                if (current == null) {
                    current = c;
                }
                if (fCost.get(c) < fCost.get(current)) {
                    current = c;
                }
            }
                       
            if (current.isGoal) {
                
                open.forEach((c) -> {
                    c.setFill(Color.BROWN); // opened set
                });
        
                closed.forEach((c) -> {
                    c.setFill(Color.AQUA); // closed set
                });
                
                for (Cell c : reconstructPath(current)) {
                    
                    if (!c.isStart && !c.isGoal) {
                        c.setFill(Color.PINK);
                    }
                    start.setFill(Color.GREEN);
                    goal.setFill(Color.RED);
                    
                    //System.out.println(c.getX() + " " + c.getY());
                } 
            }
            
            if (current.isStart) {
                current.setFill(Color.GREEN);
            }
            
            open.remove(current);
            closed.add(current);
            
            System.out.println(current.getNeighbours().values());
            
            for (Cell neighbor : current.getNeighbours().values()) {
                
                if (neighbor.isTraversable() && !closed.contains(neighbor)) {
                    
                    int tentativeGCost = gCost.get(current) + calcCost8D(current, neighbor);
                    
                    if (!open.contains(neighbor)) {
                        open.add(neighbor);
                    } else if (tentativeGCost >= gCost.get(neighbor)) {
                        break;
                    }
                  
                    cameFrom.put(neighbor, current);
                    gCost.put(neighbor, tentativeGCost);
                    fCost.put(neighbor, gCost.get(neighbor) + calcHeuristicCost8D(neighbor));
                  
                }
            }
        }     
    }
    
    private int calcHeuristicCost8D(Cell cell) {
        int dx = Math.abs(cell.getX() - goal.getX());
        int dy = Math.abs(cell.getY() - goal.getY());
        return H_V_COST * (dx + dy) + (D_COST - 2 * H_V_COST) * Math.min(dx, dy);
    }
    
    private int calcCost8D(Cell current, Cell neighbor) {
        for (Direction dir : Direction.values()) {
            if (current.getNeighbours().get(dir) == neighbor) {
                for (Direction diag : dir.diagonal()) {
                    if (dir == diag) {
                        return D_COST;
                    }
                }
                for (Direction norm : dir.normal()) {
                    if (dir == norm) {
                        return H_V_COST;
                    }
                }
            }
        }
        return 0;
    }
    
    private ArrayList<Cell> reconstructPath(Cell current) {
        ArrayList<Cell> totalPath = new ArrayList();
        
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        return totalPath;
    }
    
    private Cell lowestCostInOpen() {
        Cell min = null;
            
        for (Cell c : open) {
            if (min == null) {
                min = c;
            }
            if (fCost.get(c) < fCost.get(c)) {
                min = c;
            }
        }
        return min;
    }
}
