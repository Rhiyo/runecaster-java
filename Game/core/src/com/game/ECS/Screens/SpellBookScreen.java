package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Storage.SpellPattern;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.ECS.Tools.SpellDrawing;
import com.game.Main;

/**
 * Created by Keirron on 16/06/2015.
 *
 * Used for browsing spell patterns.
 *
 */
public class SpellBookScreen implements Screen {

    private ImageButton cancelSpellBtn;
    private Texture cancelTexture;

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    private Array<SpellPattern> spells;

    private TextButton leftBtn;
    private TextButton rightBtn;
    private Table table = new Table();

    private int currentSpell;
    private Label spellNameLabel;

    Vector2[] points;
    float buffer = Gdx.graphics.getHeight()/4;

    private SpellDrawing spellDrawing;

    private float LINE_SIZE = 10f;

    public SpellBookScreen(Main game, final Stage stage, PlayerInputComponent playerInput,
                           final Array<SpellPattern> spells)
    {
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;
        this.spells = spells;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();
        points = new Vector2[9];

        LINE_SIZE*=scale;

        Vector2 start = new Vector2(Gdx.graphics.getWidth()/2, buffer*2);
        points[0] = new Vector2(start).add(-buffer*0.7071f,buffer*0.7071f);
        points[1] = new Vector2(start).add(0,buffer);
        points[2] = new Vector2(start).add(buffer*0.7071f,buffer*0.7071f);
        points[3] = new Vector2(start).add(-buffer,0);
        points[4] = new Vector2(start);
        points[5] = new Vector2(start).add(buffer,0);
        points[6] = new Vector2(start).add(-buffer*0.7071f,-buffer*0.7071f);
        points[7] = new Vector2(start).add(0,-buffer);
        points[8] = new Vector2(start).add(buffer*0.7071f,-buffer*0.7071f);

        Skin skin = new Skin(Gdx.files.internal("UI/uiskin.json"));

        //Left and right buttons
        table.setPosition(stage.getCamera().viewportWidth*0.5f, stage.getCamera().viewportHeight*0.5f);
        leftBtn = createButton(skin, "<");
        leftBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                if (currentSpell - 1 < 0) {
                    currentSpell = spells.size - 1;
                }else{
                    currentSpell-=1;
                }
                setCurrentSpell(spells.get(currentSpell));
            }
        });
        rightBtn = createButton(skin, ">");
        rightBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                if (currentSpell+ 1 >= spells.size) {
                    currentSpell = 0;
                }else{
                    currentSpell+=1;
                }
                setCurrentSpell(spells.get(currentSpell));
            }
        });
        table.add(leftBtn).size(stage.getCamera().viewportWidth*0.15f,
                stage.getCamera().viewportHeight*0.2f);
        table.add().size(stage.getCamera().viewportWidth*0.50f,
                stage.getCamera().viewportHeight*0.2f);
        table.add(rightBtn).size(stage.getCamera().viewportWidth*0.15f,
                stage.getCamera().viewportHeight*0.2f);
        spellNameLabel = createSpellNameLabel();
        currentSpell = 0;
        setCurrentSpell(spells.get(currentSpell));
        this.cancelSpellBtn = createCancelSpellBtn();
    }

    private void setCurrentSpell(SpellPattern spell){
        spellNameLabel.setText(spell.getName());
        spellDrawing = spell.getSpellDrawing();
    }

    @Override
    public void show() {
        playerInput.gameSpeed = 0.1f;
        this.stage.addActor(this.cancelSpellBtn);
        stage.addActor(table);
        stage.addActor(spellNameLabel);
    }

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Override
    public void render(float delta) {
        shapeRenderer.setAutoShapeType(true);
        ;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(Color.RED));
        shapeRenderer.circle(points[0].x,points[0].y, buffer* 0.2f);
        shapeRenderer.circle(points[1].x,points[1].y, buffer* 0.2f);
        shapeRenderer.circle(points[2].x,points[2].y, buffer* 0.2f);
        shapeRenderer.circle(points[3].x,points[3].y, buffer* 0.2f);
        shapeRenderer.circle(points[4].x,points[4].y, buffer* 0.2f);
        shapeRenderer.circle(points[5].x,points[5].y, buffer* 0.2f);
        shapeRenderer.circle(points[6].x,points[6].y, buffer* 0.2f);
        shapeRenderer.circle(points[7].x,points[7].y, buffer* 0.2f);
        shapeRenderer.circle(points[8].x,points[8].y, buffer* 0.2f);
        //shapeRenderer.end();

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(255, 255, 255, 1));
        for (SpellDrawing.Edge edge : spellDrawing.getEdges()) {
            shapeRenderer.rectLine(points[edge.p1], points[edge.p2], LINE_SIZE);
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
        cancelSpellBtn.remove();
        table.remove();
        leftBtn.remove();
        rightBtn.remove();
        spellNameLabel.remove();
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
                Gdx.input.vibrate(75);
                game.setScreen(new SpellCastingScreen(game, stage, playerInput));
            }
        });

        return cancelSpellBtn;
    }

    private Label createSpellNameLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("Spell",textStyle);
        text.setAlignment(Align.center);
        text.setFontScale(4f*scale,4f*scale);
        text.setPosition(stage.getViewport().getCamera().viewportWidth * 0.5f,
                stage.getViewport().getCamera().viewportHeight * 0.1f);
        return text;
    }

    private TextButton createButton(Skin skin, String text){
        TextButton btn = new TextButton(text, skin);
        btn.getLabel().setFontScale(3*scale, 3*scale);
        //btn.setPosition(x-btn.getWidth()/2, y-btn.getHeight()/2);
        return btn;
    }
}
