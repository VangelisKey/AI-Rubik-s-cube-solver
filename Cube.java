import java.util.*;
public class Cube implements Comparable<Cube>{
    private static String possible_moves[] = {"UL","UR","DL","DR","RU","RD","LU","LD","FU","FD","BU","BD"};
    //array that holds all 6 of our this.sides
    private Side[] sides;
    //array with the 6 colors of the cube
    private static char[] colors;
    private static int[] axon1 = {0,1,2,3};
    private static int[] axon2 = {0,5,2,4};
    private static int[] axon3 = {1,5,3,4};
    private int offPlace;
    private int score;
    private int completed_sides;
    private static int k;
    private Cube father = null;
    private int c1;
    private int c2;
    

    //constructor for the cube
    Cube(boolean random,int n){
        k = n;
        this.init(random);
        
    }
    //make a new cube based on an existing oe
    Cube(Side[] initial_sides,int k){
        this.sides =  new Side[6];
        for (int c = 0; c<6; c++){
            this.sides[c] = new Side(initial_sides[c].get_side());     
        }
    }
    //cube initialization
    public void init(boolean random){

        colors = new char[6];
        this.sides =  new Side[6];
        colors[0] = 'W';
        colors[1] = 'R';
        colors[2] = 'Y';
        colors[3] = 'O';
        colors[4] = 'G';
        colors[5] = 'B';
        for (int c = 0; c<6; c++){
            this.sides[c] = new Side(colors[c]);
        }
        if(random){

            this.randomize(50);
        }

    }
    void randomize(int times){
        Random rand = new Random();
        for (int i=0;i<times;i++){

            this.move(possible_moves[rand.nextInt(12)]);

        }
    }

    //return the array of this.sides
    public Side[] get_sides(){
        return this.sides;
    }


    //intererperets the array of directions
    void move(String direction){
        int sider = 0;
        int x = 0;
        int y = 0;
        int[] axon = new int[4];
        boolean follow_x = true;
        boolean clockwise = true;
        boolean clockwiser = true;
        boolean face = true;
        if (direction.startsWith("U")){
            sider = 0;
            x = 3;
            y = 0;
            axon = axon3;
            if (direction.endsWith("R")){
                clockwise = false;
                clockwiser = false;
            }

        }
        if (direction.startsWith("D")){
            sider = 2;
            x = 3;
            y = 2;
            axon = axon3;
            if (direction.endsWith("R")){
                clockwise = false;
                clockwiser = true;
            }else{
                clockwiser = false;
            }

        }
        if (direction.startsWith("R")){
            sider = 5;
            x = 2;
            y = 3;
            follow_x = false;
            axon = axon1;
            if (direction.endsWith("D")){
                clockwise = false;
                clockwiser = false;
            }else{
                clockwise = true;
                clockwiser= true;
            }
        }
        if (direction.startsWith("L")){
            sider = 4;
            x = 0;
            y = 3;
            follow_x = false;
            axon = axon1;
            if (direction.endsWith("D")){
                clockwise = false;
                clockwiser = true;
            }else{
                clockwise = true;
                clockwiser = false;
            }
        }
        if (direction.startsWith("F")){
            sider = 1;
            x = 2;
            y = 3;
            follow_x = false;
            axon = axon2;
            face = true;
            if (direction.endsWith("U")){
                clockwiser = true;
                clockwise = true;
            }else{
                clockwiser = false;
                clockwise = false;
            }

        }
        if (direction.startsWith("B")){
            sider = 3;
            x = 0;
            y = 3;
            follow_x = false;
            axon = axon2;
            face = false;
            if (direction.endsWith("U")){
                clockwise = false;
                clockwiser = true;
            }else{
                clockwiser = false;
            }
        }


        this.swap_line(x,y,follow_x,axon,clockwise,face);
        this.rotate(sider,clockwiser);

    }
    //moves 1 line of blocks of the cube
     void swap_line(int x, int y,boolean follow_x, int[] axon,boolean clockwise,boolean face){
        if (axon!=axon1){
            this.rotate(3,true);
            this.rotate(3,true);

        }
        if(axon!=axon2){
            if(clockwise){
                this.swap_line_clockwise(x, y, follow_x, axon);
            }else{
                this.swap_line_counter_clockwise(x, y, follow_x, axon);
            }
        }else{
            if(clockwise){
                if (face){
                    this.swap_axon2_F_clockwise();

                }else{
                    this.swap_axon2_B_clockwise();
                }

            }else{
                if (face){
                    this.swap_axon2_F_counter_clockwise();
                }else{
                    this.swap_axon2_B_counter_clockwise();
                }
            }
        }
        if (axon!=axon1){
            this.rotate(3,true);
            this.rotate(3,true);
        }
    }
     void swap_line_clockwise(int x, int y,boolean follow_x, int[] axon){

        char[][] blocks_first_side_copy = new char[3][3];
        for (int i=0;i<3;i++){
            for(int j=0; j<3;j++){
                blocks_first_side_copy[i][j] = this.sides[axon[0]].get_side()[i][j];
            }
        }

        char[][] blocks_copy=this.sides[axon[0]].get_side();
        for(int k=0;k<3;k++){
            if (follow_x){
                for(int i =0;i<x;i++){
                    blocks_copy[i][y] = this.sides[axon[k+1]].get_side()[i][y];
                }
            }else{
                for(int j =0;j<y;j++){
                    blocks_copy[x][j] = this.sides[axon[k+1]].get_side()[x][j];
                }
            }
            this.sides[axon[k]].set_side(blocks_copy);
            blocks_copy=this.sides[axon[k+1]].get_side();
        }

        if (follow_x){
            for(int i =0;i<x;i++){
                blocks_copy[i][y] = blocks_first_side_copy[i][y];
            }
        }else{
            for(int j =0;j<y;j++){
                blocks_copy[x][j] = blocks_first_side_copy[x][j];
            }
        }
        this.sides[axon[3]].set_side(blocks_copy);
    }
     void swap_line_counter_clockwise(int x, int y,boolean follow_x, int[] axon){
        char[][] blocks_last_side_copy = new char[3][3];
        for (int i=0;i<3;i++){
            for(int j=0; j<3;j++){
                blocks_last_side_copy[i][j] = this.sides[axon[3]].get_side()[i][j];
            }
        }

        char[][] blocks_copy=this.sides[axon[3]].get_side();
        for(int k=3;k>0;k--){
            if (follow_x){
                for(int i = 0;i<x;i++){
                    blocks_copy[i][y] = this.sides[axon[k-1]].get_side()[i][y];
                }
            }else{
                for(int j = 0;j<y;j++){
                    blocks_copy[x][j] = this.sides[axon[k-1]].get_side()[x][j];
                }
            }
            this.sides[axon[k]].set_side(blocks_copy);
            blocks_copy=this.sides[axon[k-1]].get_side();

        }
        if (follow_x){
            for(int i =0;i<x;i++){
                blocks_copy[i][y] = blocks_last_side_copy[i][y];
            }
        }else{
            for(int j =0;j<y;j++){
                blocks_copy[x][j] = blocks_last_side_copy[x][j];
            }
        }
        this.sides[axon[0]].set_side(blocks_copy);
    }
    //rotates the side next to the line of blocks
     void rotate(int side, boolean clockwise){
        char[][] blocks = new char[3][3];
        char[][] blocks_copy = this.sides[side].get_side();
        if (clockwise){
            for (int i = 0; i<3; i++){
                for (int j = 0; j<3; j++){
                    blocks[2-j][i] = blocks_copy[i][j];
                }
            }
        }else{
            for (int i = 0; i<3; i++){
                for (int j = 0; j<3; j++){
                    blocks[j][2-i] = blocks_copy[i][j];
                }
            }
        }

        this.sides[side].set_side(blocks);

    }
    //seperate function to turn face clockwise
     void swap_axon2_F_clockwise(){
        char[][] blocks_first_side_copy = new char[3][3];
        Side[] rotated_sides = new Side[4];
        for (int k=0;k<4;k++){
            rotated_sides[k] = new Side('N');
            rotated_sides[k].set_side((this.sides[axon2[k]].get_side()));

        }

        for (int i=0;i<3;i++){
            for(int j=0; j<3;j++){
                blocks_first_side_copy[i][j] = this.sides[axon2[0]].get_side()[i][j];
            }
        }

        char[][] blocks_copy=this.sides[axon2[0]].get_side();
        int l = 2;
        for(int i =0;i<3;i++){
            blocks_copy[i][2] = rotated_sides[3].get_side()[2][l];
            l--;
        }
        this.sides[axon2[0]].set_side(blocks_copy);

        blocks_copy=this.sides[axon2[3]].get_side();
        for(int j =0;j<3;j++){
            blocks_copy[2][j] = rotated_sides[2].get_side()[j][0];
        }
        this.sides[axon2[3]].set_side(blocks_copy);
        blocks_copy=this.sides[axon2[2]].get_side();
        l=2;
        for(int i =0;i<3;i++){
            blocks_copy[l][0] = rotated_sides[1].get_side()[0][i];
            l--;
        }
        this.sides[axon2[2]].set_side(blocks_copy);
        blocks_copy=this.sides[axon2[1]].get_side();
        for(int j =0;j<3;j++){
            blocks_copy[0][j] = blocks_first_side_copy[j][2];
        }
        this.sides[axon2[1]].set_side(blocks_copy);
    }
    //seperate function to turn face counter clockwise
     void swap_axon2_F_counter_clockwise(){
        char[][] blocks_last_side_copy = new char[3][3];
        Side[] rotated_sides = new Side[4];
        for (int k=0;k<4;k++){
            rotated_sides[k] = new Side('N');
            rotated_sides[k].set_side((this.sides[axon2[k]].get_side()));
        }

        for (int i=0;i<3;i++){
            for(int j=0; j<3;j++){
                blocks_last_side_copy[i][j] = this.sides[axon2[0]].get_side()[i][j];
            }
        }

        char[][] blocks_copy=this.sides[axon2[0]].get_side();
        int l = 2;
        for(int i =0;i<3;i++){
            blocks_copy[i][2] = rotated_sides[1].get_side()[0][i];
        }
        this.sides[axon2[0]].set_side(blocks_copy);

        blocks_copy=this.sides[axon2[1]].get_side();
        for(int j =0;j<3;j++){
            blocks_copy[0][l] = rotated_sides[2].get_side()[j][0];
            l--;
        }
        this.sides[axon2[1]].set_side(blocks_copy);

        blocks_copy=this.sides[axon2[2]].get_side();
        l=2;
        for(int i =0;i<3;i++){
            blocks_copy[i][0] = rotated_sides[3].get_side()[2][i];
        }
        this.sides[axon2[2]].set_side(blocks_copy);

        blocks_copy=this.sides[axon2[3]].get_side();
        l=2;
        for(int j =0;j<3;j++){
            blocks_copy[2][j] = blocks_last_side_copy[l][2];
            l--;
        }
        this.sides[axon2[3]].set_side(blocks_copy);
    }
    //seperate function to turn back clockwise
     void swap_axon2_B_clockwise(){
        char[][] blocks_first_side_copy = new char[3][3];
        Side[] rotated_sides = new Side[4];
        for (int k=0;k<4;k++){
            rotated_sides[k] = new Side('N');
            rotated_sides[k].set_side((this.sides[axon2[k]].get_side()));

        }

        for (int i=0;i<3;i++){
            for(int j=0; j<3;j++){
                blocks_first_side_copy[i][j] = this.sides[axon2[0]].get_side()[i][j];
            }
        }

        char[][] blocks_copy=this.sides[axon2[0]].get_side();
        int l = 2;
        for(int i =0;i<3;i++){
            blocks_copy[i][0] = rotated_sides[3].get_side()[0][l];
            l--;
        }
        this.sides[axon2[0]].set_side(blocks_copy);

        blocks_copy=this.sides[axon2[3]].get_side();
        for(int j =0;j<3;j++){
            blocks_copy[0][j] = rotated_sides[2].get_side()[j][2];
        }
        this.sides[axon2[3]].set_side(blocks_copy);
        blocks_copy=this.sides[axon2[2]].get_side();
        l=2;
        for(int i =0;i<3;i++){
            blocks_copy[l][2] = rotated_sides[1].get_side()[2][i];
            l--;
        }
        this.sides[axon2[2]].set_side(blocks_copy);
        blocks_copy=this.sides[axon2[1]].get_side();
        for(int j =0;j<3;j++){
            blocks_copy[2][j] = blocks_first_side_copy[j][0];
        }
        this.sides[axon2[1]].set_side(blocks_copy);
    }
    //seperate function to turn back counter clockwise
     void swap_axon2_B_counter_clockwise(){
        char[][] blocks_last_side_copy = new char[3][3];
        Side[] rotated_sides = new Side[4];
        for (int k=0;k<4;k++){
            rotated_sides[k] = new Side('N');
            rotated_sides[k].set_side((this.sides[axon2[k]].get_side()));
        }

        for (int i=0;i<3;i++){
            for(int j=0; j<3;j++){
                blocks_last_side_copy[i][j] = this.sides[axon2[0]].get_side()[i][j];
            }
        }

        char[][] blocks_copy=this.sides[axon2[0]].get_side();
        int l = 2;
        for(int i =0;i<3;i++){
            blocks_copy[i][0] = rotated_sides[1].get_side()[2][i];
        }
        this.sides[axon2[0]].set_side(blocks_copy);

        blocks_copy=this.sides[axon2[1]].get_side();
        for(int j =0;j<3;j++){
            blocks_copy[2][l] = rotated_sides[2].get_side()[j][2];
            l--;
        }
        this.sides[axon2[1]].set_side(blocks_copy);

        blocks_copy=this.sides[axon2[2]].get_side();
        for(int i =0;i<3;i++){
            blocks_copy[i][2] = rotated_sides[3].get_side()[0][i];
        }
        this.sides[axon2[2]].set_side(blocks_copy);

        blocks_copy=this.sides[axon2[3]].get_side();
        l=2;
        for(int j =0;j<3;j++){
            blocks_copy[0][j] = blocks_last_side_copy[l][0];
            l--;
        }
        this.sides[axon2[3]].set_side(blocks_copy);
    }
    //return father
    Cube getFather()
    {
        return this.father;
    }
    //set father
    void setFather(Cube father)
    {
        this.father = father;
    }
    //function that generates the children for each state
    ArrayList<Cube> getChildren(){
        ArrayList<Cube> children=new ArrayList<>();
        //Create a new child for every move "UL","UR","DL","DR","RU","RD","LU","LD","FU","FD","BU","BD"
        Cube child;
        for(int i = 0; i<12;i++){
            child=new Cube(this.sides,k);
            child.move(possible_moves[i]);
            child.calculate_score();
            child.setFather(this);          //Every child has a father
            children.add(child); 
        }
        return children;
    }
    // manhattan distance
    private int ManhattanDistance(){
        int[] coordinates= new int[3];
        double distance =0;
        for(int c = 0; c<6; c++){
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    if (i==1 && j==1){continue;}
                    coordinates = find_correct_place(i,j,c); //the incomplete part
                    distance = Math.pow((Math.pow((coordinates[0]-i),2) + Math.pow((coordinates[1]-j),2) + Math.pow((coordinates[2]-c),2)),1/2);
                }
            }
        }
        return (int)Math.round(distance); 
    }
    //incomplete
    int[] find_correct_place(int x, int y, int z){
        int[] coordinates = new int[3];
        char color =sides[z].get_side()[1][1];

        if(color=='W'){
            coordinates[2]=0;
        } else if (color=='R') {
            coordinates[2]=1;
        } else if (color=='Y') {
            coordinates[2]=2;
        } else if (color=='O') {
            coordinates[2]=3;
        } else if (color=='G') {
            coordinates[2]=4;
        }else {// Blue
            coordinates[2]=5;
        }
        
        return coordinates;
    }
    // our heuretic
    private void count_all_out_of_place(){
        int count = 0;
        for(int c=0; c<6;c++){
            for (int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(i==1 && j==1){continue;}
                    if(this.sides[c].get_side()[i][j]!=this.sides[c].get_side()[1][1]){
                        count++; //adds to score when a cubie is the wrong color
                    }
                    if(i==0 && j==0 || i==2 && j == 0 || i==0 && j==2|| i==2 && j==2){
                       if(isEdgecorrect(i,j,c)){count-=2;}//subtracts 2 from the score when an edge is fully correct 
                    }else if (!(i==1 && j==1)){
                        if(isCornercorrect(i,j,c)){count-=3;}//subtracts 3 from the score when a corner is fully correct  
                    }
                }
            }
            
        }
        set_off_place(count);
    }

    private boolean isEdgecorrect(int i, int j, int c){       
        //Check to see if the cubies are in the right position and not just the right side
        boolean correct = false;
        if(this.sides[c].get_side()[i][j]==this.sides[c].get_side()[1][1]){
            if (i==0&j==1) {
                c1=up(this.sides[c].get_side()[i][j]);

                if(this.sides[c1].get_side()[2][1]==this.sides[c1].get_side()[1][1]){
                    correct = true;
                }
                } else if (i==1&j==0) {
                c1=left(this.sides[c].get_side()[i][j]);

                    if(this.sides[c1].get_side()[1][2]==this.sides[c1].get_side()[1][1]){
                        correct = true;
                    }
                }  else if (i==2&j==1) {
                        c1=down(this.sides[c].get_side()[i][j]);

                    if(this.sides[c1].get_side()[0][1]==this.sides[c1].get_side()[1][1]){
                        correct = true;
                    }
                } else if (i==1&j==2) {
                    c1=right(this.sides[c].get_side()[i][j]);
                if(this.sides[c1].get_side()[1][0]==this.sides[c1].get_side()[1][1]){
                    correct = true;
                }
            }
        }
        return correct;
    }
    private int[] findEdges(int i, int j, int c){   //incomplete
        //Check to see if the cubies are in the right position and not just the right side
        int coordinates[] = new int[3];
        boolean correct = false;
        if(this.sides[c].get_side()[i][j]==this.sides[c].get_side()[1][1]){
            if (i==0&j==1) {
                c1=up(this.sides[c].get_side()[i][j]);

                if(this.sides[c1].get_side()[2][1]==this.sides[c1].get_side()[1][1]){
                    correct = true;
                }
                } else if (i==1&j==0) {
                c1=left(this.sides[c].get_side()[i][j]);

                    if(this.sides[c1].get_side()[1][2]==this.sides[c1].get_side()[1][1]){
                        correct = true;
                    }
                }  else if (i==2&j==1) {
                        c1=down(this.sides[c].get_side()[i][j]);

                    if(this.sides[c1].get_side()[0][1]==this.sides[c1].get_side()[1][1]){
                        correct = true;
                    }
                } else if (i==1&j==2) {
                    c1=right(this.sides[c].get_side()[i][j]);
                if(this.sides[c1].get_side()[1][0]==this.sides[c1].get_side()[1][1]){
                    correct = true;
                }
            }
        }
        return coordinates;
    }

    private int[] findCorners(int i, int j, int c){ //incomplete /* */
        
        boolean correct = false;
        int coordinates[] = new int[3];
        if(this.sides[c].get_side()[i][j]==this.sides[c].get_side()[1][1]){
            if(i==0&j==0){//For corner [0,1] check the other sides to see if it is placed correctly
                c1=left(this.sides[c].get_side()[i][j]);
                c2=up(this.sides[c].get_side()[i][j]);

                if(this.sides[c1].get_side()[0][2]==this.sides[c1].get_side()[1][1]&this.sides[c2].get_side()[2][0]==this.sides[c2].get_side()[1][1]){
                    correct = true;
                }
            } else if (i==2&j==2) {
                c1=right(this.sides[c].get_side()[i][j]);
                c2=down(this.sides[c].get_side()[i][j]);

                if(this.sides[c1].get_side()[2][0]==this.sides[c1].get_side()[1][1]&this.sides[c2].get_side()[0][2]==this.sides[c2].get_side()[1][1]){
                    correct = true;
                }

            } else if (i==2&j==0) {
                c1=left(this.sides[c].get_side()[i][j]);
                c2=down(this.sides[c].get_side()[i][j]);

                if(this.sides[c1].get_side()[2][2]==this.sides[c1].get_side()[1][1]&this.sides[c2].get_side()[0][0]==this.sides[c2].get_side()[1][1]){
                    correct = true;
                }
            } else if (i==0&j==2) {
                c1=right(this.sides[c].get_side()[i][j]);
                c2=up(this.sides[c].get_side()[i][j]);

                if(this.sides[c1].get_side()[0][0]==this.sides[c1].get_side()[1][1]&this.sides[c2].get_side()[2][2]==this.sides[c2].get_side()[1][1]){
                    correct = true;
                }
            }
        }
        return coordinates;
    }

    private boolean isCornercorrect(int i, int j, int c){
        boolean correct = false;
        if(this.sides[c].get_side()[i][j]==this.sides[c].get_side()[1][1]){
            if(i==0&j==0){//For corner [0,1] check the other sides to see if it is placed correctly
                c1=left(this.sides[c].get_side()[i][j]);
                c2=up(this.sides[c].get_side()[i][j]);

                if(this.sides[c1].get_side()[0][2]==this.sides[c1].get_side()[1][1]&this.sides[c2].get_side()[2][0]==this.sides[c2].get_side()[1][1]){
                    correct = true;
                }
            } else if (i==2&j==2) {
                c1=right(this.sides[c].get_side()[i][j]);
                c2=down(this.sides[c].get_side()[i][j]);

                if(this.sides[c1].get_side()[2][0]==this.sides[c1].get_side()[1][1]&this.sides[c2].get_side()[0][2]==this.sides[c2].get_side()[1][1]){
                    correct = true;
                }

            } else if (i==2&j==0) {
                c1=left(this.sides[c].get_side()[i][j]);
                c2=down(this.sides[c].get_side()[i][j]);

                if(this.sides[c1].get_side()[2][2]==this.sides[c1].get_side()[1][1]&this.sides[c2].get_side()[0][0]==this.sides[c2].get_side()[1][1]){
                    correct = true;
                }
            } else if (i==0&j==2) {
                c1=right(this.sides[c].get_side()[i][j]);
                c2=up(this.sides[c].get_side()[i][j]);

                if(this.sides[c1].get_side()[0][0]==this.sides[c1].get_side()[1][1]&this.sides[c2].get_side()[2][2]==this.sides[c2].get_side()[1][1]){
                    correct = true;
                }
            }
        }
        return correct;
    }

    int up(char color){
        if(color=='R'){
            return 0;
        } else if (color=='W') {
            return 3;
        } else if (color=='G') {
            return 0;
        } else if (color=='B') {
            return 0;
        } else if (color=='O') {
            return 2;
        }else {// Yellow
            return 1;
        }
    }
    int down(char color){
        if(color=='R'){
            return 2;
        } else if (color=='W') {
            return 1;
        } else if (color=='G') {
            return 2;
        } else if (color=='B') {
            return 2;
        } else if (color=='O') {
            return 0;
        }else {//Yellow
            return 3;
        }
    }
    int left(char color){
        if(color==1){
            return 4;
        } else if (color=='W') {
            return 4;
        } else if (color=='G') {
            return 3;
        } else if (color=='B') {
            return 1;
        } else if (color=='O') {
            return 4;
        }else {//Yellow
            return 4;
        }
    }
    int right(char color){
        if(color=='R'){
            return 5;
        } else if (color=='W') {
            return 5;
        } else if (color=='G') {
            return 1;
        } else if (color=='B') {
            return 3;
        } else if (color=='O') {
            return 5;
        }else {//Yellow
            return 5;
        }
    }
 

    public void set_off_place(int offPlace){
        this.offPlace = offPlace;
    }

    public void set_score(int offPlace){
        this.score = offPlace;
    }

    public void calculate_score(){
        this.count_all_out_of_place();
        this.isFinal();
        this.set_score(this.offPlace-(30-this.completed_sides*5)*this.completed_sides);
    }
    public boolean isFinal(){
        this.completed_sides=0;
        boolean completed_side;
        boolean completed = true;
        for (int c = 0; c<6; c++){
            completed_side = true;
            for (int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(this.sides[c].get_side()[i][j]!=this.sides[c].get_side()[1][1]){
                       completed = false;
                       completed_side = false;
                    }
                }
            }
            if (completed_side == true){
                this.completed_sides++;
            }
            if (completed_sides>=k){
                return true;
            }
        }
        return completed;  
    }

    //prints the current state of the cube
    void print(){//horizontally

       /*/ System.out.println("-------------------------------------");

        System.out.print("         "+this.sides[5].get_side()[0][0]+this.sides[5].get_side()[0][1]+this.sides[5].get_side()[0][2]+"\n");
        System.out.print("         "+this.sides[5].get_side()[1][0]+this.sides[5].get_side()[1][1]+this.sides[5].get_side()[1][2]+"\n");
        System.out.print("         "+this.sides[5].get_side()[2][0]+this.sides[5].get_side()[2][1]+this.sides[5].get_side()[2][2]+"\n"+"\n");

        System.out.print(""+this.sides[0].get_side()[0][0]+this.sides[0].get_side()[0][1]+this.sides[0].get_side()[0][2]+"      "+this.sides[1].get_side()[0][0]+this.sides[1].get_side()[0][1]+this.sides[1].get_side()[0][2]+"      "+this.sides[2].get_side()[0][0]+this.sides[2].get_side()[0][1]+this.sides[2].get_side()[0][2]+"      "+this.sides[3].get_side()[0][0]+this.sides[3].get_side()[0][1]+this.sides[3].get_side()[0][2]+"\n");
        System.out.print(""+this.sides[0].get_side()[1][0]+this.sides[0].get_side()[1][1]+this.sides[0].get_side()[1][2]+"      "+this.sides[1].get_side()[1][0]+this.sides[1].get_side()[1][1]+this.sides[1].get_side()[1][2]+"      "+this.sides[2].get_side()[1][0]+this.sides[2].get_side()[1][1]+this.sides[2].get_side()[1][2]+"      "+this.sides[3].get_side()[1][0]+this.sides[3].get_side()[1][1]+this.sides[3].get_side()[1][2]+"\n");
        System.out.print(""+this.sides[0].get_side()[2][0]+this.sides[0].get_side()[2][1]+this.sides[0].get_side()[2][2]+"      "+this.sides[1].get_side()[2][0]+this.sides[1].get_side()[2][1]+this.sides[1].get_side()[2][2]+"      "+this.sides[2].get_side()[2][0]+this.sides[2].get_side()[2][1]+this.sides[2].get_side()[2][2]+"      "+this.sides[3].get_side()[2][0]+this.sides[3].get_side()[2][1]+this.sides[3].get_side()[2][2]+"\n"+"\n");

        System.out.print("         "+this.sides[4].get_side()[0][0]+this.sides[4].get_side()[0][1]+this.sides[4].get_side()[0][2]+"\n");
        System.out.print("         "+this.sides[4].get_side()[1][0]+this.sides[4].get_side()[1][1]+this.sides[4].get_side()[1][2]+"\n");
        System.out.print("         "+this.sides[4].get_side()[2][0]+this.sides[4].get_side()[2][1]+this.sides[4].get_side()[2][2]+"\n");

        System.out.println("\n"+"-------------------------------------");
    /*/
        System.out.println("-------------------------------------");//vertically

        System.out.print("         "+this.sides[0].get_side()[0][0]+this.sides[0].get_side()[1][0]+this.sides[0].get_side()[2][0]+"\n");
        System.out.print("         "+this.sides[0].get_side()[0][1]+this.sides[0].get_side()[1][1]+this.sides[0].get_side()[2][1]+"\n");
        System.out.print("         "+this.sides[0].get_side()[0][2]+this.sides[0].get_side()[1][2]+this.sides[0].get_side()[2][2]+"\n"+"\n");

        System.out.print(""+this.sides[4].get_side()[0][0]+this.sides[4].get_side()[1][0]+this.sides[4].get_side()[2][0]+"      "+this.sides[1].get_side()[0][0]+this.sides[1].get_side()[1][0]+this.sides[1].get_side()[2][0]+"      "+this.sides[5].get_side()[0][0]+this.sides[5].get_side()[1][0]+this.sides[5].get_side()[2][0]+"\n");
        System.out.print(""+this.sides[4].get_side()[0][1]+this.sides[4].get_side()[1][1]+this.sides[4].get_side()[2][1]+"      "+this.sides[1].get_side()[0][1]+this.sides[1].get_side()[1][1]+this.sides[1].get_side()[2][1]+"      "+this.sides[5].get_side()[0][1]+this.sides[5].get_side()[1][1]+this.sides[5].get_side()[2][1]+"\n");
        System.out.print(""+this.sides[4].get_side()[0][2]+this.sides[4].get_side()[1][2]+this.sides[4].get_side()[2][2]+"      "+this.sides[1].get_side()[0][2]+this.sides[1].get_side()[1][2]+this.sides[1].get_side()[2][2]+"      "+this.sides[5].get_side()[0][2]+this.sides[5].get_side()[1][2]+this.sides[5].get_side()[2][2]+"\n"+"\n");

        System.out.print("         "+this.sides[2].get_side()[0][0]+this.sides[2].get_side()[1][0]+this.sides[2].get_side()[2][0]+"\n");
        System.out.print("         "+this.sides[2].get_side()[0][1]+this.sides[2].get_side()[1][1]+this.sides[2].get_side()[2][1]+"\n");
        System.out.print("         "+this.sides[2].get_side()[0][2]+this.sides[2].get_side()[1][2]+this.sides[2].get_side()[2][2]+"\n"+"\n");

        System.out.print("         "+this.sides[3].get_side()[0][0]+this.sides[3].get_side()[1][0]+this.sides[3].get_side()[2][0]+"\n");
        System.out.print("         "+this.sides[3].get_side()[0][1]+this.sides[3].get_side()[1][1]+this.sides[3].get_side()[2][1]+"\n");
        System.out.print("         "+this.sides[3].get_side()[0][2]+this.sides[3].get_side()[1][2]+this.sides[3].get_side()[2][2]+"\n");

        System.out.println("\n"+"-------------------------------------");
    }


    @Override
    public int compareTo(Cube s) {
        return Double.compare(this.score, s.score); // compare based on the heuristic score.
    }
    

   @Override
    public boolean equals(Object objective)
    {
     // check for equality of numbers in the tiles.
        for(int c=0;c<6;c++){
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(this.get_sides()[c].get_side()[i][j]!=((Cube)objective).get_sides()[c].get_side()[i][j]){
                        return false;
                    }
                }
    
            }
        }
        return true;
    
    }
    // override this for proper hash set comparisons.
    @Override
    public int hashCode()
    {
        return this.identifier();
    }

    int identifier()
    {
        int result = 0;
        for (int c =0;c<6;c++){
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    // a unique sum based on the numbers in each state.
                    // for another state, this will not be the same
                    result += (Math.pow(6, (3 * i) + c)  + Math.pow(3,(3*c)+j) + Math.pow(3,(3*i)+j))*c;
                }
            }
        }

        return result;
    }
}
