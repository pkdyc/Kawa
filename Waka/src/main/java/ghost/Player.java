package ghost;

import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Creature {
    ArrayList<Ghost> ghosts;
    PImage closedMouth;
    int nextPossibleMove;
    int possibleX;
    int possibleY;
    boolean flagLock = true;
    int frameCount = 8;
    boolean minusCount = true;
    HashMap<Integer,PImage> sprite;

    Player(PImage image, int x, int y, App pApplet, Block[][] map, int step) {
        super(image, x, y, pApplet, map, step);
        eat_fruit = true;
        closedMouth = image;
        mode = 39;
        speed = 4;
        cor_x = x_index * 16;
        cor_y = y_index * 16;
    }

    @Override
    public void otherFunction() {
        meetGhost();
        changeIcon();
        display_life();
    }

    public void meetGhost(){
        boolean meet = ghosts.stream().filter(e -> e.x_index == x_index && e.y_index == y_index)
                .toArray().length != 0;
        if(meet){
            Ghost ghost = (Ghost) game.ghosts.stream().filter(e -> e.x_index == x_index && e.y_index == y_index)
                    .toArray()[0];
            if(ghost.isFrightened){
                ghosts.remove(ghost);
                return;
            }
            System.out.println("lose your life");
            game.lives--;
            ghosts.forEach(e -> e.reset());
            reset();
        }
    }
    public void changeIcon(){
        if(frameCount == 0){
            pImage = closedMouth;
            minusCount = false;
        }else if(frameCount == 8){
            if(mode != 0){
                pImage = sprite.get(mode);
            }
            minusCount = true;
        }
        if(minusCount){
            frameCount--;
        }else {
            frameCount++;
        }
    }


    @Override
    void changeMod(int newMode) {
        if(Math.abs(newMode - mode) == 2){
            mode = newMode;
            nextPossibleMove = mode;
            return;
        }
        if(newMode != mode){
            nextPossibleMove = newMode;
            findPossibleTurn(nextPossibleMove);
        }
    }


    @Override
    void move(){
        boolean onPosition = cor_x % 16 == 0 && cor_y % 16 == 0;
        if(y_index == possibleY && x_index == possibleX && onPosition){
            mode = nextPossibleMove;
            possibleY = -1;
            possibleX = -1;
            flagLock = true;
        }
        super.move();

    }

    void findPossibleTurn(int nextMove){
        int x = x_index;
        int y = y_index;
        if(flagLock){
            if(goLeft){
                x -= 1;
            }else if(goRight){
                x += 1;
            } else if(goUp){
                y -= 1;
            }else if(goDown){
                y += 1;
            }
            flagLock = false;
        }

        if(nextMove == 37){  //want to go left
            if(mode == 38){          //current in up
                while (true){
                    if(checkOutOfBound(x - 1,y) == false){
                        possibleX = x;
                        possibleY = y;
                        return;
                    }
                    y--;
                    if(isWall(x,y)){
                        return;
                    }
                }
            }
            else if(mode == 40){          //current in down
                while (true){
                    if(checkOutOfBound(x -1,y) == false){
                        possibleX = x;
                        possibleY = y;
                        return;
                    }
                    y++;
                    if(isWall(x,y)){
                        return;
                    }
                }
            }
        }
        else if(nextMove == 38){  //want to go up
            if(mode == 39){          //current in right
                while (true){
                    if(checkOutOfBound(x,y-1) == false){
                        possibleX = x;
                        possibleY = y;
                        return;
                    }
                    x++;
                    if(isWall(x,y)){
                        return;
                    }
                }
            }
            else if(mode == 37){          //current in left
                while (true){
                    if(checkOutOfBound(x,y-1) == false){
                        possibleX = x;
                        possibleY = y;
                        return;
                    }
                    x--;
                    if(isWall(x,y)){
                        return;
                    }
                }
            }
        }
        else if(nextMove == 39){  //want to go right
            if(mode == 38){          //current in up
                while (true){
                    if(checkOutOfBound(x + 1,y) == false){
                        possibleX = x;
                        possibleY = y;
                        return;
                    }
                    y--;
                    if(isWall(x,y)){
                        return;
                    }
                }
            }
            else if(mode == 40){          //current in down
                while (true){
                    if(checkOutOfBound(x + 1,y) == false){
                        possibleX = x;
                        possibleY = y;
//                        System.out.printf("(%s %s)\n",possibleX,possibleY);
                        return;
                    }
                    y++;
                    if(isWall(x,y)){
                        return;
                    }
                }
            }
        }
        else if(nextMove == 40){  //want to go down
            if(mode == 39){          //current in right
                while (true){
                    if(checkOutOfBound(x,y + 1) == false){
                        possibleX = x;
                        possibleY = y;
                        return;
                    }
                    x++;
                    if(isWall(x,y)){

                        return;
                    }
                }
            }
            else if(mode == 37){          //current in left
                while (true){
                    if(checkOutOfBound(x,y + 1) == false){
                        possibleX = x;
                        possibleY = y;
                        return;
                    }
                    x--;
                    if(isWall(x,y)){
                        return;
                    }
                }
            }
        }
    }


    public void display_life(){
        PImage image = sprite.get(39);
        int offset = 5;
        for (int i = 0; i < game.lives; i++) {
            game.image(image,offset,545);
            offset += 30;
        }
    }
    @Override
    void display(){
        move();
        otherFunction();
        game.image(pImage,cor_x - offset_x,cor_y - offset_y);
    }

    @Override
    public void reset(){
        super.reset();
        possibleX = -1;
        possibleY = -1;
    }



}
