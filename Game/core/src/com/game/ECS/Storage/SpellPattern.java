package com.game.ECS.Storage;

import com.game.ECS.Components.SpellComponent;
import com.game.ECS.Tools.SpellDrawing;

/**
 * Created by Sean on 16/06/2015.
 *
 * Used for the pattern of certain spells
 *
 */
public class SpellPattern {
    public static enum AimType{
        ACCELEROMETER, AIM
    }

    private AimType aimType = AimType.AIM;
    private SpellDrawing spellDrawing;
    private SpellComponent.Spell spellType;
    private String name;

    public SpellPattern(AimType aimType, SpellDrawing spellDrawing, SpellComponent.Spell spellType,
                        String name){
        this.aimType = aimType;
        this.spellDrawing = spellDrawing;
        this.spellType = spellType;
        this.name = name;
    }

    public AimType getAimType(){
        return aimType;
    }

    public SpellDrawing getSpellDrawing(){
        return spellDrawing;
    }

    public SpellComponent.Spell getSpellType(){
        return spellType;
    }

    public String getName(){
        return name;
    }

}
