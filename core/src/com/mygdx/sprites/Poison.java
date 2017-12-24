package com.mygdx.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Poison {

    private Texture poison;
    private Vector2 posPoison;
    private Random rand;
    private static final int FLUCTUATION = 500;

    private Rectangle boundPoison;

    public Poison(float x) {

        rand = new Random();
        poison = new Texture("materials/poison-01.png");
        posPoison = new Vector2(rand.nextInt((int) x), rand.nextInt(FLUCTUATION));
        boundPoison = new Rectangle(posPoison.x, posPoison.y, poison.getWidth(), poison.getHeight());
    }

    public void rePosition(float x) {
        posPoison.set(x, 100);
        boundPoison.setPosition(x, posPoison.y);
    }

    public Texture getPoison() {
        return poison;
    }

    public Vector2 getPosPoison() {
        return posPoison;
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundPoison);
    }

    public void dispose() {
        poison.dispose();
    }
}