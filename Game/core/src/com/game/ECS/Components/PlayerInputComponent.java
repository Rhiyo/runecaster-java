package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Sean on 27/04/2015.
 *
 * The inbetween of the UI and the Player.
 *
 * Used for controlling aspects of the game via the UI, and receiving aspects of the game to give
 * to the UI,
 *
 */
public class PlayerInputComponent extends Component {
    public static enum States{
        FREE, DRAWING, AIMING, DEAD
    }


    public static enum GameState{
        Menu, Playing
    }

    public Vector2 touchpadDir = new Vector2(0, 0);
    public Vector2 screenPos; //Touched screen position converted to world cood
    public States currentState = States.FREE;
    public HealthComponent playerHealth;
    public InkComponent playerInk;
    public float gameScore = 0;
    public float gameSpeed = 1;
    public int lives = 3;
    public SpellComponent.Spell spellCast; //if null no spell is being cast
    public Vector2 spellDir;
    public int healthPots = 2;
    public int inkPots = 3;
    public float[] highscores;
    //TODO rework spellcast
}
