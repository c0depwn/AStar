/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.lb.AStar;

import ch.lb.AStar.model.Grid;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author LB
 */
public class MainViewController implements Initializable {
    
    @FXML
    private Pane gridContainer;
    @FXML
    private Slider sliderColumns;
    @FXML
    private Slider sliderRows;
    @FXML
    private Slider sliderCellSize;
    @FXML
    private Slider sliderBorder;
    @FXML
    private Label valueColumns;
    @FXML
    private Label valueRows;
    @FXML
    private Label valueCellSize;
    @FXML
    private Label valueBorder;
    @FXML
    private CheckBox checkBoxVisualize;
    @FXML
    private CheckBox checkBoxDiagonal;
   
    // Stage properties
    private MainApp mainApp;
    private double X;
    private double Y;
    
    // Grid properties
    private Grid grid;
   
    private double gridX = 0;
    private double gridY = 0;
    
    private int columns = 21;
    private int rows = 16;
    private int cellSize = 30;
    private double borderWidth = 2;
    private boolean diagonalAllowed = false;
    private boolean visualize = true;
    private final Color strokeColor = Color.BLACK;
    
    // Other
    private boolean isInitializing = true;  
    
    /* Cell properties
    private final Color defaultCellColor = Color.WHITE;
    private final Color goalCell = Color.RED;
    private final Color startCell = Color.GREENYELLOW;
    */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Initialize check boxes with default values 
        // disable -> feature not supported yet
        this.checkBoxDiagonal.setSelected(diagonalAllowed);
        this.checkBoxDiagonal.setDisable(true);
        
        this.checkBoxVisualize.setSelected(visualize);
        this.checkBoxVisualize.setDisable(true);
        
        // Initalize labels for slider values with default values
        this.valueColumns.setText(String.valueOf(this.columns));
        this.valueRows.setText(String.valueOf(this.rows));
        this.valueCellSize.setText(String.valueOf(this.cellSize));
        this.valueBorder.setText(String.valueOf(this.borderWidth));
        
        // Set max and min for sliders
        sliderColumns.setMax(40);
        sliderColumns.setMin(1);
        sliderRows.setMax(40);
        sliderRows.setMin(1);
        sliderCellSize.setMax(50);
        sliderCellSize.setMin(10);
        sliderBorder.setMax(5);
        sliderBorder.setMin(1);
        
        // set default values
        sliderColumns.setValue(this.columns);
        sliderRows.setValue(this.rows);
        sliderCellSize.setValue(this.cellSize);
        sliderBorder.setValue(this.borderWidth);

        sliderColumns.valueProperty().addListener((obseravble, oldValue, newValue) -> {
            
            if ((int) Math.round(newValue.doubleValue()) * cellSize < 695) {

                columns = (int) Math.round(newValue.doubleValue());
            
                this.valueColumns.setText(String.valueOf(columns));
            
                if (!isInitializing) {
                    updateGrid();
                }
            }
            
        });
        
        sliderRows.valueProperty().addListener((observable, oldValue, newValue) -> {
          
            if ((int) Math.round(newValue.doubleValue()) * cellSize < 560) {
                
                rows = (int) Math.round(newValue.doubleValue());
            
                this.valueRows.setText(String.valueOf(rows));

                if (!isInitializing) {
                    updateGrid();
                }
            }
            
        });
        
        sliderCellSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            
            int newVal = (int) Math.round(newValue.doubleValue()); 
            
            if (newVal * columns < 695 && newVal * rows < 560) {
                cellSize = (int) Math.round(newValue.doubleValue());
            
                this.valueCellSize.setText(String.valueOf(cellSize));

                if (!isInitializing) {
                    updateGrid();
                }
            }         
   
        });
        
        sliderBorder.valueProperty().addListener((observable, oldValue, newValue) -> {
            
            borderWidth = (int) Math.round(newValue.doubleValue());
            
            this.valueBorder.setText(String.valueOf(borderWidth));
            
            if (!isInitializing) {
                updateGrid();
            }
        });
        
        // Calculate x and y of grid so its centered
        gridX = (gridContainer.getPrefWidth()) / 2 - cellSize*columns/2;
        gridY = gridContainer.getPrefHeight() / 2 - cellSize*rows/2;
        
        grid = new Grid(gridX, gridY, columns, rows, cellSize, borderWidth, diagonalAllowed);
        gridContainer.getChildren().add(grid);
        
        isInitializing = false;
    }   
    
    private void updateGrid() {
        
        gridX = (gridContainer.getPrefWidth()) / 2 - cellSize*columns/2;
        gridY = gridContainer.getPrefHeight() / 2 - cellSize*rows/2;
        
        gridContainer.getChildren().remove(grid);
        System.out.println(gridX + " " + gridY);
        
        grid = new Grid(gridX, gridY, columns, rows, cellSize, borderWidth, diagonalAllowed);
        gridContainer.getChildren().add(grid);
    }

    @FXML
    private void solveAction(ActionEvent event) {
        if (diagonalAllowed) {
            grid.runAStar8D();
        } else {
            grid.runAStar4D();
        }
    }

    @FXML
    private void resetAction(ActionEvent event) {
        
        // Set max and min for sliders
        sliderColumns.setMax(40); 
        sliderRows.setMax(40);
        sliderCellSize.setMax(50);
        sliderBorder.setMax(5);
        
        // Reset Values
        sliderColumns.setValue(21);
        sliderRows.setValue(16);
        sliderCellSize.setValue(30);
        sliderBorder.setValue(2);
        
        updateGrid();
    }

    @FXML
    private void diagonalClicked(ActionEvent event) {
        diagonalAllowed = !diagonalAllowed;
        updateGrid();
    }
    
    @FXML
    private void visualizeClicked(ActionEvent event) {
        visualize = !visualize;
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleOnMouseDragged(MouseEvent event) {
        mainApp.getPrimaryStage().setX(event.getScreenX() + X);
        mainApp.getPrimaryStage().setY(event.getScreenY() + Y);
    }

    @FXML
    private void handleMouseDragReleased(MouseDragEvent event) {
        mainApp.getPrimaryStage().setX(event.getScreenX());
        mainApp.getPrimaryStage().setY(event.getScreenY());
    }

    @FXML
    private void handleMousePressed(MouseEvent event) {
        X = mainApp.getPrimaryStage().getX() - event.getScreenX();
        Y = mainApp.getPrimaryStage().getY() - event.getScreenY();
    }

    @FXML
    private void handleCloseAction(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleMinimizeAction(MouseEvent event) {
        mainApp.getPrimaryStage().setIconified(true);
    }

    @FXML
    private void handleHelpAction(MouseEvent event) {
        
        String instructions = "1.\tClick on the grid to set the start cell. \n"
                + "2.\tClick again to set the goal cell. \n"
                + "3.\tClick or hold the shift key and drag your mouse \n\tover the grid to draw walls.";
        
        Alert help = new Alert(AlertType.INFORMATION);   
        help.setTitle("? Help ?");
        help.setHeaderText("Instructions");
        help.setContentText(instructions);
        help.show();
    }
}
