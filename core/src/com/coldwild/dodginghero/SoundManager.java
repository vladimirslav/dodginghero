package com.coldwild.dodginghero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by comrad_gremlin on 12/28/2016.
 */

public class SoundManager {
    /*
        swing.wav -> swing0.ogg
        swing2.wav -> swing1.ogg
        swing3.wav -> swing2.ogg
        coin.wav -> coin.ogg
        bite-small.wav -> heal.ogg
        fantozzi-sandl1 -> walk0.ogg
        fantozzi-sandl2 -> walk1.ogg
        Fantozzi-SandR1 -> walk2.ogg

        Attack sounds: http://opengameart.org/content/rpg-sound-pack by artisticdude
        Battle Music: http://opengameart.org/content/8-bit-music-pack-loopable#comment-55568 by CodeManu
        Footsteps: http://opengameart.org/content/fantozzis-footsteps-grasssand-stone by Fantozzi (submitted by qubodup)
     */

    public static AssetManager assets = new AssetManager();

    public static void LoadSounds()
    {
        for (int i = 0; i < 3; i++)
        {
            assets.load(Gdx.files.internal("music/swing" + i + ".ogg").path(), Sound.class);
            assets.load(Gdx.files.internal("music/walk" + i + ".ogg").path(), Sound.class);
        }

        assets.load(Gdx.files.internal("music/coin.ogg").path(), Sound.class);
        assets.load(Gdx.files.internal("music/heal.ogg").path(), Sound.class);

        assets.finishLoading();
    }

    public static void ReleaseSounds()
    {
        assets.dispose();
    }

    private static void playSoundRandomVolume(Sound sound, float min, float max)
    {
        if (sound != null)
        {
            sound.play(MathUtils.random(min, max));
        }
    }

    public static void PlaySwingSound()
    {
        Sound s = assets.get("music/swing" + MathUtils.random(2) + ".ogg", Sound.class);
        playSoundRandomVolume(s, 0.9f, 1.0f);
    }

    public static void PlayWalkSound()
    {
        Sound s = assets.get("music/walk" + MathUtils.random(2) + ".ogg", Sound.class);
        playSoundRandomVolume(s, 0.5f, 0.6f);
    }

    public static void PlayCoinSound()
    {
        Sound coin = assets.get("music/coin.ogg", Sound.class);
        playSoundRandomVolume(coin, 0.9f, 1.0f);
    }

    public static void PlayHealSound()
    {
        Sound heal = assets.get("music/heal.ogg", Sound.class);
        playSoundRandomVolume(heal, 0.9f, 1.0f);
    }
}
