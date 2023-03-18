package GameOfLife;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.io.Serializable; 


/**
 * This class defines the gameboard object used in the Game of Life.
 */
public class GameBoard extends JComponent implements ActionListener, Serializable{
  
  //field variables
 // private LifeGame grid;
  private JButton board [][];
  private Dimension dim;
  private int row = 19;
  private int col = 19;
  private Color hotPink;
  private JButton cell;
  private Color newGrid [][];
  
  /**
   * This is the class constructor. It sets up the initial state of the
   * game board.
   */
  public GameBoard() {
    //instantiate objects
    newGrid = new Color[row][col];
    board = new JButton[row][col];
    dim = new Dimension(45,45);
    hotPink = new Color(255, 0, 127);
    Random rand = new Random();

       
    //assign the layout manager
    setLayout(new GridLayout(row,col));
    
    //start the board with an initial state
    //give each cell an action listener
    for (int i=0; i<row; i++){
      for (int j=0; j<col; j++){
        //create a new cell
        cell = new JButton("");
        cell.setPreferredSize(dim);
        //Get a new random number between [0 - 11].
        int n = rand.nextInt(11);
        //randomly set some cells to dead, some to alive
        //to set the initial state
        if (n % 2 == 0){
        cell.setBackground(Color.gray);
        cell.addActionListener(this);
        }else{
          cell.setBackground(hotPink);
          cell.setIcon(new ImageIcon(getClass().getResource("../cell.jpg")));
          cell.addActionListener(this);
        }
        board[i][j] = cell;
        add(board[i][j]); 
      }
    }
    validate();
  }
  
  /*
   * This method calculates the state of the next generation of life cells
   * and returns an updated color grid
   * @return newGrid the updated grid
   */
  public void nextGen(){
    
    //instance variables
    Color tempGrid [][] = new Color [row][col];
    int[][] newStatus = new int[row][col];   
    
    //loops through the board and retrieve the state of the cells
    for (int i  =0; i<row; i++){
      for (int j = 0; j<col; j++){
        Color color = board[i][j].getBackground();
          newGrid[i][j] = color;
      }
    }
    
    //initialize the temp arrays to a lifeless state
    for (int i = 0; i < row; i++){
      for (int j = 0; j < col; j++){
        newStatus[i][j] = 0;
        tempGrid[i][j] = Color.gray;
      }
    }
    
    //call the helper method to get the updated status value for the target 
    //cell.  Use the results to assign the correct cell values
    //to our newGrid
    for (int i = 1; i < row-1; i++){
      for (int j = 1; j < col-1; j++){  
        //int array value will either be zero or one
        newStatus[i][j] = nextGenHelper(newGrid, i, j);
        //if cell is alive, make it pink
        if (newStatus[i][j] == 1){
          tempGrid[i][j] = hotPink;
        }//if cell is dead, make it gray 
        if(newStatus[i][j] == 0){
          tempGrid[i][j] = Color.gray;
        }
      }
    }
    
    //update newGrid
    newGrid = tempGrid;
    
    //clear the container
    removeAll();
    
    //loop through the next gen
    for (int i=0; i<row; i++){
      for (int j=0; j<col; j++){
        cell = new JButton();
        cell.setPreferredSize(dim);
        cell.setBackground(Color.gray);
        board[i][j] = cell;
        if (newGrid[i][j] == hotPink){  
          cell.setBackground(hotPink);
          cell.setIcon(new ImageIcon(getClass().getResource("../cell.jpg")));
        }
        add(board[i][j]); 
      }
    } 
    //make everything work
    validate();
  }
  
  /**
   * This method takes a cell in a grid, scans the neighbor cells for life,
   * and returns and updated life status based on the neighboring cells.
   * @param theGrid the game board to evaluate
   * @param rowIndex the index of the target cell
   * @param colIndex the index of the target cell
   * @return status returns 1 for alive or 0 for dead
   */
  public int nextGenHelper(Color[][] theGrid, int rowIndex, int colIndex){
    //instance variables
    Color[][] grid = theGrid;     
    int targetRow = rowIndex;
    int targetCol = colIndex;
    Color alive = hotPink;
    int counterAlive = 0;
    int status = 0;
    int targetCell = 0;
    
    //find out if our target cell is alive or dead
    Color theCell = grid[targetRow][targetCol];
    if (theCell.getRGB() == alive.getRGB()){
      targetCell = 1;
      counterAlive = -1;
    }
    
// loop to cycle through the cell and its neighbors
    for (int row = targetRow - 1; row < targetRow + 2; row++) {
      for (int col = targetCol - 1; col < targetCol + 2; col++) {
        
        //test for life
        Color testVal = grid[row][col];
        if (testVal.getRGB() == alive.getRGB()){
          counterAlive++;
        }
      }
    }
    
    //A live cell with two or three live neighbors stays alive.
    if (targetCell == 1 && counterAlive == 2){
      status = 1;
    } 
    if (targetCell == 1 && counterAlive == 3){
      status = 1;
    }
    //A dead cell with exactly three live neighbors becomes a live cell 
    if (targetCell == 0 && counterAlive == 3){
      status = 1;
    }
    return status;   
  }
  
  
  /**
   * This is the action listener.  It changes the color of the buttons when clicked.
   * @param e  The action event
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Color color =((JButton)e.getSource()).getBackground();
    if (color == (Color.gray)){
      ((JButton)e.getSource()).setBackground(hotPink);
      ((JButton)e.getSource()).setIcon(new ImageIcon(getClass().getResource("../cell.jpg")));
    }
    else{
      ((JButton)e.getSource()).setBackground(Color.gray);
      ((JButton)e.getSource()).setIcon(null);
    }
  }
  
  
  /**
   * This method displays the next generation of the game.
   */
  public void displayNextGen(){
    
    //clear the container
    removeAll();
    
    //loop through the next gen
    for (int i=0; i<row; i++){
      for (int j=0; j<col; j++){
        cell = new JButton();
        cell.setPreferredSize(dim);
        cell.setBackground(Color.gray);
        board[i][j] = cell;
        if (newGrid[i][j] == hotPink){  
          cell.setBackground(hotPink);
          cell.setIcon(new ImageIcon(getClass().getResource("../cell.jpg")));
        }
        add(board[i][j]); 
      }
    } 
    //make everything work
    validate();
  }
  
  
  /**
   * The necessary method. This method
   * renders the component.
   * @param g The Graphics object 
   */
  public void paintComponent(Graphics g) {
    // paint the background
    super.paintComponent(g);   
  }

}