package com.coldwild.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.graph.SizeEvaluator;

/**
 * Created by comrad_gremlin on 9/8/2016.
 */
public class Player extends Character {

    private int fieldX;
    private int fieldY;


    private final int max_lives;
    public static final float APPROACH_TIME = 0.5f;

    public Player(int fx, int fy, Resources res, int _lives)
    {
        super(_lives);
        fieldX = fx;
        fieldY = fy;
        set(res.player);
        max_lives = _lives;
    }

    public int getFieldX()
    {
        return fieldX;
    }

    public void setFieldX(int fx)
    {
        fieldX = fx;
    }

    public int getFieldY()
    {
        return fieldY;
    }

    public void setFieldY(int fy)
    {
        fieldY = fy;
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator)
    {
        preDraw();
        if (timeAlive < APPROACH_TIME)
        {
            float t = timeAlive / APPROACH_TIME; // 0..1
            t = t * t;
            setPosition(
                    t * sizeEvaluator.getBaseScreenX(fieldX),
                    sizeEvaluator.getBaseScreenY(fieldY));
        }
        else
        {
            setPosition(sizeEvaluator.getBaseScreenX(fieldX),
                    sizeEvaluator.getBaseScreenY(fieldY));
        }
        super.draw(batch);
        postDraw();
    }

    public void addLives(int amount) {
        lives += amount;
        if (lives > max_lives)
        {
            lives = max_lives;
        }
    }
}
