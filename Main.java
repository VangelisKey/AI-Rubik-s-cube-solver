//trexe to programma
import java.util.*;

public class Main{
    //contains the valid moves that we can use on the cube
    // UP DOWN LEFT RIGHT FACE AND BACK, when looking from side 1 as face and with up being 0
    public static String possible_moves[] = {"UL","UR","DL","DR","RU","RD","LU","LD","FU","FD","BU","BD"};
    public static void main(String[] args){
        //getting the number k, sides to be solved.
        Scanner sc=new Scanner(System.in); 
        System.out.println("Insert the number of sides you would like solved in the cube: (1 or 2 should be fine :D)");
        int k = sc.nextInt();
        while (k<0||k>6){
            System.out.println("Invalid input. Number of sides should be 0-6. Insert number of sides again:");
            k = sc.nextInt();
        }
        //making the cube
        Cube initialState = new Cube(true,k);
       
        System.out.println("\nThe initial State of the cube: ");
        initialState.print();

        SpaceSearcher searcher = new SpaceSearcher();
        long start = System.currentTimeMillis();
        Cube terminalState = searcher.AStarClosedSet(initialState); // need K parameter
        long end = System.currentTimeMillis();
        if(terminalState == null) System.out.println("Could not find a solution.");
        else
        {
            // print the path from beggining to start.
            Cube temp = terminalState; // begin from the end.
            ArrayList<Cube> path = new ArrayList<>();
            path.add(terminalState);
            while(temp.getFather() != null) // if father is null, then we are at the root.
            {
                path.add(temp.getFather());
                temp = temp.getFather();
            }
            // reverse the path and print.
            Collections.reverse(path);
            for(Cube item: path)
            {
                item.print();
            }
            // printing the time it took for the search
            System.out.println();
            System.out.println("Search time:" + (double)(end - start) / 1000 + " sec.");  // total time of searching in seconds.

        }
    }
}


