package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Sean on 16/06/2015.
 */
public class SoundSetComponent extends Component {

    public Array<Sound> damageTaken;
    public Array<Sound> death;
    public Array<Sound> lunge;
    public Sound step;
    public Sound currentSound;
}
