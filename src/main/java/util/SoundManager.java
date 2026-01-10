package main.java.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class SoundManager {
    public enum SoundId {REVEAL, SELECTION, MINE, WIN, LOSE, BLOCK, POINTS_WIN, POINTS_LOSE}

    private static final SoundManager INSTANCE = new SoundManager();
    public static SoundManager getInstance() { return INSTANCE; }

    private final Map<SoundId, Clip> clips = new EnumMap<>(SoundId.class);

    private SoundManager() {}

    public void preload() {
        load(SoundId.REVEAL, "/sounds/reveal.wav");
        load(SoundId.SELECTION, "/sounds/selection.wav");
        load(SoundId.MINE, "/sounds/mine.wav");
        load(SoundId.WIN, "/sounds/win.wav");
        load(SoundId.LOSE, "/sounds/lose.wav");
        load(SoundId.BLOCK, "/sounds/block.wav");
        load(SoundId.POINTS_WIN, "/sounds/points-win.wav");
        load(SoundId.POINTS_LOSE, "/sounds/points-lose.wav");

    }

    private void load(SoundId id, String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url == null) throw new IllegalArgumentException("Missing: " + resourcePath);

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clips.put(id, clip);
        } catch (Exception e) {
            System.err.println("Failed to load sound " + id + ": " + e.getMessage());
        }
    }

    public void playOnce(SoundId id) {
        Clip c = clips.get(id);
        if (c == null) return;

        c.stop();              // Stop if playing
        c.flush();             // Clear the buffer
        c.setFramePosition(0); // Rewind
        c.start();             // Play
    }
}
