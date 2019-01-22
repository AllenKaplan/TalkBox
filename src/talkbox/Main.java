package talkbox;

import java.io.IOException;

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
import javafx.stage.Stage;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;

public class Main extends Application  {


    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	MaryInterface marytts = new LocalMaryInterface();
    	System.out.println(marytts.getAvailableOutputTypes());
		Sentence newPhrase = new Sentence();
		
        primaryStage.setTitle("TalkBox Experiment 1");
        primaryStage.setHeight(400);
        primaryStage.setWidth(1000);

        Label label1 = new Label(" ");
        label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        
        ComboBox<String> cb1 = new ComboBox<String>(FXCollections.observableArrayList(
        		"me", "you", "they")
        );
        cb1.setPromptText("Set Subject");
        cb1.setOnAction( value -> {
        	newPhrase.addWord(new Word(Part_Of_Speech.Subject, Phrase_Type.Noun, cb1.getValue()));
        	label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        });
        
        ComboBox<String> cb2 = new ComboBox<String>(FXCollections.observableArrayList(
        		"hungry", "sleep", "run", "walk", "drive", "go")
        );
        cb2.setPromptText("Set Subject");
        cb2.setOnAction( value -> {
        	newPhrase.addWord(new Word(Part_Of_Speech.Verb, Phrase_Type.Verb, cb2.getValue()));
        	label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        });
        
        ComboBox<String> cb3 = new ComboBox<String>(FXCollections.observableArrayList(
        		"me", "you", "they", "for food", "the washroom", "home", "dank kush")
        );
        cb3.setPromptText("Set Object");
        cb3.setOnAction(value -> {
        	newPhrase.addWord(new Word(Part_Of_Speech.Object, Phrase_Type.Noun, cb3.getValue()));
        	label1.setText("\"" + newPhrase.getConstructedScentence() + "\"");
        });
        
        ToggleButton button6 = new ToggleButton("Toggle Type of Question");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
        
        HBox hbox = new HBox(cb1, cb2, cb3, button6, button7, button8, label1);

        Scene scene = new Scene(hbox, 400, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}