package com.mygdx.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Bird {
    private static final int FLUCTUATION = 400;
    public static final int BIRD_WIDTH = 230;
    private static final int BIRD_GAP = 200;
    private static final int BIRD_LOWEST_OPENING = 100;
    private Texture bird;
    private Vector2 posBird;
    private Rectangle boundsBird;

    private Texture drop;
    private Vector2 posDrop;
    private Rectangle boundsDrop;

    private Animation birdAnimation;

    private Random rand, rand2;

    public Bird(float x) {
        rand = new Random();
        rand2 = new Random();

        bird = new Texture("materials/rocket-01.png");
        posBird = new Vector2(x, rand.nextInt(FLUCTUATION) + BIRD_GAP + BIRD_LOWEST_OPENING);
        boundsBird = new Rectangle(posBird.x+20, posBird.y+10, bird.getWidth() / 3 - 20, bird.getHeight() - 20);
        birdAnimation = new Animation(new TextureRegion(bird), 3, 0.5f);


        drop = new Texture("materials/monster-01.png");
        posDrop = new Vector2(rand2.nextInt((int)x), posBird.y - BIRD_GAP - drop.getHeight());
        boundsDrop = new Rectangle(posDrop.x+10, posDrop.y+10, drop.getWidth()-20, drop.getHeight()-50);

    }

    public void update(float dt) {
        birdAnimation.update(dt);
    }

    public TextureRegion getBird() {
        return birdAnimation.getFrame();
    }

    public Vector2 getPosBird() {
        return posBird;
    }

    public Texture getDrop() {
        return drop;
    }

    public Vector2 getPosDrop() {
        return posDrop;
    }

    public void rePosition(float x) {
        posBird.set(x, rand.nextInt(FLUCTUATION));
        boundsBird.setPosition(x, posBird.y);

        posDrop.set(x, rand.nextInt(FLUCTUATION));
        boundsDrop.setPosition(rand.nextInt((int) x), posDrop.y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsBird);
    }

    public boolean collidesLowest(Rectangle player) {
        return player.overlaps(boundsDrop);
    }

    public void dispose() {
        bird.dispose();
        drop.dispose();
    }
}
