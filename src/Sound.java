import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

// **SOUND CLASS**: WILL BE USED TO ADD SOUND INTO GAME
public class Sound {

    private Clip clip;

    // CONSTRUCTOR
    public Sound(String filePath) {
        
        AudioInputStream audioStream = null;
        try {
            // Try loading from classpath (recommended when resources are inside jar/bin)
            URL resource = getClass().getClassLoader().getResource(filePath);
            if (resource != null) {
                audioStream = AudioSystem.getAudioInputStream(resource);
            } 
            else {
                // Fallback to file system path
                File audioFile = new File(filePath);
                audioStream = AudioSystem.getAudioInputStream(audioFile);
            }

            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } 
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        } 
        finally {
            // Close the stream if it's not needed anymore. Clip.open usually reads the stream fully,
            // but ensure we don't leak the stream handle.
            if (audioStream != null) {
                try {
                    audioStream.close();
                } 
                catch (IOException ignored) {
                }
            }
        }
    }
    // END OF CONSTRUCTOR


    // Play method
    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {     // Stop playing if already playing
                clip.stop();
            }
            clip.setFramePosition(0);   // Rewind to the beginning
            clip.start();
        }
    }

    // Stop playing method
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    // Loop audio method
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
