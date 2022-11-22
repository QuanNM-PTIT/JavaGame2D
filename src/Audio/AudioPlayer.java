package Audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class AudioPlayer
{
    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LEVEL_COMPLETED = 3;
    public static int ATTACK_1 = 4;
    public static int ATTACK_2 = 5;
    public static int ATTACK_3 = 6;

    private ArrayList<Clip> songs, effects;
    private int curSongId;
    private float volume = 1f;
    private boolean songMute, effectMute;
    private Random random = new Random();

    public AudioPlayer()
    {
        loadSong();
        loadEffect();
        playSong(MENU_1);
    }

    private void loadSong()
    {
        ArrayList<String> names = new ArrayList<>();
        names.add("menu");
        names.add("level1");
        names.add("level2");
        songs = new ArrayList<>();
        for (int i = 0; i < names.size(); ++i)
            songs.add(getClip(names.get(i)));
    }

    private void loadEffect()
    {
        ArrayList<String> effectsName = new ArrayList<>();
        effectsName.add("die");
        effectsName.add("jump");
        effectsName.add("gameover");
        effectsName.add("lvlcompleted");
        effectsName.add("attack1");
        effectsName.add("attack2");
        effectsName.add("attack3");
        effects = new ArrayList<>();
        for (int i = 0; i < effectsName.size(); ++i)
            effects.add(getClip(effectsName.get(i)));
        updateEffectsVolume();
    }

    private Clip getClip(String name)
    {
        URL url = getClass().getResource("/Audios/" + name + ".wav");
        AudioInputStream audio;

        try
        {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setVolume(float volume)
    {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    public void stopSong()
    {
        if (songs.get(curSongId).isActive())
            songs.get(curSongId).stop();
    }

    public void setLevelSong(int lvlIdx)
    {
        if (lvlIdx % 2 == 0)
            playSong(LEVEL_1);
        else
            playSong(LEVEL_2);
    }

    public void levelCompleted()
    {
        stopSong();
        playEffect(LEVEL_COMPLETED);
    }

    public void playAttackSound()
    {
        int start = 4;
        start += random.nextInt(3);
        playEffect(start);
    }

    public void playEffect(int effect)
    {
        effects.get(effect).setMicrosecondPosition(0);
        effects.get(effect).start();
    }

    public void playSong(int song)
    {
        stopSong();
        curSongId = song;
        updateSongVolume();
        songs.get(curSongId).setMicrosecondPosition(0);
        songs.get(curSongId).loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void toggleSongMute()
    {
        this.songMute = !songMute;
        for (Clip i : songs)
        {
            BooleanControl booleanControl = (BooleanControl) i.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void toggleEffectMute()
    {
        this.effectMute = !effectMute;
        for (Clip i : effects)
        {
            BooleanControl booleanControl = (BooleanControl) i.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute)
            playEffect(JUMP);
    }

    private void updateSongVolume()
    {
        FloatControl gainControl = (FloatControl) songs.get(curSongId).getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    private void updateEffectsVolume()
    {
        for (Clip i : effects)
        {
            FloatControl gainControl = (FloatControl) i.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}
