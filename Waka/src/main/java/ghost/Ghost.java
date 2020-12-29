package ghost;
import processing.core.PImage;
import java.util.*;

public class Ghost extends Creature {
    Player player;
    boolean[][] history;
    int[][] corner;
    boolean IsScatter = true;
    int modeArray[];
    int modeArrayCopy[];
    int modeArrayIndex = 0;
    int modeEachFrame = 0;
    int modeEachFrameFrightened = 0;
    int scatterIndex;
    int originalIndex;
    int frightenedTime;
    boolean isFrightened = false;
    boolean normalChase;
    int target_x = -1;
    int target_y = -1;
    String type;
    HashMap<Integer, Integer> hasDir;
    Ghost(PImage image, int x, int y, App pApplet, Block[][] map, int step) {
        super(image, x, y, pApplet, map, step);
        frightenedTime = 0;
        speed = 8;
        mode = 39;
        target_x = init_x;
        target_y = init_y;
        hasDir = new HashMap<>();
        hasDir.put(37,-1);
        hasDir.put(38,-1);
        hasDir.put(39,1);
        hasDir.put(40,1);
    }
    void getCorner(){
        corner = new int[4][2];
        int index = 0;
        boolean flag = false;
        for (int i = 0; i < cell[0].length; i++) {  // leftUp
            for (int j = 0; j < cell.length; j++) {
                if(isWall(i,j) && !isWall(i + 1,j + 1)){
                    corner[index++] = new int[]{i + 1,j + 1};
                    flag = true;
                    break;
                }
            }
            if(flag) break;
        }
        for (int i = cell[0].length - 1; i >= 0 ; i--) {  // leftUp
            for (int j = 0; j < cell.length; j++) {
                if(isWall(i,j) && !isWall(i - 1,j + 1)){
                    corner[index++] = new int[]{i - 1,j + 1};
                    flag = true;
                    break;
                }
            }
            if(flag) break;
        }

        for (int i = cell[0].length - 1; i >= 0 ; i--) {   // RightDown
            for (int j = cell.length - 1; j >= 0; j--) {
                if(isWall(i,j) && !isWall(i - 1,j - 1)){
                    corner[index++] = new int[]{i - 1,j - 1};
                    flag = true;
                    break;
                }
            }
            if(flag) break;
        }

        for (int i = 0; i < cell[0].length; i++) {   // leftDown
            for (int j = cell.length - 1; j >= 0; j--) {
                if(isWall(i,j) && !isWall(i + 1,j - 1)){
                    corner[index++] = new int[]{i + 1,j - 1};
                    flag = true;
                    break;
                }
            }
            if(flag) break;
        }
    }
    void move(){
        isFrightened = !(modeEachFrameFrightened == 0 && frightenedTime == 0);
        if(isFrightened){
            if(modeEachFrameFrightened == 0){
                frightenedTime -= 1;
                modeEachFrameFrightened = 60;
            }
            modeEachFrameFrightened -= 1;
            frightenMode();
            return;
        }
        if(modeEachFrame == 0){
            if(modeArrayCopy[modeArrayIndex] == 0){
                if(modeArrayCopy[modeArrayCopy.length - 1] == 0){
                    modeArrayCopy = modeArray;
                    modeArrayIndex = -1;
                }
                modeArrayIndex++;
                IsScatter = !IsScatter;
                scatterIndex = originalIndex;
            }else {
                modeArrayCopy[modeArrayIndex]--;
            }
            modeEachFrame = 60;
        }
        else{
            modeEachFrame --;
            if(IsScatter){
                scatter();
            }else {
                chase();
            }
        }
    }
    void scatter(){
        DoNotHitWall();
        boolean a = x_index == corner[scatterIndex][0] && y_index == corner[scatterIndex][1];
        boolean b = cor_x % 16 == 0 && cor_y % 16 == 0;
        if(a && b){
            scatterIndex = ++scatterIndex % 4;
            System.out.println("change target");
        }
        mode = bestSolution(corner[scatterIndex][0],corner[scatterIndex][1]);
        super.move();
    }

    boolean reachTarget(){
        return x_index == target_x && y_index == target_y;
    }
    void chase(){
        DoNotHitWall();
        //System.out.printf("current (%s %s) target (%s %s) mode : %s\n",x_index,y_index,target_x,target_y,mode);
        mode = bestSolution(player.x_index,player.y_index);
        game.stroke(255,255,255);
        super.move();
    }
    void chase(int x,int y){
        DoNotHitWall();
        mode = bestSolution(x,y);
        game.stroke(255,255,255);
        super.move();
    }
    void superMove(){
        super.move();
    }
    void DoNotHitWall(){
        if(mode == 37){
            if(superCheckOutOfBound(x_index - 1,y_index)) {
                target_x = x_index;
                target_y = y_index;
            }
        }else if(mode == 38){
            if(superCheckOutOfBound(x_index,y_index - 1)){
                target_x = x_index;
                target_y = y_index;
            }
        }else if(mode == 39){
            if(superCheckOutOfBound(x_index + 1,y_index)){
                target_x = x_index;
                target_y = y_index;
            }
        }else if(mode == 40){
            if(superCheckOutOfBound(x_index,y_index + 1)){
                target_x = x_index;
                target_y = y_index;
            }
        }
    }
    void frightenMode(){
        boolean onPosition = cor_x % 16 == 0 && cor_y % 16 == 0;
        if(game.fixMode == true && type.equals("a")){
            System.out.println("debug mode");
        }
        if(!onPosition){
            super.move();
            return;
        }
        if(reachTarget() == false){
            super.move();
            DoNotHitWall();
            return;
        }

        ArrayList<Integer> possibleDirection = getPossibleDirection(x_index,y_index);
        //one other way left
        possibleDirection.remove(new Integer(mode));
        possibleDirection.remove(new Integer(mode + 2));
        possibleDirection.remove(new Integer(mode - 2));
        if(possibleDirection.size() == 1){
            ArrayList<int[]> possibleCorner = getPossibleTarget(possibleDirection);
            int target_x1 = possibleCorner.get(0)[0];
            int target_y1 = possibleCorner.get(0)[1];
            int val = possibleCorner.get(0)[2];
            mode = bestSolution(target_x1,target_y1);
            super.move();
            return;
        }
        possibleDirection = getPossibleDirection(x_index,y_index);
        possibleDirection.remove(new Integer(mode + 2));
        possibleDirection.remove(new Integer(mode - 2));
        ArrayList<int[]> possibleCorner = getPossibleTarget(possibleDirection);
        Random random = new Random();
        int index = random.nextInt(possibleCorner.size());
        int target_x1 = possibleCorner.get(index)[0];
        int target_y1 = possibleCorner.get(index)[1];
        int val = possibleCorner.get(index)[2];
        mode = bestSolution(target_x1,target_y1);
        super.move();


    }
    ArrayList<int[]> getPossibleTarget(ArrayList<Integer> directions){
        ArrayList<int[]> targets = new ArrayList<>();
        for (Integer integer : directions) {
            int x = x_index;
            int y = y_index;
            if(integer == 37){
                x -= 1;
                while (true){
                    //System.out.println(1);
                    ArrayList<Integer> possibleDirection = getPossibleDirection(x,y);
                    if(possibleDirection.size() == 2){
                        possibleDirection.remove(new Integer(mode));
                        possibleDirection.remove(new Integer(mode + 2));
                        possibleDirection.remove(new Integer(mode - 2));
                        if(possibleDirection.size() == 0){
                            x--;
                            continue;
                        }else {
                            if(super.checkOutOfBound(x,y)){
                                return null;
                            }
                            Block block = cell[y][x];
                            targets.add(new int[]{x,y,37});
                            break;
                        }
                    }else {
                        targets.add(new int[]{x,y,37});
                        break;
                    }
                }
            }else if(integer == 38){
                y -= 1;
                while (true){
                    //System.out.println(2);
                    ArrayList<Integer> possibleDirection = getPossibleDirection(x,y);
                    if(possibleDirection.size() == 2){
                        possibleDirection.remove(new Integer(mode));
                        possibleDirection.remove(new Integer(mode + 2));
                        possibleDirection.remove(new Integer(mode - 2));
                        if(possibleDirection.size() == 0){
                            y--;
                            continue;
                        }else {
                            if(super.checkOutOfBound(x,y)){
                                return null;
                            }
                            Block block = cell[y][x];
                            targets.add(new int[]{x,y,38});
                            break;
                        }
                    }else {
                        targets.add(new int[]{x,y,38});
                        break;
                    }
                }
            }else if(integer == 39){
                x += 1;
                while (true){
                    Block block = cell[y][x];
                    //System.out.println(3);
                    ArrayList<Integer> possibleDirection = getPossibleDirection(x,y);
                    if(possibleDirection.size() == 2){
                        possibleDirection.remove(new Integer(mode));
                        possibleDirection.remove(new Integer(mode + 2));
                        possibleDirection.remove(new Integer(mode - 2));
                        if(possibleDirection.size() == 0){
                            x++;
                            continue;
                        }else {
                            if(super.checkOutOfBound(x,y)){
                                return null;
                            }
                            targets.add(new int[]{x,y,39});
                            break;
                        }
                    }else {
                        targets.add(new int[]{x,y,39});
                        break;
                    }
                }
            }else if(integer == 40){
                y += 1;
                while (true){
                    //Block block = cell[y][x];
                    //System.out.println(4);
                    ArrayList<Integer> possibleDirection = getPossibleDirection(x,y);
                    if(possibleDirection.size() == 2){
                        possibleDirection.remove(new Integer(mode));
                        possibleDirection.remove(new Integer(mode + 2));
                        possibleDirection.remove(new Integer(mode - 2));
                        if(possibleDirection.size() == 0){
                            y++;
                            continue;
                        }else {
                            if(super.checkOutOfBound(x,y)){
                                return null;
                            }
                            targets.add(new int[]{x,y,40});
                            break;
                        }
                    }else {
                        targets.add(new int[]{x,y,40});
                        break;
                    }
                }
            }

        }
        return targets;
    }
    ArrayList<Integer> getPossibleDirection(int x_index,int y_index){

        ArrayList<Integer> possibleMode = new ArrayList<>();
        if(!super.checkOutOfBound(x_index - 1,y_index)){
            possibleMode.add(37);
        }
        if(!super.checkOutOfBound(x_index,y_index - 1)){
            possibleMode.add(38);
        }
        if(!super.checkOutOfBound(x_index + 1,y_index)){
            possibleMode.add(39);
        }
        if(!super.checkOutOfBound(x_index,y_index + 1)){
            possibleMode.add(40);
        }
        return possibleMode;
    }


    @Override
    boolean checkOutOfBound(int x,int y){
        if(goLeft){
            x -= 1;
        }else if(goRight){
            x += 1;
        } else if(goUp){
            y -= 1;
        }else if(goDown){
            y += 1;
        }
        return super.checkOutOfBound(x,y);
    }

    boolean superCheckOutOfBound(int x,int y){
        return super.checkOutOfBound(x,y);
    }

    int bestSolution(int x,int y){
        if(reachTarget() == false && mode != 0){
            return mode;
        }
        ArrayList<Path> pathStack = new ArrayList<>();
        ArrayList<Integer> initPath = new ArrayList<>();
        history = new boolean[cell.length][cell[0].length];
        pathStack.addAll(getPossibleMode(x_index,y_index,initPath));
        int[] target = new int[]{x,y};
        boolean win = false;
        Path removed = null;
        while (pathStack.size() != 0){
            removed = pathStack.remove(0);
            if(removed.arrived(target)){
                win = true;
                break;
            }
            pathStack.addAll(getPossibleMode(removed.x,removed.y,removed.trace));
        }
        if(win == false){
            System.out.println("error!!!!!!!!");
            return -1;
        }
        int pre = -1;
        int first = removed.trace.get(0);
        target_y = y_index;
        target_x = x_index;
        while (removed.trace.size() != 0){
            int next = removed.trace.remove(0);
            if(next == pre || pre == -1){
                pre = next;
                if(pre == 37){
                    target_x--;
                }else if(pre == 38){
                    target_y--;
                }else if(pre == 39){
                    target_x++;
                }else if(pre == 40){
                    target_y++;
                }
            }else {
                break;
            }
        }
        return first;

    }
    void display(){
        move();
        otherFunction();
        isFrightened = !(modeEachFrameFrightened == 0 && frightenedTime == 0);
        if(isFrightened){
            pImage = game.ghostFrightened;
        }else {
            pImage = originalImage;
        }
        if(game.debugMode == true  && isFrightened == false){
            showTarget();
        }
        game.image(pImage,cor_x - offset_x,cor_y - offset_y);
    }
    public void showTarget(){
//        if(normalChase){
//            game.line(cor_x,cor_y,player.cor_x,player.cor_y);
//        }else {
//            game.line(cor_x,cor_y,target_x * 16,target_y * 16);
//        }
        game.line(cor_x,cor_y,target_x * 16,target_y * 16);

    }
    ArrayList<Path> getPossibleMode(int x_index,int y_index,ArrayList<Integer> path){
        ArrayList<Path> possibleMode = new ArrayList<>();
        ArrayList<Integer> integers;
        if(!super.checkOutOfBound(x_index - 1,y_index) && !history[y_index][x_index -1]){
            integers = new ArrayList<>();
            integers.addAll(path);
            integers.add(37);
            history[y_index][x_index -1] = true;
            possibleMode.add(new Path(x_index - 1,y_index,integers));
        }
        if(!super.checkOutOfBound(x_index,y_index - 1) && !history[y_index -1][x_index]){
            integers = new ArrayList<>();
            integers.addAll(path);
            integers.add(38);
            history[y_index - 1][x_index] = true;
            possibleMode.add(new Path(x_index,y_index - 1,integers));
        }
        if(!super.checkOutOfBound(x_index + 1,y_index) && !history[y_index][x_index + 1]){
            integers = new ArrayList<>();
            integers.addAll(path);
            integers.add(39);
            history[y_index][x_index + 1] = true;
            possibleMode.add(new Path(x_index + 1,y_index,integers));
        }
        if(!super.checkOutOfBound(x_index,y_index + 1) && !history[y_index +1][x_index]){
            integers = new ArrayList<>();
            integers.addAll(path);
            integers.add(40);
            history[y_index + 1][x_index] = true;
            possibleMode.add(new Path(x_index,y_index + 1,integers));
        }
        return possibleMode;
    }


}
