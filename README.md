# Game Of Life
## A Java package for playing John Conway's "Game of Life".  

### Files:
The 'PlayGame' file drives the game.  
It creates a 'GameBoard' object.  
'cell.jpg' is the image used on each occupied 'life' cell.  

### How to play:  
The board opens with an initial seed of cells. 
Occupied cells are 'alive'.  
Click an empty cell to add a life.  
Click an occupied cell to make it empty.    
Set the game speed by changing the value in the 'Generations per minute' field. The default is one generation per second.

### Rules:  
A live cell with only one neighbor will die of loneliness.  
A live cell with four or more neighbors will die from scarcity of resources.  
Any live cell with two or three live neighbours is healthy and happy and will survive to the next generation.  
Three live neighbours around an empty cell will cause a new life to be 'born' in the empty cell.  
  
More information on the history and theory behind the game is available [here](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life).
