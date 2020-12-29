package ghost;

import processing.core.PImage;

import java.util.Random;

public class Whim extends Ghost{
    Whim(PImage image, int x, int y, App pApplet, Block[][] map, int step) {
        super(image, x, y, pApplet, map, step);
        originalIndex = 2;
        scatterIndex = originalIndex;
    }

    @Override
    void chase() {
        boolean b = game.ghosts.stream().filter(e -> e.type.equals("c")).toArray().length != 0;
        if (b) {  // has a chaser
            DoNotHitWall();
            Ghost ghost = game.ghosts.stream().filter(e -> e.type.equals("c")).toArray(Ghost[]::new)[0];
            int x = player.x_index;
            int y = player.y_index;
            if (player.mode == 37) {
                x -= 2;
            } else if (player.mode == 38) {
                y -= 2;
            } else if (player.mode == 39) {
                x += 2;
            } else if (player.mode == 40) {
                y += 2;
            }
            x += (x - ghost.x_index) * 2;
            y += (y - ghost.y_index) * 2;
            if(y == 0){
                System.out.println();
            }
            int x_res = -1;
            int y_res = -1;
            double smallest = 10000;
            for (int y1 = 0; y1 < game.gameManage.extraWall.length; y1++) {
                for (int x1 = 0; x1 < game.gameManage.extraWall[0].length; x1++) {
                    if (game.gameManage.extraWall[y1][x1] == true) {
                        double dis = Math.pow(y1 - y, 2) + Math.pow(x1 - x, 2);
                        dis = Math.pow(dis,0.5);
                        if (dis < smallest) {
                            x_res = x1;
                            y_res = y1;
                            smallest = dis;
                        }else {
                        }
                    }
                }
            }
            target_x = x_res;
            target_y = y_res;
            super.chase(target_x, target_y);
            return;


        } else {
            normalChase = true;
            super.chase();
        }
    }
}
