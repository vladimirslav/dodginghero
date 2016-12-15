package com.coldwild.dodginghero.logic.objects;

/**
 * Created by comrad_gremlin on 12/15/2016.
 */

public class CharacterRecord {

    public final int levelsForHpUpgrade;
    public final int levelsForHpRegenUpgrade;
    public final int levelsForAttackUpgrade;
    public final int levelsForBonusSpawnUpgrade;

    public final String name;

    public CharacterRecord(int lvlHp, int lvlRegen, int lvlAttack, int lvlBonus, String _name)
    {
        levelsForHpUpgrade = lvlHp;
        levelsForHpRegenUpgrade = lvlRegen;
        levelsForAttackUpgrade = lvlAttack;
        levelsForBonusSpawnUpgrade = lvlBonus;
        name = _name;
    }

    public static String CHAR_NAME_HUMAN = "Human";
    public static String CHAR_NAME_SPIDER = "Spider";
    public static String CHAR_NAME_SKELETON = "Mr.Skeletal";
    public static String CHAR_NAME_GHOST = "Ghost";
    public static String CHAR_NAME_SLIME = "Slimey";

    public static CharacterRecord CHARACTERS[] = {
        new CharacterRecord(2, 2, 4, 4, CHAR_NAME_HUMAN),
        new CharacterRecord(3, 6, 3, 3, CHAR_NAME_SPIDER),
        new CharacterRecord(6, 12, 1, 3, CHAR_NAME_SKELETON),
        new CharacterRecord(4, 4, 2, 4, CHAR_NAME_GHOST),
        new CharacterRecord(3, 3, 4, 1, CHAR_NAME_SLIME)
    };
}
