package com.mygdx.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.MyGame;

import static com.badlogic.gdx.Gdx.input;

public class MenuState extends State {

    private Texture background;
    private Texture earth;
    private Texture snow;

    private Texture titleImage;

    private Texture playBtnActive;
    private Texture playBtnInactive;

    private Texture howToActive;
    private Texture howTOInactive;

    private Stage stage;

    //private Texture text;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        background = new Texture("Background/bg_menu.png");
        earth = new Texture("Background/bg_menu_earth-01.png");
        snow = new Texture("Background/BDF-01.png");

        playBtnInactive = new Texture("button/playActive.png");
        playBtnActive = new Texture("button/playInactive.png");

        howTOInactive = new Texture("button/howtoActive.png");
        howToActive = new Texture("button/howtoInactive.png");
        titleImage = new Texture("materials/logo-01.png");

        stage = new Stage();

        MyGame.setHeart(9);
        MyGame.setTime(0);
        MyGame.setFinal_time(0);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {

        stage.act(dt);
        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        sb.draw(background, cam.position.x - cam.viewportWidth / 2, 0, MyGame.WIDTH, MyGame.HEIGHT);
        sb.draw(earth, cam.position.x - earth.getWidth() / 2, cam.position.y - earth.getHeight() / 2);
        sb.draw(titleImage, cam.position.x - titleImage.getWidth() / 2, cam.position.y - titleImage.getHeight() / 2);
        sb.draw(snow, cam.position.x - cam.viewportWidth / 2, 0, MyGame.WIDTH, MyGame.HEIGHT);

        float temp = cam.position.x - cam.viewportWidth / 2 + howTOInactive.getWidth() / 4;
        if (input.getX() > temp && input.getX() < temp + howTOInactive.getWidth() / 2 && input.getY() > cam.position.y - howTOInactive.getHeight() / 2 && input.getY() < cam.position.y) {
            sb.draw(howTOInactive, cam.position.x - cam.viewportWidth / 2 + howTOInactive.getWidth() / 4, cam.position.y, howTOInactive.getWidth() / 2, howTOInactive.getHeight() / 2);
            if (input.isTouched()) {
                gsm.set(new HowToPlay(gsm));
            }
        } else
            sb.draw(howToActive, cam.position.x - cam.viewportWidth / 2 + howToActive.getWidth() / 4, cam.position.y, howToActive.getWidth() / 2, howToActive.getHeight() / 2);
        float temp2 = cam.position.x + cam.viewportWidth / 4;
        if (input.getX() > temp2 && input.getX() < temp2 + playBtnInactive.getWidth() / 2 &&
                input.getY() > 3 * cam.viewportHeight / 4 - playBtnInactive.getHeight() / 4 && input.getY() < 3 * cam.viewportHeight / 4 + playBtnInactive.getHeight() / 4) {
            sb.draw(playBtnInactive, cam.position.x + cam.viewportWidth / 4, cam.viewportHeight / 4 - playBtnInactive.getHeight() / 4, playBtnInactive.getWidth() / 2, playBtnInactive.getHeight() / 2);
            if (input.isTouched()) {
                gsm.set(new PlayState(gsm));
            }
        } else {
            sb.draw(playBtnActive, cam.position.x + cam.viewportWidth / 4, cam.viewportHeight / 4 - playBtnActive.getHeight() / 4, playBtnActive.getWidth() / 2, playBtnActive.getHeight() / 2);

        }
        sb.end();

    }

    @Override
    public void dispose() {
        background.dispose();
        playBtnActive.dispose();
        playBtnInactive.dispose();
        howToActive.dispose();
        howTOInactive.dispose();
    }
}
