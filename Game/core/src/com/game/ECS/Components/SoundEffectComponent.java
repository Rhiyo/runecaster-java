package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Sean on 16/06/2015.
 *
 * Used to put single sounds on ane entity
 *
 */
public class SoundEffectComponent extends Component {
    public Sound sound;
    public SoundEffectComponent(Sound sound){
        this.sound = sound;
    }
}
