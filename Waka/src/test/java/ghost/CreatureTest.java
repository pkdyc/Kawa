package ghost;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.*;

class CreatureTest {
    App app;
    @BeforeEach
    public void init(){
        app = new App();
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
    }

    @Test
    void changeMod() {
        assertNotNull(app.ghosts);
        Ghost ghost = app.ghosts.get(0);
        assertTrue(ghost.mode == 39);
        ghost.changeMod(40);
        assertTrue(ghost.mode == 40);
    }

    @Test
    void move() {
        // test before move
        Ghost ghost = app.ghosts.get(0);
        assertTrue(ghost.cor_x % 16 == 0);
        assertTrue(app.player.cor_x % 16 == 0);
    }

    @Test
    void display() {
        app.draw();
        Ghost ghost = app.ghosts.get(0);
        assertNotNull(ghost.pImage);
        assertTrue(ghost.pImage == ghost.originalImage);
    }

    @Test
    void collision() {
        //collision with wall
        Ghost ghost = app.ghosts.get(0);
        assertTrue(ghost.collision(0,13));
        assertFalse(ghost.collision(1,13));
        //eat fruit
        Player player = app.player;
        player.collision(1,13);
        assertTrue(app.cell[13][1].type.equals("0"));
    }

    @Test
    void reset() {
        //check the reset function and figure out if it works
        Ghost ghost = app.ghosts.get(0);
        Player player = app.player;
        ghost.reset();
        player.reset();
        assertTrue(ghost.x_index == ghost.init_x);
        assertTrue(ghost.y_index == ghost.init_y);
        assertTrue(ghost.cor_x /16 == ghost.init_x);
        assertTrue(ghost.cor_y /16 == ghost.init_y);
        assertTrue(player.y_index == player.init_y);
        assertTrue(player.x_index == player.init_x);
        assertTrue(player.cor_y / 16 == player.init_y);
        assertTrue(player.cor_x / 16 == player.init_x);
    }

    @Test
    void checkOutOfBound() {
        Ghost ghost = app.ghosts.get(0);
        assertFalse(ghost.superCheckOutOfBound(1,13));
        assertTrue(ghost.superCheckOutOfBound(0,13));
        assertTrue(ghost.superCheckOutOfBound(0,0));
        assertTrue(ghost.superCheckOutOfBound(1,1));
    }

    @Test
    void isWall() {
        Ghost ghost = app.ghosts.get(0);
        assertFalse(ghost.superCheckOutOfBound(1,13));
        assertTrue(ghost.superCheckOutOfBound(0,13));
    }
}