/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.lb.AStar.model;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author LB
 */
public class Cell extends Group {
        
    private int x;
    private int y;
    
    public int heuristicCost = 0; // calculated heuristic cost 
    public int finalCost = 0; // G+H

    public Cell parent = null;    
    private final HashMap<Direction, Cell> neighbours = new HashMap<>();  

    private boolean traversable = true;    
    public boolean isGoal = false;
    public boolean isStart = false;

    private final Rectangle bg;
    
    private final Color COLOR = Color.TRANSPARENT;
    private final Color BORDER_COLOR = Color.BLACK;
    
    public Cell(Grid grid, double posX, double posY, double width, double height, double strokeWidth) {
        //draw the walls as lines
        Line wallU = new Line(posX, posY, posX + width, posY);
        Line wallD = new Line(posX, posY + height, posX + width, posY + height);
        Line wallL = new Line(posX, posY, posX, posY + height);
        Line wallR = new Line(posX + width, posY, posX + width, posY + height);
    
        wallU.setStrokeWidth(strokeWidth);
        wallD.setStrokeWidth(strokeWidth);
        wallL.setStrokeWidth(strokeWidth);
        wallR.setStrokeWidth(strokeWidth);
        
        wallU.setStroke(BORDER_COLOR);
        wallD.setStroke(BORDER_COLOR);
        wallL.setStroke(BORDER_COLOR);
        wallR.setStroke(BORDER_COLOR);
       
        wallU.setStrokeType(StrokeType.CENTERED);
        wallD.setStrokeType(StrokeType.CENTERED);
        wallL.setStrokeType(StrokeType.CENTERED);
        wallR.setStrokeType(StrokeType.CENTERED);
        
        super.getChildren().add(wallU);
        super.getChildren().add(wallD);
        super.getChildren().add(wallL);
        super.getChildren().add(wallR);
        
        bg = new Rectangle();
        bg.setWidth(width);
        bg.setHeight(height);
        bg.setLayoutX(posX);
        bg.setLayoutY(posY);
        bg.setStroke(Color.BLACK);
        bg.setFill(COLOR);
        
        super.getChildren().add(bg);
        bg.toBack();
        
        // add action
        bg.setOnMouseClicked((javafx.scene.input.MouseEvent event) -> {
            // do something on the grid
                        
            if (grid.getStart() == null) {
                isStart = true;
                bg.setFill(Color.GREEN);
                grid.setStart(this);
            } else if (grid.getGoal() == null) {
                isGoal = true;
                bg.setFill(Color.RED);
                grid.setGoal(this);
            } else if (!isGoal && !isStart && traversable) {
                
                this.bg.setFill(Color.BLACK);
                traversable = false;
            
            } else if (!traversable) {
                
                this.bg.setFill(COLOR);
                traversable = true;
                
            }
            
                        System.out.println(isStart + " " + isGoal + " " + traversable);

                
        });
        
        this.setOnMouseEntered((MouseEvent event) -> {
            if (event.isShiftDown()) {
                if (!this.isGoal && !this.isStart) {
                if (this.bg.getFill() == Color.BLACK) {
                    this.bg.setFill(COLOR);
                    traversable = true;
                } else {
                    this.bg.setFill(Color.BLACK);
                    traversable = false;
                }
            }
            }
        });
        
        
        // System.out.println(posX + " " + posY);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public boolean isTraversable() {
        return this.traversable;
    }

    public void putNeighbour(Direction dir, Cell cell) {
        neighbours.put(dir, cell);
    }
    
    public HashMap<Direction, Cell> getNeighbours() {
        return neighbours;
    }
    
    public void setFill(Color color) {
        this.bg.setFill(color);
    }
    
    
}   
