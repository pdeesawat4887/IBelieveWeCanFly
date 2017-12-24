package com.mygdx.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Coins {

    public static final int COIN_WIDTH = 132;
    public static final int MAX = 500;
    public static final int MIN = 200;

    private Texture coin100;
    private Texture coin500;
    private Vector2 posCoin100, posCoin500;

    private Random rand, rand2;
    private Rectangle boundCoin100, boundCoin500;

    public Coins(float x) {

        coin100 = new Texture("materials/coin100-01.png");
        coin500 = new Texture("materials/coin500-01.png");
        rand = new Random();
        rand2 = new Random();

        int random = rand.nextInt((MAX - MIN) + 1) + MIN;
        int random2 = rand2.nextInt((MAX - MIN) + 1) + MIN;

        posCoin100 = new Vector2(x, random);
        posCoin500 = new Vector2(x, random2);

        boundCoin100 = new Rectangle(posCoin100.x, posCoin100.y, coin100.getWidth(), coin100.getHeight());
        boundCoin500 = new Rectangle(posCoin500.x, posCoin500.y, coin500.getWidth(), coin500.getHeight());

    }

    public Texture getCoin100() {
        return coin100;
    }

    public Vector2 getPosCoin100() {
        return posCoin100;
    }

    public Texture getCoin500() {
        return coin500;
    }

    public Vector2 getPosCoin500() {
        return posCoin500;
    }

    public void rePosition100(float x) {
        int temp = rand.nextInt((MAX - MIN) + 1) + MIN;

        posCoin100.set(x, temp);
        boundCoin100.setPosition(x, posCoin100.y);
    }

    public void rePosition500(float x) {
        int temp2 = rand2.nextInt((MAX - MIN) + 1) + MIN;
        posCoin500.set(x, temp2);
        boundCoin500.setPosition(x, posCoin500.y);
    }

    public boolean collides100(Rectangle player) {
        return player.overlaps(boundCoin100);
    }

    public boolean collides500(Rectangle player) {
        return player.overlaps(boundCoin500);
    }

    public void dispose100() {
        coin100.dispose();
    }

    public void dispose500() {
        coin500.dispose();
    }
}
