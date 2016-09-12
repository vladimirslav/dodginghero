package com.coldwild.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.graph.SizeEvaluator;

/**
 * Created by comrad_gremlin on 9/12/2016.
 */
public class Enemy extends Sprite {
    public Enemy(Resources res)
    {
        set(res.enemy);
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator)
    {
        setPosition(sizeEvaluator.getEnemyX(this), sizeEvaluator.getEnemyY(this));
        super.draw(batch);
    }
}
