package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by Sean on 15/06/2015.
 *
 * For single sprite animations
 *
 */
public class SpriteAnimatingComponent extends Component {
    public Animation animation;
    public float time = 0.0f;
    public SpriteAnimatingComponent(Animation anim){
        this.animation = anim;
    }
}
