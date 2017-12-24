package com.mygdx.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Door {

    private Texture door;
    private Vector2 posDoor;
    public static final int DOOR_WIDTH = 32;

    private Rectangle boundDoor;

    public Door(float x) {

        door = new Texture("materials/door_proto-01.png");
        posDoor = new Vector2(x, 0);
        boundDoor = new Rectangle(posDoor.x + (door.getWidth() / 2), posDoor.y, door.getWidth() / 2, door.getHeight());
    }

    public void rePosition(float x) {

        posDoor.set(x, 100);
        boundDoor.setPosition(x, posDoor.y);
    }

    public Texture getDoor() {
        return door;
    }

    public Vector2 getPosDoor() {
        return posDoor;
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundDoor);
    }

    public void dispose() {
        door.dispose();
    }
}