package com.coldwild.dodginghero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by comrad_gremlin on 9/6/2016.
 */
public class Resources {

    TextureAtlas gameSprites;

    public TextureRegion ground;
    public TextureRegion wall;

    public Resources()
    {
        gameSprites = new TextureAtlas(Gdx.files.internal("packed/game.atlas"));
        ground = gameSprites.findRegion("ground");
        wall = gameSprites.findRegion("wall");
    }

    public void dispose()
    {
        gameSprites.dispose();
    }
}
