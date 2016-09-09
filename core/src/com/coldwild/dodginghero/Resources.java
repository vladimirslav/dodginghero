package com.coldwild.dodginghero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by comrad_gremlin on 9/6/2016.
 */
public class Resources {

    TextureAtlas gameSprites;

    public TextureRegion ground;
    public TextureRegion wall;

    public Sprite player;
    public TextureRegion base;
    public TextureRegion warning;

    public static final int TILE_SIZE = 16;

    public Resources()
    {
        gameSprites = new TextureAtlas(Gdx.files.internal("packed/game.atlas"));
        ground = gameSprites.findRegion("ground");
        wall = gameSprites.findRegion("wall");

        player = new Sprite(gameSprites.findRegion("player"));
        base = gameSprites.findRegion("base");
        warning = gameSprites.findRegion("warning");
    }

    public void dispose()
    {
        gameSprites.dispose();
    }
}
