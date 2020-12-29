package ghost;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GhostTest {
    App app;
    Ghost ghost;
    @BeforeEach
    public void init(){
        app = new App();
        app.main(new String[]{""});
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
        ghost = app.ghosts.get(0);
    }

    @Test
    void getCorner() {
        // get the four different corner in the map
        ghost.getCorner();
        assertArrayEquals(new int[]{1,4},ghost.corner[0]);
        assertArrayEquals(new int[]{26,4},ghost.corner[1]);
        assertArrayEquals(new int[]{26,32},ghost.corner[2]);
        assertArrayEquals(new int[]{1,32},ghost.corner[3]);
    }

    @Test
    void reachTarget() {
        ghost.target_y = 1;
        ghost.target_x = 1;
        assertFalse(ghost.reachTarget());
        ghost.x_index = 1;
        ghost.y_index = 1;
        assertTrue(ghost.reachTarget());

    }



    @Test
    void doNotHitWall() {
    }

    @Test
    void getPossibleTarget() {
        // find the possible turning point in each direction
        ghost.x_index = 2;
        ghost.y_index = 4;
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(37);
        ArrayList<int[]> possibleTarget = ghost.getPossibleTarget(integers);
        assertTrue(possibleTarget.size() == 1);
        assertArrayEquals(possibleTarget.get(0),new int[]{1,4,37});

        integers = new ArrayList<>();
        integers.add(39);
        ghost.x_index = 1;
        ghost.y_index = 4;
        possibleTarget = ghost.getPossibleTarget(integers);
        assertTrue(possibleTarget.size() == 1);
        assertArrayEquals(possibleTarget.get(0),new int[]{6,4,39});

        integers = new ArrayList<>();
        integers.add(39);
        integers.add(37);
        integers.add(38);
        ghost.x_index = 5;
        ghost.y_index = 4;
        possibleTarget = ghost.getPossibleTarget(integers);
        assertTrue(possibleTarget.size() == 3);
        assertArrayEquals(possibleTarget.get(0),new int[]{6,4,39});
        assertArrayEquals(possibleTarget.get(1),new int[]{1,4,37});
        assertArrayEquals(possibleTarget.get(2),new int[]{5,3,38});



    }

    @Test
    void getPossibleDirection() {
        //find out the direction at a corner
        ArrayList<Integer> possibleDirection = ghost.getPossibleDirection(5, 4);
        assertEquals(possibleDirection.size(),2);
        assertEquals(possibleDirection.get(0),37);
        assertEquals(possibleDirection.get(1),39);


        possibleDirection = ghost.getPossibleDirection(8, 10);
        assertEquals(possibleDirection.size(),1);
        assertEquals(possibleDirection.get(0),39);


        possibleDirection = ghost.getPossibleDirection(15, 20);
        assertEquals(possibleDirection.size(),2);
        assertEquals(possibleDirection.get(0),37);
        assertEquals(possibleDirection.get(1),39);


    }

    @Test
    void bestSolution() {
        // using bfs algorithm to figure out the pass to a specific location
        ghost.x_index = 6;
        ghost.y_index = 4;
        ghost.target_x = ghost.x_index;
        ghost.target_y = ghost.y_index;
        int mode = ghost.bestSolution(6, 20);
        assertEquals(mode,40);

        ghost.x_index = 6;
        ghost.y_index = 4;
        ghost.target_x = ghost.x_index;
        ghost.target_y = ghost.y_index;
        mode = ghost.bestSolution(12, 8);
        assertEquals(mode,39);


        // test for impossible case
        ghost.x_index = 14;
        ghost.y_index = 8;
        ghost.target_x = ghost.x_index;
        ghost.target_y = ghost.y_index;
        mode = ghost.bestSolution(27, 4);
        assertEquals(mode,-1);


        ghost.x_index = 6;
        ghost.y_index = 20;
        ghost.target_x = ghost.x_index;
        ghost.target_y = ghost.y_index;
        mode = ghost.bestSolution(1, 4);
        assertEquals(mode,38);


    }
    
}