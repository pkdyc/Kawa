package ghost;

import processing.core.PImage;

public class Chaser extends Ghost {
    Chaser(PImage image, int x, int y, App pApplet, Block[][] map, int step) {
        super(image, x, y, pApplet, map, step);
        originalIndex = 0;
        scatterIndex = originalIndex;
        normalChase = true;
    }

    @Override
    void scatter() {
        DoNotHitWall();
        super.scatter();
    }

    @Override
    void chase() {
        DoNotHitWall();
        super.chase();
    }
}
