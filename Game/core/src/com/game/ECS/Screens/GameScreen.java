package com.game.ECS.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.game.ECS.Components.ConsumableComponent;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Storage.Assets;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;

/**
 * Created by Keirron on 26/04/2015.
 *
 * Screen containing the HUD for the game.
 *
 */
public class GameScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Texture castTexture;
    private float minButtonHeight;
    private float maxButtonHeight;
    private ImageButton castSpellBtn;
    private Image castSpellBtnShadow;
    private Texture castSpellBtnShadowTexture;
    private Label scoreLabel;
    private Label scoreFloatLabel;
    private Label livesLabel;
    private Label livesIntLabel;

    private Label healthPotionsLbl;
    private Label inkPotionsLbl;
    private ImageButton healthPotBtn;
    private ImageButton inkPotBtn;

    private float barSize;

    public GameScreen(Main game, Stage stage, PlayerInputComponent playerInput){
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        //Screen components
        this.touchpad = createTouchpad();
        this.castSpellBtn = createCastSpellBtn();
        this.scoreLabel = createScoreLabel();
        this.scoreFloatLabel = createScoreFloatLabel();
        this.livesLabel = createLivesLabel();
        this.livesIntLabel = createLivesIntLabel();
        this.healthPotionsLbl = createPotQtyLbl(stage.getViewport().getScreenWidth() * 0.25f + 50*scale,
                20*scale);
        this.inkPotionsLbl = createPotQtyLbl(stage.getViewport().getScreenWidth() * 0.75f -75*scale + 50*scale,
                20*scale);
        this.healthPotBtn = healthBtn(stage.getViewport().getScreenWidth() * 0.25f,
                20*scale, ConsumableComponent.ConsumeType.Health, ResourceManager.healthPot());
        this.healthPotBtn.setSize(75*scale,75*scale);
        this.inkPotBtn = inkBtn(stage.getViewport().getScreenWidth() * 0.75f - 75*scale,
                20*scale, ConsumableComponent.ConsumeType.Ink, ResourceManager.inkPot());
        this.inkPotBtn.setSize(75*scale,75*scale);
        //this.inkPotionsLbl = createPotQtyLbl(1,1);

        //Scale some things
        this.barSize = 200*scale;

        //Set the input as this stage
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        stage.addActor(touchpad);
        stage.addActor(castSpellBtn);
        stage.addActor(castSpellBtnShadow);
        stage.addActor(scoreLabel);
        stage.addActor(scoreFloatLabel);
        stage.addActor(livesLabel);
        stage.addActor(livesIntLabel);
        stage.addActor(healthPotBtn);
        stage.addActor(inkPotBtn);
        stage.addActor(healthPotionsLbl);
        stage.addActor(inkPotionsLbl);

        this.playerInput.currentState = PlayerInputComponent.States.FREE;
        this.playerInput.gameSpeed = 1;
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

    //For the animation of the cast spell button
    private boolean isRising = true;
    private float moveSpeed = 10;

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Override
    public void render(float delta) {
        //Feed touchpad input to player entity

        playerInput.touchpadDir.x = touchpad.getKnobPercentX();
        playerInput.touchpadDir.y = touchpad.getKnobPercentY();

        //Update score
        scoreFloatLabel.setText(""+Math.round(playerInput.gameScore));
        //Update lives
        livesIntLabel.setText(""+playerInput.lives);

        //Animate cast spell button
        //TODO need to lerp book floating up and down
        moveSpeed = 20f;
        if(isRising){
            castSpellBtn.setY(castSpellBtn.getY()+ moveSpeed*Gdx.graphics.getDeltaTime());
            if(castSpellBtn.getY() >= maxButtonHeight){
                isRising = false;
            }
        }else if(!isRising){
            castSpellBtn.setY(castSpellBtn.getY() - moveSpeed*Gdx.graphics.getDeltaTime());
            if(castSpellBtn.getY() <= minButtonHeight) {
                isRising = true;
            }
        }

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float barOffset = 55*scale; //Offset from bottom
        Vector2 healthStart = new Vector2();
        Vector2 healthEnd = new Vector2();
        float barLength = 1;
        if(playerInput.playerHealth != null) {
            healthStart.x = stage.getViewport().getScreenWidth() * 0.5f - barSize * 0.5f;
            healthStart.y = barOffset;

            healthEnd.x = healthStart.x + barSize;
            healthEnd.y = barOffset;


            //Behind Health Bar
            shapeRenderer.setColor(Color.BLACK);
            //Draw fancier back bar

            barLength = 1*scale;
            for (int i = 15; i > 0; i = i - 3) {
                shapeRenderer.rectLine(new Vector2(healthStart.x - barLength, healthStart.y),
                        new Vector2(healthEnd.x + barLength, healthEnd.y), i*scale);
                barLength++;
            }
            //Actual Health Representation
            shapeRenderer.setColor(Color.RED);
            healthEnd.x = healthStart.x + barSize * (playerInput.playerHealth.currentHealth /
                    playerInput.playerHealth.maxHealth);
            shapeRenderer.rectLine(healthStart, healthEnd, 10*scale);
        }

        if(playerInput.playerInk != null) {
            //Draw InkBar
            barOffset = 35 * scale; //Offset from bottom


            //Actual Ink
            healthStart.x = stage.getViewport().getScreenWidth() * 0.5f - barSize * 0.5f;
            healthStart.y = barOffset;
            healthEnd.x = healthStart.x + barSize;
            healthEnd.y = barOffset;


            //Behind Ink Bar
            shapeRenderer.setColor(Color.BLACK);
            //Draw fancier back bar
            barLength = 1*scale;
            for (int i = 15; i > 0; i = i - 3) {
                shapeRenderer.rectLine(new Vector2(healthStart.x - barLength, healthStart.y),
                        new Vector2(healthEnd.x + barLength, healthEnd.y), i*scale);
                barLength++;
            }
            //Actual Health Representation
            shapeRenderer.setColor(Color.PURPLE);
            Vector2 healthMini = new Vector2(healthEnd);
            healthEnd.x = healthStart.x + barSize * (playerInput.playerInk.currentInk /
                    playerInput.playerInk.maxInk);
            healthMini.x = (healthStart.x + 5*scale) + (barSize - 10*scale) * (playerInput.playerInk.currentInk /
                    playerInput.playerInk.maxInk);
            shapeRenderer.rectLine(healthStart, healthEnd, 10*scale);

            shapeRenderer.setColor(Color.MAROON);
            shapeRenderer.rectLine(new Vector2(healthStart.x + 5*scale, healthStart.y - 1*scale),
                    new Vector2(healthMini.x, healthMini.y - 1*scale), 1*scale);
            //Border
        }

        //Potion slots
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(stage.getViewport().getScreenWidth() * 0.25f,
                20*scale,
                75*scale,
                75*scale);
        shapeRenderer.rect(stage.getViewport().getScreenWidth() * 0.75f,
                20*scale,
                -75*scale,
                75*scale);
        shapeRenderer.end();

        healthPotionsLbl.setText(""+playerInput.healthPots);
        inkPotionsLbl.setText(""+playerInput.inkPots);
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
        touchpad.remove();
        castSpellBtn.remove();
        castSpellBtnShadow.remove();
        scoreLabel.remove();
        scoreFloatLabel.remove();
        livesLabel.remove();
        livesIntLabel.remove();
        healthPotionsLbl.remove();
        inkPotionsLbl.remove();
        healthPotBtn.remove();
        inkPotBtn.remove();
        //No longer giving input
        playerInput.touchpadDir.x = 0;
        playerInput.touchpadDir.y = 0;
    }

    @Override
    public void dispose() {
        touchpadSkin.dispose();
        castTexture.dispose();
        castSpellBtnShadowTexture.dispose();
    }

    public Touchpad createTouchpad(){
        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", ResourceManager.uiKnobBG());
        touchpadSkin.add("touchKnob", ResourceManager.uiKnob());
        touchpadStyle = new Touchpad.TouchpadStyle();
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        touchKnob.setMinWidth(touchKnob.getMinWidth()*scale);
        touchKnob.setMinHeight(touchKnob.getMinHeight()*scale);
        touchpad = new Touchpad(20, touchpadStyle);
        touchpad.setBounds(15, 15, 200, 200);
        touchpad.setPosition(5f*scale,5f*scale);
        touchpad.setSize(touchpad.getWidth()*scale, touchpad.getHeight()*scale);

        return touchpad;
    }

    public ImageButton createCastSpellBtn(){
        castTexture = ResourceManager.uiCastSpellBtn();
        Skin castBtnSkin = new Skin();
        castBtnSkin.add("up", castTexture);
        castBtnSkin.add("down", castTexture);
        castBtnSkin.add("over", castTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = castBtnSkin.getDrawable("up");
        buttonStyle.down = castBtnSkin.getDrawable("down");
        buttonStyle.over = castBtnSkin.getDrawable("over");
        castSpellBtn = new ImageButton(buttonStyle);
        castSpellBtn.setSize(castSpellBtn.getWidth()*scale, castSpellBtn.getHeight()*scale);
        castSpellBtn.setPosition(stage.getViewport().getCamera().viewportWidth - (45*scale + castSpellBtn.getWidth()), 40*scale);
        castSpellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Todo remove this when SpellPreparing screen is implemented
                if(playerInput.playerHealth.currentHealth >= 4 ||
                        playerInput.playerInk.currentInk > 0){
                    Gdx.input.vibrate(75);
                    game.musicTime = game.currentMusic.getPosition();
                    game.setScreen(new SpellCastingScreen(game, stage, playerInput));
                }

            }
        });

        castSpellBtnShadowTexture = ResourceManager.uiCastSpellShadow();
        castSpellBtnShadow = new Image(new TextureRegion(castSpellBtnShadowTexture));
        castSpellBtnShadow.setSize(castSpellBtnShadow.getWidth()*scale, castSpellBtnShadow.getHeight()*scale);
        castSpellBtnShadow.setPosition(stage.getViewport().getCamera().viewportWidth-(45*scale+castSpellBtn.getWidth()), 40*scale);
        //For animating Cast Spell button...
        minButtonHeight = castSpellBtn.getY();
        maxButtonHeight = castSpellBtn.getY()+10*scale;

        return castSpellBtn;
    }

    //Show potions
    private Label createPotQtyLbl(float x, float y){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("3",textStyle);
        text.setFontScale(2f*scale,2f*scale);
        text.setAlignment(Align.left);
        text.setPosition(x,y);
        return text;
    }

    private ImageButton healthBtn(float x, float y, ConsumableComponent.ConsumeType type,
                                   Texture texture){
        Texture castTexture = texture;
        Skin castBtnSkin = new Skin();
        castBtnSkin.add("up", castTexture);
        castBtnSkin.add("down", castTexture);
        castBtnSkin.add("over", castTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = castBtnSkin.getDrawable("up");
        buttonStyle.down = castBtnSkin.getDrawable("down");
        buttonStyle.over = castBtnSkin.getDrawable("over");
        ImageButton castSpellBtn = new ImageButton(buttonStyle);
        castSpellBtn.setSize(castSpellBtn.getWidth()*scale, castSpellBtn.getHeight()*scale);
        castSpellBtn.setPosition(x, y);
        castSpellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Todo remove this when SpellPreparing screen is implemented
                if(playerInput.healthPots > 0
                        && playerInput.playerHealth.currentHealth !=
                        playerInput.playerHealth.maxHealth){
                    playerInput.playerHealth.currentHealth+=3;
                    if(playerInput.playerHealth.currentHealth >
                            playerInput.playerHealth.maxHealth)
                        playerInput.playerHealth.currentHealth = playerInput.playerHealth.maxHealth;
                    playerInput.healthPots--;
                    ResourceManager.soundDrinkPotion().play();
                }

            }
        });

        return castSpellBtn;
    }

    private ImageButton inkBtn(float x, float y, ConsumableComponent.ConsumeType type,
                                  Texture texture){
        Texture castTexture = texture;
        Skin castBtnSkin = new Skin();
        castBtnSkin.add("up", castTexture);
        castBtnSkin.add("down", castTexture);
        castBtnSkin.add("over", castTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = castBtnSkin.getDrawable("up");
        buttonStyle.down = castBtnSkin.getDrawable("down");
        buttonStyle.over = castBtnSkin.getDrawable("over");
        ImageButton castSpellBtn = new ImageButton(buttonStyle);
        castSpellBtn.setSize(castSpellBtn.getWidth()*scale, castSpellBtn.getHeight()*scale);
        castSpellBtn.setPosition(x, y);
        castSpellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Todo remove this when SpellPreparing screen is implemented
                if(playerInput.inkPots > 0
                        && playerInput.playerInk.currentInk !=
                        playerInput.playerInk.maxInk){
                    playerInput.playerInk.currentInk+=10;
                    if(playerInput.playerInk.currentInk >
                            playerInput.playerInk.maxInk)
                        playerInput.playerInk.currentInk = playerInput.playerInk.maxInk;
                    playerInput.inkPots--;
                    ResourceManager.soundDrinkPotion().play();
                }

            }
        });

        return castSpellBtn;
    }

    //Showing the label with text as the actual int lives
    private Label createLivesIntLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("3",textStyle);
        text.setFontScale(2f*scale,2f*scale);
        text.setAlignment(Align.left);
        text.setPosition(stage.getViewport().getCamera().viewportWidth * 0.18f,
                stage.getViewport().getCamera().viewportHeight * 0.80f);
        return text;
    }

    //Showing the label with text 'Lives'
    private Label createLivesLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("Lives:",textStyle);
        text.setFontScale(2f*scale,2f*scale);
        text.setPosition(stage.getViewport().getCamera().viewportWidth * 0.05f,
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
        text.setFontScale(2f*scale,2f*scale);
        text.setPosition(stage.getViewport().getCamera().viewportWidth * 0.05f,
                stage.getViewport().getCamera().viewportHeight * 0.90f);
        return text;
    }
    //Showing the label with text as the actual float score
    private Label createScoreFloatLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("0",textStyle);
        text.setFontScale(2f*scale,2f*scale);
        text.setAlignment(Align.left);
        text.setPosition(stage.getViewport().getCamera().viewportWidth * 0.18f,
                stage.getViewport().getCamera().viewportHeight * 0.90f);
        return text;
    }
}
