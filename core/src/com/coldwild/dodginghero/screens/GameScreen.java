package com.coldwild.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.coldwild.dodginghero.DodgingHero;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.graph.Background;
import com.coldwild.dodginghero.graph.SizeEvaluator;
import com.coldwild.dodginghero.graph.effects.WarningEffect;
import com.coldwild.dodginghero.logic.GameLogic;
import com.coldwild.dodginghero.logic.objects.Bonus;
import com.coldwild.dodginghero.logic.objects.Player;

/**
 * Created by comrad_gremlin on 9/6/2016.
 */
public class GameScreen extends DefaultScreen implements InputProcessor {
    SpriteBatch batch;

    // 8 height
    // 12 width
    public static final int SCREEN_W = 12 * Resources.TILE_SIZE; // 192
    public static final int SCREEN_H = 8 * Resources.TILE_SIZE; // 128


    private SizeEvaluator sizeEvaluator;

    private Stage gameStage;
    private Background bg;
    private GameLogic logic;

    private Player player;

    public GameScreen(DodgingHero _game) {
        super(_game);
        batch = new SpriteBatch();

        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);
        gameStage = new Stage(viewport, batch);
        bg = new Background();
        sizeEvaluator = new SizeEvaluator(gameStage,
                game.res,
                GameLogic.MAX_BASE_X,
                GameLogic.MAX_BASE_Y);

        logic = new GameLogic(game);
        player = logic.getPlayer();

        Gdx.input.setInputProcessor(this);
    }

    public void update(float delta)
    {
        gameStage.act(delta);
        if (player.getLives() > 0)
        {
            logic.update(delta);
        }
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

        if (player.getLives() <= 0)
        {
            DrawShadowed("DEFEAT!",
                    0,
                    gameStage.getHeight() / 2,
                    gameStage.getWidth(),
                    Align.center,
                    Color.RED);
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
        logic.getEffectEngine().draw(batch, sizeEvaluator);

        batch.begin();
        for (Bonus bonus : logic.getBonuses())
        {
            bonus.draw(batch, sizeEvaluator);
        }
        player.draw(batch, sizeEvaluator);
        logic.getEnemy().draw(batch, sizeEvaluator);
        batch.end();

        DrawUI();
        gameStage.draw();
    }

    @Override
    public void dispose()
    {
        super.dispose();
        batch.dispose();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
        gameStage.getViewport().update(width, height, true);
    }

    public void AttempMove(int dx, int dy)
    {
        if (player.getLives() > 0 &&
            logic.CanMove(player.getFieldX() + dx, player.getFieldY() + dy))
        {
            logic.AssignPlayerPosition(player.getFieldX() + dx, player.getFieldY() + dy);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
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

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
