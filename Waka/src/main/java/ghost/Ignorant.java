package ghost;

import processing.core.PImage;

import javax.swing.*;

public class Ignorant extends Ghost{
    Ignorant(PImage image, int x, int y, App pApplet, Block[][] map, int step) {
        super(image, x, y, pApplet, map, step);
        originalIndex = 3;
        scatterIndex = originalIndex;
        normalChase = true;
    }

    @Override
    void chase() {
        boolean onPosition = cor_x % 16 == 0 && cor_y % 16 == 0;
        if(game.fixMode){
            System.out.println();
        }
        if(!onPosition){
            super.superMove();
            return;
        }
        double val = Math.pow(x_index - player.x_index,2) + Math.pow(y_index - player.y_index,2);
        val = Math.pow(val,0.5);
        if(val > 8){
            super.chase();
        }else {
            if(x_index == corner[scatterIndex][0] && y_index == corner[scatterIndex][1]){
                mode = 0;
                return;
            }
            super.scatter();
        }
    }
}
