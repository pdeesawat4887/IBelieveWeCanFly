package com.mygdx.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Rocket {
    //private static final int LOWEST_OPENING = 100;
    private static final int FLUCTUATION = 400;
    public static final int BIRD_WIDTH = 230;
    private static final int BIRD_GAP = 200;
    private static final int BIRD_LOWEST_OPENING = 100;
    private Texture bird;
    private Vector2 posBird;
    private Rectangle boundsBird;

    private Animation birdAnimation;

    private Random rand;

    public Rocket(float x) {
        rand = new Random();

        bird = new Texture("materials/rocket-01.png");
        posBird = new Vector2(x, rand.nextInt(FLUCTUATION) + BIRD_GAP + BIRD_LOWEST_OPENING);
        boundsBird = new Rectangle(posBird.x + 20, posBird.y + 10, bird.getWidth() / 3 - 20, bird.getHeight() - 20);
        birdAnimation = new Animation(new TextureRegion(bird), 3, 0.5f);

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

    public void rePosition(float x) {
        posBird.set(x, rand.nextInt(FLUCTUATION));
        boundsBird.setPosition(x, posBird.y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsBird);
    }

    public void dispose() {
        bird.dispose();
    }
}
