package com.game.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.MapManager;
import com.game.ECS.Managers.WorldManager;
import com.game.ECS.Storage.EnemyPrefabs;

/**
 * Created by Sean on 3/05/2015.
 */
public class AIDirectorSystem extends EntitySystem {

    private MapManager mapManager;
    private WorldManager worldManager;

    private Engine engine;

    public AIDirectorSystem(MapManager mapManager, WorldManager worldManager) {
        this.mapManager = mapManager;
        this.worldManager = worldManager;
    }

    public void addedToEngine(Engine engine){
        this.engine = engine;
    }


    //Todo remove
    private boolean added = false;
    private float spawnTime = 10f;
    private float currentSpawnTime = spawnTime;

    //Difficulty effects the spawn rate by removing this amount each spawn
    private float difficulty = 0.1f;

    //Switch enemies
    boolean switchEnemies = false;
    public void update(float deltatime){
        if(currentSpawnTime <= 0){
            Vector2 spawn = mapManager.getRandomEnemySpawn();
            Entity enemy = EnemyPrefabs.createWolf(worldManager, spawn);
            Entity enemy2 = EnemyPrefabs.createFae(worldManager, spawn);

            if(!switchEnemies) {
                engine.addEntity(enemy);
                switchEnemies = true;
            }else if(switchEnemies){
                engine.addEntity(enemy2);
                switchEnemies = false;
            }
            added = true;
            currentSpawnTime = spawnTime;
            if(spawnTime <= 1f){
                spawnTime = 1f;
            }else {
                if(spawnTime <= 9)
                    spawnTime -= difficulty*1.3;
                if(spawnTime <= 8)
                    spawnTime -= difficulty*1.7;
                if(spawnTime <= 7)
                    spawnTime -= difficulty*2;
                if(spawnTime <= 4)
                    spawnTime -= difficulty*2.5;
                if(spawnTime <= 2)
                    spawnTime -= difficulty*2;
                spawnTime -= difficulty;
            }
        }else{
            //Todo Delta here should be fixed
            currentSpawnTime -= deltatime;
        }
    }
}
