package com.mygdx.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.sprites.*;

public class StateLava extends State {

    private static final int DOOR_START = 20000;
    private static final int ROCK_COUNT = 10;
    private static final int ROCK_SPACE = 250;
    private static final int BLOOD_SPACE = 220;
    private static final int BLOOD_COUNT = 4;
    private static final int BLOOD_WIDTH = 90;
    private static final int POISON_SPACE = 180;
    private static final int POISON_COUNT = 4;
    private static final int POISON_WIDTH = 90;
    private static final int COIN_SPACING = 500;
    private static final int COIN_SPACING_500 = 2000;
    private static final int COIN_COUNT = 17;
    private static final int COIN_COUNT_500 = 7;
    private static final int GROUND_Y_OFFSET = -50;
    private static float counter = 0;
    private Texture background;
    private PonyCharacter ponyCharacter;
    private Array<Door> door;
    private Array<RockLava> rocks;
    private Array<Blood> bloods;
    private Array<Poison> poisons;
    private Array<Coins> coins;
    private Array<Coins> coins500;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Texture heartIcon;
    private Texture gameOverImg;
    private Texture fade;
    private Texture wait;
    private Sound bgSound;
    private Sound collisionSound;
    private Sound coinCrash;
    private BitmapFont font;
    private String scoreString;
    private String lifeString;
    private int check = 1;
    private boolean pauseState = false;

    public StateLava(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        background = new Texture("Background/bg_stage_4.png");
        ponyCharacter = new PonyCharacter(0, 450);

        heartIcon = new Texture("materials/heartIcon.png");
        gameOverImg = new Texture("materials/gameover.png");
        fade = new Texture("materials/fade.png");
        wait = new Texture("materials/loading.png");

        ground = new Texture("materials/lava2p.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);

        font = new BitmapFont();
        bgSound = Gdx.audio.newSound(Gdx.files.internal("audio/lava.ogg"));
        bgSound.loop(0.5f);
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("audio/sword.wav"));
        coinCrash = Gdx.audio.newSound(Gdx.files.internal("audio/coin.wav"));

        PonyCharacter.setMOVEMENT(300);


        door = new Array<Door>();
        for (int i = 1; i <= 1; i++) {
            door.add(new Door(i * (52 + DOOR_START)));
        }

        rocks = new Array<RockLava>();
        for (int i = 0; i < ROCK_COUNT; i++) {
            rocks.add(new RockLava(i * (RockLava.ROCK_WIDTH + ROCK_SPACE)));
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
            updateGround();
            ponyCharacter.update(dt);
            cam.position.x = ponyCharacter.getPosition().x + 80;

            for (int i = 0; i < rocks.size; i++) {
                RockLava rock = rocks.get(i);
                rock.update(dt);
                if (cam.position.x - cam.viewportWidth * 2 > rock.getPosRock().x + rock.getRock().getWidth()) {
                    rock.rePosition(rock.getPosRock().x + ((RockLava.ROCK_WIDTH + ROCK_SPACE) * ROCK_COUNT));
                }
                if (rock.collides(ponyCharacter.getBounds())) {
                    rocks.removeIndex(i);
                    collisionSound.play();
                    ponyCharacter.down();
                    checkIf();
                    collisionSound.dispose();
                }

                if (rock.collidesAnimation(ponyCharacter.getBounds())) {
                    rocks.removeIndex(i);
                    collisionSound.play();
                    ponyCharacter.up();
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
                    gsm.set(new StateExtream(gsm));
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

            if (ponyCharacter.getY() <= ground.getHeight() + GROUND_Y_OFFSET) {
                collisionSound.play();
                checkIf();
                ponyCharacter.up();
                collisionSound.dispose();
                if (MyGame.getHeart() < 0) {
                    ponyCharacter.colliding = true;
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

            counter += Gdx.graphics.getDeltaTime() * 10;
            if ((int) counter % 100 == 0) {
                PonyCharacter.setMOVEMENT(PonyCharacter.getMOVEMENT() + 30);
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

        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);

        font.getData().setScale(4f);
        font.setColor(Color.BLACK);
        font.draw(sb, scoreString, cam.position.x - 60, cam.position.y + (MyGame.HEIGHT / 2));

        for (int i = 0; i < MyGame.getHeart(); i++) {
            sb.draw(heartIcon, cam.position.x - (cam.viewportWidth / 2) + (i * (heartIcon.getWidth() / 14)), cam.position.y + (MyGame.HEIGHT / 2) - heartIcon.getHeight() / 14, heartIcon.getWidth() / 14, heartIcon.getHeight() / 14);
        }

        for (Door doors : door) {
            sb.draw(doors.getDoor(), doors.getPosDoor().x, doors.getPosDoor().y);
        }

        for (RockLava rock : rocks) {
            sb.draw(rock.getRock(), rock.getPosRock().x, rock.getPosRock().y);
            sb.draw(rock.getAnimationWorm(), rock.getPosWorm().x, rock.getPosWorm().y);
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

        if (ponyCharacter.colliding) {
            sb.draw(fade, cam.position.x - MyGame.WIDTH / 2, cam.position.y - MyGame.HEIGHT / 2, MyGame.WIDTH, MyGame.HEIGHT);
            sb.draw(gameOverImg, cam.position.x - gameOverImg.getWidth() / 2, cam.position.y);
        }


        sb.draw(ponyCharacter.getBird(), ponyCharacter.getX(), ponyCharacter.getY());


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
        bgSound.dispose();
        for (RockLava rock : rocks) {
            rock.dispose();
        }
        for (Door doors : door) {
            doors.dispose();
        }
        for (Blood blood : bloods) {
            blood.dispose();
        }
        for (Coins coin : coins) {
            coin.dispose100();
        }
        for (Coins coin500 : coins500) {
            coin500.dispose500();
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

    private void updateGround() {
        if (cam.position.x - (cam.viewportWidth / 2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() * 2, 0);
        if (cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth() * 2, 0);
    }
}
