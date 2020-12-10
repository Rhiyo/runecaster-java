package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by sean on 10/10/15.
 */
public class EnemyTypeComponent extends Component {

    public enum EnemyType{
        WOLF, FAE
    }

    public EnemyType type;

    public EnemyTypeComponent(EnemyType type){ this.type = type; }
}
