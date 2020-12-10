package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Components.SpellComponent;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;

import java.util.LinkedList;


/**
 * Created by Keirron on 13/05/2015.
 *
 * Used for debugging spells. Lists spells to cast.
 *
 */
public class SpellSelectScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    Table table;

    LinkedList<TextButton> buttons;

    public SpellSelectScreen(Main game, Stage stage, PlayerInputComponent playerInput) {
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        buttons = new LinkedList<TextButton>();
        this.playerInput.gameSpeed = 0.1f;

        table = new Table().align(Align.center);
        table.setPosition(stage.getCamera().viewportWidth*0.5f, stage.getCamera().viewportHeight*0.5f);
    }

        @Override
        public void show() {
            Skin skin = new Skin(Gdx.files.internal("UI/uiskin.json"));

            TextButton frostBtn = createButton(skin, Gdx.graphics.getWidth()/2,
                    Gdx.graphics.getHeight()/2, "Frost Ball (Aimed)");
            frostBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new SpellAimingScreen(game, stage, playerInput, SpellComponent.Spell.FROST));
                }
            });
            table.add(frostBtn).size(200*scale, 75*scale);
            table.row();
            table.add().size(200 * scale, 75 * scale);

            TextButton gravBtn = createButton(skin, Gdx.graphics.getWidth()/2,
                    Gdx.graphics.getHeight()/2 - 50*scale, "Gravity Shift");
            gravBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playerInput.spellCast = SpellComponent.Spell.GRAVITY_SHIFT;
                    game.setScreen(new AccelerometerScreen(game, stage, playerInput, 7));
                }
            });
            table.row();
            table.add(gravBtn).size(200*scale, 75*scale);

            stage.addActor(table);
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
        table.remove();
        for(TextButton btn : buttons){
            btn.remove();
        }
    }

    @Override
    public void dispose() {

    }


    private TextButton createButton(Skin skin, float x, float y, String text){
        TextButton btn = new TextButton(text, skin);
        btn.getLabel().setFontScale(1*scale, 1*scale);
        //btn.setPosition(x-btn.getWidth()/2, y-btn.getHeight()/2);
        return btn;
    }
}
