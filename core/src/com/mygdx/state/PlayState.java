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


public class PlayState extends State {

    private static final int TUBE_SPACING = 300;        //Each Obstacle random space
    private static final int TUBE_COUNT = 13;
    private static final int COIN_SPACING = 500;
    private static final int COIN_SPACING_500 = 2000;
    private static final int COIN_COUNT = 15;
    private static final int COIN_COUNT_500 = 5;
    private static final int BLOOD_SPACE = 200;
    private static final int BLOOD_COUNT = 4;
    private static final int BLOOD_WIDTH = 90;
    private static final int POISON_SPACE = 185;
    private static final int POISON_COUNT = 8;
    private static final int POISON_WIDTH = 90;
    private static final int DOOR_START = 20000;        //SET IN REAL
    private PonyCharacter ponyCharacter;
    private Texture bg;
    private Array<Tube> tubes;
    private Array<Coins> coins;
    private Array<Coins> coins500;
    private Array<Blood> bloods;
    private Array<Poison> poisons;
    private Array<Door> door;
    private BitmapFont font;

    private Texture heartIcon;
    private Texture gameOverImg;
    private String scoreString;
    private String lifeString;
    private String setScore;
    private Texture fade;
    private Texture wait;
    private int check = 1;

    private Sound crash;
    private Sound coinCrash;
    private Sound bgMusic;

    private boolean pauseState = false;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        ponyCharacter = new PonyCharacter(0, 300);

        cam.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);

        bg = new Texture("Background/bg_stage_1.png");
        heartIcon = new Texture("materials/heartIcon.png");
        gameOverImg = new Texture("materials/gameover.png");
        fade = new Texture("materials/fade.png");
        wait = new Texture("materials/loading.png");

        crash = Gdx.audio.newSound(Gdx.files.internal("audio/crash.ogg"));
        coinCrash = Gdx.audio.newSound(Gdx.files.internal("audio/coin.wav"));
        bgMusic = Gdx.audio.newSound(Gdx.files.internal("audio/closer.ogg"));
        bgMusic.loop(0.5f, 1, 0);

        tubes = new Array<Tube>();
        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }

        coins = new Array<Coins>();
        for (int i = 1; i <= COIN_COUNT; i++) {
            coins.add(new Coins(i * (COIN_SPACING + Coins.COIN_WIDTH)));
        }

        coins500 = new Array<Coins>();
        for (int i = 1; i <= COIN_COUNT_500; i++) {
            coins500.add(new Coins(i * (COIN_SPACING_500 + Coins.COIN_WIDTH)));
        }

        door = new Array<Door>();
        for (int i = 1; i <= 1; i++) {
            door.add(new Door(i * (52 + DOOR_START)));
        }

        bloods = new Array<Blood>();
        for (int i = 1; i <= BLOOD_COUNT; i++) {
            bloods.add(new Blood(i * BLOOD_SPACE * BLOOD_WIDTH));
        }
        poisons = new Array<Poison>();
        for (int i = 1; i <= POISON_COUNT; i++) {
            poisons.add(new Poison(i * POISON_SPACE * POISON_WIDTH));
        }


        //Set Font Style
        font = new BitmapFont();

        PonyCharacter.setMOVEMENT(300);

    }

    @Override
    protected void handleInput() {

        //Touch or Press SPACE BAR To Jump
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
            if (ponyCharacter.colliding) {
                gsm.set(new OverState(gsm));
                dispose();
                bgMusic.dispose();
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


            //  CREATE CLOUD & LAND
            for (int i = 0; i < tubes.size; i++) {
                Tube tube = tubes.get(i);
                if (tube.collides(ponyCharacter.getBounds())) {
                    crash.play();
                    checkIf();
                    if (MyGame.getHeart() < 0) {
                        ponyCharacter.colliding = true;
                        crash.dispose();
                    } else {
                        tubes.removeIndex(i);
                        tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
                    }
                }
                if (cam.position.x - cam.viewportWidth > tube.getPosCloud().x + tube.getCloud().getWidth()) {
                    tube.rePosition(tube.getPosCloud().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
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

            //  CREATE DOOR //
            for (int i = 0; i < door.size; i++) {
                Door doors = door.get(i);
                if (doors.collides(ponyCharacter.getBounds())) {
                    bgMusic.dispose();
                    dispose();
                    gsm.set(new StateButterflyOcean(gsm));
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
                    System.out.println("Final :" + MyGame.getFinal_time());
                    check = 0;
                }
            }
        }

        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0, MyGame.WIDTH, MyGame.HEIGHT);

        if (MyGame.getHeart() < 0) {
            lifeString = "0";
        }

        for (Door doors : door) {
            sb.draw(doors.getDoor(), doors.getPosDoor().x, doors.getPosDoor().y);
        }

        for (Tube tube : tubes) {
            sb.draw(tube.getCloud(), tube.getPosCloud().x, tube.getPosCloud().y, tube.getCloud().getWidth() / 2, tube.getCloud().getHeight() / 2);
            sb.draw(tube.getTree(), tube.getPosTree().x, tube.getPosTree().y);
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
        bg.dispose();
        font.dispose();
        ponyCharacter.dispose();
        bgMusic.dispose();
        crash.dispose();
        coinCrash.dispose();
        heartIcon.dispose();
        fade.dispose();
        wait.dispose();
        gameOverImg.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
        }
        for (Coins coin : coins) {
            coin.dispose100();
        }
        for (Coins coin500 : coins500) {
            coin500.dispose500();
        }
        for (Door doors : door) {
            doors.dispose();
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
        MyGame.setTime(MyGame.getTime() - 20);
        if (MyGame.getTime() < 0)
            MyGame.setTime(0);
    }

}
