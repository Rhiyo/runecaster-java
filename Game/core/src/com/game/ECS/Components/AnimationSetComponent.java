package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.game.ECS.Storage.Assets;

/**
 * Created by Sean on 27/04/2015.
 */
public class AnimationSetComponent extends Component {
    public Assets.DirAnimation animations;

    public AnimationSetComponent(Assets.DirAnimation animations){
        this.animations = animations;
    }
}
