package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class State {

    /**
     * 
     * @param move - next move to be made
     * @param parent - 
     */
    public State(State parent, String botPos, ArrayList<Crate> crateData, char move , char[][] mapData){
        boolean[][] taken = new boolean[mapData.length][mapData[0].length]; // targets already assigned with a crate
        int heu = 0; // heuristic - summ of distances of crates to their closest target 
        this.botPos = new int[2];
        this. move = move;
    
        this.botPos[0] = Integer.parseInt(botPos.split(",")[0]); // gets the row of player pos string 
        this.botPos[1] = Integer.parseInt(botPos.split(",")[1]);
        

        this.crateData = new ArrayList<>();
        stateStr = botPos + " ";



        int[][] matrix = new int[crateData.size()][crateData.size()];
        int row = 0;
        int col = 0;
        int noLines = 0;
        for(Crate c : crateData){
            for(col = 0; col < targetData.size(); col++){
                int dist = targetData.get(col).getDistanceField()[c.getRow()][c.getCol()]; 
                
                matrix[row][col] = dist; // assign distances of each crate to each target 
            }
            row++;
        }
        

        for (Crate c : crateData){
            Crate copy = new Crate(c.getRow(), c.getCol());
            this.crateData.add(copy);
            heu += findClosestTarget(copy, taken);
            
           
        }
        int[][] matrixCopy = new int[crateData.size()][];
        for (int i = 0; i < matrix.length; i++) {
            matrixCopy[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }


        /* 
        HungarianAlgorithm hung = new HungarianAlgorithm(matrix);
     
        
        heu = hung.findTotal(matrixCopy);
        System.out.println("opt is" + heu);
        */

        // sort crate order so no state has the same crates but different order
    Collections.sort(this.crateData, new Comparator<Crate>() {
        @Override
        public int compare(Crate c1, Crate c2) {
            if (c1.getRow() != c2.getRow()) {
                return Integer.compare(c1.getRow(), c2.getRow());
            } else {
                return Integer.compare(c1.getCol(), c2.getCol());
            }
        }
        });
         



        for (Crate c : crateData){

            stateStr = stateStr.concat(c.getPosition());
            stateStr = stateStr.concat(" ");
        }

        if(parent == null){
            aScore = heu;
            depth = 0;
        }
        else{
            depth  = parent.getDepth() + 1;
            aScore = heu + depth;
            
        }
        



        this.parent = parent;
        
    }
    // not working
    public int hungarianAlg(char[][] mapData, ArrayList<Crate> crateData){
        int[][] matrix = new int[crateData.size()][crateData.size()];
        int row = 0;
        int col = 0;
        int noLines = 0;
        for(Crate c : crateData){
            for(col = 0; col < targetData.size(); col++){
                int dist = targetData.get(col).getDistanceField()[c.getRow()][c.getCol()]; 
                
                matrix[row][col] = dist; // assign distances of each crate to each target 
            }
            row++;
        }

        //  1 find min of row then subtract

        for(row = 0; row < crateData.size(); row++){
            int lowest = Integer.MAX_VALUE;
            for(col = 0; col < crateData.size(); col++){
                if(matrix[row][col] < lowest){
                    lowest = matrix[row][col];
                }


            }
            for(col = 0; col < crateData.size(); col++){
                matrix[row][col] -= lowest;
            }
        }

        // 2 find min of col then subtrac to each member in the column

        for(col = 0; col < crateData.size(); col++){
            int lowest = Integer.MAX_VALUE;
            for(row = 0 ; row < crateData.size(); row++){
                if(matrix[row][col] < lowest){
                    lowest = matrix[row][col];
                }
            }
            for(row = 0 ; row < crateData.size(); row++){
                matrix[row][col] -= lowest;
            }
        }

       
 int coveredCols = 0;
        do { 
            coveredCols = 0;
             int n = matrix.length;
            boolean[] rowCovered = new boolean[n];
            boolean[] colCovered = new boolean[n];
            boolean[][] starredZero = new boolean[n][n];
            int minUncovered = Integer.MAX_VALUE;

            // Step 1: Star zeros greedily
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (matrix[i][j] == 0 && !rowCovered[i] && !colCovered[j]) {
                        starredZero[i][j] = true;
                        rowCovered[i] = true;
                        colCovered[j] = true;
                    }
                }
            }

            // Step 2: Reset row/col cover arrays
            Arrays.fill(rowCovered, false);
            Arrays.fill(colCovered, false);

            // Step 3: Cover columns with a starred zero
          
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    if (starredZero[i][j]) {
                        colCovered[j] = true;
                        coveredCols++;
                        break;
                    }
                }
            }

            if(coveredCols == n){
                break;
            }
            

            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    if(!colCovered[j] && matrix[i][j] < minUncovered){
                        minUncovered = matrix[i][j];
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (!rowCovered[i] && !colCovered[j])
                        matrix[i][j] -= minUncovered;
                    else if (rowCovered[i] && colCovered[j])
                        matrix[i][j] += minUncovered;
                }
        }

        } while (coveredCols != crateData.size());

         return coveredCols;
    }

   

    /**
     * 
     * @return - returns minimum moves to get for crate to closest target
     *           while ignoring crates but including walls 
     */

    public int findClosestTarget(Crate crate, boolean[][] taken){
        Target closestTarget = null;
        Target tempTarget = null;
        int lowestDistance = Integer.MAX_VALUE;
        int rowToVisit = crate.getRow();
        int colToVisit = crate.getCol();
        Target bestTarget = null;
        

        Iterator<Target> iTarget = targetData.iterator();

        while(iTarget.hasNext()){
            tempTarget = iTarget.next();
            int targetRow = tempTarget.getRow(); 
            int targetCol = tempTarget.getCol();
            if(tempTarget.getDistanceField()[rowToVisit][colToVisit] < lowestDistance && !taken[targetRow][targetCol]){
                lowestDistance = tempTarget.getDistanceField()[rowToVisit][colToVisit];
                bestTarget = tempTarget;
            }
        }

        //taken[bestTarget.getRow()][bestTarget.getCol()] = true;        

        return lowestDistance;
    }

    public boolean hasCrateAt(int row, int col){
        Iterator<Crate> iCrates = crateData.iterator();
        Crate tempCrate;
        boolean exists = false;
        while(iCrates.hasNext()){
            tempCrate = iCrates.next();
            if(tempCrate.getRow() == row && tempCrate.getCol() == col){
                exists = true;
            }

        }

        return exists;
    }

    public Crate getCrateAt(int row, int col){
        Iterator<Crate> iCrates = crateData.iterator();
        Crate tempCrate;
        while(iCrates.hasNext()){
            tempCrate = iCrates.next();
            if(tempCrate.getRow() == row && tempCrate.getCol() == col){
                return tempCrate;
            }

        }

        return null;
    }

    public String getStateStr(){
        return stateStr;
    }

    public int getHeu(){
        return aScore - depth;
    }

    public int getDepth(){
        return depth;
    }

    public int getBotRow(){
        return botPos[0];
    }

    public int getBotCol(){
        return botPos[1];
    }

    public void setMove(char move){
        this.move = move;
    }

    public char getMove(){
        return move;
    }

    public State getParent(){
        return parent;
    }

    public ArrayList<Crate> getCrateData(){
        return crateData;
    }

    public static void setTargetData(ArrayList<Target> targetData){
        State.targetData = targetData;
    }

    private int[] botPos;
    private ArrayList<Crate> crateData;
    private int depth;
    private String stateStr; // where player and crates are
    private char move;
    private State parent;
    private int aScore; // a star score
    private static ArrayList<Target> targetData;
    
}
