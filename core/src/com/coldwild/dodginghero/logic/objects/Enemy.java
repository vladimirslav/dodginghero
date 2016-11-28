package com.coldwild.dodginghero.logic.objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.coldwild.dodginghero.Resources;
import com.coldwild.dodginghero.graph.SizeEvaluator;
import com.coldwild.dodginghero.logic.GameLogic;
import com.coldwild.dodginghero.logic.GameProgress;

/**
 * Created by comrad_gremlin on 9/12/2016.
 */
public class Enemy extends Character {

    private static final float BASE_ATTACK_TIME = 1.0f;
    private static final float WARM_UP_TIME = 2.0f;

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

    private int type;

    public Enemy(Resources res, EnemyAttackListener listener, int _type)
    {
        super(GameProgress.getEnemyLives());
        type = _type;
        set(res.enemySprites.get(type));

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
        if (timeAlive > WARM_UP_TIME && timeSinceAttack > nextAttackTime)
        {
            switch (type)
            {
                case Resources.ENEMY_VERTICAL:
                    performVerticalLineAttack();
                    break;
                case Resources.ENEMY_HORIZONTAL:
                    performHorizontalLineAttack();
                    break;
                case Resources.ENEMY_DIAGONAL:
                    performDiagonalAttack();
                    break;
                case Resources.ENEMY_RANDOM:
                    performRandomAttack();
                    break;
                default:
                    performUltimateAttack();
                    break;
            }
            attackListener.OnAttack(targetTiles);
            resetAttackTime();
        }
    }

    private void performVerticalLineAttack()
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
    }

    private void performHorizontalLineAttack()
    {
        int row1 = MathUtils.random(GameLogic.MAX_BASE_Y);
        int row2 = 0;
        do {
            row2 = MathUtils.random(GameLogic.MAX_BASE_Y);
        } while (row1 == row2);

        for (int x = 0; x < GameLogic.MAX_BASE_X + 1; x++)
        {
            for (int y = 0; y < GameLogic.MAX_BASE_Y +1; y++)
            {
                targetTiles[x][y] = (y == row1 || y == row2);
            }
        }
    }

    private void fillDiagonal(int xstart, int dx)
    {
        for (int i = 0; i <= GameLogic.MAX_BASE_Y; i++)
        {
            int nx = xstart + i * dx;
            if (nx > GameLogic.MAX_BASE_X)
            {
                nx = nx - GameLogic.MAX_BASE_X - 1;
            }

            if (nx < 0)
            {
                nx = nx + GameLogic.MAX_BASE_X + 1;
            }

            targetTiles[nx][i] = true;
        }
    }

    private void performDiagonalAttack()
    {
        int dx1 = -1 + MathUtils.random(1) * 2; // 1 .. -1
        int dx2 = -1 + MathUtils.random(1) * 2; // 1 .. -1

        int col1 = MathUtils.random(GameLogic.MAX_BASE_X);
        int col2 = 0;
        do {
            col2 = MathUtils.random(GameLogic.MAX_BASE_X);
        } while (col1 == col2);

        for (int x = 0; x < GameLogic.MAX_BASE_X + 1; x++)
        {
            for (int y = 0; y < GameLogic.MAX_BASE_Y +1; y++)
            {
                targetTiles[x][y] = false;
            }
        }

        fillDiagonal(col1, dx1);
        fillDiagonal(col2, dx2);
    }

    private void performRandomAttack()
    {
        for (int x = 0; x < GameLogic.MAX_BASE_X + 1; x++)
        {
            for (int y = 0; y < GameLogic.MAX_BASE_Y +1; y++)
            {
                targetTiles[x][y] = false;
            }
        }

        for (int i = 0; i < 10; i++)
        {
            int nx = MathUtils.random(GameLogic.MAX_BASE_X);
            int ny = MathUtils.random(GameLogic.MAX_BASE_Y);

            targetTiles[nx][ny] = true;
        }
    }

    private void performUltimateAttack()
    {
        int rnd = MathUtils.random(3);
        switch (rnd)
        {
            case 0:
                performVerticalLineAttack();
                break;
            case 1:
                performHorizontalLineAttack();
                break;
            case 2:
                performDiagonalAttack();
                break;
            default:
                performRandomAttack();
                break;
        }
    }

}
