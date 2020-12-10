package com.game.ECS.Managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 * Created by Sean on 6/05/2015.
 */
public class ResourceManager {
    private static AssetManager manager;


    public static void load()
    {
        manager = new AssetManager();

        /*
            Textures
        */

        //Start Screen
        manager.load("Logo/GameLogo.png", Texture.class);
        manager.load("Logo/LogoSpiralSheet.png", Texture.class);
        manager.load("Logo/LogoSpiral2Sheet.png", Texture.class);
        manager.load("Logo/LogoBook.png", Texture.class);
        manager.load("Logo/LogoSpiralPoints.png", Texture.class);
        manager.load("UI/TapToStart.png", Texture.class);
        manager.load("UI/NewGame.png", Texture.class);
        manager.load("UI/Exit.png", Texture.class);
        manager.load("UI/Highscores.png", Texture.class);
        manager.load("UI/MenuBtnDown.png", Texture.class);
        manager.load("UI/MenuBtnUp.png", Texture.class);

        //Music
        manager.load("Music/MenuMusic.mp3", Music.class);
        manager.load("Music/GameMusic.ogg", Music.class);
        manager.load("Music/GameOverMusic.ogg", Music.class);
        manager.load("Music/SpellCastingMusic.mp3", Music.class);

        //Sound effects
        manager.load("soundeffects/druid/Damage1.wav", Sound.class);
        manager.load("soundeffects/druid/Damage2.wav", Sound.class);
        manager.load("soundeffects/druid/Damage3.wav", Sound.class);
        manager.load("soundeffects/druid/Damage4.wav", Sound.class);
        manager.load("soundeffects/druid/Death1.wav", Sound.class);
        manager.load("soundeffects/druid/Death2.wav", Sound.class);
        manager.load("soundeffects/druid/Walk.wav", Sound.class);

        manager.load("soundeffects/wolf/Damage1.wav", Sound.class);
        manager.load("soundeffects/wolf/Death1.wav", Sound.class);
        manager.load("soundeffects/wolf/Death2.wav", Sound.class);
        manager.load("soundeffects/wolf/Death3.wav", Sound.class);
        manager.load("soundeffects/wolf/Lunge1.wav", Sound.class);
        manager.load("soundeffects/wolf/Lunge2.wav", Sound.class);
        manager.load("soundeffects/wolf/Lunge3.wav", Sound.class);

        manager.load("soundeffects/PickUp.wav", Sound.class);
        manager.load("soundeffects/DrinkPotion.wav", Sound.class);

        //UI
        manager.load("UI/castButton.png", Texture.class);
        manager.load("UI/castButtonShadow.png", Texture.class);
        manager.load("UI/cancelButton.png", Texture.class);

        manager.load("UI/touchpad/touchBackground.png", Texture.class);
        manager.load("UI/touchpad/touchKnob.png", Texture.class);

        manager.load("UI/CastSpiral.png", Texture.class);
        manager.load("UI/CastSpiral2.png", Texture.class);

        //Consumable
        manager.load("consumable/HealthPot.png", Texture.class);
        manager.load("consumable/InkPot.png", Texture.class);
        manager.load("consumable/ConsumeSheet.png", Texture.class);
        manager.load("consumable/LifePotion.png", Texture.class);

        //Character Sheets
        manager.load("character/DruidSheet.png", Texture.class);
        manager.load("character/WolfSheet.png", Texture.class);
        manager.load("character/FaeSheet.png", Texture.class);

        //Projectiles
        manager.load("projectiles/iceball.png", Texture.class);

        //Missing Texture
        manager.load("Blank.png", Texture.class);

        /*
            Particle Effects
        */
        ParticleEffectLoader.ParticleEffectParameter param = new ParticleEffectLoader.ParticleEffectParameter();
        //param.imagesDir = new FileHandle("particles/");
        manager.load("particles/ice.p", ParticleEffect.class, param);
        manager.load("particles/iceExplosion.p", ParticleEffect.class, param);
    }

    public static boolean update(){
        return manager.update();
    }

    public static float getProgress(){
        return manager.getProgress();
    }

    public static boolean isLoaded()
    {
        if(manager != null) {
            if (manager.getProgress() >= 1)
                return true;
        }
        return false;
    }

    public static void dispose()
    {
        manager.dispose();
        manager = null;
    }

    public static Texture healthPot() {
        return manager.get("consumable/HealthPot.png", Texture.class);
    }

    public static Texture inkPot() {
        return manager.get("consumable/InkPot.png", Texture.class);
    }

    public static Texture sheetDruid() {
        return manager.get("character/DruidSheet.png", Texture.class);
    }

    public static Texture sheetWolf() {
        return manager.get("character/WolfSheet.png", Texture.class);
    }
    public static Texture sheetFae() {
        return manager.get("character/FaeSheet.png", Texture.class);
    }

    public static Texture uiCastSpellBtn() {
        return manager.get("UI/castButton.png", Texture.class);
    }

    public static Texture uiCastSpellShadow() {
        return manager.get("UI/castButtonShadow.png", Texture.class);
    }

    public static Texture uiCancelButton() {
        return manager.get("UI/cancelButton.png", Texture.class);
    }

    public static Texture uiKnobBG(){
        return manager.get("UI/touchpad/touchBackground.png", Texture.class);
    }

    public static Texture uiKnob(){
        return manager.get("UI/touchpad/touchKnob.png", Texture.class);
    }


    public static Texture projIce() {
        return manager.get("projectiles/iceball.png", Texture.class);
    }

    //UI
    public static Texture gameLogo() {
        return manager.get("Logo/GameLogo.png", Texture.class);
    }
    public static Texture spiralLogoSheet() {
        return manager.get("Logo/LogoSpiralSheet.png", Texture.class);
    }
    public static Texture spiralLogo2Sheet() {
        return manager.get("Logo/LogoSpiral2Sheet.png", Texture.class);
    }

    public static Texture spiralLogoPoints() {
        return manager.get("Logo/LogoSpiralPoints.png", Texture.class);
    }

    public static Texture bookLogo() {
        return manager.get("Logo/LogoBook.png", Texture.class);
    }

    public static Texture tapToStart() {
        return manager.get("UI/TapToStart.png", Texture.class);
    }

    public static Texture exitMenuButton() {
        return manager.get("UI/Exit.png", Texture.class);
    }

    public static Texture newGameMenuButton() {
        return manager.get("UI/NewGame.png", Texture.class);
    }

    public static Texture highscoresButton() {
        return manager.get("UI/Highscores.png", Texture.class);
    }

    public static Texture menuBtnUp() {
        return manager.get("UI/MenuBtnUp.png", Texture.class);
    }

    public static Texture menuBtnDown() {
        return manager.get("UI/MenuBtnDown.png", Texture.class);
    }

    public static Texture castSpiral() {
        return manager.get("UI/CastSpiral.png", Texture.class);
    }

    public static Texture castSpiral2() {
        return manager.get("UI/CastSpiral2.png", Texture.class);
    }

    //Items

    public static Texture consumeSheet(){
        return manager.get("consumable/ConsumeSheet.png", Texture.class);
    }

    public static Texture sheetLifePot(){
        return manager.get("consumable/LifePotion.png", Texture.class);
    }

    //Music

    public static Music menuMusic(){
        return manager.get("Music/MenuMusic.mp3", Music.class);
    }
    public static Music gameOverMusic(){
        return manager.get("Music/GameOverMusic.ogg", Music.class);
    }
    public static Music gameMusic(){
        return manager.get("Music/GameMusic.ogg", Music.class);
    }
    public static Music CastSpellMusic(){
        return manager.get("Music/SpellCastingMusic.mp3", Music.class);
    }
    //Sound effects
    public static Sound soundDruidDmg1(){
        return manager.get("soundeffects/druid/Damage1.wav", Sound.class);
    }
    public static Sound soundDruidDmg2(){
        return manager.get("soundeffects/druid/Damage2.wav", Sound.class);
    }
    public static Sound soundDruidDmg3(){
        return manager.get("soundeffects/druid/Damage3.wav", Sound.class);
    }
    public static Sound soundDruidDmg4(){
        return manager.get("soundeffects/druid/Damage4.wav", Sound.class);
    }

    public static Sound soundDruidWalk(){
        return manager.get("soundeffects/druid/Walk.wav", Sound.class);
    }

    public static Sound soundDruidDeath1(){
        return manager.get("soundeffects/druid/Death1.wav", Sound.class);
    }
    public static Sound soundDruidDeath2(){
        return manager.get("soundeffects/druid/Death2.wav", Sound.class);
    }

    public static Sound soundWolfDmg1(){
        return manager.get("soundeffects/wolf/Damage1.wav", Sound.class);
    }
    public static Sound soundWolfLunge1(){
        return manager.get("soundeffects/wolf/Lunge1.wav", Sound.class);
    }
    public static Sound soundWolfLunge2(){
        return manager.get("soundeffects/wolf/Lunge2.wav", Sound.class);
    }
    public static Sound soundWolfLunge3(){
        return manager.get("soundeffects/wolf/Lunge3.wav", Sound.class);
    }

    public static Sound soundWolfDeath1(){
        return manager.get("soundeffects/wolf/Death1.wav", Sound.class);
    }
    public static Sound soundWolfDeath2(){
        return manager.get("soundeffects/wolf/Death2.wav", Sound.class);
    }
    public static Sound soundWolfDeath3(){
        return manager.get("soundeffects/wolf/Death3.wav", Sound.class);
    }

    public static Sound soundPickUp(){
        return manager.get("soundeffects/PickUp.wav", Sound.class);
    }

    public static Sound soundDrinkPotion(){
        return manager.get("soundeffects/DrinkPotion.wav", Sound.class);
    }

    public static Texture blank(){
        return manager.get("Blank.png", Texture.class);
    }

}