package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 25/04/2015.
 *
 * The component of a player
 *
 */
public class PlayerComponent extends Component {
    int playerNumber = 0;

    public PlayerComponent(int playerNumber){
        this.playerNumber = playerNumber;
    }
}
