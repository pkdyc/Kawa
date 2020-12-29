package ghost;

import processing.core.PApplet;
import processing.core.PImage;

public class Block extends PApplet {
    PImage pImage;
    int x;
    int y;
    String type;
    PApplet game;

    public Block(PImage image, int x, int y,PApplet pApplet,String type) {
        this.pImage = image;
        this.x = x;
        this.y = y;
        this.game = pApplet;
        this.type = type;
    }

    public void display(){
        if(type.equals("0")){
            return;
        }
        if(pImage == null){
            return;
        }
        game.image(pImage,x*16,y*16);
    }

}
