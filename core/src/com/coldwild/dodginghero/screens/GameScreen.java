package com.coldwild.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.coldwild.dodginghero.DodgingHero;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.graph.Background;
import com.coldwild.dodginghero.graph.SizeEvaluator;
import com.coldwild.dodginghero.graph.effects.WarningEffect;
import com.coldwild.dodginghero.logic.GameLogic;
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
        WarningEffect.Create(0, 0, logic.getEffectEngine(), sizeEvaluator, game.res);
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

    @Override
    public void render(float delta)
    {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        bg.draw(gameStage, game.res);
        drawBases();
        logic.getEffectEngine().draw(batch);

        batch.begin();
        player.draw(batch, sizeEvaluator);
        logic.getEnemy().draw(batch, sizeEvaluator);
        batch.end();

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
        if (logic.CanMove(player.getFieldX() + dx, player.getFieldY() + dy))
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
