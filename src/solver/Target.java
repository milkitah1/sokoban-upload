package solver;
import java.util.LinkedList;
import java.util.Queue;

public class Target {

    

    public Target(int row, int col, char[][] mapData) {
        this.row = row;
        this.col = col;
        distanceField = new int[mapData.length][mapData[0].length];
        logDistances(row, col, mapData);
    }


    /*
     * finds distances of each tile to target considering walls
     */
    public void logDistances(int row, int col, char[][] mapData){
        Queue<int[]> bfs = new LinkedList<>();
        int[] tempCoordinate = new int[3];

        boolean[][] visited = new boolean[mapData.length][mapData[0].length];

        int[] dRow = {-1 , 1 , 0 , 0};
        int[] dCol = {0,0, -1, 1};

        int i = 0;
        int rowToVisit;
        int colToVisit;

        int curDist = 0;

        visited[row][col] = true;

        bfs.add(new int[]{row, col , curDist});
        
        while(!bfs.isEmpty()){
            tempCoordinate = bfs.poll();
            rowToVisit = tempCoordinate[0];
            colToVisit = tempCoordinate[1];
            curDist = tempCoordinate[2];
            
            
            for(i = 0; i < 4; i++){
                int newRow = rowToVisit + dRow[i];
                int newCol = colToVisit + dCol[i];

                if(mapData[newRow][newCol] != '#' && !visited[newRow][newCol]){
                    
                    bfs.add(new int[]{newRow , newCol , curDist + 1});
                    distanceField[newRow][newCol] = curDist + 1;
                    visited[newRow][newCol] = true;
                }
            }

        }
    
    }

    public int[][] getDistanceField(){
        return distanceField;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    private int row;
    private int col;
    private int[][] distanceField; // min distance for each tile in the map to the target with respect to walls 
}

