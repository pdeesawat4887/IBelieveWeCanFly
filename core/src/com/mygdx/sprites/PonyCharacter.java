package com.mygdx.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.state.PlayState;

public class PonyCharacter {

    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;

    private Animation birdAnimation;
    public Texture texture;

    public boolean colliding;

    private static int GRAVITY = -15;
    private static int MOVEMENT = 300;
    private static final int BACK_PONY = 200;

    public PonyCharacter(int x, int y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);

        texture = new Texture("materials/char2-01.png");
        birdAnimation = new Animation(new TextureRegion(texture), 8, 0.5f);

        bounds = new Rectangle(position.x, position.y, texture.getWidth() / 8 - 20, texture.getHeight() - 20);

        colliding = false;
    }

    public void update(float dt) {

        birdAnimation.update(dt);
        velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);

        if (!colliding)
            position.add(MOVEMENT * dt, velocity.y, 0);

        if (position.y < 0)
            position.y = 0;

        velocity.scl(1 / dt);
        updateBounds(BACK_PONY);

    }

    public void updateBounds(int back) {
        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getBird() {
        return birdAnimation.getFrame();
    }

    public void jump() {
        velocity.y = 250;
    }

    public void down() {
        velocity.y = -300;
    }

    public void up() {
        velocity.y = 500;
    }

    public void oceanJump() {
        velocity.y = 180;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
    }

    public static int getMOVEMENT() {
        return MOVEMENT;
    }

    public static void setMOVEMENT(int MOVEMENT) {
        PonyCharacter.MOVEMENT = MOVEMENT;
    }

    public static void setGravity(int gravity) {
        PonyCharacter.GRAVITY = gravity;
    }
}
