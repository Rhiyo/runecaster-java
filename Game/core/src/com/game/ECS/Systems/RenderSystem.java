package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.ParticleEffectComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SpriteComponent;
import com.game.ECS.Managers.MapManager;
import com.game.ECS.Storage.GameVars;

import java.util.Comparator;


/**
 * Created by Sean on 25/04/2015.
 *
 * Handles the rendering of all images in this order:
 *
 * TiledMap
 * Sprites with no Y Depth that appear under
 * Sprites with Y Depth sorted correctly
 * Sprites with no Y Depth that appear over
 * World filters
 *
 */
//TODO class doesn't need to be iterated anymore
public class RenderSystem extends SortedIteratingSystem {

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<DepthComponent> dm;
    private ComponentMapper<SpriteComponent> sm;
    private ComponentMapper<ParticleEffectComponent> pem;

    private SpriteBatch sb;
    private MapManager mapManager;
    private World world;

    private Array<Entity> depthEntities;
    private OrthographicCamera cam;

    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;

    public RenderSystem(int order, SpriteBatch sb, MapManager mapManager, World world) {
        super(Family.all(PositionComponent.class, DepthComponent.class).one(
                        SpriteComponent.class, ParticleEffectComponent.class).get(),
                new Comparator<Entity>() {
                    @Override
                    public int compare(Entity e1, Entity e2) {
                        float e1y =  e1.getComponent(PositionComponent.class).y +
                                e1.getComponent(DepthComponent.class).y;
                        float e2y =  e2.getComponent(PositionComponent.class).y +
                                e2.getComponent(DepthComponent.class).y;
                        if (e1y == e2y)
                            return 0;
                        return e1y > e2y ? -1 : 1;
                    }
                }, order
        );
        sm = ComponentMapper.getFor(SpriteComponent.class);
        pm = ComponentMapper.getFor(PositionComponent.class);
        dm = ComponentMapper.getFor(DepthComponent.class);
        pem = ComponentMapper.getFor(ParticleEffectComponent.class);

        this.sb = sb;
        this.mapManager = mapManager;
        this.world = world;

        this.depthEntities = new Array<Entity>();

        //TODO move to a debug renderer system
        this.debugRenderer = new Box2DDebugRenderer();

    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //TODO makes lag fix
        super.forceSort();

        MapLayers mapLayers = mapManager.getMapLayers();
        for(int i = 0; i < mapLayers.getCount();i++){
            MapLayer mapLayer = mapLayers.get(i);
            if(mapLayer.equals(mapManager.getObjectLayer())){
                cam.update();
                sb.setProjectionMatrix(cam.combined);
                sb.begin();
                for(Entity entity : depthEntities){

                    PositionComponent pos = pm.get(entity);

                    SpriteComponent sprite = sm.get(entity);
                    ParticleEffectComponent effect = pem.get(entity);

                    //Draw particle effects
                    if(effect != null){
                        //todo Delta should be fixed here
                        effect.effect.setPosition(pos.x, pos.y);
                        effect.effect.draw(sb, deltaTime);
                        if (effect.effect.isComplete()) {
                            effect.effect.free();
                        }
                    }
                    //Draw sprite centred at position
                    if(sprite != null) {
                        sprite.sprite.setPosition(pos.x - sprite.sprite.getWidth() * 0.5f + sprite.offset.x,
                                pos.y - sprite.sprite.getHeight() * 0.5f + sprite.offset.y);
                        sprite.sprite.draw(sb);
                    }


                }
                sb.end();

            }else{
                mapManager.getTiledMapRenderer().setView(cam);
                mapManager.getTiledMapRenderer().render(new int[]{i});
            }
        }

        //Render Box2D debug
        this.debugMatrix = sb.getProjectionMatrix().cpy().scale(GameVars.PTM,
                GameVars.PTM, 0);
        //debugRenderer.render(world, debugMatrix);
        depthEntities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        depthEntities.add(entity);
    }

    //Setters
    public void setRenderCamera(OrthographicCamera camera){
        this.cam = camera;
    }
}
