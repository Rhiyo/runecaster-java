package com.game.ECS.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Components.SpellComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Storage.Assets;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;

/**
 * Created by Keirron on 28/04/2015.
 *
 * Used for aiming projectile spells.
 *
 */
public class SpellAimingScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;
    private SpellComponent.Spell spellType;

    private float scale;
    private InputMultiplexer multiplexer;

    private ImageButton cancelSpellBtn;
    private Texture cancelTexture;

    private Label adviceLbl;

    public SpellAimingScreen(Main game, Stage stage, PlayerInputComponent playerInput, SpellComponent.Spell spellType) {
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;
        this.spellType = spellType;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        //More input
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(createInputAdapter());
        //Screen components
        this.cancelSpellBtn = createCancelSpellBtn();

        this.adviceLbl = createAdviceLabel();
    }

    @Override
    public void show() {


        this.stage.addActor(this.cancelSpellBtn);
        Gdx.input.setInputProcessor(multiplexer);

        this.playerInput.currentState = PlayerInputComponent.States.AIMING;
        this.playerInput.gameSpeed = 1;
        stage.addActor(adviceLbl);
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
        this.cancelSpellBtn.remove();
        adviceLbl.remove();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        cancelTexture.dispose();
    }

    public ImageButton createCancelSpellBtn(){
        cancelTexture = ResourceManager.uiCancelButton();
        Skin castBtnSkin = new Skin();
        castBtnSkin.add("up", cancelTexture);
        castBtnSkin.add("down", cancelTexture);
        castBtnSkin.add("over", cancelTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = castBtnSkin.getDrawable("up");
        buttonStyle.down = castBtnSkin.getDrawable("down");
        buttonStyle.over = castBtnSkin.getDrawable("over");
        cancelSpellBtn = new ImageButton(buttonStyle);
        cancelSpellBtn.setSize(cancelSpellBtn.getWidth()*scale, cancelSpellBtn.getHeight()*scale);
        cancelSpellBtn.setPosition(stage.getViewport().getCamera().viewportWidth - (45*scale + cancelSpellBtn.getWidth()),
                stage.getViewport().getCamera().viewportHeight - (45*scale + cancelSpellBtn.getHeight()));
        cancelSpellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, stage, playerInput));
            }
        });

        return cancelSpellBtn;
    }

    public InputAdapter createInputAdapter(){
        return new InputAdapter() {
            private float camModifierX;
            private float camModifierY;

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Gdx.input.vibrate(90);
                playerInput.screenPos = null;
                game.setScreen(new GameScreen(game, stage, playerInput));
                playerInput.spellCast = spellType;
                playerInput.spellDir = new Vector2(screenX, screenY);
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if(playerInput.screenPos == null)
                    playerInput.screenPos = new Vector2(screenX, screenY);
                else {
                    playerInput.screenPos.x = screenX;
                    playerInput.screenPos.y = screenY;
                }

                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                playerInput.screenPos = new Vector2(screenX, screenY);
                adviceLbl.remove();
                return true;
            }
        };
    }

    private Label createAdviceLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("Touch and drag on screen to aim, release to fire...",textStyle);
        text.setAlignment(Align.center);
        text.setFontScale(2f*scale,2f*scale);
        text.setPosition(stage.getViewport().getCamera().viewportWidth * 0.35f,
                stage.getViewport().getCamera().viewportHeight * 0.66f);
        return text;
    }
}
