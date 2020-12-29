package ghost;

import java.util.ArrayList;

public class Path {
    int x;
    int y;
    ArrayList<Integer> trace;
    ArrayList<int[]> movement;

    public Path(int x, int y, ArrayList<Integer> path,ArrayList<int[]> movement) {
        this.x = x;
        this.y = y;
        this.trace = path;
        this.movement = movement;
    }
    public Path(int x, int y, ArrayList<Integer> path) {
        this.x = x;
        this.y = y;
        this.trace = path;
    }
    boolean arrived(int[] position){
        return position[0] == x && position[1] == y;
    }


}
