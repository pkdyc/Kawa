package ghost;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.HashMap;
import java.util.Random;
/**
 * Creature class is the the super class of Ghosts and Player
 * it is created because there functions of ghosts and Player are highly duplicated
 * so that we could use this to reduce code repetition
 * the same methods that both Class used are "move","check out of bound" and so on
 * and the "display" method has been set as abstract is due to that these two classes
 * has totally different ways in implementing this method.
 */
abstract public class Creature {
    /**
     *
     @param x_index the x index of instance in cell
     @param y_index : the y index of instance in cell
     @param init_x : the initial value of x index of  instance in cell
     @param init_y : the initial value of y index of  instance in cell
     @pImage : the image of current state
     @mode : the current direction
     @cell : the cell of maze (map ) of the game
     @game : the App instance
     @offset_x : used of adjust the location of image display
     @offset_y : used of adjust the location of image display
     @eat_fruit : used to judge whether it could eat fruit
     @cor_x : used of display the image
     @cor_y : used of display the image
     @goUp : used to record the moving state
     @goDown : used to record the moving state
     @goLeft : used to record the moving state
     @goRight : used to record the moving state
     @speed : used to set the speed
     @originalImage : the originalImage of instance

     */
    int x_index;
    int y_index;
    int init_x;
    int init_y;
    PImage pImage;
    int mode;
    Block[][] cell;
    App game;
    int offset_x = 3;
    int offset_y = 3;
    Boolean eat_fruit = false;
    int cor_x = 0;
    int cor_y = 0;
    boolean goUp = false;
    boolean goDown = false;
    boolean goLeft = false;
    boolean goRight = false;
    int speed;
    PImage originalImage;

    /**
     * standard instructor of Creature instance
     */
    Creature(PImage image,int x,int y,App pApplet,Block[][] map,int step){
        x_index = x;
        y_index = y;
        init_x = x;
        init_y = y;
        pImage = image;
        game = pApplet;
        cell = map;
        cor_x = x_index * 16;
        cor_y = y_index * 16;
        speed = step;
        originalImage = image;
    }

    /**
     * used to change the state of mode
     */

    void changeMod(int newMode){
        mode = newMode;
    }

    /**
     * general moving method for all creatures
     */

    void move(){
        boolean onPosition = cor_x % 16 == 0 && cor_y % 16 == 0;
        if(goUp){
            cor_y -= speed;
            if(cor_y % 16 == 0){
                goUp = false;
                y_index -= 1;
                return;
            }
        }else if(goLeft){
            cor_x -= speed;
            if(cor_x % 16 == 0){
                goLeft = false;
                x_index -= 1;
            }
            return;
        }else if(goRight){
            cor_x += speed;
            if(cor_x % 16 == 0){
                goRight = false;
                x_index += 1;
            }
            return;
        }else if(goDown){
            cor_y += speed;
            if(cor_y % 16 == 0){
                goDown = false;
                y_index += 1;
            }
            return;
        }

        if (onPosition == false) return;
        int mid_x = x_index;
        int mid_y = y_index;
        if(mode == 37){
            mid_x -= 1;
            if(collision(mid_x,mid_y) == false) goLeft = true;
        }else if(mode == 38){
            mid_y--;
            if(collision(mid_x,mid_y) == false) goUp = true;
        }else if(mode == 39){
            mid_x++;
            if(collision(mid_x,mid_y) == false) goRight = true;
        }else if(mode == 40){
            mid_y++;
            if(collision(mid_x,mid_y) == false) goDown = true;
        }
    }

    /**
     * a display method which needs to be override
     */

    abstract void display();

    /**
     * some extra component could be put inside to extend the function
     */

    public void otherFunction() {

    }

    /**
     * general method for detecting collision
     */

    Boolean collision(int x,int y){
        if(cell[y][x].type.equals("0") || cell[y][x].type.equals("7") || cell[y][x].type.equals("8") || cell[y][x].type.equals("b")){
            if(eat_fruit){
                if(cell[y][x].type.equals("8")){
                    game.frightenedMode = true;
                    game.fruitLeft--;
                }
                if(cell[y][x].type.equals("b")){
                    game.gameManage.bombNumber ++;
                }
                if(cell[y][x].type.equals("7") == true){
                    game.fruitLeft--;
                }
                cell[y][x].type = "0";
            }
        }else {
            return true;
        }
        return false;
    }

    /**
     * general method for resetting all the state
     */
    void reset(){
        x_index = init_x;
        y_index = init_y;
        cor_x = x_index * 16;
        cor_y = y_index * 16;
        goUp = false;
        goDown = false;
        goLeft = false;
        goRight = false;
        mode = 39;
        game.image(pImage,cor_x - offset_x,cor_y - offset_y);
    }

    /**
     * general method for detecting outOfBound
     */

    boolean checkOutOfBound(int x,int y){
        boolean xAxis = 0 <= x && x < cell[0].length;
        boolean yAxis = 0 <= y && y < cell.length;
        if(xAxis == false || yAxis == false){
            return true;
        }else {
            boolean notWall = game.gameManage.extraWall[y][x];
            if(notWall && isWall(x,y) == false){
                return false;
            }return true;
        }
    }

    /**
     * general method for detecting wall
     */

    boolean isWall(int x,int y){
        if(cell[y][x].type.equals("0") || cell[y][x].type.equals("7") || cell[y][x].type.equals("8") ||
           cell[y][x].type.equals("b")){
            return false;
        }
        return true;
    }


}
