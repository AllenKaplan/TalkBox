package talkbox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Wavplayer extends JFrame { 
	private static final long serialVersionUID = 7526472295622776147L;  // unique id
    JButton btn = new JButton("Press to Play Sound");
    File wavFile;
    URL defaultSound;
    public static Clip clip;
    public static AudioInputStream audioInputStream;

    public Wavplayer(String url) {
        try {
            setSize(300, 100);
            setLocation(400, 300);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            JPanel jp = new JPanel();
            defaultSound = new URL (url);

            jp.add(btn);

            getContentPane().add(jp);
            pack();

            btn.addActionListener(new ActionListener() {             
                @Override
                public void actionPerformed(ActionEvent e) {
                    play();
                }
            });
        } catch (MalformedURLException ex) {
            Logger.getLogger(Wavplayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void play() {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(defaultSound);

            try {
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(20000);
                clip.start();

            } catch (LineUnavailableException e) {
            }

        } catch (UnsupportedAudioFileException | IOException e) {
        }
    }

    public void stop() {
        clip.stop();
    }

    public static void main(String args[]) {
        Wavplayer t = new Wavplayer("file:Desktop/sounds/0.mp3");
        t.setVisible(true);

    }
}