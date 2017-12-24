package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.state.GameStateManager;
import com.mygdx.state.MenuState;
import com.mygdx.state.StateButterflyOcean;

public class MyGame extends ApplicationAdapter {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final String TITLE = "I BELIEVE WE CAN FLY !";

    private GameStateManager gsm;
    private SpriteBatch batch;

    private static int heart = 9;
    private static float time = 0;
    private static int final_time = 0;
    private static int HIGH_SCORE = 0;


    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();

        //Set New Game
        gsm.push(new MenuState(gsm));
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public static int getHeart() {
        return heart;
    }

    public static void setHeart(int heart) {
        MyGame.heart = heart;
    }

    public static float getTime() {
        return time;
    }

    public static void setTime(float time) {
        MyGame.time = time;
    }

    public static int getFinal_time() {
        return final_time;
    }

    public static void setFinal_time(int final_time) {
        MyGame.final_time = final_time;
    }

    public static int getHighScore() {
        return HIGH_SCORE;
    }

    public static void setHighScore(int highScore) {
        HIGH_SCORE = highScore;
    }
}
