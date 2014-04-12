package controllers;

import config.GameConfig;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Play awesome pirate metal music while coding
 *
 * @note must have !
 * @note singleton
 * @author freaxmind
 */
public class MusicController {

    private List<File> musicFiles;
    private static MusicController instance = null;

    /**
     * Initialize the list of music file (random order to avoid repetition)
     */
    private MusicController() {
        File musicDir = new File(GameConfig.MUSIC_DIR);
        this.musicFiles = Arrays.asList(musicDir.listFiles());
        Collections.shuffle(this.musicFiles);
    }

    /**
     * Singleton method
     *
     * @return unique instance of controller
     */
    public static MusicController getInstance() {
        if (instance == null) {
            instance = new MusicController();
        }

        return instance;
    }

    /**
     * Play songs until the end of time
     */
    public void run() {
        // don't run if the list is empty
        if (this.musicFiles.isEmpty()) {
            Logger.getLogger("").log(Level.WARNING, "Aucun morceau de musique dans la collection");
            return;
        }

        // wait 1 sec before the music start (less aggresiv)
        try {

            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            // WHO CARES ABOUT THIS EXCEPTION !
            Logger.getLogger("").log(Level.WARNING, ex.toString());
        }

        try {
            Clip clip = AudioSystem.getClip();

            while (true) {      // until the end of time
                for (File music : this.musicFiles) {
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(music);
                    clip.open(inputStream);
                    clip.start();   // asynchronous

                    // wait until the end of the song
                    do {
                        Thread.sleep(1000);
                    } while (clip.isRunning());

                    clip.close();
                }
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException ex) {
            Logger.getLogger("").log(Level.WARNING, "Impossible de jouer de la musique: {0}", ex);
        }
    }
}
