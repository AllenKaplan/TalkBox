package talkbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.text.html.ListView;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import simplenlg.features.Tense;

public class Simulator extends Application {

	// talk box interface attributes
	private List<Word> subjects = new ArrayList<Word>();
	private List<Word> objects = new ArrayList<Word>();
	private List<Word> verbs = new ArrayList<Word>();

	// SimpleNLG interface
	private MaryInterface marytts;
	private Sentence currentSentence;

	// Label
	private Label sentenceLabel;
	
	//ComboBoxes
	private List<ComboBox<Word>> verbiage;
	
	//buttons
	ToggleButton questionToggleButton;
	
	@Override
	public void start(Stage primaryStage) throws MaryConfigurationException {
		currentSentence = new Sentence();
		marytts = new LocalMaryInterface();
		verbiage = new ArrayList<ComboBox<Word>>();
		sentenceLabel = new Label();
		
		/* element generation */
		sentenceLabel.setText("\"" + currentSentence.getConstructedScentence() + "\"");

		addVerbiage("Subject", subjects);
		addVerbiage("Verb", verbs);
		addVerbiage("Object", objects);

		ToggleButton questionToggleButton = new ToggleButton("Question?");
		questionToggleButton.setOnAction(value -> {
			currentSentence.setQuestion(questionToggleButton.isSelected());
			sentenceLabel.setText("\"" + currentSentence.getConstructedScentence() + "\"");
		});

		ComboBox<String> tenseBox = new ComboBox<String>(FXCollections.observableArrayList(currentSentence.getTenses()));
		tenseBox.setPromptText("Select Tense");
		tenseBox.setOnAction(value -> {
			switch (tenseBox.getValue()) {
			case "Past":
				currentSentence.setTense(Tense.PAST);
				break;
			case "Present":
				currentSentence.setTense(Tense.PRESENT);
				break;
			case "Future":
				currentSentence.setTense(Tense.FUTURE);
				break;
			}
			sentenceLabel.setText("\"" + currentSentence.getConstructedScentence() + "\"");
		});

		Button playButton = new Button("PLAY");
		playButton.setOnAction(value -> {
			AudioInputStream audio;
			try {
				Clip clip = AudioSystem.getClip();
				audio = marytts.generateAudio(sentenceLabel.getText());
				clip.open(audio);
				clip.start();
			} catch (SynthesisException | LineUnavailableException | IOException e) {
				System.err.println("ERROR GENERATING SPEECH");
			} 
		});

		/* construct scene from elements */
		HBox verbiageBox = new HBox();
		verbiageBox.getChildren().addAll(verbiage);
		HBox selectionsBox = new HBox();
		selectionsBox.getChildren().add(questionToggleButton);
		selectionsBox.getChildren().add(tenseBox);
		selectionsBox.getChildren().add(playButton);
		//TODO: add 5 custom audio buttons
		HBox customAudioBox = new HBox();
		
		
		VBox vbox = new VBox(sentenceLabel, verbiageBox, selectionsBox);
		Scene scene = new Scene(vbox);

		primaryStage.setTitle("TalkBox TTS Prototype");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}// end start

	public void addWord(Word word) {
		switch (word.getPartOfSpeech()) {
		case Subject:
			subjects.add(word);
			break;
		case Object:
			objects.add(word);
			break;
		case Verb:
			verbs.add(word);
			break;
		default:
		}
	}
	
	private void addVerbiage(String verbiageType, List<Word> words) {
		ComboBox<Word> cb = new ComboBox<Word>(FXCollections.observableArrayList(words));
		cb.setPromptText("Set " + verbiageType);
		cb.setOnAction(value -> {
			currentSentence.addWord(cb.getValue());
			sentenceLabel.setText("\"" + currentSentence.getConstructedScentence() + "\"");
		});
		verbiage.add(cb);
	}
	
}