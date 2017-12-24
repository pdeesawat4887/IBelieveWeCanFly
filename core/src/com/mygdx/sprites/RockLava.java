package com.mygdx.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class RockLava {

    private static final int FLUCTUATION = 400;
    public static final int ROCK_WIDTH = 97;
    private static final int ROCK_GAP = 200;
    private static final int ROCK_LOWEST_OPENING = 100;

    private Texture worm;
    private Vector2 posWorm;
    private Rectangle boundsWorm;
    private Animation wormAnimation;

    private Texture rock;
    private Vector2 posRock;
    private Rectangle boundsRock;

    private Random rand;

    public RockLava(float x) {
        rand = new Random();
        rock = new Texture("materials/rocklava-01.png");
        posRock = new Vector2(x, rand.nextInt(FLUCTUATION) + ROCK_LOWEST_OPENING + ROCK_GAP);
        boundsRock = new Rectangle(posRock.x, posRock.y, rock.getWidth(), rock.getHeight());

        worm = new Texture("materials/worm-01.png");
        posWorm = new Vector2(x, posRock.y - ROCK_GAP - worm.getHeight());
        boundsWorm = new Rectangle(posWorm.x, posWorm.y, worm.getWidth() / 4-20, worm.getHeight()-50);
        wormAnimation = new Animation(new TextureRegion(worm), 4, 0.8f);

    }

    public Texture getRock() {
        return rock;
    }

    public void update(float dt) {
        wormAnimation.update(dt);
    }

    public TextureRegion getAnimationWorm() {
        return wormAnimation.getFrame();
    }

    public Vector2 getPosWorm() {
        return posWorm;
    }

    public Vector2 getPosRock() {
        return posRock;
    }

    public void rePosition(float x) {
        posRock.set(x, rand.nextInt(FLUCTUATION));
        boundsRock.setPosition(x, posRock.y);

        posWorm.set(x, posRock.y - ROCK_GAP - worm.getHeight());
        boundsWorm.setPosition(x, posWorm.y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsRock);
    }

    public boolean collidesAnimation(Rectangle player) {
        return player.overlaps(boundsWorm);
    }


    public void dispose() {
        rock.dispose();
        worm.dispose();
    }
}
