package main.java.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class SoundManager {
    public enum SoundId {
        REVEAL, SELECTION, MINE, WIN, LOSE, BLOCK, POINTS_WIN, POINTS_LOSE
    }

    private static final SoundManager INSTANCE = new SoundManager();

    public static SoundManager getInstance() {
        return INSTANCE;
    }

    private final Map<SoundId, Clip> clips = new EnumMap<>(SoundId.class);

    private SoundManager() {
        updateVolume();
    }

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
            if (url == null)
                throw new IllegalArgumentException("Missing: " + resourcePath);

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
        if (c == null)
            return;

        c.stop(); // Stop if playing
        c.flush(); // Clear the buffer
        c.setFramePosition(0); // Rewind
        c.start(); // Play
    }

    private boolean muted = false;

    public boolean isMuted() {
        return muted;
    }

    public void toggleMute() {
        muted = !muted;
        updateVolume();
    }

    private void updateVolume() {
        if (backgroundMusic != null && backgroundMusic.isOpen()) {
            try {
                javax.sound.sampled.FloatControl gainControl = (javax.sound.sampled.FloatControl) backgroundMusic
                        .getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN);
                if (muted) {
                    gainControl.setValue(gainControl.getMinimum());
                } else {
                    float targetGain = 0.0f;
                    // original music too loud need to reduce the volume
                            
                    if (currentMusicPath != null && (currentMusicPath.contains("76 Load Game.wav") || currentMusicPath.contains("62 Mines (Star Lumpy).wav"))) {
                        targetGain = -25.0f; // Reduce by 25 dB
                    }
                    gainControl.setValue(targetGain);
                }
            } catch (Exception e) {
                System.err.println("Volume control not supported: " + e.getMessage());
            }
        }
    }

    private Clip backgroundMusic;
    private String currentMusicPath;

    public void playBackgroundMusic(String resourcePath) {
        // If the requested music is already playing, do nothing
        if (backgroundMusic != null && backgroundMusic.isRunning() && resourcePath.equals(currentMusicPath)) {
            return;
        }

        stopBackgroundMusic(); // Stop any existing music
        try {
            URL url = getClass().getResource(resourcePath);
            if (url == null) {
                System.err.println("Background music not found: " + resourcePath);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(ais);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Loop forever
            backgroundMusic.start();
            currentMusicPath = resourcePath;
            updateVolume();
        } catch (Exception e) {
            System.err.println("Failed to play background music: " + e.getMessage());
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            if (backgroundMusic.isRunning()) {
                backgroundMusic.stop();
            }
            backgroundMusic.close();
            backgroundMusic = null;
        }
        currentMusicPath = null;
    }
}
