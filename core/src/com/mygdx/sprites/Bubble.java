package com.mygdx.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class Bubble {

    private static final int FLUCTUATION = 480;
    public static final int BUBBLE_WIDTH = 112;

    private Animation coralAnimation, bubbleAnimation;

    private Texture bubble;
    private Rectangle boundsBubble;
    private Vector3 position;

    private Texture coral;
    private Vector2 posCoral;
    private Rectangle boundsCoral;

    private Random rand, rand2;

    public Bubble(float x) {
        rand = new Random();
        bubble = new Texture("materials/jellyfishAi-01.png");

        position = new Vector3(x, rand.nextInt(FLUCTUATION), 0);
        boundsBubble = new Rectangle(position.x+20, position.y, bubble.getWidth() / 3 - 40, bubble.getHeight() - 50);
        bubbleAnimation = new Animation(new TextureRegion(bubble), 3, 1.0f);

        rand2 = new Random();
        coral = new Texture("materials/coralA.png");
        posCoral = new Vector2(rand2.nextInt((int) x), 0);
        boundsCoral = new Rectangle(posCoral.x, posCoral.y, coral.getWidth() / 2, coral.getHeight() - 50);
        coralAnimation = new Animation(new TextureRegion(coral), 2, 1.0f);

    }

    public void update(float dt) {
        coralAnimation.update(dt);
        bubbleAnimation.update(dt);
    }

    public TextureRegion getBubble() {

        return bubbleAnimation.getFrame();
    }

    public Texture getBubbleTexture() {
        return bubble;
    }

    public Vector3 getPosBubble() {
        return position;
    }

    public TextureRegion getCoral() {
        return coralAnimation.getFrame();
    }

    public Vector2 getPosCoral() {
        return posCoral;
    }

    public void rePosition(float x) {
        position.set(x, rand.nextInt(FLUCTUATION), 0);
        boundsBubble.setPosition(x, position.y);

        posCoral.set(rand2.nextInt((int) x), 0);
        boundsCoral.setPosition(rand2.nextInt((int) x), posCoral.y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsBubble) || player.overlaps(boundsCoral);
        //return false;
    }

    public void dispose() {
        bubble.dispose();
        coral.dispose();
    }
}
