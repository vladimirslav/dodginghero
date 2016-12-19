package com.coldwild.dodginghero.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by comrad_gremlin on 10/20/2016.
 */

public class GameProgress {

    public static int playerLives = 3;
    public static int maxPlayerLives = 3;
    public static int playerDamage = 1;
    public static int currentLevel = 0;
    public static int currentCharacter = 0;
    public static int currentGold = 0;

    private static final String PROGRESS_SAVE_NAME = "progress";

    private static final String SAVE_KEY_LIVES = "lives";
    private static final String SAVE_KEY_LIVES_MAX = "livesmax";
    private static final String SAVE_KEY_CURRENT_LEVEL = "currentlevel";
    private static final String SAVE_KEY_PLAYER_DAMAGE = "playerdamage";
    private static final String SAVE_KEY_PLAYER_GOLD = "playergold";

    public static int getEnemyLives()
    {
        return 3 + currentLevel * 2; // 3 lives on lvl0, 5 lives on lvl1, etc
    }

    public static void Save()
    {
        Preferences prefs = Gdx.app.getPreferences(PROGRESS_SAVE_NAME);
        prefs.putInteger(SAVE_KEY_LIVES, playerLives);
        prefs.putInteger(SAVE_KEY_LIVES_MAX, maxPlayerLives);
        prefs.putInteger(SAVE_KEY_CURRENT_LEVEL, currentLevel);
        prefs.putInteger(SAVE_KEY_PLAYER_DAMAGE, playerDamage);

        prefs.putInteger(SAVE_KEY_PLAYER_GOLD, currentGold);
        prefs.flush();
    }

    public static void Load()
    {
        Preferences prefs = Gdx.app.getPreferences(PROGRESS_SAVE_NAME);
        playerLives = prefs.getInteger(SAVE_KEY_LIVES, 3);
        maxPlayerLives = prefs.getInteger(SAVE_KEY_LIVES_MAX, 3);
        currentLevel = prefs.getInteger(SAVE_KEY_CURRENT_LEVEL, 0);
        playerDamage = prefs.getInteger(SAVE_KEY_PLAYER_DAMAGE, 1);
        currentGold = prefs.getInteger(SAVE_KEY_PLAYER_GOLD, 0);
    }

    public static void Reset() {
        playerLives = 3;
        maxPlayerLives = 3;
        currentLevel = 0;
        playerDamage = 1;
    }
}
