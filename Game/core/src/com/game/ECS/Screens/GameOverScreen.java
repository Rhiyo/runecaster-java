package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Systems.AIDirectorSystem;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;

/**
 * Created by Keirron on 13/06/2015.
 *
 * Shows when the player has ran out of lives.
 *
 */
public class GameOverScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    private Label gameOverLbl;
    private Label scoreLbl;
    private Label scoreFloatLbl;
    private ImageButton newGameBtn;
    private ImageButton highscoresGameBtn;
    private ImageButton exitGameBtn;

    public GameOverScreen(Main game, Stage stage, PlayerInputComponent playerInput){
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();


        //Create objects
        this.gameOverLbl = createGameOverLabel();
        this.scoreLbl = createScoreLabel();
        this.scoreFloatLbl = createScoreFloatLabel();
        Preferences prefs = Gdx.app.getPreferences("score");
        for(int i = 0; i < 5; i++){
            if(playerInput.gameScore > prefs.getFloat("hscore"+i)){
                prefs.putFloat("hscore"+i, playerInput.gameScore);
                playerInput.gameScore=0;
            }
        }
        prefs.flush();
        this.newGameBtn = createNewGameButton();
        this.highscoresGameBtn = createHighscoresButton();
        this.exitGameBtn = createExitButton();
    }

    @Override
    public void show() {
        stage.addActor(gameOverLbl);
        stage.addActor(scoreLbl);
        stage.addActor(scoreFloatLbl);
        stage.addActor(newGameBtn);
        stage.addActor(highscoresGameBtn);
        stage.addActor(exitGameBtn);
        if (game.currentMusic != ResourceManager.gameOverMusic()) {
            if (game.currentMusic != null)
                game.currentMusic.stop();
            game.currentMusic = ResourceManager.gameOverMusic();
            game.currentMusic.play();
            game.currentMusic.setLooping(true);
            game.currentMusic.setVolume(0.5f);
        }
    }


    @Override
    public void render(float delta) {

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
        gameOverLbl.remove();
        scoreLbl.remove();
        scoreFloatLbl.remove();
        newGameBtn.remove();
        highscoresGameBtn.remove();
        exitGameBtn.remove();
    }

    @Override
    public void dispose() {
        //Dispose stuff
    }

    //Showing the label with text 'Score'
    private Label createGameOverLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("GAME OVER",textStyle);
        text.setAlignment(Align.center);
        text.setFontScale(4f*scale,4f*scale);
        text.setPosition(stage.getViewport().getCamera().viewportWidth * 0.5f,
                stage.getViewport().getCamera().viewportHeight * 0.80f);
        return text;
    }



    //Showing the label with text 'Score'
    private Label createScoreLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("Score:",textStyle);
        text.setAlignment(Align.center);
        text.setFontScale(2f*scale,2f*scale);
        text.setPosition(stage.getViewport().getCamera().viewportWidth * 0.5f,
                stage.getViewport().getCamera().viewportHeight * 0.70f);
        return text;
    }
    //Showing the label with text as the actual float score
    private Label createScoreFloatLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label(""+Math.round(playerInput.gameScore),textStyle);
        text.setFontScale(2f*scale,2f*scale);
        text.setAlignment(Align.center);
        text.setPosition(stage.getViewport().getCamera().viewportWidth * 0.5f,
                stage.getViewport().getCamera().viewportHeight * 0.60f);
        return text;
    }

    public ImageButton createExitButton() {
        Texture exitTexture = ResourceManager.exitMenuButton();
        Skin exitBtnSkin = new Skin();
        exitBtnSkin.add("up", exitTexture);
        exitBtnSkin.add("down", exitTexture);
        exitBtnSkin.add("over", exitTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = exitBtnSkin.getDrawable("up");
        buttonStyle.down = exitBtnSkin.getDrawable("down");
        buttonStyle.over = exitBtnSkin.getDrawable("over");
        exitGameBtn = new ImageButton(buttonStyle);
        exitGameBtn.setSize(exitGameBtn.getWidth()*scale*0.5f, exitGameBtn.getHeight()*scale*0.5f);
        exitGameBtn.setPosition((stage.getViewport().getCamera().viewportWidth * 0.5f) - (exitGameBtn.getWidth() * 0.5f),
                (stage.getViewport().getCamera().viewportHeight * 0.10f) - (exitGameBtn.getHeight() * 0.5f));
        exitGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                Gdx.app.exit();
            }
        });

        return exitGameBtn;
    }


    //Highscore button
    public ImageButton createHighscoresButton() {
        Texture highscoreTexture = ResourceManager.highscoresButton();
        Skin highscoreBtnSkin = new Skin();
        highscoreBtnSkin.add("up", highscoreTexture);
        highscoreBtnSkin.add("down", highscoreTexture);
        highscoreBtnSkin.add("over", highscoreTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = highscoreBtnSkin.getDrawable("up");
        buttonStyle.down = highscoreBtnSkin.getDrawable("down");
        buttonStyle.over = highscoreBtnSkin.getDrawable("over");
        highscoresGameBtn = new ImageButton(buttonStyle);
        highscoresGameBtn.setSize(highscoresGameBtn.getWidth()*scale*0.5f, highscoresGameBtn.getHeight()*scale*0.5f);
        highscoresGameBtn.setPosition((stage.getViewport().getCamera().viewportWidth * 0.5f) - (highscoresGameBtn.getWidth() * 0.5f),
                (stage.getViewport().getCamera().viewportHeight * 0.30f) - (highscoresGameBtn.getHeight() * 0.5f));
        highscoresGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                game.setScreen(new HighscoreScreen(game, stage, playerInput));
            }
        });

        return highscoresGameBtn;
    }

    public ImageButton createNewGameButton() {
        Texture newGameTexture = ResourceManager.newGameMenuButton();
        Skin newGameBtnSkin = new Skin();
        newGameBtnSkin.add("up", newGameTexture);
        newGameBtnSkin.add("down", newGameTexture);
        newGameBtnSkin.add("over", newGameTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = newGameBtnSkin.getDrawable("up");
        buttonStyle.down = newGameBtnSkin.getDrawable("down");
        buttonStyle.over = newGameBtnSkin.getDrawable("over");
        newGameBtn = new ImageButton(buttonStyle);
        newGameBtn.setSize(newGameBtn.getWidth()*scale*0.5f, newGameBtn.getHeight()*scale*0.5f);
        newGameBtn.setPosition((stage.getViewport().getCamera().viewportWidth * 0.5f) - (newGameBtn.getWidth() * 0.5f),
                (stage.getViewport().getCamera().viewportHeight * 0.50f) - (newGameBtn.getHeight() * 0.5f));
        newGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                game.getEngine().addSystem(new AIDirectorSystem(
                        game.getEntityManager().getMapManager(),
                        game.getEntityManager().getWorldManager()));
                playerInput.currentState = PlayerInputComponent.States.FREE;
                playerInput.gameScore = 0;
                playerInput.lives = 3;
                game.getEntityManager().createPlayer(playerInput);
                game.setScreen(new GameScreen(game, stage, playerInput));
            }
        });
        return newGameBtn;
    }
}
