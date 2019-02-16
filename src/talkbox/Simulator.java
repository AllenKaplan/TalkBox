package talkbox;

import java.io.IOException;
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
import marytts.exceptions.SynthesisException;
import simplenlg.features.Tense;

public class Simulator extends Application {

	// talk box interface attributes
	List<String> subjects = new ArrayList<String>();
	List<String> objects = new ArrayList<String>();
	List<String> verbs = new ArrayList<String>();

	// SimpleNLG interface
	private static MaryInterface marytts;
	private static Sentence newPhrase;

	// Label
	Label currentSentance;
	List<ComboBox> verbiage = new ArrayList<ComboBox>();;

	@Override
	public void start(Stage primaryStage) throws Exception {
		newPhrase = new Sentence();
		marytts = new LocalMaryInterface();

		// replace w/ dictionary
		subjects.addAll(Arrays.asList("me", "you", "they"));
		objects.addAll(Arrays.asList("the washroom", "home", "the food", "a present"));
		verbs.addAll(Arrays.asList("go", "eat", "sleep", "use", "buy", "refuel"));

		/* element generation */
		currentSentance = new Label(" ");
		currentSentance.setText("\"" + newPhrase.getConstructedScentence() + "\"");

		addVerbiage("Subject", subjects, Part_Of_Speech.Subject, Phrase_Type.Noun);
		addVerbiage("Verb", verbs, Part_Of_Speech.Verb, Phrase_Type.Verb);
		addVerbiage("Object", objects, Part_Of_Speech.Object, Phrase_Type.Noun);

		ToggleButton questionToggleButton = new ToggleButton("Question?");
		questionToggleButton.setOnAction(value -> {
			newPhrase.setQuestion(questionToggleButton.isSelected());
			currentSentance.setText("\"" + newPhrase.getConstructedScentence() + "\"");
		});

		ComboBox<String> tenseBox = new ComboBox<String>(FXCollections.observableArrayList(newPhrase.getTenses()));
		tenseBox.setPromptText("Select Tense");
		tenseBox.setOnAction(value -> {
			switch (tenseBox.getValue()) {
			case "Past":
				newPhrase.setTense(Tense.PAST);
				break;
			case "Present":
				newPhrase.setTense(Tense.PRESENT);
				break;
			case "Future":
				newPhrase.setTense(Tense.FUTURE);
				break;
			}
			currentSentance.setText("\"" + newPhrase.getConstructedScentence() + "\"");
		});

		Button playButton = new Button("PLAY");
		playButton.setOnAction(value -> {
			AudioInputStream audio;
			try {
				Clip clip = AudioSystem.getClip();
				audio = marytts.generateAudio(currentSentance.getText());
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

		/* construct scene from elements */
		HBox verbiageBox = new HBox();
		verbiageBox.getChildren().addAll(verbiage);
		HBox selectionsBox = new HBox();
		selectionsBox.getChildren().add(questionToggleButton);
		selectionsBox.getChildren().add(tenseBox);
		selectionsBox.getChildren().add(playButton);
		VBox vbox = new VBox(currentSentance, verbiageBox, selectionsBox);
		Scene scene = new Scene(vbox);

		primaryStage.setTitle("TalkBox TTS Prototype");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}// end start

	public void addSubject(String word) {
		subjects.add(word);
	}

	public void addObject(String word) {
		objects.add(word);
	}

	public void addVerb(String word) {
		verbs.add(word);
	}

	void addVerbiage(String verbiageType, List<String> words, Part_Of_Speech partOfSpeech, Phrase_Type phraseType) {
		ComboBox<String> cb = new ComboBox<String>(FXCollections.observableArrayList(words));
		cb.setPromptText("Set " + verbiageType);
		cb.setOnAction(value -> {
			newPhrase.addWord(new Word(partOfSpeech, phraseType, cb.getValue()));
			currentSentance.setText("\"" + newPhrase.getConstructedScentence() + "\"");
		});
		verbiage.add(cb);
	}
}