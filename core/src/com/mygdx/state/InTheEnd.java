package com.mygdx.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGame;

public class InTheEnd extends State {
    private Texture announcement;
    private Texture background;

    protected InTheEnd(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        background = new Texture("Background/bg_stage_5.png");
        announcement = new Texture("Background/laststand-01.png");
    }

    @Override
    protected void handleInput() {

        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
            gsm.set(new OverState(gsm));
        }
    }

    @Override
    public void update(float dt) {

        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {


        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, cam.position.x - (cam.viewportWidth / 2), 0, MyGame.WIDTH, MyGame.HEIGHT);
        sb.draw(announcement, cam.position.x - announcement.getWidth() / 2, cam.position.y - announcement.getHeight() / 2);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        announcement.dispose();

    }
}
