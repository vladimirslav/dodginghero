package com.coldwild.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.coldwild.dodginghero.DodgingHero;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.graph.Background;

/**
 * Created by comrad_gremlin on 9/6/2016.
 */
public class GameScreen extends DefaultScreen {
    SpriteBatch batch;

    // 8 height
    // 12 width
    public static final int SCREEN_W = 12 * Resources.TILE_SIZE; // 192
    public static final int SCREEN_H = 8 * Resources.TILE_SIZE; // 128

    private Stage gameStage;
    private Background bg;

    public GameScreen(DodgingHero _game) {
        super(_game);
        batch = new SpriteBatch();

        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);
        gameStage = new Stage(viewport, batch);
        bg = new Background();
    }

    public void update(float delta)
    {
        gameStage.act(delta);
    }

    @Override
    public void render(float delta)
    {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        bg.draw(gameStage, game.res);
        gameStage.draw();
    }

    @Override
    public void dispose()
    {
        super.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
        gameStage.getViewport().update(width, height, true);
    }


}
