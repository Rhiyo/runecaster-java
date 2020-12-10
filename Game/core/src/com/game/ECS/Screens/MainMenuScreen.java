package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Storage.Assets;
import com.game.ECS.Storage.GameVars;
import com.game.ECS.Storage.Particles;
import com.game.ECS.Systems.AIDirectorSystem;
import com.game.ECS.Tools.AnimatedImage;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;
import com.badlogic.gdx.audio.Music;


/**
 * Created by Keirron on 13/06/2015.
 */
public class MainMenuScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    private Image gameLogo;
    private Image bookLogo;
    private Sprite bookSprite;
    private Image spiralPoints;
    private AnimatedImage spiralLogo;
    private AnimatedImage spiral2Logo;
    private ImageTextButton newGameBtn;
    private ImageTextButton exitGameBtn;
    private ImageTextButton highscoresGameBtn;

    private ParticleEffectPool.PooledEffect effect;
    private SpriteBatch sb;

    //Point animation
    boolean isPointReverse;
    float pointAnimMax;
    float pointAnimCurrent;
    Table pointsTable;

    public MainMenuScreen(final Main game, final Stage stage, final PlayerInputComponent playerInput){
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        //Create objects

        this.gameLogo = createGameLogo();
        this.spiralLogo = createSpiralLogo();
        this.spiral2Logo = createSpiral2Logo();
        this.spiralPoints = createSpiralPoints();
        this.bookLogo = createBookLogo();
        this.bookSprite = new Sprite(ResourceManager.bookLogo());

        bookSprite.setSize(bookLogo.getWidth() * 2f,
                bookLogo.getHeight() * 2f);

        this.newGameBtn = createButton("NEW GAME", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                //Stop music
                game.getEngine().addSystem(new AIDirectorSystem(
                        game.getEntityManager().getMapManager(),
                        game.getEntityManager().getWorldManager()));
                game.getEntityManager().createPlayer(playerInput);
                game.setScreen(new GameScreen(game, stage, playerInput));
            }
        });

        this.highscoresGameBtn = createButton("HIGHSCORES", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                game.setScreen(new HighscoreScreen(game, stage, playerInput));
            }
        });

        this.exitGameBtn = createButton("LEAVE", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                Gdx.app.exit();
            }
        });

        //Root table
        Table table = new Table();
        table.setFillParent(true);
        Stack stack = new Stack();
        Table spiralTable = new Table();
        Table spiral2Table = new Table();
        Table logoTable = new Table();
        Table bookTable = new Table();
        pointsTable = new Table();
        spiralTable.add(spiralLogo).size(spiralLogo.getWidth() * 1.5f,
                spiralLogo.getHeight() * 1.5f).pad(0, 0, 80 * scale, 0);
        spiral2Table.add(spiral2Logo).size(spiralLogo.getWidth() * 1.7f,
                spiralLogo.getHeight() * 1.7f).pad(0, 0, 80 * scale, 0);
        logoTable.add(gameLogo).size(gameLogo.getWidth() * 2,
                gameLogo.getHeight() * 2);
        bookTable.add(bookLogo).size(bookLogo.getWidth() * 2f,
                bookLogo.getHeight() * 2f).pad(0, 0, 40 * scale, 0);;
        pointsTable.add(spiralPoints).size(spiralPoints.getWidth() * 3,
                spiralPoints.getHeight() *3).pad(0, 0, 90 * scale, 0);
        pointAnimCurrent = pointsTable.getY();
        pointAnimMax = 5*scale;
        //stack.add(bookTable);
        stack.add(spiralTable);
        stack.add(spiral2Table);
        stack.add(pointsTable);
        stack.add(logoTable);
        //.size(ResourceManager.gameLogo().getWidth()*2,
        //ResourceManager.gameLogo().getHeight()*2);
        table.add(stack);
        table.row();
        //Set up buttons
        Table rowTable = new Table();
        rowTable.add(newGameBtn).size(ResourceManager.menuBtnUp().getWidth() * 2 * scale,
                ResourceManager.menuBtnUp().getHeight() * 3).pad(10*scale);
        rowTable.add(highscoresGameBtn).size(ResourceManager.menuBtnUp().getWidth() * 2 * scale,
                ResourceManager.menuBtnUp().getHeight() * 3).pad(10 * scale);;
        rowTable.add(exitGameBtn).size(ResourceManager.menuBtnUp().getWidth() * 2 * scale,
                ResourceManager.menuBtnUp().getHeight() * 3).pad(10 * scale);

        table.add(rowTable).height(Gdx.graphics.getHeight() * 0.25f);
        stage.addActor(table);

        //Particle effect for logo BG
        effect = Particles.logoBackground();
        effect.setPosition(gameLogo.getX(),
                gameLogo.getY()+gameLogo.getHeight()/2+25 * scale);
        sb = new SpriteBatch();
        sb.setProjectionMatrix(stage.getViewport().getCamera().projection);

    }

    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        sb.begin();
        bookSprite.setPosition(gameLogo.getX() - bookSprite.getWidth() / 2,
                gameLogo.getY() + gameLogo.getHeight() / 2 - bookSprite.getHeight() / 2 - 40 * scale);
        bookSprite.draw(sb);
        effect.draw(sb, delta);
        if (effect.isComplete()) {
            effect.free();
        }
        sb.end();

        //Point animation

        float pointAnimSpeed = 10;
        if(isPointReverse){
            pointsTable.setY(pointsTable.getY() - pointAnimSpeed*delta*scale);
            if(pointsTable.getY()<pointAnimCurrent)
                isPointReverse=false;
        }else{
            pointsTable.setY(pointsTable.getY() + pointAnimSpeed*delta*scale);
            if(pointsTable.getY()>pointAnimCurrent+pointAnimMax)
                isPointReverse=true;
        }
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
        gameLogo.remove();
        newGameBtn.remove();
        spiralPoints.remove();
        bookLogo.remove();
        spiralLogo.remove();
        spiral2Logo.remove();
        highscoresGameBtn.remove();
        exitGameBtn.remove();
    }

    @Override
    public void dispose() {
        //Dispose shit
    }

    private Image createGameLogo(){
        Image gameLogo = new Image(new TextureRegion(ResourceManager.gameLogo()));
        //GameLogo.setPosition((stage.getViewport().getCamera().viewportWidth*0.5f)-(GameLogo.getWidth()*0.5f),
        //        (stage.getViewport().getCamera().viewportHeight*0.85f)-(GameLogo.getHeight()*0.5f));
        return gameLogo;
    }

    private Image createBookLogo(){
        Image bookLogo = new Image(new TextureRegion(ResourceManager.bookLogo()));
        //GameLogo.setPosition((stage.getViewport().getCamera().viewportWidth*0.5f)-(GameLogo.getWidth()*0.5f),
        //        (stage.getViewport().getCamera().viewportHeight*0.85f)-(GameLogo.getHeight()*0.5f));
        return bookLogo;
    }

    private Image createSpiralPoints(){
        Image spiralPoints = new Image(new TextureRegion(ResourceManager.spiralLogoPoints()));
        //GameLogo.setPosition((stage.getViewport().getCamera().viewportWidth*0.5f)-(GameLogo.getWidth()*0.5f),
        //        (stage.getViewport().getCamera().viewportHeight*0.85f)-(GameLogo.getHeight()*0.5f));
        return spiralPoints;
    }

    private AnimatedImage createSpiralLogo(){
        AnimatedImage spiralLogo = new AnimatedImage(Assets.spiralAnim());
        //GameLogo.setPosition((stage.getViewport().getCamera().viewportWidth*0.5f)-(GameLogo.getWidth()*0.5f),
        //        (stage.getViewport().getCamera().viewportHeight*0.85f)-(GameLogo.getHeight()*0.5f));
        //spiralLogo.setOrigin(ResourceManager.spiralLogo().getWidth()/2,ResourceManager.spiralLogo().getHeight()/2);
        return spiralLogo;
    }

    private AnimatedImage createSpiral2Logo(){
        AnimatedImage spiral2Logo = new AnimatedImage(Assets.spiral2Anim());
        //GameLogo.setPosition((stage.getViewport().getCamera().viewportWidth*0.5f)-(GameLogo.getWidth()*0.5f),
        //        (stage.getViewport().getCamera().viewportHeight*0.85f)-(GameLogo.getHeight()*0.5f));
        //spiralLogo.setOrigin(ResourceManager.spiralLogo().getWidth()/2,ResourceManager.spiralLogo().getHeight()/2);
        return spiral2Logo;
    }

    public ImageTextButton createButton(String text, ClickListener clickListener) {

        //Setup Button Style
        Texture upTexture = ResourceManager.menuBtnUp();
        Skin btnSkin = new Skin();
        btnSkin.add("up", upTexture);
        btnSkin.add("down", ResourceManager.menuBtnDown());
        btnSkin.add("over", upTexture);

        ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButton.ImageTextButtonStyle();
        buttonStyle.up = btnSkin.getDrawable("up");
        buttonStyle.down = btnSkin.getDrawable("down");
        buttonStyle.over = btnSkin.getDrawable("over");
        buttonStyle.font = new BitmapFont();
        buttonStyle.downFontColor = Color.WHITE;
        buttonStyle.fontColor = Color.RED;

        //Button with listener
        ImageTextButton newBtn = new ImageTextButton(text, buttonStyle);
        newBtn.addListener(clickListener);
        return newBtn;
    }
}
