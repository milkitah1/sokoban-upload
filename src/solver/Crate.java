package solver;

public class Crate {

    

    public Crate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void updateRow(int dRow){
        this.row = dRow;
    }
    public void updateCol(int dCol){
        this.col = dCol;
    }

    // returns string representaion of crate coordinate
    public String getPosition(){
        return Integer.toString(row).concat("," + Integer.toString(col));
    }

    private int row;
    private int col;
    
}
