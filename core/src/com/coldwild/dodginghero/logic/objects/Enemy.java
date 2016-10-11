package com.coldwild.dodginghero.logic.objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.graph.SizeEvaluator;
import com.coldwild.dodginghero.logic.GameLogic;

/**
 * Created by comrad_gremlin on 9/12/2016.
 */
public class Enemy extends Character {

    private static final float BASE_ATTACK_TIME = 3.0f;
    private static final int DEFAULT_ENEMY_LIVES = 10;

    private float timeSinceAttack;
    private float nextAttackTime;
    private int lives;

    private static float SCALE_TIME = 0.5f;

    private boolean targetTiles[][];

    public interface EnemyAttackListener
    {
        void OnAttack(boolean[][] tiles);
    }

    private EnemyAttackListener attackListener;

    public Enemy(Resources res, EnemyAttackListener listener)
    {
        super(DEFAULT_ENEMY_LIVES);
        set(res.enemy);
        resetAttackTime();
        attackListener = listener;

        targetTiles = new boolean[GameLogic.MAX_BASE_X + 1][];
        for (int i = 0; i < GameLogic.MAX_BASE_Y + 1; i++)
        {
            targetTiles[i] = new boolean[GameLogic.MAX_BASE_Y + 1];
        }
    }

    public void resetAttackTime()
    {
        timeSinceAttack = 0;
        nextAttackTime = BASE_ATTACK_TIME + MathUtils.random(2);
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator)
    {
        preDraw();
        setPosition(sizeEvaluator.getEnemyX(this), sizeEvaluator.getEnemyY(this));
        if (timeAlive < SCALE_TIME)
        {
            float t = timeAlive / SCALE_TIME; // 0..1
            t = t * t;
            setScale(t);
        }
        else
        {
            setScale(1);
        }
        super.draw(batch);
        postDraw();
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        timeSinceAttack += delta;
        if (timeSinceAttack > nextAttackTime)
        {
            int col1 = MathUtils.random(GameLogic.MAX_BASE_X);
            int col2 = 0;
            do {
                col2 = MathUtils.random(GameLogic.MAX_BASE_X);
            } while (col1 == col2);

            for (int x = 0; x < GameLogic.MAX_BASE_X + 1; x++)
            {
                for (int y = 0; y < GameLogic.MAX_BASE_Y +1; y++)
                {
                    targetTiles[x][y] = (x == col1 || x == col2);
                }
            }

            attackListener.OnAttack(targetTiles);
            resetAttackTime();
        }
    }
}
