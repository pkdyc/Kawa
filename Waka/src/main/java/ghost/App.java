package ghost;
import org.json.simple.JSONArray;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
public class App extends PApplet {
    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;
    public int lives,speed,fruitLeft,frightenedLength;
    HashMap<Integer,PImage> sprite;
    public Block[][] cell;
    Player player;
    PImage ghostFrightened,player_image,ghost_image,gameWin,gameLost,fire,bomb,theBomb;
    PFont font,title;
    ArrayList<Ghost> ghosts;
    HashMap<String,PImage> map_hash;
    String fileName;
    JSONArray modeLengthsMid;
    int[] modeLengths;
    int smoothFrame = 5;
    boolean modeSwitch,gameOver = false;
    boolean debugMode = false;
    boolean frightenedMode = false;
    GameManage gameManage = null;
    boolean fixMode = false;
    public App(){

    }
    public void setup() {
        frameRate(60);
        player_image = loadImage("src/main/resources/playerClosed.png");
        ghost_image = loadImage("src/main/resources/chaser.png");
        ghostFrightened = loadImage("src/main/resources/frightened.png");
        gameWin = loadImage("src/main/resources/gameover - Copy.png");
        gameLost = loadImage("src/main/resources/gameover - Copy.png");
        fire = loadImage("src/main/resources/fire.png");
        bomb = loadImage("src/main/resources/bomb.png");
        theBomb = loadImage("src/main/resources/thebomb.png");
        theBomb.resize(45,45);
        bomb.resize(20,20);
        player = new Player(player_image,1,1,this,cell,speed);
        ghosts = new ArrayList<>();
        sprite = new HashMap<>();
        map_hash = new HashMap<>();
        sprite.put(37,loadImage("src/main/resources/playerLeft.png"));
        sprite.put(38,loadImage("src/main/resources/playerUp.png"));
        sprite.put(39,loadImage("src/main/resources/playerRight.png"));
        sprite.put(40,loadImage("src/main/resources/playerDown.png"));
        map_hash.put("0",null);
        map_hash.put("1",loadImage("src/main/resources/horizontal.png"));
        map_hash.put("2",loadImage("src/main/resources/vertical.png"));
        map_hash.put("3",loadImage("src/main/resources/upLeft.png"));
        map_hash.put("4",loadImage("src/main/resources/upRight.png"));
        map_hash.put("5",loadImage("src/main/resources/downLeft.png"));
        map_hash.put("6",loadImage("src/main/resources/downRight.png"));
        map_hash.put("7",loadImage("src/main/resources/fruit.png"));
        map_hash.put("8",loadImage("src/main/resources/doubleFruit.png"));
        map_hash.put("p",loadImage("src/main/resources/playerClosed.png"));
        map_hash.put("c",loadImage("src/main/resources/chaser.png"));
        map_hash.put("a",loadImage("src/main/resources/ambusher.png"));
        map_hash.put("i",loadImage("src/main/resources/ignorant.png"));
        map_hash.put("w",loadImage("src/main/resources/whim.png"));
//        title = createFont("src/main/resources/PressStart2P-Regular.ttf",30);
        map_hash.put("b",bomb);
        gameManage = new GameManage(this);
        gameManage.initialize();
        cell = gameManage.map_init(fileName);
    }
    public void settings() { size(WIDTH, HEIGHT);}
    public void draw() {
        gameManage.draw();
    }
    @Override
    public void keyPressed(KeyEvent event) { gameManage.keyPressed(event); }
    public static void main(String[] args) { PApplet.main("ghost.App"); }
}