package com.coldwild.dodginghero.graph.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.graph.SizeEvaluator;

/**
 * Created by comrad_gremlin on 9/9/2016.
 */
public class WarningEffect extends Effect {

    private static final float WARNING_TIME = 2.0f;
    private int fieldX;
    private int fieldY;
    private SizeEvaluator sizeEvaluator;
    private Resources resources;

    public static WarningEffect Create(int fx,
                                       int fy,
                                       EffectEngine engine,
                                       SizeEvaluator sz,
                                       Resources res)
    {
        WarningEffect effect = warningPool.obtain();
        effect.init(fx, fy, engine, sz, res);
        return effect;
    }

    public WarningEffect()
    {

    }

    public void init(int fx, int fy, EffectEngine parent, SizeEvaluator sz, Resources res)
    {
        fieldX = fx;
        fieldY = fy;
        sizeEvaluator = sz;
        resources = res;
        super.init(parent);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(resources.warning,
                sizeEvaluator.getBaseScreenX(fieldX),
                sizeEvaluator.getBaseScreenY(fieldY));
        batch.end();
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if (timeAlive > WARNING_TIME)
        {
            isAlive = false;
        }
    }

    @Override
    public void release() {
        warningPool.free(this);
    }

    static Pool<WarningEffect> warningPool = new Pool<WarningEffect>() {
        @Override
        protected WarningEffect newObject() {
            return new WarningEffect();
        }
    };
}
