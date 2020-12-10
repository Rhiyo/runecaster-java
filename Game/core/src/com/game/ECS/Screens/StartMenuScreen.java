package com.game.ECS.Screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;

/**
 * Created by Keirron on 13/06/2015.
 *
 * Shown when the game first starts up.
 *
 */
public class StartMenuScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    private Image gameLogo;
    private Image tapToStart;

    private InputMultiplexer multiplexer;

    public StartMenuScreen(Main game, Stage stage, PlayerInputComponent playerInput){
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        //Create objects
        this.gameLogo = createGameLogo();
        this.tapToStart = createTapToStart();

        //Input
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(createInputAdapter());
    }

    @Override
    public void show() {
        stage.addActor(gameLogo);
        stage.addActor(tapToStart);
        Gdx.input.setInputProcessor(multiplexer);
        if (game.currentMusic != ResourceManager.menuMusic()) {
            if (game.currentMusic != null)
                game.currentMusic.stop();
            game.currentMusic = ResourceManager.menuMusic();
            game.currentMusic.play();
            game.currentMusic.setLooping(true);
            game.currentMusic.setVolume(0.5f);
        }

    }


    private boolean tapToStartDir = false;
    private float tapToStartAlpha = 0;

    @Override
    public void render(float delta) {
        //Fade
        if(!tapToStartDir){
            tapToStartAlpha += delta;
            setTapToStartAlpha(tapToStartAlpha);
        }else{
            tapToStartAlpha -= delta;
            setTapToStartAlpha(tapToStartAlpha);
        }

        //Switch fade in/out
        if(tapToStartAlpha < 0 || tapToStartAlpha > 1){
            tapToStartDir = !tapToStartDir;
        }
    }


    private void setTapToStartAlpha(float alpha){
        tapToStart.setColor(tapToStart.getColor().r,
                tapToStart.getColor().g,
                tapToStart.getColor().b,
                alpha);
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
        tapToStart.remove();
        gameLogo.remove();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        //Todo dispose this
    }

    private Image createGameLogo(){
        Image GameLogo = new Image(new TextureRegion(ResourceManager.gameLogo()));
        GameLogo.setSize(GameLogo.getWidth()*scale, GameLogo.getHeight()*scale);
        GameLogo.setPosition((stage.getViewport().getCamera().viewportWidth*0.5f)-(GameLogo.getWidth()*0.5f),
                (stage.getViewport().getCamera().viewportHeight*0.66f)-(GameLogo.getHeight()*0.5f));
        return GameLogo;
    }

    private Image createTapToStart(){
        Image tapToStart = new Image(new TextureRegion(ResourceManager.tapToStart()));
        tapToStart.setSize(tapToStart.getWidth()*scale*0.5f, tapToStart.getHeight()*scale*0.5f);
        tapToStart.setPosition((stage.getViewport().getCamera().viewportWidth*0.5f)-(tapToStart.getWidth()*0.5f),
                (stage.getViewport().getCamera().viewportHeight*0.18f)-(tapToStart.getHeight()*0.5f));
        return tapToStart;
    }

    public InputAdapter createInputAdapter() {
        return new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Gdx.input.vibrate(75);
                game.setScreen(new MainMenuScreen(game, stage, playerInput));
                return true;
            }


        };
    }
}
