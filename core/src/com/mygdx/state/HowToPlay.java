package com.mygdx.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGame;

public class HowToPlay extends State {
    private Texture announcement;
    private Texture background;

    protected HowToPlay(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        announcement = new Texture("Background/howto-01.png");
        background = new Texture("Background/bg_how_to.jpg");
    }

    @Override
    protected void handleInput() {

        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
            gsm.set(new PlayState(gsm));
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
        sb.draw(announcement, cam.position.x - cam.viewportWidth / 2, 0, MyGame.WIDTH, MyGame.HEIGHT);
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
