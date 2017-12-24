package com.mygdx.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;


public class RockExtream {

    public static final int ROCKEX_WIDTH = 97;
    private Texture rockEx;
    private Vector2 posRockEx;
    private Rectangle boundsRockEx;

    private Random rand;

    public RockExtream(float x) {
        rand = new Random();
        rockEx = new Texture("materials/rocklava-01.png");
        posRockEx = new Vector2(rand.nextInt((int) x), 0);
        boundsRockEx = new Rectangle(posRockEx.x, posRockEx.y, rockEx.getWidth(), rockEx.getHeight());

    }

    public Texture getRockEx() {
        return rockEx;
    }

    public Vector2 getPosRockEx() {
        return posRockEx;
    }

    public void rePosition(float x) {
        posRockEx.set(x, 0);
        boundsRockEx.setPosition(x, posRockEx.y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsRockEx);
    }


    public void dispose() {
        rockEx.dispose();
    }
}
