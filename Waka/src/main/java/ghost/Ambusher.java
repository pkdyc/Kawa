package ghost;

import processing.core.PImage;

public class Ambusher extends Ghost{
    Ambusher(PImage image, int x, int y, App pApplet, Block[][] map, int step) {
        super(image, x, y, pApplet, map, step);
        originalIndex = 1;
        scatterIndex = originalIndex;
    }

//    void chase() {
//        boolean onPosition = cor_x % 16 == 0 && cor_y % 16 == 0;
//        if(!onPosition){
//            superMove();
//            return;
//        }
//        if(player.mode == 37){
//            for (int i = 0; i <= 4; i++) {
//                int target = player.x_index - 4 + i;
//                if(checkOutOfBound(target,player.y_index)){
//                    if(target == x_index && player.y_index == y_index){
//                        mode = 0;
//                        return;
//                    }
//                    target_x = target;
//                    target_y = player.y_index;
//                    super.chase(target_x,target_y);
//                    return;
//                }
//            }
//        }else if(player.mode == 38){
//            for (int i = 0; i <= 4; i++) {
//                int target = player.y_index - 4 + i;
//                if(checkOutOfBound(player.x_index,target)){
//                    if(player.x_index == x_index && target == y_index){
//                        mode = 0;
//                        return;
//                    }
//                    target_x = player.x_index;
//                    target_y = target;
//                    super.chase(target_x,target_y);
//                    return;
//                }
//            }
//        }else if(player.mode == 39){
//            for (int i = 0; i <= 4; i++) {
//                int target = player.x_index + 4 - i;
//                if(checkOutOfBound(target,player.y_index)){
//                    if(target == x_index && player.y_index == y_index){
//                        mode = 0;
//                        return;
//                    }
//                    target_x = target;
//                    target_y = player.y_index;
//                    super.chase(target_x,target_y);
//                    return;
//                }
//            }
//        }else if(player.mode == 40){
//            for (int i = 0; i <= 4; i++) {
//                int target = player.y_index + 4 - i;
//                if(checkOutOfBound(player.x_index,target)){
//                    if(player.x_index == x_index && target == y_index){
//                        mode = 0;
//                        return;
//                    }
//                    target_x = player.x_index;
//                    target_y = target;
//                    super.chase(target_x,target_y);
//                    return;
//                }
//            }
//        }
//    }
void chase() {
    boolean onPosition = cor_x % 16 == 0 && cor_y % 16 == 0;
    if(!onPosition){
        superMove();
        return;
    }
    DoNotHitWall();
//    System.out.printf("target : (%s,%s) current : (%s,%s) player  : (%s,%s) mode : %s\n",target_x,target_y,x_index,y_index,player.x_index,player.y_index,mode
//                                                                        );
    if(game.fixMode){
        System.out.println();
        DoNotHitWall();
    }


    if(player.mode == 37){
        for (int i = 0; i <= 4; i++) {
            int target = player.x_index - 4 + i;
            if(!superCheckOutOfBound(target,player.y_index)){
                if(target == x_index && player.y_index == y_index){
                    mode = 0;
                    return;
                }
                int x = target;
                int y = player.y_index;
                super.chase(x,y);
                return;
            }
        }
    }else if(player.mode == 38){
        for (int i = 0; i <= 4; i++) {
            int target = player.y_index - 4 + i;
            if(!superCheckOutOfBound(player.x_index,target)){
                if(player.x_index == x_index && target == y_index){
                    mode = 0;
                    return;
                }
                int x = player.x_index;
                int y = target;
                super.chase(x,y);
                return;
            }
        }
    }else if(player.mode == 39){
        for (int i = 0; i <= 4; i++) {
            int target = player.x_index + 4 - i;
            if(!superCheckOutOfBound(target,player.y_index)){
                if(target == x_index && player.y_index == y_index){
                    mode = 0;
                    return;
                }
                int x = target;
                int y = player.y_index;
                super.chase(x,y);
                return;
            }
        }
    }else if(player.mode == 40){
        for (int i = 0; i <= 4; i++) {
            int target = player.y_index + 4 - i;
            if(!superCheckOutOfBound(player.x_index,target)){
                if(player.x_index == x_index && target == y_index){
                    mode = 0;
                    return;
                }
                int x = player.x_index;
                int y = target;
                super.chase(x,y);
                return;
            }
        }
    }
}

}
