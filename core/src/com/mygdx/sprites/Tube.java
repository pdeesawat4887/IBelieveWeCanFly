package com.mygdx.sprites;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.state.PlayState;

import java.util.Random;

public class Tube {

    private static final int FLUCTUATION = 200;
    private static final int TUBE_GAP = 280;
    private static final int LOWEST_OPENING = 100;

    public static final int TUBE_WIDTH = 208;

    private Texture cloud, tree;
    private Vector2 posCloud, posTree;

    private Random rand;

    private Rectangle boundsTop, bountsBot;

    public Tube(float x) {
        cloud = new Texture("materials/clouds_d-01.png");
        tree = new Texture("materials/f1-01.png");
        rand = new Random();

        posCloud = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        try {
            posTree = new Vector2(rand.nextInt((int) x), rand.nextInt(100));
        } catch (Exception e) {
            posTree = new Vector2(x, rand.nextInt(100));
        }
            boundsTop = new Rectangle(posCloud.x + 20, posCloud.y, cloud.getWidth() / 2, cloud.getHeight() / 2);
            bountsBot = new Rectangle(posTree.x, posTree.y, tree.getWidth(), tree.getHeight() - 40);

        }

    public Texture getCloud() {
        return cloud;
    }

    public Texture getTree() {
        return tree;
    }

    public Vector2 getPosCloud() {
        return posCloud;
    }

    public Vector2 getPosTree() {
        return posTree;
    }

    public void rePosition(float x) {

        int temp = rand.nextInt((int) x);

        posCloud.set(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posTree.set(temp, rand.nextInt(100));

        boundsTop.setPosition(x, posCloud.y);
        bountsBot.setPosition(temp, posTree.y);
    }

    public boolean collides(Rectangle player) {

        return player.overlaps(boundsTop) || player.overlaps(bountsBot);
    }

    public void dispose() {
        cloud.dispose();
        tree.dispose();

    }
}
