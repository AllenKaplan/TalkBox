package talkbox;

import java.io.IOException;
import java.nio.file.Path;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.SynthesisException;

public class Main extends Application implements TalkBoxConfiguration {

	private static final long serialVersionUID = -7803131998105179478L;

	@Override
    public void start(Stage primaryStage) throws Exception {
    	
    	final String[] SUBJECTS = {
    			"me", "you", "they"
    	};
    	
    	final String[] OBJECTS = {
        		"me", "you", "they", "the washroom", "home", "the food", "a present", "dank kush"//the vs. for?
        };
    	
    	final String[] VERBS = {
        		"go", "eat", "sleep", "use", "buy", "smoke"  //go vs go to?
        };
    	
    	MaryInterface marytts = new LocalMaryInterface();
		Sentence newPhrase = new Sentence();
		
		/*element generation*/
        Label label1 = new Label(" ");
        label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        
        ComboBox<String> cb1 = new ComboBox<String>(FXCollections.observableArrayList(SUBJECTS));
        cb1.setPromptText("Set Subject");
        cb1.setOnAction( value -> {
        	newPhrase.addWord(new Word(Part_Of_Speech.Subject, Phrase_Type.Noun, cb1.getValue()));
        	label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        });
        
        ComboBox<String> cb2 = new ComboBox<String>(FXCollections.observableArrayList(VERBS));
        cb2.setPromptText("Set Subject");
        cb2.setOnAction( value -> {
        	newPhrase.addWord(new Word(Part_Of_Speech.Verb, Phrase_Type.Verb, cb2.getValue()));
        	label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        });
        
        ComboBox<String> cb3 = new ComboBox<String>(FXCollections.observableArrayList(OBJECTS));
        cb3.setPromptText("Set Object");
        cb3.setOnAction(value -> {
        	newPhrase.addWord(new Word(Part_Of_Speech.Object, Phrase_Type.Noun, cb3.getValue()));
        	label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        });
        
        ToggleButton button6 = new ToggleButton("Question?");
        button6.setOnAction(value -> {
        	newPhrase.setQuestion(button6.isSelected());
        	label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        });
        
        Button button7 = new Button("Change Tense");
        button7.setOnAction( value -> {
        	newPhrase.rotateTense();
        	label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        });
        
        Button button8 = new Button("PLAY");
        button8.setOnAction( value -> {
    		AudioInputStream audio;
			try {
				Clip clip = AudioSystem.getClip();
				audio = marytts.generateAudio(label1.getText());
	    		clip.open(audio);
	    		clip.start();
			} catch (SynthesisException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				System.err.println("No text found:\n");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
        
        /*construct scene from elements*/
        HBox hbox = new HBox(cb1, cb2, cb3, button6, button7, button8);
        VBox vbox = new VBox(label1, hbox);
        Scene scene = new Scene(vbox);
        
        primaryStage.setTitle("TalkBox TTS Prototype");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

	@Override
	public int getNumberOfAudioButtons() {
		return 0;
	}

	@Override
	public int getNumberOfAudioSets() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalNumberOfButtons() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Path getRelativePathToAudioFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] getAudioFileNames() {
		// TODO Auto-generated method stub
		return null;
	}
}