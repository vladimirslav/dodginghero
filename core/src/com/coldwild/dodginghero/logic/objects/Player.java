package com.coldwild.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.graph.SizeEvaluator;

/**
 * Created by comrad_gremlin on 9/8/2016.
 */
public class Player extends Sprite {

    private int fieldX;
    private int fieldY;

    private int lives;
    private final int max_lives;

    public Player(int fx, int fy, Resources res, int _lives)
    {
        fieldX = fx;
        fieldY = fy;
        set(res.player);
        lives = _lives;
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
        setPosition(sizeEvaluator.getBaseScreenX(fieldX),
                sizeEvaluator.getBaseScreenY(fieldY));
        super.draw(batch);
    }

    public void takeDamage(int val)
    {
        lives -= val;
        if (lives < 0)
        {
            lives = 0;
        }
    }

    public int getLives()
    {
        return lives;
    }

    public void addLives(int amount) {
        lives += amount;
        if (lives > max_lives)
        {
            lives = max_lives;
        }
    }
}
