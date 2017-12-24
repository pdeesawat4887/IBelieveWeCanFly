package com.mygdx.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Cuby {
    private static final int FLUCTUATION = 300;
    public static final int CUBY_WIDTH = 200;
    private Texture cuby;
    private Vector2 posCuby;
    private Rectangle boundsCuby;

    private Animation cubyAnimation;

    private Random rand;

    public Cuby(float x) {
        rand = new Random();

        cuby = new Texture("materials/cuby-01.png");
        posCuby = new Vector2(x, rand.nextInt(FLUCTUATION));
        boundsCuby = new Rectangle(posCuby.x + 30, posCuby.y, cuby.getWidth() / 8 - 30, cuby.getHeight() - 50);
        cubyAnimation = new Animation(new TextureRegion(cuby), 8, 0.5f);

    }

    public void update(float dt) {
        cubyAnimation.update(dt);
    }

    public TextureRegion getCuby() {
        return cubyAnimation.getFrame();
    }

    public Vector2 getPosCuby() {
        return posCuby;
    }

    public void rePosition(float x) {
        posCuby.set(x, rand.nextInt(FLUCTUATION));
        boundsCuby.setPosition(x, posCuby.y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsCuby);
    }

    public void dispose() {
        cuby.dispose();
    }
}
