package com.coldwild.dodginghero.graph;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.coldwild.dodginghero.Resources;

/**
 * Created by comrad_gremlin on 9/7/2016.
 */
public class SizeEvaluator {
    private Stage measuredStage;
    private Resources resources;

    public static final int BASE_MARGIN = 3;

    // 4x4
    private final int maxTileBaseX;
    private final int maxTileBaseY;

    public SizeEvaluator(Stage _stage, Resources _res, int maxBaseX, int maxBaseY)
    {
        measuredStage = _stage;
        resources = _res;
        maxTileBaseX = maxBaseX;
        maxTileBaseY = maxBaseY;
    }

    // 4x4
    // x (0 -> 3), 0 at the left, maxTileBaseX at the right
    // y (0 -> 3), 0 at the bottom, maxTileBaseY at the top

    public float getBaseScreenX(int baseX) // 0 .. 3
    {
        return measuredStage.getWidth() / 2
                - (resources.TILE_SIZE + BASE_MARGIN)
                * (maxTileBaseX + 1 - baseX);
        // TILE SIZE = 16 px;
        // baseX = 0
    }

    public float getBaseScreenY(int baseY)
    {
        return measuredStage.getHeight() / 2
                - ((resources.TILE_SIZE + BASE_MARGIN) * 2 / 3)
                * ((maxTileBaseY + 1) / 2 - baseY);
    }

    public float getEnemyX(Sprite enemy)
    {
        return (measuredStage.getWidth() * 3 / 4) - enemy.getWidth() / 2;
    }

    public float getEnemyY(Sprite enemy)
    {
        return measuredStage.getHeight() / 2 - enemy.getHeight() / 2;
    }

}
