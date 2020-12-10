package com.game.ECS.Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SpriteComponent;
import com.game.ECS.Storage.Assets;
import com.game.ECS.Storage.GameVars;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sean on 25/04/2015.
 *
 * Extracts all the necessary objects from a map.
 *
 */
public class MapManager {

    private Engine engine;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private List<Vector2> playerSpawns;
    private List<Vector2> enemySpawns;
    private List<MapObject> cameraPath;

    //Layers
    private MapLayer spawnLayer;
    private MapLayer camLayer;
    private MapLayer hitboxLayer;
    private MapLayer collisionLayer;
    private MapLayer objectLayer;

    public MapManager(Engine engine){
        this.engine = engine;
        tiledMap = new TmxMapLoader().load(Assets.tiledMap);

        //Convert map to game units
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f / GameVars.PTM);

        objectLayer = tiledMap.getLayers().get("Objects");
        camLayer = tiledMap.getLayers().get("CameraPath");
        spawnLayer = tiledMap.getLayers().get("Spawns");

        playerSpawns = new ArrayList<Vector2>();
        enemySpawns = new ArrayList<Vector2>();
        cameraPath = new ArrayList<MapObject>();
    }


    //Finds objects and adds them to list
    public void extractObjects() {
        //Get all the objects currently on the map, convert them to sprites to add depth
        for (TextureMapObject textureObj :
                objectLayer.getObjects().getByType(TextureMapObject.class))
        {
            Sprite objSprite = new Sprite(textureObj.getTextureRegion());
            //Convert from pixel perfect
            objSprite.setX(textureObj.getX()/GameVars.PTM);
            objSprite.setY(textureObj.getY() / GameVars.PTM);
            objSprite.setSize(objSprite.getWidth() / GameVars.PTM, objSprite.getHeight() / GameVars.PTM);
            Entity entity = new Entity();
            entity.add(new PositionComponent(objSprite.getX() + objSprite.getWidth() * 0.5f,
                    objSprite.getY() + objSprite.getHeight() * 0.5f))
                    .add(new SpriteComponent(objSprite))
                    .add(new DepthComponent(-objSprite.getHeight() * 0.5f));
            engine.addEntity(entity);
        }

    }

    //Turns spawns into entities and adds them to engine
    public void extractSpawns(){
        MapObjects spawns = spawnLayer.getObjects();
        for(MapObject spawn : spawns){
            Rectangle recSpawn = ((RectangleMapObject) spawn).getRectangle();
            Vector2 spawnLoc = new Vector2();
            spawnLoc.x = ((recSpawn.x+ recSpawn.width * 0.5f) /GameVars.PTM);
            spawnLoc.y = ((recSpawn.y + recSpawn.height * 0.5f) /GameVars.PTM);
            if(spawn.getName().equals("EnemySpawn")){
                enemySpawns.add(spawnLoc);
            }
            if(spawn.getName().equals("PlayerSpawn")){
                playerSpawns.add(spawnLoc);
            }
        }
    }

    public void extractCamPath(){
        MapObjects points = camLayer.getObjects();
        for(MapObject point : points){
            cameraPath.add(point);
        }
    }

    /**
     * Getters
     */

    public List<MapObject> getCameraPath(){
        return cameraPath;
    }

    public MapLayers getMapLayers(){
        return tiledMap.getLayers();
    }

    public MapLayer getObjectLayer(){
        return objectLayer;
    }

    public OrthogonalTiledMapRenderer getTiledMapRenderer() {
        return tiledMapRenderer;
    }

    public Map getMap() {
        return tiledMap;
    }

    private Random rand = new Random();
    public Vector2 getRandomPlayerSpawn(){
        if(playerSpawns.size() == 1){
           return playerSpawns.get(0);
        }
        if(playerSpawns.size() != 0){
            return playerSpawns.get(rand.nextInt(playerSpawns.size()-1));
        }else{
            return null;
        }

    }
    public Vector2 getRandomEnemySpawn(){
        if(enemySpawns.size() == 1){
            return playerSpawns.get(0);
        }
        if(enemySpawns.size() != 0){
            return enemySpawns.get(rand.nextInt(enemySpawns.size()-1));
        }else{
            return null;
        }
    }
}
