package solver;

public class Tile {

    public Tile(int row, int col){
        visited = false;
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }
    private int row;
    private int col;
    private boolean visited;


}
