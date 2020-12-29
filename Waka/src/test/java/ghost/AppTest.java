package ghost;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    App app;
    @BeforeEach
    public void init(){
        app = new App();
        app.main(new String[]{""});
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
    }

    @Test
    public void constructor(){
        // test the constructor of every class
        assertNotNull(new App());
        assertNotNull(new Ignorant(app.map_hash.get("i"),5,5,app,app.cell,app.speed));
        assertNotNull(new Chaser(app.map_hash.get("i"),5,5,app,app.cell,app.speed));
        assertNotNull(new Whim(app.map_hash.get("i"),5,5,app,app.cell,app.speed));
        assertNotNull(new Ambusher(app.map_hash.get("i"),5,5,app,app.cell,app.speed));
        assertNotNull(new Ghost(app.map_hash.get("i"),5,5,app,app.cell,app.speed));
        assertNotNull(new Player(app.map_hash.get("i"),5,5,app,app.cell,app.speed));
        assertNotNull(new Block(app.map_hash.get("7"),5,5,app,"7"));
        assertNotNull(new Path(5,5,new ArrayList<Integer>()));
        assertNotNull(new Path(5,5,new ArrayList<Integer>(),new ArrayList<int[]>()));
        Path path = new Path(5, 5, new ArrayList<Integer>());
        assertTrue(path.arrived(new int[]{5,5}));


    }
    @Test
    public void readConfig(){
        // test the result after reading configuration
        assertTrue(app.ghosts.size() == 5);
        assertTrue(app.fruitLeft == 299);
        assertTrue(app.lives == 10);
        assertTrue(app.frightenedLength == 3);
        assertTrue(app.speed == 1);

    }


}