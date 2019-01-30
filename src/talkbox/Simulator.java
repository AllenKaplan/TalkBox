package talkbox;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;

public class Simulator extends Application{
	
	//talk box interface attributes
	List<String> subjects = new ArrayList<String>();
	List<String> objects = new ArrayList<String>();
	List<String> verbs = new ArrayList<String>();
	
	//SimpleNLG interface
	private static MaryInterface marytts;
	private static Sentence newPhrase;
	
	@Override
    public void start(Stage primaryStage) throws Exception {
		newPhrase = new Sentence();
		marytts = new LocalMaryInterface();
		
		/*element generation*/
        Label label1 = new Label();
        label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        
        ComboBox<String> cb1 = new ComboBox<String>(FXCollections.observableArrayList(subjects));
        cb1.setPromptText("Set Subject");
        cb1.setOnAction( value -> {
        	newPhrase.addWord(new Word(Part_Of_Speech.Subject, Phrase_Type.Noun, cb1.getValue()));
        	label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        });
        
        ComboBox<String> cb2 = new ComboBox<String>(FXCollections.observableArrayList(verbs));
        cb2.setPromptText("Set Verb");
        cb2.setOnAction( value -> {
        	newPhrase.addWord(new Word(Part_Of_Speech.Verb, Phrase_Type.Verb, cb2.getValue()));
        	label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        });
        
        ComboBox<String> cb3 = new ComboBox<String>(FXCollections.observableArrayList(objects));
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
    }//end start

	
	public void addSubject(String word) {
		subjects.add(word);
	}
	
	public void addObject(String word) {
		objects.add(word);
	}
	
	public void addVerb(String word) {
		verbs.add(word);
	}
}