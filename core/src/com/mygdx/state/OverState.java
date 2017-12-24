package com.mygdx.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGame;

public class OverState extends State {

    private static final String GAVEOVER = "GAME OVER";
    private Texture background;
    private BitmapFont font;

    public OverState(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);

        background = new Texture("Background/menu.jpg");

        font = new BitmapFont();

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
            gsm.set(new MenuState(gsm));
            dispose();
        }

    }

    @Override
    public void render(SpriteBatch sb) {

        String temp = Integer.toString(MyGame.getFinal_time());
        String temp2 = Integer.toString(MyGame.getHighScore());

        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        sb.draw(background, cam.position.x - (cam.viewportWidth / 2), 0, MyGame.WIDTH, MyGame.HEIGHT);
        font.setColor(Color.RED);
        font.getData().setScale(5f);
        font.draw(sb, GAVEOVER, MyGame.WIDTH / 2 - 210, MyGame.HEIGHT - 100);
        font.getData().setScale(3f);
        font.draw(sb, "HIGH SCORE", MyGame.WIDTH / 2 - 145, MyGame.HEIGHT - 200);
        font.draw(sb, temp2, MyGame.WIDTH / 2 - 30, MyGame.HEIGHT - 280);
        font.draw(sb, "YOUR SCORE", MyGame.WIDTH / 2 - 150, MyGame.HEIGHT - 360);
        font.draw(sb, temp, MyGame.WIDTH / 2 - 30, MyGame.HEIGHT - 420);
        font.getData().setScale(2f);
        font.draw(sb, "TAP TO MENU", MyGame.WIDTH / 2 - 80, MyGame.HEIGHT / 8);

        sb.end();

    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
    }
}
