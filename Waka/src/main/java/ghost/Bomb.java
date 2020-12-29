package ghost;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Bomb {
    int frame;
    int frameValue = 10;
    int timeLeft = 100;
    boolean lightOn = true;
    boolean exploded = false;
    int remainFrame = 30;
    int x_index;
    int y_index;
    App game;
    ArrayList<int[]> paths = new ArrayList<>();
    public Bomb(int x_index, int y_index, App game) {
        this.x_index = x_index;
        this.y_index = y_index;
        this.game = game;
        this.frame = this.frameValue;
    }
    void display(){
        if(exploded){
            if(remainFrame == 0) return;
            paths.forEach(e -> {
                game.image(game.fire,e[0] * 16,e[1] * 16);
                ArrayList<Ghost> ghostsss = new ArrayList<>();
                for (Ghost ghost : game.ghosts) {
                    if(ghost.x_index == e[0] && ghost.y_index == e[1]){
                        ghostsss.add(ghost);
                    };
                }
                game.ghosts.removeAll(ghostsss);
            });

            remainFrame--;
            return;
        }
        if(timeLeft == 0){
            exploded = true;
            explosion();
        }
        timeLeft--;
        if(lightOn){
            frame--;
            game.image(game.theBomb,x_index * 16 - 8, y_index * 16 - 16);
            if(frame == 0){
                lightOn = false;
            }
        }else {
            frame++;
            if(frame == frameValue){
                lightOn = true;
            }
        }
    }
    void explosion(){
        getPath();
    }
    void getPath(){
        int x = x_index;
        int y = y_index;
        ArrayList<int[]> midPath = new ArrayList<>();
        paths.add(new int[]{x,y});
        Random random = new Random();
        while (true){
            if(!checkOutOfBound(++x,y)){
                midPath.add(new int[]{x,y});
            }else {
                if(midPath.size() != 0){
                    for (int i = 0; i < random.nextInt(midPath.size()) %(midPath.size() - 1 + 1) + 1; i++) {
                        paths.add(midPath.get(i));
                    }
                }
                break;
            }
        }
        midPath = new ArrayList<>();
        x = x_index;
        y = y_index;
        while (true){
            if(!checkOutOfBound(--x,y)){
                midPath.add(new int[]{x,y});
            }else {
                if(midPath.size() != 0){
                    for (int i = 0; i < random.nextInt(midPath.size()) %(midPath.size() - 1 + 1) + 1; i++) {
                        paths.add(midPath.get(i));
                    }
                }
                break;
            }
        }
        midPath = new ArrayList<>();
        x = x_index;
        y = y_index;
        while (true){
            if(!checkOutOfBound(x,++y)){
                midPath.add(new int[]{x,y});
            }else {
                if(midPath.size() != 0){
                    for (int i = 0; i < random.nextInt(midPath.size()) %(midPath.size() - 1 + 1) + 1; i++) {
                        paths.add(midPath.get(i));
                    }
                }
                break;
            }
        }
        midPath = new ArrayList<>();
        x = x_index;
        y = y_index;
        while (true){
            if(!checkOutOfBound(x,--y)){
                midPath.add(new int[]{x,y});
            }else {
                if(midPath.size() != 0){
                    for (int i = 0; i < random.nextInt(midPath.size()) %(midPath.size() - 1 + 1) + 1; i++) {
                        paths.add(midPath.get(i));
                    }
                }
                break;
            }
        }
    }

    boolean checkOutOfBound(int x,int y){
        boolean xAxis = 0 <= x && x < game.cell[0].length;
        boolean yAxis = 0 <= y && y < game.cell.length;
        if(xAxis == false || yAxis == false){
            return true;
        }else {
            boolean notWall = game.gameManage.extraWall[y][x];
            if(notWall && isWall(x,y) == false){
                return false;
            }return true;
        }
    }

    boolean isWall(int x,int y){
        if(game.cell[y][x].type.equals("0") || game.cell[y][x].type.equals("7") || game.cell[y][x].type.equals("8") ||
                game.cell[y][x].type.equals("b")){
            return false;
        }
        return true;
    }

}
