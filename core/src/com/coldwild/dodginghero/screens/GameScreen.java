package com.coldwild.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.coldwild.dodginghero.DodgingHero;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.SoundManager;
import com.coldwild.dodginghero.graph.Background;
import com.coldwild.dodginghero.graph.SizeEvaluator;
import com.coldwild.dodginghero.graph.effects.WarningEffect;
import com.coldwild.dodginghero.logic.GameLogic;
import com.coldwild.dodginghero.logic.GameProgress;
import com.coldwild.dodginghero.logic.objects.Bonus;
import com.coldwild.dodginghero.logic.objects.Player;

import sun.security.provider.SHA;

/**
 * Created by comrad_gremlin on 9/6/2016.
 */
public class GameScreen extends DefaultScreen
        implements GameLogic.GameEventListener {
    SpriteBatch batch;

    // 8 height
    // 12 width
    public static final int SCREEN_W = 12 * Resources.TILE_SIZE; // 192
    public static final int SCREEN_H = 8 * Resources.TILE_SIZE; // 128

    private static final float SHAKE_TIME_ON_DMG = 0.3f;
    private static final float SHAKE_DIST = 4.0f;

    private SizeEvaluator sizeEvaluator;

    private Stage gameStage;
    private Background bg;
    private GameLogic logic;

    private Player player;
    private ImageButton sndBtn;

    public static final float GAME_END_FADEOUT = 0.5f;
    public static final float GAME_START_FADEIN = 0.25f;

    boolean endgame = false;

    public GameScreen(DodgingHero _game) {
        super(_game);
        batch = new SpriteBatch();

        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);
        gameStage = new Stage(viewport, batch);
        bg = new Background();
        SoundManager.PlayBattleMusic();

        sizeEvaluator = new SizeEvaluator(gameStage,
                game.res,
                GameLogic.MAX_BASE_X,
                GameLogic.MAX_BASE_Y,
                gameStage.getWidth());

        logic = new GameLogic(game, this);
        player = logic.getPlayer();

        Gdx.input.setInputProcessor(gameStage);

        gameStage.addAction(
                new Action() {
                    float time = 0;
                    @Override
                    public boolean act(float delta) {
                        time += delta;
                        float t = time / GAME_START_FADEIN;
                        t *= t;
                        if (t > 1.0f)
                        {
                            t = 1.0f;
                        }

                        batch.setColor(1, 1, 1, t);
                        return time >= GAME_START_FADEIN;
                    }
                }
        );

        sndBtn = new ImageButton(game.res.soundBtn[GameProgress.soundVolume]);
        sndBtn.setPosition(gameStage.getWidth() - sndBtn.getWidth() - 10, 10);
        sndBtn.addListener(new ClickListener()
        {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                SoundManager.AdjustVolume();
                sndBtn.getStyle().imageUp = game.res.soundBtn[GameProgress.soundVolume];
                super.touchUp(event, x, y, pointer, button);
            }
        });
        gameStage.addActor(sndBtn);
        gameStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode)
                {
                    case Input.Keys.UP:
                        AttempMove(0, 1);
                        break;
                    case Input.Keys.DOWN:
                        AttempMove(0, -1);
                        break;
                    case Input.Keys.LEFT:
                        AttempMove(-1, 0);
                        break;
                    case Input.Keys.RIGHT:
                        AttempMove(1, 0);
                        break;
                };
                return false;
            }
        });

        gameStage.getCamera().update();
        batch.setProjectionMatrix(gameStage.getCamera().combined);
    }

    public void update(float delta)
    {
        gameStage.act(delta);
        logic.update(delta);
    }

    public void drawBases()
    {
        batch.begin();

        // draw 4 x 4 bases:
        for (int x = 0; x <= GameLogic.MAX_BASE_X; x++)
        {
            for (int y = 0; y <= GameLogic.MAX_BASE_Y; y++)
            {
                batch.draw(game.res.base,
                        sizeEvaluator.getBaseScreenX(x),
                        sizeEvaluator.getBaseScreenY(y));
            }
        }

        batch.end();
    }

    private void DrawShadowed(String str, float x, float y, float width, int align, Color color)
    {
        game.res.gamefont.setColor(Color.BLACK);

        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                game.res.gamefont.draw(batch, str, x + i, y + j, width, align, false);
            }
        }

        game.res.gamefont.setColor(color);
        game.res.gamefont.draw(batch, str, x, y, width, align, false);
        game.res.gamefont.setColor(Color.WHITE);
    }

    private void ShowGameResult(String result)
    {
        DrawShadowed(result,
                0,
                gameStage.getHeight() / 2,
                gameStage.getWidth(),
                Align.center,
                Color.RED);
    }

    private void DrawUI()
    {
        batch.begin();
        DrawShadowed("LIVES:" + player.getLives(),
                5,
                gameStage.getHeight() - 7,
                gameStage.getWidth(),
                Align.left,
                Color.WHITE);

        DrawShadowed("ENEMY:" + logic.getEnemy().getLives(),
                0,
                gameStage.getHeight() - 7,
                gameStage.getWidth() - 5,
                Align.right,
                Color.WHITE);

        batch.draw(game.res.coinBonus,
                gameStage.getViewport().getScreenX() + 2,
                gameStage.getViewport().getScreenY() + 5);

        DrawShadowed("" + GameProgress.currentGold,
                gameStage.getViewport().getScreenX() + game.res.coinBonus.getWidth() + 4,
                gameStage.getViewport().getScreenY() + 8 + game.res.coinBonus.getHeight() / 2,
                gameStage.getWidth() - 4,
                Align.left,
                Color.WHITE);

        if (player.getLives() <= 0)
        {
            ShowGameResult("DEFEAT!");
        }
        else if (logic.getEnemy().getLives() <= 0)
        {
            ShowGameResult("VICTORY!");
        }
        batch.end();
    }

    @Override
    public void render(float delta)
    {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        bg.draw(gameStage, game.res);
        drawBases();

        batch.begin();
        for (Bonus bonus : logic.getBonuses())
        {
            bonus.draw(batch, sizeEvaluator);
        }
        player.draw(batch, sizeEvaluator);
        logic.getEnemy().draw(batch, sizeEvaluator);
        batch.end();

        logic.getEffectEngine().draw(batch, sizeEvaluator);

        gameStage.getCamera().position.set(gameStage.getWidth() / 2, gameStage.getHeight() / 2, 0);
        if (player.getLives() > 0 &&
            player.getTimeAlive() - player.getTimeOfDmgTaken() < SHAKE_TIME_ON_DMG)
        {
            gameStage.getCamera().translate(-(SHAKE_DIST/2) + MathUtils.random(SHAKE_DIST),
                    -(SHAKE_DIST/2) + MathUtils.random(SHAKE_DIST), 0);
        }
        gameStage.getCamera().update();

        DrawUI();
        gameStage.draw();

        if (endgame)
        {
            dispose();
            if (playerWon)
            {
                game.setScreen(new GameScreen(game));
            }
            else
            {
                game.setScreen(new CharacterSelectionScreen(game));
            }
        }
    }

    @Override
    public void dispose()
    {
        SoundManager.StopBattleMusic();
        super.dispose();
        batch.dispose();
        gameStage.dispose();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
        gameStage.getViewport().update(width, height, true);
        sizeEvaluator.setRightSideX(gameStage.getWidth());
    }

    public void AttempMove(int dx, int dy)
    {
        if (player.getLives() > 0 &&
            logic.getEnemy().getLives() > 0 &&
            logic.CanMove(player.getFieldX() + dx, player.getFieldY() + dy))
        {
            SoundManager.PlayWalkSound();
            logic.AssignPlayerPosition(player.getFieldX() + dx, player.getFieldY() + dy);
        }
    }

    boolean playerWon;
    @Override
    public void OnGameEnd(final boolean playerWon) {
        this.playerWon = playerWon;
        gameStage.addAction(
            Actions.sequence(
                new Action() {
                    float time = 0;
                    @Override
                    public boolean act(float delta) {
                        time += delta;

                        float t = time / GAME_END_FADEOUT; // 0 .. 1
                        t *= t;
                        batch.setColor(1, 1, 1, 1 - t);
                        return time >= GAME_END_FADEOUT;
                    }
                },
                new Action()
                {
                    @Override
                    public boolean act(float delta) {
                        endgame = true;
                        return true;
                    }
                }
            )
        );
    }

    @Override
    public void OnBonusPickup(byte bonusType) {
        if (bonusType == Bonus.BONUS_TYPE_COIN)
        {
            SoundManager.PlayCoinSound();
        }
        else if (bonusType == Bonus.BONUS_TYPE_HEALTH)
        {
            SoundManager.PlayHealSound();
        }
    }
}
