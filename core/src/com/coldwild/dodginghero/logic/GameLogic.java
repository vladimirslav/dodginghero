package com.coldwild.dodginghero.logic;

import com.badlogic.gdx.math.MathUtils;
import com.coldwild.dodginghero.logic.objects.Player;

/**
 * Created by comrad_gremlin on 9/8/2016.
 */
public class GameLogic {

    public static final int MAX_BASE_X = 3;
    public static final int MAX_BASE_Y = 3;

    Player player;

    public GameLogic() {
        player = new Player(
                MathUtils.random(MAX_BASE_X),
                MathUtils.random(MAX_BASE_Y)
        ); // 0..3
    }

    public Player getPlayer()
    {
        return player;
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
}
