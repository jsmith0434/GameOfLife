package GameOfLife;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;

/**
 * This is the top level container.
 */
public class PlayGame extends JFrame implements ActionListener{
  //field variables
  private GameBoard game;
  private JButton button;
  private JButton saveButton;
  private JButton restoreButton;
  private JTextField text;
  private JTextField text2;
  private int gpm;
  private int counter;
  private boolean paused;
  private String fname;
  private String gamesPerMin;
  
  /**
   * This is the constructor for the class.  It gets the initial
   * game board and adds it to the frame.
   */
  public PlayGame(){
    //initialize the counter and gpm
    gpm = 0;
    counter = 0;
    paused = true;
    
    //add the gameboard to the frame
    game = new GameBoard();
    setTitle("Game of Life");
    add(game);
    
    //create the toolbar with controls and input
    JToolBar toolBar = new JToolBar("");
    button = new JButton("Start");
    button.addActionListener(this);
    Dimension dim = new Dimension(100,30);
    button.setPreferredSize(dim);
    JLabel label = new JLabel("Generations Per Minute:");
    text = new JTextField("60");  
    
    //add first toolbar to the frame
    toolBar.add(label);
    toolBar.add(text);
    toolBar.add(button);
    add(toolBar, BorderLayout.PAGE_START);
    
    //create another textbox and two buttons for save and restore
    text2 = new JTextField("Enter the file name"); 
    JLabel label2 = new JLabel("File Name:");
    JToolBar toolBar2 = new JToolBar("");
    //create the save button and action listener
    saveButton = new JButton("Save");
    saveButton.setPreferredSize(dim);
    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fname = text2.getText();
        gamesPerMin = text.getText();
        //call the save method with the user input filename and the current GPM setting
        save(fname, gamesPerMin);
        text2.setText(fname + " has been saved with a desired speed of " + gamesPerMin + " games per minute.");
      }
    }
    );
    //create the restore button and action listener
    restoreButton = new JButton("Restore");
    restoreButton.setPreferredSize(dim);
    restoreButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //use a file chooser to help 'guide' the user to selecting an appropriate file
        JFileChooser getfile=new JFileChooser();
        getfile.showSaveDialog(null);
        String fname=getfile.getSelectedFile().getName();
        // call the restore method, which restore the game and returns the gpm setting
        gamesPerMin = restore(fname);
        text2.setText(fname + " has been restored with a speed of " + gamesPerMin + " games per minute.");
        text.setText(gamesPerMin);
      }
    }
    );
    
    //add second toolbar to the frame, add second text box and additional buttons to the toolbar
    add(toolBar2, BorderLayout.PAGE_END);
    toolBar2.add(label2);
    toolBar2.add(text2);
    toolBar2.add(saveButton);
    toolBar2.add(restoreButton);
    
    // show the window
    setVisible(true);
    toFront();
    pack();
  }
  
  /**
   * This method checks the input validity
   * @param word  The user defined input
   * @return true if the input is valid, false otherwise
   */
  public boolean isValid(String word){
    String userEntry = word;  
    
    //check the input for validity
    try{
      gpm = Integer.parseInt(userEntry);
    }catch (NumberFormatException m){
      return false;
    }
    if (gpm > 250 || gpm < 1){
      text.setText("Invalid entry.  Please enter a number between 1 and 250.");
      return false;
    }else{
      //if the input is valid, make the text field read only,
      //update button text, and start the game
      text.setEditable(false); 
      return true;
    }
  }
  
  
  /**
   * This method overrides the action performed method inherited from action listener.
   * It gets the text the user entered from the text field when the start button is pushed.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    counter = counter+1;
    String userEntry = text.getText();
    //if input is valid, start the game
    if (isValid(userEntry) == true && paused == true){
      //anonymous inner class
      paused = false;
      Runnable r = new Runnable() {
        public void run() {
          while(paused == false){
            //calls the startGame method 
            startGame();
          }
        }
      };
      Thread t = new Thread(r);
      //The call to start tells the thread to run r
      t.start();
      button.setText("Stop");
    }
    
    //this will pause the game
    if( counter > 1 && counter % 2 == 0){
      paused = true;
      button.setText("Start");
    }
    
    //this will restart the game
    if(counter > 2 && counter % 2 != 0){
      System.out.print(counter);
      button.setText("Stop");
      paused = false;
    }
  }
  
  
  /**
   * This method gets and displays the next gen of the gameBoard.
   */
  public void startGame() {
    while (paused == false ){
      //determine the number of milliseconds for the thread to sleep
      int speed = (60/gpm)*1000;
      game.nextGen(); 
      try {
        //causes the current thread to temporarily cease execution for the
        //specified number of milliseconds.  Allows the generations to advance
        //at the desired speed.
        Thread.sleep(speed);
      }
      //exception handling to deal with thread.sleep
      catch(InterruptedException e) {
        // do nothing
      }
    }
  }
  
  
  /*
   *This method will serialize and save the file with the desired filename and speed
   *param filename the name of the file  
   *param speed the number of generations per minute
   */
  public void save(String filename, String speed){
    //check to see if the file already exist
    File check = new File(filename);
    boolean overwrite = check.exists();
    if (overwrite == true){
    JOptionPane.showMessageDialog(this, "Click to confirm overwriting the file.");
    }
    
    // Serialization  
    try{    
      FileOutputStream file = new FileOutputStream(filename); 
      ObjectOutputStream out = new ObjectOutputStream(file);  
      out.writeObject(game); 
      out.writeObject(speed);
      out.close(); 
      file.close(); 
    } 
    catch(IOException ex) { 
      System.out.println("File could not be saved"); 
    }
  }
  
  
  /*
   *This method will restore a saved state and will repaint the display. 
   *@param filename the name of the file 
   *@return the number of generations per minute saved with the file 
   */
  public String restore(String filename){
    // declare an instance variable and initialize wth a default value 
    String speed = "60";
    //remove the old game from the frame
    remove(game);
    
    // Deserialization 
    try{    
      // Reading the object from a file 
      FileInputStream file = new FileInputStream(filename); 
      ObjectInputStream in = new ObjectInputStream(file); 
      
      // Method for deserialization of object 
      game = (GameBoard)in.readObject(); 
      speed = (String)in.readObject(); 
      //close the files
      in.close(); 
      file.close(); 
    }
    catch(IOException ex) { 
      System.out.println("File could not be restored"); 
    }
    catch(ClassNotFoundException ex){ 
      System.out.println("ClassNotFoundException is caught"); 
    }
    
    //update the contents of the frame
    add(game);
    repaint();
    
    //return the speed
    return speed;
  }
  
  
  /**
   * This is the main method
   * @param args The command-line arguments
   */
  public static void main(String[] args) {
    //declare and instantiate a new game
    PlayGame myGame;
    myGame = new PlayGame();
  }
}