package com.mygdx.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Blood {

    private Texture blood;
    private Vector2 posBlood;
    private Random rand;
    private static final int FLUCTUATION = 500;

    private Rectangle boundBlood;

    public Blood(float x) {

        rand = new Random();
        blood = new Texture("materials/blood-01.png");
        posBlood = new Vector2(rand.nextInt((int) x), rand.nextInt(FLUCTUATION));
        boundBlood = new Rectangle(posBlood.x, posBlood.y, blood.getWidth(), blood.getHeight());
    }

    public void rePosition(float x) {
        posBlood.set(x, 100);
        boundBlood.setPosition(x, posBlood.y);
    }

    public Texture getBlood() {
        return blood;
    }

    public Vector2 getPosBlood() {
        return posBlood;
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundBlood);
    }

    public void dispose() {
        blood.dispose();
    }
}