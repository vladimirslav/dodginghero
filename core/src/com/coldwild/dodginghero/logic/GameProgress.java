package com.coldwild.dodginghero.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.coldwild.dodginghero.logic.objects.CharacterRecord;

/**
 * Created by comrad_gremlin on 10/20/2016.
 */

public class GameProgress {

    public static int playerLives = 3;
    public static int currentCharacter = 0;
    public static int currentGold = 0;

    public static final int CHARACTER_PRICE = 1000;
    public static int levels[]; // level of each character, 0 = locked
    public static int stages[]; // how far the character has progressed

    private static final String PROGRESS_SAVE_NAME = "progress";

    private static final String SAVE_KEY_LIVES = "lives";
    private static final String SAVE_KEY_PLAYER_LEVEL = "playerlevel";
    private static final String SAVE_KEY_PLAYER_STAGE = "playerstage";
    private static final String SAVE_KEY_PLAYER_GOLD = "playergold";

    public static int getEnemyLives()
    {
        return 3 + stages[currentCharacter] * 2; // 3 lives on lvl0, 5 lives on lvl1, etc
    }

    public static int getEnemyDamage()
    {
        return 1 + stages[currentCharacter] / 10;
    }

    public static void Save()
    {
        Preferences prefs = Gdx.app.getPreferences(PROGRESS_SAVE_NAME);
        prefs.putInteger(SAVE_KEY_LIVES, playerLives);

        prefs.putInteger(SAVE_KEY_PLAYER_GOLD, currentGold);

        for (int i = 0; i < CharacterRecord.CHARACTERS.length; i++)
        {
            prefs.putInteger(SAVE_KEY_PLAYER_LEVEL + i, levels[i]);
            prefs.putInteger(SAVE_KEY_PLAYER_STAGE + i, stages[i]);
        }

        prefs.flush();
    }

    public static void Load()
    {
        levels = new int[CharacterRecord.CHARACTERS.length];
        stages = new int[CharacterRecord.CHARACTERS.length];

        Preferences prefs = Gdx.app.getPreferences(PROGRESS_SAVE_NAME);
        playerLives = prefs.getInteger(SAVE_KEY_LIVES, 3);
        currentGold = prefs.getInteger(SAVE_KEY_PLAYER_GOLD, 0);

        for (int i = 0; i < CharacterRecord.CHARACTERS.length; i++)
        {
            levels[i] = prefs.getInteger(SAVE_KEY_PLAYER_LEVEL + i, i == 0 ? 1 : 0);
            stages[i] = prefs.getInteger(SAVE_KEY_PLAYER_STAGE + i, 0);
        }
    }

    public static void Reset(boolean resetProgress) {
        if (resetProgress)
        {
            stages[currentCharacter] -= 5;
            if (stages[currentCharacter] < 0)
            {
                stages[currentCharacter] = 0;
            }
        }

        playerLives = getPlayerMaxHp();
    }

    public static int getNextUpgradeCost(int currentCharacter) {
        return levels[currentCharacter] * 2;
    }

    public static int getPlayerMaxHp() {
        CharacterRecord currentChar = CharacterRecord.CHARACTERS[currentCharacter];
        return currentChar.getMaxHp(levels[currentCharacter]);
    }

    public static int getPlayerDamage() {
        CharacterRecord currentChar = CharacterRecord.CHARACTERS[currentCharacter];
        return currentChar.getDmg(levels[currentCharacter]);
    }

    public static int getPlayerHealthRestored() {
        CharacterRecord currentChar = CharacterRecord.CHARACTERS[currentCharacter];
        return currentChar.getHpRestored(levels[currentCharacter]);
    }

    public static float getPlayerBonusReduction() {
        CharacterRecord currentChar = CharacterRecord.CHARACTERS[currentCharacter];
        return currentChar.getBonusSpawnReduction(levels[currentCharacter]);
    }

    public static int getBonusReductionValue() {
        CharacterRecord currentChar = CharacterRecord.CHARACTERS[currentCharacter];
        return levels[currentCharacter] / currentChar.levelsForBonusSpawnUpgrade;
    }

    public static void increaseStage() {
        currentGold += 1 + stages[currentCharacter] / 4;
        stages[currentCharacter]++;
    }
}
