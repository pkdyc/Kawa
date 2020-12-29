package ghost;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    App app;
    @BeforeEach
    public void init(){
        app = new App();
        app.main(new String[]{""});
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
    }


    @Test
    void meetGhost() {
        // test will the live lost after collapse with ghost
        Player player = app.player;
        player.x_index = 5;
        player.y_index = 5;
        Ghost ghost = app.ghosts.get(0);
        ghost.x_index = 5;
        ghost.y_index = 5;
        player.meetGhost();
        assertTrue(app.lives == 9);
    }

    @Test
    void changeIcon() {
        // testing the change of Icon
        // test for open / closed mouth
        Player player = app.player;
        player.frameCount = 0;
        player.changeIcon();
        assertTrue(player.minusCount == false);
        assertTrue(player.pImage == player.closedMouth);

        // test the image in different direction
        player.frameCount = 8;
        player.mode = 37;
        player.changeIcon();
        assertTrue(player.pImage == player.sprite.get(37));

        player.frameCount = 8;
        player.mode = 38;
        player.changeIcon();
        assertTrue(player.pImage == player.sprite.get(38));

        player.frameCount = 8;
        player.mode = 39;
        player.changeIcon();
        assertTrue(player.pImage == player.sprite.get(39));

        player.frameCount = 8;
        player.mode = 40;
        player.changeIcon();
        assertTrue(player.pImage == player.sprite.get(40));


    }

    @Test
    void findPossibleTurn() {
        // test if the player are able to find out the next possible turn in multiple directions
        Player player = app.player;
        player.x_index = 1;
        player.y_index = 4;
        player.mode = 39;
        player.findPossibleTurn(40);
        assertTrue(player.possibleX == 1);
        assertTrue(player.possibleY == 4);

        player = app.player;
        player.x_index = 2;
        player.y_index = 4;
        player.mode = 39;
        player.goUp = false;
        player.goDown = false;
        player.goRight = false;
        player.goLeft = false;

        player.findPossibleTurn(40);
        assertTrue(player.possibleX == 6);
        assertTrue(player.possibleY == 4);


        player = app.player;
        player.x_index = 9;
        player.y_index = 8;
        player.mode = 39;
        player.goUp = false;
        player.goDown = false;
        player.goRight = false;
        player.goLeft = false;
        player.findPossibleTurn(40);
        assertTrue(player.possibleX == 9);
        assertTrue(player.possibleY == 8);


        player = app.player;
        player.x_index = 8;
        player.y_index = 8;
        player.mode = 37;
        player.goUp = false;
        player.goDown = false;
        player.goRight = false;
        player.goLeft = false;
        player.findPossibleTurn(40);
        assertTrue(player.possibleX == 6);
        assertTrue(player.possibleY == 8);


    }
}