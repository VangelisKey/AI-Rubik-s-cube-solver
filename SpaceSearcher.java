import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class SpaceSearcher {
    private ArrayList<Cube> frontier;
    private HashSet<Cube> closedSet;
    //contains the valid moves that we can use on the cube
    // UP DOWN LEFT RIGHT FACE AND BACK, when looking from side 1 as face and with up being 0
    public static String possible_moves[] = {"UL","UR","DL","DR","RU","RD","LU","LD","FU","FD","BU","BD"};

    SpaceSearcher(){
        this.frontier = new ArrayList<>();
        this.closedSet = new HashSet<>();
    }

    Cube AStarClosedSet(Cube initialState){
        if(initialState.isFinal()) return initialState;
        // step 1: put initial state in the frontier.
        this.frontier.add(initialState);
        // step 2: check for empty frontier.
        while(this.frontier.size() > 0)
        {
            // step 3: get the first node out of the frontier.
            Cube currentState = this.frontier.remove(0);
            // step 4: if final state, return.
            if(currentState.isFinal()) return currentState;
            // step 5: if the node is not in the closed set, put the children at the frontier.
            // else go to step 2.
            if(!this.closedSet.contains(currentState))
            {
                this.closedSet.add(currentState);
                this.frontier.addAll(currentState.getChildren());
                // step 6: sort the frontier based on the heuristic score to get best as first
                Collections.sort(this.frontier); // sort the frontier to get best as first
            }
        }
        return null;
    }
}
