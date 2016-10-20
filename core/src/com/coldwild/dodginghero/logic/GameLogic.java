package com.coldwild.dodginghero.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.coldwild.dodginghero.DodgingHero;
import com.coldwild.dodginghero.graph.effects.EffectEngine;
import com.coldwild.dodginghero.graph.effects.WarningEffect;
import com.coldwild.dodginghero.logic.objects.Bonus;
import com.coldwild.dodginghero.logic.objects.Enemy;
import com.coldwild.dodginghero.logic.objects.Player;

import java.util.ArrayList;

/**
 * Created by comrad_gremlin on 9/8/2016.
 */
public class GameLogic implements Enemy.EnemyAttackListener, WarningEffect.WarningEffectListener {

    public static final int MAX_BASE_X = 3;
    public static final int MAX_BASE_Y = 3;
    private static final float BONUS_SPAWN_INTERVAL = 2.0f; // spawn bonus every 2 seconds
    private static final int MAX_BONUSES_ON_FIELD = 3;

    public interface GameEventListener
    {
        void OnGameEnd(boolean playerWon);
    }

    Player player;
    Enemy enemy;

    EffectEngine effectEngine;
    DodgingHero game;

    ArrayList<Bonus> bonuses;
    float gameTime;
    float lastBonusSpawnTime;

    GameEventListener eventListener;
    public GameLogic(DodgingHero _game, GameEventListener _listener) {
        eventListener = _listener;
        game = _game;
        player = new Player(
                MathUtils.random(MAX_BASE_X),
                MathUtils.random(MAX_BASE_Y),
                game.res,
                GameProgress.playerLives
        ); // 0..3

        enemy = new Enemy(game.res, this);
        effectEngine = new EffectEngine();

        bonuses = new ArrayList<Bonus>();
        gameTime = 0;
        lastBonusSpawnTime = 0;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Enemy getEnemy()
    {
        return enemy;
    }

    private void SpawnRandomBonus()
    {
        int fx = 0;
        int fy = 0;
        boolean targetNonEmpty = true;
        do {
            fx = MathUtils.random(MAX_BASE_X);
            fy = MathUtils.random(MAX_BASE_Y);
            targetNonEmpty = player.getFieldX() == fx || fy == player.getFieldY();

            for (int i = 0; i < bonuses.size() && (targetNonEmpty == false); i++)
            {
                if (bonuses.get(i).getFieldX() == fx &&
                        bonuses.get(i).getFieldY() == fy)
                {
                    targetNonEmpty = true;
                }
            }
        } while (targetNonEmpty);

        bonuses.add(Bonus.Create(fx, fy,
                MathUtils.random(3) == 0 ? Bonus.BONUS_TYPE_HEALTH : Bonus.BONUS_TYPE_ATTACK,
                game.res));
        lastBonusSpawnTime = gameTime;
    }

    public void update(float delta)
    {
        gameTime += delta;
        player.update(delta);

        if (player.getLives() > 0 && enemy.getLives() > 0) {
            effectEngine.update(delta);
            enemy.update(delta);

            if (lastBonusSpawnTime + BONUS_SPAWN_INTERVAL < gameTime &&
                    bonuses.size() < MAX_BONUSES_ON_FIELD) {
                SpawnRandomBonus();
            }
        }
    }

    public boolean CanMove(int fx, int fy)
    {
        return (fx >= 0 && fx <= MAX_BASE_X) &&
                (fy >= 0 && fy <= MAX_BASE_Y);
    }

    public void AssignPlayerPosition(int fx, int fy)
    {
        player.setFieldX(fx);
        player.setFieldY(fy);

        for (int i = bonuses.size() - 1; i >= 0; i--)
        {
            Bonus currentBonus = bonuses.get(i);
            if (currentBonus.getFieldX() == fx &&
                    currentBonus.getFieldY() == fy)
            {

                if (currentBonus.getBonusType() == Bonus.BONUS_TYPE_HEALTH)
                {
                    player.addLives(1);
                }
                else if (currentBonus.getBonusType() == Bonus.BONUS_TYPE_ATTACK)
                {
                    enemy.takeDamage(GameProgress.playerDamage);
                    if (enemy.getLives() <= 0)
                    {
                        GameProgress.currentLevel ++;
                        GameProgress.playerLives = player.getLives();
                        player.markVictorious();
                        eventListener.OnGameEnd(true);
                    }
                }

                currentBonus.release();
                bonuses.remove(i);
                break;
            }
        }
    }

    public EffectEngine getEffectEngine()
    {
        return effectEngine;
    }

    @Override
    public void OnAttack(boolean[][] tiles) {
        for (int x = 0; x < tiles.length; x++)
        {
            for (int y = 0; y < tiles[x].length; y++)
            {
                if (tiles[x][y])
                {
                    WarningEffect.Create(x, y, effectEngine, game.res, this);
                }
            }
        }
    }

    @Override
    public void OnEffectOver(WarningEffect effect) {
        if (effect.getFieldX() == player.getFieldX() &&
                effect.getFieldY() == player.getFieldY())
        {
            player.takeDamage(1);
            if (player.getLives() <= 0)
            {
                GameProgress.Reset();
            }
        }
    }

    public ArrayList<Bonus> getBonuses()
    {
        return bonuses;
    }
}
