package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Components.SpellComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Storage.Particles;
import com.game.ECS.Storage.SpellPattern;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;
import com.game.ECS.Tools.SpellDrawing;

import java.util.LinkedList;


/**
 * Created by Keirron on 13/05/2015.
 *
 * Used for drawing spells patterns and casting matching spells.
 *
 */
public class SpellCastingScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;


    private float scale;

    LinkedList<TextButton> buttons;

    //for spiral
    private float angle;
    private float angle2;
    private Texture spiralTexture;
    private Texture spiral2Texture;
    private SpriteBatch sb;

    private InputMultiplexer multiplexer = new InputMultiplexer();

    private SpellDrawing spellDrawing;


    private Array<SpellPattern> spells = new Array<SpellPattern>();

    private Texture castTexture;
    private ImageButton castSpellBtn;
    private Image castSpiral;
    private Image castSpiral2;

    Vector2[] points;

    float buffer = Gdx.graphics.getHeight()/4;

    private ImageButton cancelSpellBtn;
    private Texture cancelTexture;

    //Line drawing
    private float LINE_SIZE = 10f; // TODO change on resolution

    int startPoint = -1;
    int endPoint = -1;
    Vector2 currentPoint = null;

    float maxX;
    float maxY;
    float minX;
    float minY;
    private float barSize;
    private Label spellListLbl;
    private Label adviceLbl;

    //Circle effect
    private float pulse;
    private boolean pulseReversing = false;

    //Particles
    private ParticleEffectPool.PooledEffect effect;

    //The points to draw a detailed line
    LinkedList<LinkedList<Vector2>> drawnPoints;
    Vector2 currentDrawnPoint;
    final static float DRAWN_CUTOFF = 20;

    public SpellCastingScreen(Main game, final Stage stage, final PlayerInputComponent playerInput) {
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        //For spiral
        sb = new SpriteBatch();
        sb.setProjectionMatrix(stage.getViewport().getCamera().combined);

        spiralTexture = ResourceManager.castSpiral();
        spiral2Texture = ResourceManager.castSpiral2();

        pulse = buffer* 0.15f;

        buttons = new LinkedList<TextButton>();

        points = new Vector2[9];

        Vector2 start = new Vector2(Gdx.graphics.getWidth()/2, buffer*2);

        LINE_SIZE*=scale;

        points[0] = new Vector2(start).add(-buffer*0.7071f,buffer*0.7071f);
        points[1] = new Vector2(start).add(0,buffer);
        points[2] = new Vector2(start).add(buffer*0.7071f,buffer*0.7071f);
        points[3] = new Vector2(start).add(-buffer,0);
        points[4] = new Vector2(start);
        points[5] = new Vector2(start).add(buffer,0);
        points[6] = new Vector2(start).add(-buffer*0.7071f,-buffer*0.7071f);
        points[7] = new Vector2(start).add(0,-buffer);
        points[8] = new Vector2(start).add(buffer*0.7071f,-buffer*0.7071f);

        //Make frost spell
        SpellDrawing frostPattern = new SpellDrawing();
        frostPattern.addEdge(0,7);
        frostPattern.addEdge(7,2);
        frostPattern.addEdge(3,5);
        spells.add(new SpellPattern(SpellPattern.AimType.AIM, frostPattern,
                SpellComponent.Spell.FROST, "Frost Ball"));

        //Make gravity spell
        SpellDrawing gravityPattern = new SpellDrawing();
        gravityPattern.addEdge(0,2);
        gravityPattern.addEdge(3,5);
        gravityPattern.addEdge(6,8);
        spells.add(new SpellPattern(SpellPattern.AimType.ACCELEROMETER, gravityPattern,
                SpellComponent.Spell.GRAVITY_SHIFT, "Gravity Shift"));

        maxX = Gdx.graphics.getWidth()*0.75f;
        minX = Gdx.graphics.getWidth()*0.25f;
        maxY = Gdx.graphics.getHeight()*0.85f;
        minY = Gdx.graphics.getHeight()*0.15f;


        //Create actors
        this.cancelSpellBtn = createCancelSpellBtn();
        this.castSpellBtn = createCastSpellBtn();
        this.castSpiral = createCastSpiral();
        this.castSpiral2 = createCastSpiral2();

        spellDrawing = new SpellDrawing();
        //Set up drawing line input
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int screenX, int screenY, int pointer, int button) {
                setCurrentPoint(new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
                adviceLbl.remove();
                spellListLbl.remove();
                startPoint = FindClosestPoint(currentPoint);
                if(effect == null)
                    effect = Particles.spellSelectParticles();
                //effect.reset();
                effect.getEmitters().get(0).setContinuous(true);

                //For drawn points
                drawnPoints.add(new LinkedList<Vector2>());
                drawnPoints.get(drawnPoints.size()-1).add(currentPoint);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                endPoint = FindClosestPoint(currentPoint);
                if(startPoint!=endPoint) {
                    if (startPoint > -1 && endPoint > -1) {
                        spellDrawing.addEdge(startPoint, endPoint);
                        if (playerInput.playerInk.currentInk > 0)
                            playerInput.playerInk.currentInk -= 1;
                        else if (playerInput.playerHealth.currentHealth > 1)
                            playerInput.playerHealth.currentHealth -= 1;
                    }
                    spellCheck();
                }
                startPoint = -1;
                endPoint = -1;
                if(drawnPoints.size()!=0) {
                    drawnPoints.get(drawnPoints.size() - 1).add(currentPoint);
                }
                drawnPoints.clear();
                currentPoint = null;
                if (effect != null) {
                    effect.getEmitters().get(0).setContinuous(false);
                    effect.getEmitters().get(0).durationTimer = 1;
                }

                //For drawn points


                return true;
            }
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                setCurrentPoint(new Vector2(screenX,Gdx.graphics.getHeight()-screenY));

                if(drawnPoints.size()!=0) {
                    float dx = Math.abs(drawnPoints.get(drawnPoints.size() - 1)
                            .get(drawnPoints.get(drawnPoints.size() - 1).size() - 1).x - currentPoint.x);
                    float dy = Math.abs(drawnPoints.get(drawnPoints.size() - 1)
                            .get(drawnPoints.get(drawnPoints.size() - 1).size() - 1).y - currentPoint.y);
                    if (dx + dy > DRAWN_CUTOFF * scale)
                        drawnPoints.get(drawnPoints.size() - 1).add(currentPoint);
                }
                //endPoint = FindClosestPoint(clickCoordinates);
                return true;
            }

            private void setCurrentPoint(Vector2 point){
                currentPoint = point;
                if(currentPoint.x > maxX)
                    currentPoint.x = maxX;
                if(currentPoint.x < minX)
                    currentPoint.x = minX;
                if(currentPoint.y > maxY)
                    currentPoint.y = maxY;
                if(currentPoint.y < minY)
                    currentPoint.y = minY;
            }
        });

        //Scale some things
        this.barSize = 300*scale;

        this.spellListLbl = createSpellListLabel();
        this.adviceLbl = createAdviceLabel();

        if (game.currentMusic != ResourceManager.CastSpellMusic()){
            if (game.currentMusic != null)
                game.currentMusic.stop();
            game.currentMusic = ResourceManager.CastSpellMusic();
            game.currentMusic.play();
            game.currentMusic.setLooping(true);
            game.currentMusic.setVolume(1f);
        }

        //Set up drawn points TODO Probably needs to be an array
        drawnPoints = new LinkedList<LinkedList<Vector2>>();

    }

    @Override
    public void show() {

        playerInput.gameSpeed = 0.1f;
        this.stage.addActor(this.cancelSpellBtn);
        this.stage.addActor(this.castSpellBtn);
        //this.stage.addActor(this.castSpiral);
        //this.stage.addActor(this.castSpiral2);
        stage.addActor(spellListLbl);
        stage.addActor(adviceLbl);
        Gdx.input.setInputProcessor(multiplexer);
    }

    //Sees if the player casted a spell pattern
    private void spellCheck(){
        if(spellDrawing.getEdges().size() == 3){
            for(SpellPattern spell : spells){
                if(spellDrawing.Compare(spell.getSpellDrawing())){
                    if(spell.getAimType() == SpellPattern.AimType.AIM){
                        game.setScreen(new SpellAimingScreen(game, stage, playerInput, spell.getSpellType()));
                    }else if(spell.getAimType() == SpellPattern.AimType.ACCELEROMETER) {
                        playerInput.spellCast = spell.getSpellType();
                        game.setScreen(new AccelerometerScreen(game, stage, playerInput, 7));
                    }
                    spellDrawing.clearEdges();
                    return;
                }
            }
            game.setScreen(new GameScreen(game, stage, playerInput));
            spellDrawing.clearEdges();
        }

    }
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Override
    public void render(float delta) {

        //Draw animated spirals
        angle+=delta*25;
        angle%=360;
        while (angle < 0)
            angle+=360;
        angle2-=delta*40;
        angle2%=360;
        while (angle2 < 0)
            angle2+=360;
        sb.begin();
        sb.draw(new TextureRegion(spiralTexture), stage.getWidth() / 2 - spiralTexture.getWidth() / 2,
                stage.getHeight() / 2 - spiralTexture.getHeight() / 2,
                spiralTexture.getWidth() / 2, spiralTexture.getHeight() / 2,
                spiralTexture.getWidth(), spiralTexture.getHeight(), 2 * scale, 2 * scale, angle);
        sb.draw(new TextureRegion(spiral2Texture), stage.getWidth() / 2 - spiralTexture.getWidth() / 2,
                stage.getHeight() / 2 - spiralTexture.getHeight() / 2,
                spiralTexture.getWidth() / 2, spiralTexture.getHeight() / 2,
                spiralTexture.getWidth(), spiralTexture.getHeight(), 2.2f *scale, 2.2f *scale, angle2);
        sb.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setAutoShapeType(true);
        Color pointColor = new Color(Color.MAROON);
        pointColor.a = 0.8f;
        float pulseSpeed = 1*scale;
        if(pulseReversing) {
            pulse += delta * pulseSpeed;
            if(pulse > buffer* 0.15f)
                pulseReversing = false;
        }
        else {
            pulse -= delta * pulseSpeed;
            if(pulse < buffer* 0.10f)
                pulseReversing = true;
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(pointColor);
        shapeRenderer.circle(points[0].x,points[0].y, pulse);
        shapeRenderer.circle(points[1].x,points[1].y, pulse);
        shapeRenderer.circle(points[2].x,points[2].y, pulse);
        shapeRenderer.circle(points[3].x,points[3].y, pulse);
        shapeRenderer.circle(points[4].x,points[4].y, pulse);
        shapeRenderer.circle(points[5].x,points[5].y, pulse);
        shapeRenderer.circle(points[6].x,points[6].y, pulse);
        shapeRenderer.circle(points[7].x,points[7].y, pulse);
        shapeRenderer.circle(points[8].x,points[8].y, pulse);
        //shapeRenderer.end();

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (SpellDrawing.Edge edge : spellDrawing.getEdges()) {
            shapeRenderer.setColor(Color.PURPLE);
            shapeRenderer.circle(points[edge.p1].x, points[edge.p1].y, LINE_SIZE/2);
            shapeRenderer.circle(points[edge.p2].x, points[edge.p2].y, LINE_SIZE / 2);
            shapeRenderer.rectLine(points[edge.p1], points[edge.p2], LINE_SIZE);
            shapeRenderer.setColor(Color.MAGENTA);
            shapeRenderer.rectLine(points[edge.p1], points[edge.p2], 2 * scale);
        }

        /*
        shapeRenderer.setColor(Color.RED);
        if (startPoint >-1 && currentPoint != null) {
            shapeRenderer.circle(points[startPoint].x, points[startPoint].y, LINE_SIZE/2);
            shapeRenderer.circle(currentPoint.x, currentPoint.y, LINE_SIZE/2);
            shapeRenderer.rectLine(points[startPoint], currentPoint, LINE_SIZE);
            shapeRenderer.setColor(Color.PURPLE);
            shapeRenderer.rectLine(points[startPoint], currentPoint, 2 * scale);
        }
        */

        for (LinkedList<Vector2> stroke : drawnPoints) {
            shapeRenderer.setColor(Color.PURPLE);
            shapeRenderer.circle(stroke.getFirst().x, stroke.getFirst().y, LINE_SIZE / 2);
            shapeRenderer.circle(stroke.getLast().x, stroke.getLast().y, LINE_SIZE / 2);
            for(int i=0;i<stroke.size()-1;i++){
                if(stroke.size()!=1) {
                    shapeRenderer.setColor(Color.PURPLE);
                    shapeRenderer.rectLine(stroke.get(i), stroke.get(i+1), LINE_SIZE);
                    shapeRenderer.setColor(Color.MAGENTA);
                    shapeRenderer.rectLine(stroke.get(i), stroke.get(i+1), 2 * scale);
                }
            }
        }

        /*
        shapeRenderer.setColor(Color.RED);
        if (startPoint >-1 && currentPoint != null) {
            shapeRenderer.circle(points[startPoint].x, points[startPoint].y, LINE_SIZE/2);
            shapeRenderer.circle(currentPoint.x, currentPoint.y, LINE_SIZE/2);
            shapeRenderer.rectLine(points[startPoint], currentPoint, LINE_SIZE);
            shapeRenderer.setColor(Color.PURPLE);
            shapeRenderer.rectLine(points[startPoint], currentPoint, 2 * scale);
        }
        */
        shapeRenderer.end();

        //Draw bars
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float barOffset = 55*scale; //Offset from bottom
        Vector2 healthStart = new Vector2();
        Vector2 healthEnd = new Vector2();
        float barLength = 1;
        if(playerInput.playerInk.currentInk <= 0) {
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

        if(playerInput.playerInk.currentInk > 0) {
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

        shapeRenderer.end();

        //Draw particles
        if(effect != null) {
            sb.begin();
            if (currentPoint != null)
                effect.setPosition(currentPoint.x, currentPoint.y);
            effect.draw(sb, delta);
            if (effect.isComplete()) {
                effect.free();
            }
            sb.end();
        }


        Gdx.gl.glDisable(GL20.GL_BLEND);
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
        this.castSpellBtn.remove();
        this.castSpiral.remove();
        this.castSpiral2.remove();
        playerInput.gameSpeed = 1f;
        spellListLbl.remove();
        adviceLbl.remove();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        cancelTexture.dispose();
        castTexture.dispose();
    }


    private int FindClosestPoint(Vector2 p)
    {
        int closest = 0;
        float distance = 999999;

        for(int i = 0; i < 9; i++)
        {
            if(p !=null) {
                float d = Vector2.dst(p.x, p.y, points[i].x, points[i].y);
                if (d < distance) {
                    closest = i;
                    distance = d;
                }
            }
        }

        return closest;
    }

    private Image createCastSpiral(){
        Image castSpiralImg = new Image(new TextureRegion(ResourceManager.castSpiral()));
        castSpiralImg.setSize(ResourceManager.castSpiral().getWidth()*2f*scale,
                ResourceManager.castSpiral().getHeight()*2f*scale);
        castSpiralImg.setPosition((stage.getViewport().getCamera().viewportWidth * 0.5f) - (castSpiralImg.getWidth() * 0.5f),
                (stage.getViewport().getCamera().viewportHeight * 0.5f) - (castSpiralImg.getHeight() * 0.5f));
        //Update originf or rotation
        castSpiralImg.setOrigin(castSpiralImg.getWidth()/2,castSpiralImg.getHeight()/2);
        return castSpiralImg;
    }

    private Image createCastSpiral2(){
        Image castSpiralImg = new Image(new TextureRegion(ResourceManager.castSpiral2()));
        castSpiralImg.setSize(ResourceManager.castSpiral().getWidth()*2.1f*scale,
                ResourceManager.castSpiral().getHeight()*2.1f*scale);
        castSpiralImg.setPosition((stage.getViewport().getCamera().viewportWidth * 0.5f) - (castSpiralImg.getWidth() * 0.5f),
                (stage.getViewport().getCamera().viewportHeight * 0.5f) - (castSpiralImg.getHeight() * 0.5f));
        //Update originf or rotation
        castSpiralImg.setOrigin(castSpiralImg.getWidth()/2,castSpiralImg.getHeight()/2);
        return castSpiralImg;
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
                Gdx.input.vibrate(75);
                game.setScreen(new GameScreen(game, stage, playerInput));
            }
        });

        return cancelSpellBtn;
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
                Gdx.input.vibrate(75);
                game.setScreen(new SpellBookScreen(game, stage, playerInput, spells));
            }
        });

        return castSpellBtn;
    }

    private Label createSpellListLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("SpellList",textStyle);
        text.setAlignment(Align.center);
        text.setFontScale(4f*scale,4f*scale);
        text.setPosition(stage.getViewport().getCamera().viewportWidth - (120*scale + castSpellBtn.getWidth()),
                80*scale);
        return text;
    }

    private Label createAdviceLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("Touch and drag to draw spell...",textStyle);
        text.setAlignment(Align.center);
        text.setFontScale(4f*scale,4f*scale);
        text.setPosition(stage.getViewport().getCamera().viewportWidth* 0.4f,
                stage.getViewport().getCamera().viewportHeight* 0.9f);
        return text;
    }
}
