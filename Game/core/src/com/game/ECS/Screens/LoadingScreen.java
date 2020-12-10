package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;

/**
 * Created by Keirron on 6/05/2015.
 */
public class LoadingScreen implements Screen {

    Main game;
    Stage stage;
    PlayerInputComponent playerInput;

    private float barXOffset;

    private float barYOffset;

    public LoadingScreen(Main game, Stage stage, PlayerInputComponent playerInput){
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;
    }

    @Override
    public void show() {
        ResourceManager.load();
        barXOffset = Gdx.graphics.getWidth()/8;
        barYOffset = Gdx.graphics.getHeight()/3;

    }

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(ResourceManager.update()) {
            game.load();
            game.setScreen(new StartMenuScreen(game, stage, playerInput));
        }

        // display loading information
        float progress = ResourceManager.getProgress();
        Gdx.app.log("Progress:",  "%" + progress);

        //Draw loading bar offset from bottom
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        Vector2 barStart = new Vector2(barXOffset, barYOffset);
        Vector2 barEnd = new Vector2((barXOffset*7)*progress, barYOffset);
        shapeRenderer.rectLine(barStart, barEnd, 50* ResolutionHandler.getScale());
        shapeRenderer.end();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
