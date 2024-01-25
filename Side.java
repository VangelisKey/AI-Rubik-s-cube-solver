public class Side{
    //contains the colors of the blocks
    private char[][] blocks = new char[3][3];

    //constructor for a side of the cube
    Side(char color){
        this.fill(color);
    }

    Side(char[][] initial_side){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                this.blocks[i][j] = initial_side[i][j];
            }
        }
    }


    //gives the blocks their color
    void fill(char color){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                this.blocks[i][j] = color;
            }
        }
    }
    //returns the block of the side
    char[][] get_side(){
        return  this.blocks;
    }
    //sets the blocks of the side
    void set_side(char[][] blocks){
        this.blocks = blocks;
    }
}