package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Components.SpellComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;

import java.util.LinkedList;


/**
 * Created by Keirron on 13/05/2015.
 *
 * Screen for aiming accelerometer spells
 *
 */
public class AccelerometerScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;
    private float duration;
    private float currentDuration;

    private Label moveLbl;
    private float barSize;


    public AccelerometerScreen(Main game, Stage stage, PlayerInputComponent playerInput, float duration) {
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();
        this.duration = duration;
        this.currentDuration = duration;

        this.moveLbl = createMoveLabel();

        //Scalebar
        this.barSize = 300*scale;
    }

        @Override
        public void show() {
            stage.addActor(moveLbl);
            playerInput.gameSpeed = 1;

            if (game.currentMusic != ResourceManager.gameMusic()){
                if (game.currentMusic != null)
                    game.currentMusic.stop();
                game.currentMusic = ResourceManager.gameMusic();
                game.currentMusic.play();
                game.currentMusic.setPosition(game.musicTime);
                game.currentMusic.setLooping(true);
                game.currentMusic.setVolume(0.5f);
            }
        }

    ShapeRenderer shapeRenderer = new ShapeRenderer();
    float vibrateTimer = 0.2f;
    float currentTime = vibrateTimer;
    @Override
    public void render(float delta) {
        //Vibrate phone
        if(currentTime<0) {
            Gdx.input.vibrate(40);
            currentTime = vibrateTimer;
        }else{
            currentTime-=delta;
        }
        currentDuration -= delta;
        if(currentDuration <= 0) {
            game.setScreen(new GameScreen(game, stage, playerInput));
            return;
        }

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float barOffset = 55*scale; //Offset from bottom
        Vector2 timerStart = new Vector2();
        Vector2 timerEnd = new Vector2();
        float barLength = 1;
        if(duration != 0) {
            timerStart.x = stage.getViewport().getScreenWidth() * 0.5f - barSize * 0.5f;
            timerStart.y = barOffset;

            timerEnd.x = timerStart.x + barSize;
            timerEnd.y = barOffset;


            //Behind Health Bar
            shapeRenderer.setColor(Color.BLACK);
            //Draw fancier back bar

            barLength = 1*scale;
            for (int i = 15; i > 0; i = i - 3) {
                shapeRenderer.rectLine(new Vector2(timerStart.x - barLength, timerStart.y),
                        new Vector2(timerEnd.x + barLength, timerEnd.y), i*scale);
                barLength++;
            }
            //Actual Health Representation
            shapeRenderer.setColor(Color.YELLOW);
            timerEnd.x = timerStart.x + barSize * (currentDuration /
                    duration);
            shapeRenderer.rectLine(timerStart, timerEnd, 10*scale);
        }
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
        moveLbl.remove();
    }

    @Override
    public void dispose() {

    }

    //Move phone around
    private Label createMoveLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("Move device around...",textStyle);
        text.setAlignment(Align.center);
        text.setFontScale(4f*scale,4f*scale);
        text.setPosition(stage.getViewport().getCamera().viewportWidth * 0.4f,
                stage.getViewport().getCamera().viewportHeight * 0.66f);
        return text;
    }

}
