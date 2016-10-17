package com.coldwild.dodginghero.graph.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.graph.SizeEvaluator;

/**
 * Created by comrad_gremlin on 9/9/2016.
 */
public class WarningEffect extends Effect {

    private static final float WARNING_TIME = 0.5f;
    private int fieldX;
    private int fieldY;
    private Resources resources;

    public interface WarningEffectListener
    {
        void OnEffectOver(WarningEffect effect);
    };

    private WarningEffectListener listener;

    public static WarningEffect Create(int fx,
                                       int fy,
                                       EffectEngine engine,
                                       Resources res,
                                       WarningEffectListener _listener)
    {
        WarningEffect effect = warningPool.obtain();
        effect.init(fx, fy, engine, res, _listener);
        return effect;
    }

    public WarningEffect()
    {

    }

    public void init(int fx,
                     int fy,
                     EffectEngine parent,
                     Resources res,
                     WarningEffectListener _listener)
    {
        listener = _listener;
        fieldX = fx;
        fieldY = fy;
        resources = res;
        super.init(parent);
    }

    @Override
    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator) {
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
        if (timeAlive > WARNING_TIME && isAlive)
        {
            isAlive = false;
            if (listener != null)
            {
                listener.OnEffectOver(this);
            }
        }
    }

    public int getFieldX()
    {
        return fieldX;
    }

    public int getFieldY()
    {
        return fieldY;
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
