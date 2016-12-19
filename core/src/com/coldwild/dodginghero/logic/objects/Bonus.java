package com.coldwild.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.graph.SizeEvaluator;

/**
 * Created by comrad_gremlin on 9/27/2016.
 */

public class Bonus extends Sprite implements Pool.Poolable {

    public static byte BONUS_TYPE_ATTACK = 0;
    public static byte BONUS_TYPE_HEALTH = 1;
    public static byte BONUS_TYPE_COIN = 2;

    private int fieldX;
    private int fieldY;

    private byte bonusType;

    public Bonus()
    {

    }

    public void setup(int fx, int fy, byte bType, Resources res)
    {
        fieldX = fx;
        fieldY = fy;
        bonusType = bType;
        if (bType == BONUS_TYPE_ATTACK)
        {
            set(res.attackBonus);
        }
        else if (bType == BONUS_TYPE_HEALTH)
        {
            set(res.healthBonus);
        }
        else if (bType == BONUS_TYPE_COIN)
        {
            set(res.coinBonus);
        }
    }


    @Override
    public void reset() {

    }

    static final Pool<Bonus> bonusPool = new Pool<Bonus>() {
        @Override
        protected Bonus newObject() {
            return new Bonus();
        }
    };

    public void release()
    {
        bonusPool.free(this);
    }

    public static Bonus Create(int fx, int fy, byte bType, Resources res)
    {
        Bonus bonus = bonusPool.obtain();
        bonus.setup(fx, fy, bType, res);
        return bonus;
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator)
    {
        setPosition(sizeEvaluator.getBaseScreenX(fieldX),
                sizeEvaluator.getBaseScreenY(fieldY));
        super.draw(batch);
    }

    public int getFieldX()
    {
        return fieldX;
    }

    public int getFieldY()
    {
        return fieldY;
    }

    public byte getBonusType()
    {
        return bonusType;
    }

}
