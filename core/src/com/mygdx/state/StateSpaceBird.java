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
import com.mygdx.sprites.*;

import java.util.Random;

public class StateSpaceBird extends State {

    private static final int BIRD_COUNT = 12;
    private static final int BIRD_SPACE = 250;
    private static final int DOOR_START = 20000;
    private static final int BLOOD_SPACE = 180;
    private static final int BLOOD_COUNT = 5;
    private static final int BLOOD_WIDTH = 90;
    private static final int POISON_SPACE = 150;
    private static final int POISON_COUNT = 3;
    private static final int POISON_WIDTH = 90;
    private static final int COIN_SPACING = 500;
    private static final int COIN_SPACING_500 = 2000;
    private static final int COIN_COUNT = 15;
    private static final int COIN_COUNT_500 = 10;
    private static float counter = 0;
    private Random random;
    private Texture background;
    private PonyCharacter ponyCharacter;
    private Array<Bird> birds;
    private Array<Door> door;
    private Array<Blood> bloods;
    private Array<Poison> poisons;
    private Array<Coins> coins;
    private Array<Coins> coins500;
    private Texture heartIcon;
    private Texture gameOverImg;
    private Texture fade;
    private Texture wait;
    private BitmapFont font;
    private Sound bgSound;
    private Sound monsterSound;
    private Sound rocketSound;
    private Sound coinCrash;
    private String scoreString;
    private String lifeString;
    private int check = 1;
    private boolean pauseState = false;

    public StateSpaceBird(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        background = new Texture("Background/bg_stage_3.png");
        ponyCharacter = new PonyCharacter(0, 450);
        heartIcon = new Texture("materials/heartIcon.png");
        gameOverImg = new Texture("materials/gameover.png");
        fade = new Texture("materials/fade.png");
        wait = new Texture("materials/loading.png");
        random= new Random();

        font = new BitmapFont();

        bgSound = Gdx.audio.newSound(Gdx.files.internal("audio/space.ogg"));
        bgSound.loop(0.5f, 1, 0);
        rocketSound = Gdx.audio.newSound(Gdx.files.internal("audio/rocket.wav"));
        monsterSound = Gdx.audio.newSound(Gdx.files.internal("audio/monster.wav"));
        coinCrash = Gdx.audio.newSound(Gdx.files.internal("audio/coin.wav"));

        PonyCharacter.setMOVEMENT(300);

        door = new Array<Door>();
        for (int i = 1; i <= 1; i++) {
            door.add(new Door(i * (52 + DOOR_START)));
        }
        birds = new Array<Bird>();
        for (int i = 1; i < BIRD_COUNT; i++) {
            birds.add(new Bird(i * (Bird.BIRD_WIDTH + BIRD_SPACE)));
        }
        coins = new Array<Coins>();
        for (int i = 1; i <= COIN_COUNT; i++) {
            coins.add(new Coins(i * (COIN_SPACING + Coins.COIN_WIDTH)));
        }

        coins500 = new Array<Coins>();
        for (int i = 1; i <= COIN_COUNT_500; i++) {
            coins500.add(new Coins(i * (COIN_SPACING_500 + Coins.COIN_WIDTH)));
        }

        bloods = new Array<Blood>();
        for (int i = 1; i <= BLOOD_COUNT; i++) {
            bloods.add(new Blood(i * BLOOD_SPACE * BLOOD_WIDTH));
        }
        poisons = new Array<Poison>();
        for (int i = 1; i <= POISON_COUNT; i++) {
            poisons.add(new Poison(i * POISON_SPACE * POISON_WIDTH));
        }
    }

    @Override
    protected void handleInput() {
        //Touch or Press SPACE BAR To Jump
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
            if (ponyCharacter.colliding) {
                gsm.set(new OverState(gsm));
                bgSound.dispose();
                dispose();
            } else
                ponyCharacter.jump();
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

            for (int i = 0; i < birds.size; i++) {
                Bird bird = birds.get(i);
                bird.update(dt);
                if (cam.position.x - cam.viewportWidth > bird.getPosBird().x + Bird.BIRD_WIDTH) {
                    bird.rePosition(bird.getPosBird().x + ((Bird.BIRD_WIDTH + BIRD_SPACE) * BIRD_COUNT));
                }
                if (bird.collides(ponyCharacter.getBounds())) {
                    rocketSound.play();
                    birds.removeIndex(i);
                    ponyCharacter.down();
                    checkIf();
                    rocketSound.dispose();

                }
                if (bird.collidesLowest(ponyCharacter.getBounds())) {
                    monsterSound.play();
                    birds.removeIndex(i);
                    ponyCharacter.up();
                    checkIf();
                    monsterSound.dispose();
                }
            }

            for (int i = 0; i < door.size; i++) {
                Door doors = door.get(i);
                if (cam.position.x - (cam.viewportWidth) > doors.getPosDoor().x + doors.getDoor().getWidth()) {
                    doors.rePosition(doors.getPosDoor().x + ((Door.DOOR_WIDTH + DOOR_START)));
                }
                if (doors.collides(ponyCharacter.getBounds())) {
                    gsm.set(new StateLava(gsm));
                }
            }

            //  CREATE 100 POINTS COINS
            for (int i = 0; i < coins.size; i++) {
                Coins coin = coins.get(i);
                if (coin.collides100(ponyCharacter.getBounds())) {
                    coinCrash.play();
                    MyGame.setTime(MyGame.getTime() + 20);
                    coins.removeIndex(i);
                    coins.add(new Coins(i * (COIN_SPACING + Coins.COIN_WIDTH)));
                }
                if (cam.position.x - cam.viewportWidth > coin.getPosCoin100().x + coin.getCoin100().getWidth()) {
                    coin.rePosition100(coin.getPosCoin100().x + ((Coins.COIN_WIDTH + COIN_SPACING) * COIN_COUNT));
                }
            }

            //  CREATE 500 POINTS COINS
            for (int i = 0; i < coins500.size; i++) {
                Coins coin500 = coins500.get(i);
                if (coin500.collides500(ponyCharacter.getBounds())) {
                    coinCrash.play();
                    MyGame.setTime(MyGame.getTime() + 100);
                    coins500.removeIndex(i);
                    coins500.add(new Coins(i * (COIN_SPACING_500 + Coins.COIN_WIDTH)));
                }
                if (cam.position.x - (cam.viewportWidth) > coin500.getPosCoin500().x + coin500.getCoin500().getWidth()) {
                    coin500.rePosition500(coin500.getPosCoin500().x + ((Coins.COIN_WIDTH + COIN_SPACING_500) * COIN_COUNT_500));
                }
            }

            for (int i = 0; i < bloods.size; i++) {
                Blood blood = bloods.get(i);
                if (blood.collides(ponyCharacter.getBounds())) {
                    coinCrash.play();
                    bloods.removeIndex(i);
                    MyGame.setHeart(MyGame.getHeart() + 1);
                }
            }

            for (int i = 0; i < poisons.size; i++) {
                Poison poison = poisons.get(i);
                if (poison.collides(ponyCharacter.getBounds())) {
                    coinCrash.play();
                    poisons.removeIndex(i);
                    MyGame.setHeart(MyGame.getHeart() - 1);
                }
            }

            if (ponyCharacter.getY() >= MyGame.HEIGHT) {
                checkIf();
                ponyCharacter.down();
            }

            if (ponyCharacter.getY() <= 0) {
                checkIf();
                ponyCharacter.up();
            }

            counter += Gdx.graphics.getDeltaTime() * 10;
            if ((int) counter % 100 == 0) {
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


        font.getData().setScale(4f);
        font.setColor(Color.WHITE);
        font.draw(sb, scoreString, cam.position.x - 60, cam.position.y + (MyGame.HEIGHT / 2));

        for (int i = 0; i < MyGame.getHeart(); i++) {
            sb.draw(heartIcon, cam.position.x - (cam.viewportWidth / 2) + (i * (heartIcon.getWidth() / 14)), cam.position.y + (MyGame.HEIGHT / 2) - heartIcon.getHeight() / 14, heartIcon.getWidth() / 14, heartIcon.getHeight() / 14);
        }

        for (Door doors : door) {
            sb.draw(doors.getDoor(), doors.getPosDoor().x, doors.getPosDoor().y);
        }

        for (Bird bird : birds) {
            sb.draw(bird.getBird(), bird.getPosBird().x, bird.getPosBird().y);
            sb.draw(bird.getDrop(), bird.getPosDrop().x, bird.getPosDrop().y);
        }

        for (Coins coin : coins) {
            sb.draw(coin.getCoin100(), coin.getPosCoin100().x, coin.getPosCoin100().y);
        }

        for (Coins coin500 : coins500) {
            sb.draw(coin500.getCoin500(), coin500.getPosCoin500().x, coin500.getPosCoin500().y);
        }


        for (Blood blood : bloods) {
            sb.draw(blood.getBlood(), blood.getPosBlood().x, blood.getPosBlood().y);
        }

        for (Poison poison : poisons) {
            sb.draw(poison.getPoison(), poison.getPosPoison().x, poison.getPosPoison().y);
        }

        sb.draw(ponyCharacter.getBird(), ponyCharacter.getX(), ponyCharacter.getY());

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
        heartIcon.dispose();
        font.dispose();
        fade.dispose();
        gameOverImg.dispose();
        wait.dispose();
        bgSound.dispose();
        monsterSound.dispose();
        rocketSound.dispose();
        for (Door doors : door) {
            doors.dispose();
        }
        for (Bird bird : birds) {
            bird.dispose();
        }
        for (Coins coin : coins) {
            coin.dispose100();
        }
        for (Coins coin500 : coins500) {
            coin500.dispose500();
        }
        for (Blood blood : bloods) {
            blood.dispose();
        }
        for (Poison poison : poisons) {
            poison.dispose();
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
        }
    }
}
