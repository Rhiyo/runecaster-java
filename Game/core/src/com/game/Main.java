package com.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.EntityManager;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Screens.GameOverScreen;
import com.game.ECS.Screens.GameScreen;
import com.game.ECS.Screens.LoadingScreen;

/**
 * Created by Sean on 25/04/2015.
 */
public class Main extends Game {

    private EntityManager entityManager;
    private Stage stage;
    private Engine engine;
    private SpriteBatch sb;
    private PlayerInputComponent inputComponent;

    public Music currentMusic;
    public float musicTime = 0;

    @Override
    public void create() {
        engine = new Engine();
        stage = new Stage();
        sb = new SpriteBatch();
        inputComponent = new PlayerInputComponent();

        setScreen(new LoadingScreen(this, stage, inputComponent));
    }

    @Override
    public void resize(int width, int height) {

    }

    //Entity Manager ready when loaded
    public void load(){
        entityManager = new EntityManager(engine, sb, inputComponent);
    }

    //Todo FPS move this to debug render system
    FPSLogger fps = new FPSLogger();
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(inputComponent.currentState == PlayerInputComponent.States.DEAD) {
            setScreen(new GameOverScreen(this, stage, inputComponent));
            inputComponent.currentState = PlayerInputComponent.States.FREE;
        }
        //Get everything from UI
        stage.act(Gdx.graphics.getDeltaTime()); //TODO fix delta

        if(ResourceManager.isLoaded()) {
            //Update then Render the game world, which always appears in the background
            entityManager.update();
        }

        //Render the UI
        super.render();

        //Render the stage
        stage.draw();


        //fps.log();


    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    //Getters
    public Engine getEngine(){
        return engine;
    }
    public EntityManager getEntityManager() { return entityManager; }
    public SpriteBatch getSpriteBatch() { return sb; }
}

