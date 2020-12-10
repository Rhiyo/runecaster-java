package com.game.ECS.Storage;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.ParticleEffectComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.ProjectileComponent;
import com.game.ECS.Components.SpriteComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Managers.WorldManager;

/**
 * Created by Sean on 28/04/2015.
 */
public class ProjectilePrefabs {

    public static Entity createIce(Vector2 pos, Entity owner, Vector2 dir, WorldManager worldManager){
        Entity projectile = new Entity();
        SpriteComponent sc = new SpriteComponent();
        ProjectileComponent pc = new ProjectileComponent();
        BodyComponent bc = new BodyComponent();
        ParticleEffectComponent pec = new ParticleEffectComponent(Particles.iceProjectile());

        sc.sprite = new Sprite(ResourceManager.projIce());
        sc.sprite.setSize(2, 2); //Todo automate this
        pc.owner = owner;
        pc.speed = 12;
        pc.dir = dir;
        bc.body = worldManager.createProjectileBody(projectile);
        bc.body.setTransform(new Vector2(pos.x/GameVars.PTM, pos.y/GameVars.PTM), bc.body.getAngle());

        //Need to flip effect
        rotateBy(dir.angle() -180f, pec.effect);

        projectile.add(sc)
                .add(pc)
                .add(bc)
                .add(pec)
                .add(new PositionComponent(pos.x, pos.y))
                .add(new DepthComponent(-sc.sprite.getHeight()*0.5f));
        return projectile;
    }

    //Rotates the emitters of the particle effect
    private static void rotateBy(float amountInDegrees, ParticleEffect effect) {
        Array<ParticleEmitter> emitters = effect.getEmitters();
        for (int i = 0; i < emitters.size; i++) {
            ParticleEmitter.ScaledNumericValue val = emitters.get(i).getAngle();
            float amplitude = (val.getHighMax() - val.getHighMin()) / 2f;
            float h1 = amountInDegrees + amplitude;
            float h2 = amountInDegrees - amplitude;
            val.setHigh(h1, h2);
            val.setLow(amountInDegrees);
        }
    }

}
