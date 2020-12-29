package ghost;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import processing.event.KeyEvent;

import javax.sound.midi.SoundbankResource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GameManage{
    App game;
    boolean frightenedExtraKey = false;
    boolean[][] extraWall;
    String fileName;
    boolean gameLost = false;
    boolean gameWin = false;
    int bombNumber = 0;
    int initFruitNumber = 0;
    ArrayList<Bomb> bombs = new ArrayList<>();
    boolean gameEnd = false;
    GameManage(App app){
        game = app;
    }
    void initialize() {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject parse = (JSONObject) jsonParser.parse(new FileReader("config.json"));
            game.fileName = "" + parse.get("map");
            Long speed_mid = (Long) parse.get("speed");
            Long lives_mid = (Long) parse.get("lives");
            Long frightened_mid = (Long) parse.get("frightenedLength");
            game.frightenedLength = frightened_mid.intValue();
            game.speed = speed_mid.intValue();
            game.lives = lives_mid.intValue();
            game.modeLengthsMid = (JSONArray) parse.get("modeLengths");
            game.modeLengths = new int[game.modeLengthsMid.size()];
            for (int i = 0; i < game.modeLengthsMid.size(); i++) {
                Long helper = (Long) game.modeLengthsMid.get(i);
                game.modeLengths[i] = helper.intValue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void mapTraverse(Block[][] mid_cell){
        boolean[][] history = new boolean[game.cell.length][game.cell[0].length];
        ArrayList<int[]> pathStack = new ArrayList<>();
        int[] target = new int[]{game.player.init_x,game.player.init_y};
        pathStack.add(target);
        while (pathStack.size() > 0){
            int[] removed = pathStack.remove(0);
            int x = removed[0];
            int y = removed[1];
            if(history[y][x] == false){
                ArrayList<int[]> ints = possibleDirection(removed[0], removed[1], history);
                pathStack.addAll(ints);
            }
        }
        extraWall = history;

    }
    ArrayList<int[]> possibleDirection(int x_index,int y_index,boolean[][] history){
//        game.cell[y_index][x_index].pImage = game.bomb;
        history[y_index][x_index] = true;
        ArrayList<int[]> possibleMode = new ArrayList<>();
        if(!checkOutOfBound(x_index - 1,y_index) && !history[y_index][x_index -1]){
            possibleMode.add(new int[]{x_index - 1,y_index});
        }
        if(!checkOutOfBound(x_index,y_index - 1) && !history[y_index - 1][x_index]){
            possibleMode.add(new int[]{x_index,y_index - 1});
        }
        if(!checkOutOfBound(x_index + 1,y_index) && !history[y_index][x_index + 1]){
            possibleMode.add(new int[]{x_index + 1,y_index});
        }
        if(!checkOutOfBound(x_index,y_index + 1) && !history[y_index +1][x_index]){
            possibleMode.add(new int[]{x_index,y_index + 1});
        }
        return possibleMode;
    }

    boolean checkOutOfBound(int x,int y){
        boolean xAxis = 0 <= x && x < game.player.cell[0].length;
        boolean yAxis = 0 <= y && y < game.player.cell.length;
        if(xAxis == false || yAxis == false){
            return true;
        }
        return isWall(x,y);
    }

    boolean isWall(int x,int y){
        if(game.player.cell[y][x].type.equals("0") ||
           game.player.cell[y][x].type.equals("7") ||
           game.player.cell[y][x].type.equals("8") ||
           game.player.cell[y][x].type.equals("b")){
            return false;
        }
        return true;
    }

    public Block[][] map_init(String filename){
        this.fileName = filename;
        initFruitNumber = 0;
        Block[][] mid_cell = null;
        int begin_y = 0;
        int begin_x = 0;
        try {
            Scanner scanner = new Scanner(new File(filename));
            int height = 0;
            int width = 0;
            while (scanner.hasNextLine()){
                width = scanner.nextLine().length();
                height++;
            }
            scanner.close();
            mid_cell = new Block[height][width];
            scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()){
                begin_x = 0;
                String each_line = scanner.nextLine();
                for (int i = 0; i < each_line.length(); i++) {
                    String each_char = String.valueOf(each_line.charAt(i));
                    if(each_char.equals("7") || each_char.equals("8")){
                        initFruitNumber++;
                    }
                    if(each_char.equals("c")){
                        game.ghosts.add(new Chaser(game.map_hash.get(each_char),begin_x,begin_y,game,game.cell,game.speed));
                        game.ghosts.get(game.ghosts.size() - 1).type = each_char;
                        mid_cell[begin_y][begin_x] = new Block(game.map_hash.get("0"),begin_x,begin_y,game,"0");
                    }else if(each_char.equals("p")){
                        game.player = new Player(game.player_image,begin_x,begin_y,game,game.cell,game.speed);
                        mid_cell[begin_y][begin_x] = new Block(game.map_hash.get("0"),begin_x,begin_y,game,"0");
                    }else if(each_char.equals("a")){
                        game.ghosts.add(new Ambusher(game.map_hash.get(each_char),begin_x,begin_y,game,game.cell,game.speed));
                        game.ghosts.get(game.ghosts.size() - 1).type = each_char;
                        mid_cell[begin_y][begin_x] = new Block(game.map_hash.get("0"),begin_x,begin_y,game,"0");
                    }else if(each_char.equals("i")){
                        game.ghosts.add(new Ignorant(game.map_hash.get(each_char),begin_x,begin_y,game,game.cell,game.speed));
                        game.ghosts.get(game.ghosts.size() - 1).type = each_char;
                        mid_cell[begin_y][begin_x] = new Block(game.map_hash.get("0"),begin_x,begin_y,game,"0");
                    }else if(each_char.equals("8")){
                        mid_cell[begin_y][begin_x] = new Block(game.map_hash.get(each_char),begin_x,begin_y,game,each_char);
                    }else if(each_char.equals("w")){
                        game.ghosts.add(new Whim(game.map_hash.get(each_char),begin_x,begin_y,game,game.cell,game.speed));
                        game.ghosts.get(game.ghosts.size() - 1).type = each_char;
                        mid_cell[begin_y][begin_x] = new Block(game.map_hash.get("0"),begin_x,begin_y,game,"0");

                    }else {
                        mid_cell[begin_y][begin_x] = new Block(game.map_hash.get(each_char),begin_x,begin_y,game,each_char);
                    }
                    begin_x += 1;
                }
                begin_y += 1;
            }
            game.ghosts.forEach(e -> e.player = game.player);
            scanner.close();
        } catch (Exception e) {
            game.exit();
        }
        for (Ghost ghost : game.ghosts) {
            ghost.cell = mid_cell;
            ghost.getCorner();
            ghost.modeArray = game.modeLengths.clone();
            ghost.modeArrayCopy = game.modeLengths.clone();
        }
        game.player.cell = mid_cell;
        game.player.ghosts = game.ghosts;
        game.player.sprite = game.sprite;
        game.fruitLeft = initFruitNumber;
        game.cell = mid_cell;
        mapTraverse(mid_cell);
        return mid_cell;

    }
    public void show_map(){
        for (Block[] blocks : game.cell) {
            for (Block block : blocks) {
                block.display();
            }
        }
        game.player.display();
        game.ghosts.forEach(e -> e.display());
        game.font = game.createFont("Arial",20,true);
        game.fill(190,237,199);
        game.textFont(game.font,20);
        String s = String.format("fruit left : %s  Bomb holding : %s",game.fruitLeft,bombNumber);
        game.text(s,80,35);
        bombs.forEach(e -> e.display());

    }

    public void gameComplete(){
        if(game.lives == 0 || game.fruitLeft == 280){
            if(game.gameOver == false){
                game.background(0, 0, 0);
                if(game.lives == 0){
                    System.out.println("game over");
                    String s = String.format("GAME OVER");
                    game.title = game.createFont("src/main/resources/PressStart2P-Regular.ttf",30);
                    game.textFont(game.title,30);
                    game.fill(255);
                    game.text(s,100,230);
                    gameLost = true;
                }
                if(game.fruitLeft == 280){
                    System.out.println("you win!");
                    String s = String.format("YOU WIN");
                    game.title = game.createFont("src/main/resources/PressStart2P-Regular.ttf",30);
                    game.textFont(game.title,30);
                    game.fill(255);
                    game.text(s,120,230);
                    gameWin = true;
                }
                game.gameOver = true;
            }
        }
    }
    public void gameMode(int i){
        if(game.modeSwitch == false){
            game.modeSwitch = true;
            for (int i1 = 0; i1 < i; i1++) {
                game.ghosts.remove(0);
            }
        }
    }
    public void gameMode(String i){
        if(game.modeSwitch == false){
            game.modeSwitch = true;
            List<Ghost> collect = game.ghosts.stream().filter(e -> e.type.equals(i)).collect(Collectors.toList());
            game.ghosts.clear();
            game.ghosts = (ArrayList<Ghost>) collect;
        }
    }
    public void Delay(int i){
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void startOver(){
        Delay(2000);
        game.ghosts.clear();
        map_init(fileName);
        initialize();
        game.modeSwitch = false;
        game.gameOver = false;
        game.debugMode = false;
        game.frightenedMode = false;
        game.fixMode = false;
        game.smoothFrame = 5;
        game.fruitLeft = initFruitNumber;
        bombNumber = 0;
        bombs.clear();
    }
    public void draw() {
        if(game.gameOver){
            startOver();
            System.out.println("game start over");
        }
//        gameMode(6);
        if(game.frightenedMode){
            game.ghosts.forEach(e -> e.frightenedTime = game.frightenedLength);
            game.frightenedMode = false;
            game.ghosts.forEach(e -> e.target_x = e.x_index);
            game.ghosts.forEach(e -> e.target_y = e.y_index);
        }
        game.background(0, 0, 0);
        show_map();
        gameComplete();
    }

    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();
        if(key >= 37 && key <= 40) {
            game.player.changeMod(key);
        }else if ((key == 68)){
            game.fixMode = true;
        }else if(key == 70) {
            if (frightenedExtraKey) {
                game.frightenedMode = false;
                for (Ghost ghost : game.ghosts) {
                    ghost.frightenedTime = 0;
                    ghost.modeEachFrameFrightened = 0;
                }
            } else {
                game.frightenedMode = true;
            }
            frightenedExtraKey = !frightenedExtraKey;
        }else if(key == 81){
            bombs.add(new Bomb(game.player.x_index,game.player.y_index,this.game));
        }else if(key == 32){
            game.debugMode = !game.debugMode;
            System.out.println("debugging mode: " + game.debugMode);
        }
    }

}
