package solver;

public class Path {

    public Path(char move,Path parent, int row, int col){
        this(row, col, parent);
        this.move = move;

    }
    // no parent path
    public Path(int row , int col, Path parent){
        this.row = row;
        this.col = col;
        move = '\0';
        this.parent = parent;
    }

    public char getMove(){
        return move;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public Path getParent(){
        return parent;
    }
    
    private Path parent;
    private char move;
    private int row;
    private int col;
}
