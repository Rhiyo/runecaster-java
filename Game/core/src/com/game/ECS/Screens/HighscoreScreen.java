package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
 */
public class HighscoreScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    private Label[] scores = new Label[5];
    private ImageButton exitGameBtn;

    public HighscoreScreen(Main game, Stage stage, PlayerInputComponent playerInput){
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        //Create objects
        //Obtain highscores
        Preferences prefs = Gdx.app.getPreferences("score");
        for(int i = 0; i < scores.length; i++){
            this.scores[i] = createScoreFloatLabel(i, prefs.getFloat("hscore"+i));
        }
        prefs.flush();
        this.exitGameBtn = createExitButton();
    }

    @Override
    public void show() {
        for(Label score : scores){
            stage.addActor(score);
        }
        stage.addActor(exitGameBtn);
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
        for(Label score : scores)
            score.remove();
        exitGameBtn.remove();
    }

    @Override
    public void dispose() {
        //Dispose
    }

    //Showing the label with text as the actual float score
    private Label createScoreFloatLabel(int num, float score){
        float number = num+1;
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label(num+1 + ": " + Math.round(score),textStyle);
        text.setFontScale(2f*scale,2f*scale);
        text.setAlignment(Align.left);
        text.setPosition(stage.getViewport().getCamera().viewportWidth * 0.33f,
                stage.getViewport().getCamera().viewportHeight * (1 - number/10));
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
                (stage.getViewport().getCamera().viewportHeight * 0.20f) - (exitGameBtn.getHeight() * 0.5f));
        exitGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                game.setScreen(new MainMenuScreen(game, stage, playerInput));
            }
        });

        return exitGameBtn;
    }

}
