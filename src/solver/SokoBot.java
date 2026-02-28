package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class SokoBot {

public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
  /*
    * YOU NEED TO REWRITE THE IMPLEMENTATION OF THIS METHOD TO MAKE THE BOT SMARTER
    */
  /*
    * Default stupid behavior: Think (sleep) for 3 seconds, and then return a
    * sequence
    * that just moves left and right repeatedly.
    */

	State goalState = null;
	deadZone = new boolean[height][width];
	findPlayer(itemsData);
	logCratesTargets(itemsData, mapData, width, height);
	State.setTargetData(targetData);
	findDeadZones(itemsData, mapData);

	
	
/* 	for(int row = 0; row < deadZone.length; row++){
		for(int col = 0; col < deadZone[0].length; col++){
			System.out.println("row " + row + "col" + col +deadZone[row][col]);
				
		}

	}
		*/

	StringBuilder movesHolder = new StringBuilder();
	

	
	

	goalState = aStar(itemsData, mapData);


	movesHolder = getMoves(goalState, movesHolder);
/* 
	ArrayList<Crate> tempCrate = new ArrayList<>();
	tempCrate.add(new Crate(3, 2));
	tempCrate.add(new Crate(4, 2));

	State tempState = new State(null, "1,1", tempCrate, 'a');
	System.out.println(isDeadLock(mapData,tempState , 4, 2));

*/
	//targetCrate = findNearestCrate(mapData, width, height);

	
	//goToCrate(targetCrate, movesHolder, mapData, itemsData, width, height);
  //System.out.println(isPlayerBesideCrate(itemsData));
	
  //	movesHolder.reverse();
	//moves = movesHolder.toString();
  
	//System.out.print("moves is " + moves);






	

	
	
  return movesHolder.toString();
}

public void findDeadZones(char[][] itemsData, char[][] mapData){
	int row = 0;
	int col = 0;
	int i = 0;
	int streak = 0;

	

	for(row = 0; row < itemsData.length; row++){
		for(col = 0; col < itemsData[0].length; col++){
			if(mapData[row][col] != '#'){
				if(row > 0 && row < itemsData.length - 1
				&&col > 0 && col < itemsData[0].length - 1){
					if((mapData[row + 1][col] == '#' || mapData[row - 1][col] == '#') 
					&& (mapData[row][col + 1] == '#' || mapData[row ][col - 1] == '#') && mapData[row][col] != '.'){
						deadZone[row][col] = true;
					}
					else{
						if (mapData[row + 1][col] == '#' || mapData[row - 1][col]== '#') {
							if(checkRow(itemsData, mapData, row, col)){
								deadZone[row][col] = true;
							}	
						}
						else if (mapData[row][col - 1] == '#' || mapData[row][col + 1]== '#') {
							if(checkCol(itemsData, mapData, row, col)){
								deadZone[row][col] = true;
							}	
						}
						
					}
					
					
					
				}
			}			
		}
	}

}

public boolean checkRow(char[][] itemsData, char[][] mapData, int rowToCheck, int colToCheck){

	

	boolean deadLeft;
	boolean	deadRight;

	boolean leftDead = false;
	boolean rightDead = false;

	int checkCol = colToCheck;

	while(checkCol > 0 && !leftDead){
		if((mapData[rowToCheck - 1][checkCol] != '#' && mapData[rowToCheck + 1][checkCol] != '#' )
		|| mapData[rowToCheck][checkCol] == '.'  ){
			return false;
		}
		if(mapData[rowToCheck][checkCol - 1] == '#'){
			leftDead = true;
		}
		checkCol--;
	}

	checkCol = colToCheck;
	while(checkCol < mapData[0].length - 1 && !rightDead){
		if((mapData[rowToCheck - 1][checkCol] != '#' && mapData[rowToCheck + 1][checkCol] != '#' )
		|| mapData[rowToCheck][checkCol]  == '.'){
			return false;
		}
		if(mapData[rowToCheck][checkCol + 1] == '#'){
			rightDead = true;
		}
		checkCol++;
	}



	
	

	return (leftDead && rightDead);
}

public boolean checkCol(char[][] itemsData, char[][] mapData, int rowToCheck, int colToCheck){
	boolean downDead = false;
	boolean upDead = false;
	int checkRow = rowToCheck;

	while(checkRow > 0 && !upDead){
		if((mapData[checkRow][colToCheck - 1] != '#' && mapData[checkRow][colToCheck + 1] != '#') 
		|| mapData[checkRow][colToCheck]  == '.' ){
			return false;
		}
		if(mapData[checkRow - 1][colToCheck] == '#'){
			upDead = true;
		}
		checkRow--;
	}

	checkRow = rowToCheck;

	while(checkRow < mapData.length - 1 && !downDead){
		if((mapData[checkRow][colToCheck - 1] != '#' && mapData[checkRow][colToCheck + 1] != '#')
		|| mapData[checkRow][colToCheck]  == '.' ){
			return false;
		}
		if(mapData[checkRow + 1][colToCheck] == '#'){
			downDead = true;
		}
		checkRow++;
	}

	return (upDead && downDead);
}


public State aStar(char[][] itemsData, char[][] mapData){
	explored = 0;
	HashMap<String , Integer> states = new HashMap<>(); // used to check if state has been visited or not 
	Queue<State> aQueue = new PriorityQueue<>(Comparator.comparing(s -> s.getHeu()));
	String botLoc =  Integer.toString(row).concat("," + Integer.toString(col)); // location of bot

	int[] dRow = {-1 ,1 , 0 , 0}; // directions to check
	int[] dCol = {0, 0 , -1 , 1};
	char[] dirs = {'u' , 'd' , 'l', 'r'}; // directions
	int i = 0;

	
	State start = new State(null,botLoc,crateData, '\0', mapData);
	int count = 0;

	

	
	aQueue.offer(start); // place original state into queue

	//System.out.println(states.containsKey(start.getStateStr()));


	while(!aQueue.isEmpty()){
		State checkedState = aQueue.poll();

		if(isGoalState(checkedState)){
			return checkedState;
		}


		if(states.containsKey(checkedState.getStateStr())) continue;

		states.put(checkedState.getStateStr(), 0); // mark visited 
		
		ArrayList<Crate> newCrates = null;
		
		//System.out.println("queued " + checkedState.getStateStr());

		
		for(i = 0; i < 4; i++){
			String newBotLoc;
			State newState = null;
			int botRow = checkedState.getBotRow() + dRow[i]; //row bot is going to check
			int botCol = checkedState.getBotCol() + dCol[i];
			int pushRow = botRow + dRow[i]; // row where bot pushes crate
			int pushCol = botCol + dCol[i];
			if(checkedState.hasCrateAt(botRow ,botCol)){// checks if there is a crate direction bot is facing 
				if(pushRow >= 0 && pushRow < mapData.length  
				   &&pushCol >= 0 && pushCol < mapData[0].length ){ // make sure no out of bounds checking 
					if(!checkedState.hasCrateAt(pushRow,pushCol) 
							&& mapData[pushRow][pushCol]  != '#'){ // checks if the crate can be pushed 
							//  copy crates and offer state to queue and update player pos
							newCrates = new ArrayList<>();
							for (Crate c : checkedState.getCrateData()){
								newCrates.add(new Crate(c.getRow(), c.getCol())); // copies crates for the new state 
							} 
							moveCrate(newCrates,botRow, botCol, pushRow, pushCol  );
							newBotLoc = Integer.toString(botRow).concat("," + Integer.toString(botCol));
							newState = new State(checkedState, newBotLoc, newCrates, dirs[i], mapData);
							
				
							// only offer state if crate is not a deadzone
									if(!deadZone[pushRow][pushCol]){
										if(!isDeadLock(mapData, newState, pushRow, pushCol)){
												aQueue.offer(newState);
												explored++;
												
											}
											
										
									}
									
									
										
									
									
								
								
							
							
							
							
						} 
				}	
					
			}
			else if(botRow >= 0 && botRow < mapData.length 
				   &&botCol >= 0 && botCol < mapData[0].length ){		
				if(mapData[botRow][botCol] != '#' ){
					newCrates = new ArrayList<>();
					for (Crate c : checkedState.getCrateData()){
						newCrates.add(new Crate(c.getRow(), c.getCol()));
					}
					newBotLoc = Integer.toString(botRow).concat("," + Integer.toString(botCol));
					newState = new State(checkedState, newBotLoc, newCrates, dirs[i], mapData);
					aQueue.offer(newState);
				}
			}
			
		}
		
	}
	
	System.out.println("no solution found");
	return null;
	
} 

/* 
public State aStar2(char[][] itemsData, char[][] mapData){
	explored = 0;
	HashMap<String , Integer> states = new HashMap<>(); // used to check if state has been visited or not 
	Queue<State> aQueue = new PriorityQueue<>(Comparator.comparing(s -> s.getAStarScore()));
	String botLoc =  Integer.toString(row).concat("," + Integer.toString(col)); // location of bot

	int[] dRow = {-1 ,1 , 0 , 0}; // directions to check
	int[] dCol = {0, 0 , -1 , 1};
	char[] dirs = {'u' , 'd' , 'l', 'r'}; // directions
	int i = 0;
	int j = 0;

	
	State start = new State(null,botLoc,crateData, "\0");
	int count = 0;

	

	
	aQueue.offer(start); // place original state into queue

	//System.out.println(states.containsKey(start.getStateStr()));

	
	while(!aQueue.isEmpty()){
		State checkedState = aQueue.poll();
		
		if(isGoalState(checkedState)){
			return checkedState;
		}
		boolean[][] visited = new boolean[mapData.length][mapData[0].length];

		if(states.containsKey(checkedState.getStateStr())) continue;

		states.put(checkedState.getStateStr(), 0); // mark visited 
		
		ArrayList<Crate> newCrates = null;
		
		//System.out.println("queued " + checkedState.getStateStr());
		Queue<Path> bfs  = new LinkedList<>();
		bfs.offer(new Path(checkedState.getBotRow(), checkedState.getBotCol(), null));


		while(!bfs.isEmpty()){
			Path tempPath = bfs.poll();

			for(i = 0; i < 4; i ++){ // states where bot will go u d l r
				String newBotLoc;
				State newState = null;
				int newBotRow = tempPath.getRow() + dRow[i]; //row bot is going to check
				int newBotCol = tempPath.getCol() + dCol[i];
				 // checks if bot can push a crate
				int pushRow = newBotRow + dRow[i]; // row where bot pushes crate
				int pushCol = newBotCol + dCol[i];	
				if(checkedState.hasCrateAt(newBotRow ,newBotCol)){// checks if there is a crate direction bot is facing 
					if(pushRow >= 0 && pushRow < mapData.length  
					&&pushCol >= 0 && pushCol < mapData[0].length ){ // make sure no out of bounds checking 
						if(!checkedState.hasCrateAt(pushRow,pushCol) 
								&& mapData[pushRow][pushCol]  != '#'){ // checks if the crate can be pushed 
								//  copy crates and offer state to queue and update player pos
								newCrates = new ArrayList<>();
								for (Crate c : checkedState.getCrateData()){
									newCrates.add(new Crate(c.getRow(), c.getCol())); // copies crates for the new state 
								} 
								moveCrate(newCrates,newBotRow, newBotCol, pushRow, pushCol  );
								newBotLoc = Integer.toString(newBotRow).concat("," + Integer.toString(newBotCol));
								newState = new State(checkedState, newBotLoc, newCrates,+ dirs[i] + getPath(tempPath)  );
								
						/* 	for (Crate c : newState.getCrateData()){
									System.out.print("new state " + c.getRow() + " " + c.getCol());
									System.out.println(""); // copies crates for the new state 
								} 
								

								// only offer state if crate is not a deadzone
										if(!deadZone[pushRow][pushCol]){
											if(!isDeadLock(mapData, newState, pushRow, pushCol)){
													aQueue.offer(newState);
													explored++;
												}
												
											
										}
										
										
											
										
										
									
									
								
								
								
								
							} 
					}	
						
				}

				else{
					if (mapData[newBotRow][newBotCol] != '#' && !checkedState.hasCrateAt(newBotRow, newBotCol)) {
						if(!visited[newBotRow][newBotCol]){
							bfs.offer(new Path(dirs[i], tempPath, newBotRow, newBotCol));
							visited[newBotRow][newBotCol] = true;
						}
					
					}
				}
			
			
			}
		
		
		
		}
	
	

	}
	System.out.println("no solution found");
	return null;
} 
*/
// checks for a crate deadlock
public boolean isDeadLock(char[][] mapData,State checkedState, int row, int col){
	int[] dRow = {-1 ,1 , 0 , 0}; // directions to check
	int[] dCol = {0, 0 , -1 , 1};
	int i = 0;
	for(i = 0 ; i < 4; i++){
		if(hasTarget(row + dRow[i], col + dCol[i])){
		return false;
		
		}
		
	}
	if(hasTarget(row,col)){
			return false;
		}
	

	if(mapData[row + 1][col] == '#' || mapData[row - 1][col] == '#'){
		if(checkedState.hasCrateAt(row, col - 1) 
		&& (mapData[row + 1][col - 1] == '#'
	 	|| mapData[row - 1][col - 1] == '#')){
		
			return true;
		}
		if(checkedState.hasCrateAt(row, col + 1) 
		&& (mapData[row + 1][col + 1] == '#'
		|| mapData[row - 1][col + 1] == '#')){
		
			return true;
		}
	}

	 if (mapData[row ][col + 1] == '#' || mapData[row][col - 1] == '#') {
		if(checkedState.hasCrateAt(row + 1, col)
		&&( mapData[row + 1][col + 1] == '#'
		|| mapData[row + 1][col - 1] == '#' )){
		
			return true;
		}
		if(checkedState.hasCrateAt(row - 1, col)
		&&( mapData[row - 1][col + 1] == '#'
		|| mapData[row - 1][col - 1] == '#' )){
		
			return true;
		}
	}

	return false;
}

public boolean hasTarget(int row, int col){
	for(Target t: targetData){
		if(t.getRow() == row && t.getCol() == col){
			return true;
		}
	}
	return false;

}




public boolean hasObstacle(char[][] mapData,State checkedState, int row, int col ){
	if(mapData[row][col] == '#' || checkedState.hasCrateAt(row, col)){
		return true;
	}
	return false;
}
public StringBuilder getMoves(State goalState, StringBuilder moves){
	State tempState = goalState;

	if(tempState == null){
		System.out.println("nandlawndlaw");
	}

	while(tempState.getMove() != '\0'){
		moves.append(tempState.getMove());
		tempState = tempState.getParent();

	}

	moves.reverse();
	return moves;
}

	/**
	 * reconstructs the path to get to a crate
	 * 
	 */
	public String getPath(Path lastPath ){
		Path tempPath = lastPath;
		String moves = "";
		StringBuilder reversedMoves;
		while(tempPath.getMove() != '\0'){// if it return \0 you are at the starting point
			//moves = moves.concat(Character.toString(tempPath.getMove()));

			moves = moves.concat(Character.toString(tempPath.getMove()));
			tempPath = tempPath.getParent();
		} 
		reversedMoves = new StringBuilder(moves);
		reversedMoves.reverse();
		moves = reversedMoves.toString();
		return moves;

	}


public boolean isGoalState(State state){
	boolean inTarget = false; // check if crate is in a target

	for(Target t : targetData){
		inTarget = false;
		for(Crate c: state.getCrateData()){
			if(t.getRow() == c.getRow() && t.getCol() == c.getCol()){
				inTarget = true;
			}
		}
		if(!inTarget){
			return false;
		}
	}
	
	return true;
}

public void moveCrate(ArrayList<Crate> crateData, int row, int col, int dRow, int dCol){
	Iterator<Crate> iCrates = crateData.iterator();
	Crate tempCrate = null;
	while(iCrates.hasNext()){
		tempCrate = iCrates.next();

		if(tempCrate.getRow() == row && tempCrate.getCol() == col){ 
			tempCrate.updateRow(dRow); // updates crates position 
			tempCrate.updateCol(dCol);

		}
	}
}

public void goToCrate(Crate crate, StringBuilder moves,char[][] mapData,char [][] itemsData, int width, int height ){
	boolean canExplore = true;
	boolean[][] visited = new boolean[height][width];
	Queue<Path> queue = new LinkedList<>(); 
	Path tempPath;


	queue.add(new Path(row , col, null));
	visited[row][col] = true;


	while(!queue.isEmpty()){

		tempPath = queue.poll(); // tile the player would be currently in 
		
		int rowToVisit;
		int colToVisit;
		int i = 0;

		int[] dRow = {-1 ,1 , 0 , 0}; // directions to check
		int[] dCol = {0, 0 , -1 , 1};
		char[] dirs = {'u' , 'd' , 'l', 'r'}; // directions

		for(i = 0; i < 4; i++){

			rowToVisit = tempPath.getRow() + dRow[i];
			colToVisit = tempPath.getCol() + dCol[i];

			if(rowToVisit < 0 || rowToVisit >= mapData.length ||
			 colToVisit < 0 || colToVisit >= mapData[0].length){ // mapData.length is number of rows of the map
				continue;
			}

			if(mapData[rowToVisit][colToVisit] != '#' && itemsData[rowToVisit][colToVisit] != '$' &&
			!visited[rowToVisit][colToVisit]){ // checks if tile to visit is not blocked and has not been visited 

				

				visited[rowToVisit][colToVisit] = true;
				queue.add(new Path(dirs[i], tempPath, rowToVisit, colToVisit));
			}

			if(isPlayerBesideCrate(crate, rowToVisit, colToVisit)){
				tempPath = new Path(dirs[i], tempPath, rowToVisit, colToVisit);
			
				getPath(tempPath);
				return;
				
			}
			
			
		}

		
		
	}
}



/*
  finds the spawnpoint of the player 
*/
public void findPlayer(char[][] itemsData){
  
    int row = 0;
    int col= 0;
    
    for(row = 0; row < itemsData.length; row++ ){
        for(col = 0; col < itemsData[0].length; col++){
          
          if(itemsData[row][col] == '@' ){
             
             	this.row = row;
                this.col = col; 
				
                }
            }
        }

}
/**
 * logs the position of each crate and target
 */
public void logCratesTargets(char[][] itemsData, char[][] mapData, int width, int height){
    crateData = new ArrayList<Crate>();
    targetData = new ArrayList<Target>();
	Crate tempCrate;
	Target tempTarget;
    int row = 0;
    int col= 0;
    
    for(row = 0; row < height; row++ ){
       
        for(col = 0; col < width; col++){
			if(itemsData[row][col] == '$'){
				tempCrate = new Crate(row, col);
				crateData.add(tempCrate);
			}
			if (mapData[row][col] == '.') {
				tempTarget = new Target(row, col, mapData);
				targetData.add(tempTarget);
			}
        }
    }

	
}

public void isReachable(char[][] itemsData, char[][] mapData, int width, int height){
	
}


/** 
 * finds nearest crate to player without regard to walls 
 */
public Crate findNearestCrate(char[][] mapData, int width, int height){
	Crate tempCrate;
	Crate closestCrate = null;
	int mhDistance; // manhattan distance
	int lowest = Integer.MAX_VALUE; // guarantees that the first manhattan distance is lower
	Iterator<Crate> crates = crateData.iterator();

	while(crates.hasNext()){
		tempCrate = crates.next();
		mhDistance = Math.abs(tempCrate.getRow() - this.row) + Math.abs(tempCrate.getCol() - this.col); // gets the manhattan distance of player from each crate
		if(lowest > mhDistance){
			lowest = mhDistance;
			closestCrate = tempCrate;
		}

	}

	

	

	

	return closestCrate;
	
  
  
}

/**
 * 
 */




public boolean isPlayerBesideCrate(Crate crate, int rowToVisit, int colToVisit){
	int[] dRow = {-1 ,1 , 0 , 0}; // directions to check
	int[] dCol = {0, 0 , -1 , 1};
	

	for(int i = 0; i < 4; i++){
		if(crate.getRow() + dRow[i] == rowToVisit && crate.getCol() + dCol[i] == colToVisit){
			return true;
		}
	}

	return false;

}



/* checks if the box gets trapped in a corner with the move made

*/
public boolean isTrapped(char itemsData[][], char mapData[][]){
  return true;

}


int explored;
int row;
int col;
private int destX; // destination of bot 
private int destY;
private ArrayList<Crate> crateData; // holds positions of crates for quick lookup
private ArrayList<Target> targetData;
private boolean[][] deadZone;
}
