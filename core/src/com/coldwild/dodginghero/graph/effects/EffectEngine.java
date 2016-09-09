package com.coldwild.dodginghero.graph.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comrad_gremlin on 9/9/2016.
 */
public class EffectEngine {

    List<Effect> effects;
    public EffectEngine()
    {
        effects = new ArrayList<Effect>();
    }

    public void add(Effect effect)
    {
        effects.add(effect);
    }

    public void update(float delta)
    {
        int i = 0;
        while (i < effects.size())
        {
            effects.get(i).update(delta);
            if (effects.get(i).isAlive())
            {
                i++;
            }
            else
            {
                effects.get(i).release();
                effects.remove(i);
            }
        }
    }

    public void draw(SpriteBatch batch)
    {
        for (int i = 0; i < effects.size(); i++)
        {
            effects.get(i).draw(batch);
        }
    }

    public void clear()
    {
        while (effects.size() > 0)
        {
            effects.get(0).release();
            effects.remove(0);
        }
    }
}
