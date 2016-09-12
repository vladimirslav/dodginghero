package com.coldwild.dodginghero.logic;

import com.badlogic.gdx.math.MathUtils;
import com.coldwild.dodginghero.DodgingHero;
import com.coldwild.dodginghero.graph.effects.EffectEngine;
import com.coldwild.dodginghero.logic.objects.Enemy;
import com.coldwild.dodginghero.logic.objects.Player;

/**
 * Created by comrad_gremlin on 9/8/2016.
 */
public class GameLogic {

    public static final int MAX_BASE_X = 3;
    public static final int MAX_BASE_Y = 3;

    Player player;
    Enemy enemy;

    EffectEngine effectEngine;
    DodgingHero game;

    public GameLogic(DodgingHero _game) {
        game = _game;
        player = new Player(
                MathUtils.random(MAX_BASE_X),
                MathUtils.random(MAX_BASE_Y),
                game.res
        ); // 0..3

        enemy = new Enemy(game.res);
        effectEngine = new EffectEngine();
    }

    public Player getPlayer()
    {
        return player;
    }

    public Enemy getEnemy()
    {
        return enemy;
    }

    public void update(float delta)
    {
        effectEngine.update(delta);
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
    }

    public EffectEngine getEffectEngine()
    {
        return effectEngine;
    }
}
