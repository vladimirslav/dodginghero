package com.coldwild.dodginghero.graph.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.coldwild.dodginghero.graph.SizeEvaluator;

/**
 * Created by comrad_gremlin on 9/9/2016.
 */
public abstract class Effect implements Pool.Poolable {

    protected boolean isAlive;
    protected float timeAlive;

    public Effect()
    {
        isAlive = false;
        timeAlive = 0;
    }

    @Override
    public void reset() {

    }

    public void init(EffectEngine parent)
    {
        isAlive = true;
        timeAlive = 0;
        parent.add(this);
    }

    public void update(float delta)
    {
        timeAlive += delta;
    }

    public abstract void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator);

    public boolean isAlive()
    {
        return isAlive;
    }

    public abstract void release();
}
