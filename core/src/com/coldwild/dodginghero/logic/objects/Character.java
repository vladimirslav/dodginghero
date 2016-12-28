package com.coldwild.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.coldwild.dodginghero.SoundManager;

/**
 * Created by comrad_gremlin on 10/6/2016.
 */

public class Character extends Sprite {

    protected int lives;

    protected float timeAlive;
    private float timeOfDmgTaken;

    public static final float BLINK_TIME_AFTER_DMG = 0.25f;

    public Character(int _lives)
    {
        lives = _lives;
        timeAlive = 0;
        timeOfDmgTaken = -1;
    }


    public int getLives()
    {
        return lives;
    }

    public void preDraw()
    {
        if (timeAlive < timeOfDmgTaken + BLINK_TIME_AFTER_DMG)
        {
            float t = (timeAlive - timeOfDmgTaken) / BLINK_TIME_AFTER_DMG; // 0..1
            t = t * t;
            setColor(1, 1, 1, t);
        }
    }

    public void postDraw()
    {
        setColor(1, 1, 1, 1);
    }


    public void takeDamage(int amount) {
        SoundManager.PlaySwingSound();
        timeOfDmgTaken = timeAlive;
        lives -= amount;
        if (lives < 0)
        {
            lives = 0;
        }
    }

    public void update(float delta)
    {
        timeAlive += delta;
    }

    public float getTimeOfDmgTaken()
    {
        return timeOfDmgTaken;
    }

    public float getTimeAlive()
    {
        return timeAlive;
    }
}
