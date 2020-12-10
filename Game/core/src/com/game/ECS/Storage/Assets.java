package com.game.ECS.Storage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.game.ECS.Components.StateComponent;
import com.game.ECS.Managers.ResourceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sean on 25/04/2015.
 *
 * Asset locations
 *
 */

//Todo convert to asset manager
public class Assets {

    //Tiled Map
    public static String tiledMap = "map/WorldMap.tmx";

    //UI
    public static String castSpellBtn = "UI/castButton.png";
    public static String castSpellShadow = "UI/castButtonShadow.png";
    public static String cancelBtn = "UI/cancelButton.png";

    //Character Sheets
    public static String druidSheet = "character/DruidSheet.png";

    //Projectiles
    public static String iceProjEffect = "particles/ice.p";
    public static String iceProjParticle = "particles/";
    public static String iceExplosion = "particles/iceExplosion.p";
    public static String iceSprite = "projectiles/iceball.png";

    //Misc
    public static String blank = "Blank.png";

    //Animations
    //TODO add reverse animate feature

    /**
     *
     * @param sheet
     * @param startX
     * @param startY
     * @param width
     * @param height
     * @param length
     * @param speed
     * @return
     */
    private static Animation animate(Texture sheet, int startX, int startY, int width, int height, int length, float speed){
        TextureRegion[] frames = new TextureRegion[length];
        for(int i=0;i < frames.length; i++)
            frames[i] = new TextureRegion(sheet, startX + (width*i), startY, width, height);

        Animation animated = new Animation(speed, frames);
        return animated;
    }

    //Class of animations for multiple directions
    public static class DirAnimation{
        public Map<StateComponent.State, Animation> left, right, up, down;

        public DirAnimation(Map<StateComponent.State, Animation> left,
                            Map<StateComponent.State, Animation> right,
                            Map<StateComponent.State, Animation> up,
                            Map<StateComponent.State, Animation> down){
            this.left = left;
            this.right = right;
            this.up = up;
            this.down = down;
        }
    }

    //Player Druid
    public static DirAnimation animPlayerDruid(){
        Map<StateComponent.State, Animation> left = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> right = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> up = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> down = new HashMap<StateComponent.State, Animation>();
        Texture sheet = ResourceManager.sheetDruid();
        float s = 0.10f; //Anim speed

        //Still
        left.put(StateComponent.State.STILL, animate(sheet, 0, 192, 64, 64, 1, s));
        right.put(StateComponent.State.STILL, animate(sheet, 0, 128, 64, 64, 1, s));
        up.put(StateComponent.State.STILL, animate(sheet, 0, 64, 64, 64, 1, s));
        down.put(StateComponent.State.STILL, animate(sheet, 0, 0, 64, 64, 1, s));

        //Walking
        left.put(StateComponent.State.WALK, animate(sheet, 0, 192, 64, 64, 5, s));
        right.put(StateComponent.State.WALK, animate(sheet, 0, 128, 64, 64, 5, s));
        up.put(StateComponent.State.WALK, animate(sheet, 0, 64, 64, 64, 4, s));
        down.put(StateComponent.State.WALK, animate(sheet, 0, 0, 64, 64, 4, s));

        return new DirAnimation(left, right, up, down);
    }

    //Player Druid
    public static DirAnimation animWolf(){
        Map<StateComponent.State, Animation> left = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> right = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> up = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> down = new HashMap<StateComponent.State, Animation>();
        Texture sheet = ResourceManager.sheetWolf();
        float s = 0.10f; //Anim speed

        //Still
        left.put(StateComponent.State.STILL, animate(sheet, 0, 192, 64, 64, 1, s));
        right.put(StateComponent.State.STILL, animate(sheet, 0, 128, 64, 64, 1, s));
        up.put(StateComponent.State.STILL, animate(sheet, 0, 64, 64, 64, 1, s));
        down.put(StateComponent.State.STILL, animate(sheet, 0, 0, 64, 64, 1, s));

        //Walking
        left.put(StateComponent.State.WALK, animate(sheet, 0, 192, 64, 64, 1, s));
        right.put(StateComponent.State.WALK, animate(sheet, 0, 128, 64, 64, 1, s));
        up.put(StateComponent.State.WALK, animate(sheet, 0, 64, 64, 64, 1, s));
        down.put(StateComponent.State.WALK, animate(sheet, 0, 0, 64, 64, 1, s));

        return new DirAnimation(left, right, up, down);
    }
    public static DirAnimation animFae(){
        Map<StateComponent.State, Animation> left = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> right = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> up = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> down = new HashMap<StateComponent.State, Animation>();
        Texture sheet = ResourceManager.sheetFae();
        float s = 0.10f; //Anim speed

        //Still
        //left.put(StateComponent.State.STILL, animate(sheet, 0, 192, 64, 64, 1, s));
        //right.put(StateComponent.State.STILL, animate(sheet, 0, 128, 64, 64, 1, s));
        up.put(StateComponent.State.STILL, animate(sheet, 0, 64, 64, 64, 3, s));
        down.put(StateComponent.State.STILL, animate(sheet, 0, 0, 64, 64, 3, s));

        //Walking
        //left.put(StateComponent.State.WALK, animate(sheet, 0, 192, 64, 64, 1, s));
        //right.put(StateComponent.State.WALK, animate(sheet, 0, 128, 64, 64, 1, s));
        up.put(StateComponent.State.WALK, animate(sheet, 0, 64, 64, 64, 3, s));
        down.put(StateComponent.State.WALK, animate(sheet, 0, 0, 64, 64, 3, s));

        return new DirAnimation(left, right, up, down);
    }
    public static Animation healthPotAnim(){
        Texture sheet = ResourceManager.consumeSheet();
        float s = 0.19f; //Anim speed

        //Still
        return animate(sheet, 0, 0, 32, 32, 4, s);
    }

    public static Animation inkPotAnim(){
        Texture sheet = ResourceManager.consumeSheet();
        float s = 0.19f; //Anim speed

        //Still
        return animate(sheet, 0, 32, 32, 32, 4, s);
    }

    public static Animation lifePotAnim(){
        Texture sheet = ResourceManager.sheetLifePot();
        float s = 0.19f; //Anim speed

        //Still
        return animate(sheet, 0, 0, 32, 32, 4, s);
    }

    public static Animation spiralAnim(){
        Texture sheet = ResourceManager.spiralLogoSheet();
        float s = 0.19f; //Anim speed

        //Still
        return animate(sheet, 0, 0, 128, 64, 8, s);
    }
    public static Animation spiral2Anim(){
        Texture sheet = ResourceManager.spiralLogo2Sheet();
        float s = 0.19f; //Anim speed

        //Still
        return animate(sheet, 0, 0, 128, 64, 8, s);
    }
}
