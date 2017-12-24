package com.mygdx.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.sprites.Bubble;
import com.mygdx.sprites.Door;
import com.mygdx.sprites.PonyCharacter;

public class StateButterflyOcean extends State {

    private static final int BUBBLE_COUNT = 16;
    private static final int BUBBLE_SPACE = 330;
    private static final int DOOR_START = 10000;
    private static float counter = 0;
    private Texture background;
    private PonyCharacter ponyCharacter;
    private Array<Bubble> bubbles;
    private Array<Door> door;
    private Sound collisionSound;
    private Sound ocean;

    private Texture heartIcon;
    private Texture gameOverImg;
    private Texture fade;
    private Texture wait;

    private BitmapFont font;
    private String scoreString;
    private String lifeString;
    private String setScore;
    private int check = 1;

    private boolean pauseState = false;

    public StateButterflyOcean(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        background = new Texture("Background/bg_stage_2.png");
        ponyCharacter = new PonyCharacter(0, 450);

        heartIcon = new Texture("materials/heartIcon.png");
        gameOverImg = new Texture("materials/gameover.png");
        fade = new Texture("materials/fade.png");
        wait = new Texture("materials/loading.png");

        bubbles = new Array<Bubble>();
        for (int i = 1; i <= BUBBLE_COUNT; i++) {
            bubbles.add(new Bubble(i * (BUBBLE_SPACE + Bubble.BUBBLE_WIDTH)));
        }
        door = new Array<Door>();
        for (int i = 1; i <= 1; i++) {
            door.add(new Door(i * (52 + DOOR_START)));
        }

        font = new BitmapFont();
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("audio/bubble.ogg"));
        ocean = Gdx.audio.newSound(Gdx.files.internal("audio/ocean.wav"));
        ocean.loop(0.5f);

        PonyCharacter.setMOVEMENT(300);
    }

    @Override
    protected void handleInput() {
        //Touch or Press SPACE BAR To Jump
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
            if (ponyCharacter.colliding) {
                gsm.set(new OverState(gsm));
                ocean.dispose();
                dispose();
            } else
                ponyCharacter.oceanJump();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pauseState = true;
        }

    }

    @Override
    public void update(float dt) {

        handleInput();

        if (pauseState) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.C) || Gdx.input.justTouched()) {
                pauseState = false;
            }
        } else {
            ponyCharacter.update(dt);
            cam.position.x = ponyCharacter.getPosition().x + 80;

            for (int i = 0; i < bubbles.size; i++) {
                Bubble bubble = bubbles.get(i);
                bubble.update(dt);
                if (cam.position.x - cam.viewportWidth * 2 > bubble.getPosBubble().x + bubble.getBubbleTexture().getWidth()) {
                    bubble.rePosition(bubble.getPosBubble().x + ((Bubble.BUBBLE_WIDTH + BUBBLE_SPACE) * BUBBLE_COUNT));

                }
                if (bubble.collides(ponyCharacter.getBounds())) {
                    collisionSound.play();
                    checkIf();
                    collisionSound.dispose();
                }
            }

            for (int i = 0; i < door.size; i++) {
                Door doors = door.get(i);
                if (cam.position.x - (cam.viewportWidth) > doors.getPosDoor().x + doors.getDoor().getWidth()) {
                    doors.rePosition(doors.getPosDoor().x + ((Door.DOOR_WIDTH + DOOR_START)));
                }
                if (doors.collides(ponyCharacter.getBounds())) {
                    gsm.set(new StateSpaceBird(gsm));
                }
            }

            if (ponyCharacter.getY() >= MyGame.HEIGHT) {
                checkIf();
                if (MyGame.getHeart() < 0) {
                    ponyCharacter.colliding = true;
                } else {
                    gsm.set(new StateButterflyOcean(gsm));
                }
            }

            counter += Gdx.graphics.getDeltaTime() * 10;

            if ((int) counter % 200 == 0) {
                PonyCharacter.setMOVEMENT(PonyCharacter.getMOVEMENT() + 10);
            }
            cam.update();
        }
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);

        if (!pauseState) {
            if (!ponyCharacter.colliding) {
                MyGame.setTime(MyGame.getTime() + Gdx.graphics.getDeltaTime());
                scoreString = Integer.toString((int) MyGame.getTime() * 5);
                lifeString = Integer.toString(MyGame.getHeart());
            } else {
                if (check == 1) {
                    MyGame.setFinal_time((int) MyGame.getTime() * 5);
                    if (MyGame.getHighScore() < MyGame.getFinal_time()) {
                        MyGame.setHighScore(MyGame.getFinal_time());
                    }
                    check = 0;
                }
            }
        }

        sb.begin();
        sb.draw(background, cam.position.x - (cam.viewportWidth / 2), 0, MyGame.WIDTH, MyGame.HEIGHT);

        if (MyGame.getHeart() < 0) {
            lifeString = "0";
        }

        for (Door doors : door) {
            sb.draw(doors.getDoor(), doors.getPosDoor().x, doors.getPosDoor().y);
        }

        for (Bubble bubble : bubbles) {
            sb.draw(bubble.getBubble(), bubble.getPosBubble().x, bubble.getPosBubble().y);
            sb.draw(bubble.getCoral(), bubble.getPosCoral().x, bubble.getPosCoral().y);
        }

        sb.draw(ponyCharacter.getBird(), ponyCharacter.getX(), ponyCharacter.getY());


        font.getData().setScale(4f);
        font.setColor(Color.BLACK);
        font.draw(sb, scoreString, cam.position.x - 60, cam.position.y + (MyGame.HEIGHT / 2));

        for (int i = 0; i < MyGame.getHeart(); i++) {
            sb.draw(heartIcon, cam.position.x - (cam.viewportWidth / 2) + (i * (heartIcon.getWidth() / 14)), cam.position.y + (MyGame.HEIGHT / 2) - heartIcon.getHeight() / 14, heartIcon.getWidth() / 14, heartIcon.getHeight() / 14);
        }

        if (ponyCharacter.colliding) {
            sb.draw(fade, cam.position.x - MyGame.WIDTH / 2, cam.position.y - MyGame.HEIGHT / 2, MyGame.WIDTH, MyGame.HEIGHT);
            sb.draw(gameOverImg, cam.position.x - gameOverImg.getWidth() / 2, cam.position.y);
        }

        sb.end();

        if (pauseState) {
            sb.begin();
            sb.draw(fade, cam.position.x - MyGame.WIDTH / 2, cam.position.y - MyGame.HEIGHT / 2, MyGame.WIDTH, MyGame.HEIGHT);
            sb.draw(wait, cam.position.x - wait.getWidth() / 2, cam.position.y - wait.getHeight() / 2);
            sb.end();
        }

    }

    @Override
    public void dispose() {

        background.dispose();
        ponyCharacter.dispose();
        font.dispose();
        heartIcon.dispose();
        fade.dispose();
        wait.dispose();
        gameOverImg.dispose();
        collisionSound.dispose();
        ocean.dispose();
        for (Bubble bubble : bubbles) {
            bubble.dispose();
        }
        for (Door doors : door) {
            doors.dispose();
        }
    }

    private void checkIf() {
        MyGame.setHeart(MyGame.getHeart() - 1);
        MyGame.setTime(MyGame.getTime() - 2);
        if (MyGame.getTime() < 0) {
            MyGame.setTime(0);
        }
        if (MyGame.getHeart() < 0) {
            ponyCharacter.colliding = true;
        } else {
            gsm.set(new StateButterflyOcean(gsm));
        }
    }
}
