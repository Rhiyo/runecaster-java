package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Sean on 28/04/2015.
 */
public class ParticleEffectComponent extends Component {
    public ParticleEffectPool.PooledEffect effect;

    public ParticleEffectComponent(ParticleEffectPool.PooledEffect effect){
        this.effect = effect;
    }
}
