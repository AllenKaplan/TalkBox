package Simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
import simplenlg.features.Tense;

import talkbox.*;

@SuppressWarnings("restriction")
public class Simulator extends Application {

	// talk box interface attributes
	private List<Word> subjects = new ArrayList<Word>();
	private List<Word> objects = new ArrayList<Word>();
	private List<Word> verbs = new ArrayList<Word>();
	
	//custom audio sets
	private File[] audioSet;
	private List<Button> custom;

	// SimpleNLG interface
	private MaryInterface marytts;
	private Sentence currentSentence;

	// Label
	private Label sentenceLabel;
	
	//ComboBoxes
	private List<ComboBox<Word>> verbiage;
	
	//current clip playing
	private Clip currentClip;
	
	@Override
	public void init() {
		currentSentence = new Sentence();
		verbiage = new ArrayList<ComboBox<Word>>();
		custom = new ArrayList<Button>();
		sentenceLabel = new Label();
		try {
			marytts = new LocalMaryInterface();
			currentClip = AudioSystem.getClip();
			currentClip.addLineListener(event -> {
				if(LineEvent.Type.STOP.equals(event.getType())) {
					currentClip.close();
				}
			});
		}
		catch (MaryConfigurationException | LineUnavailableException e) {
			System.err.println("ERROR GENERATING AUDIO PLAYBACK");
		}
		try {
			FileInputStream file = new FileInputStream("TalkBoxData/Settings/settings.tbc");
			ObjectInputStream in = new ObjectInputStream(file); 
			Settings settings = (Settings) in.readObject();
			file.close();
			in.close();
			
			if (settings.getRelativePathToAudioFiles() != null)
				this.audioSet = settings.getRelativePathToAudioFiles().toFile().listFiles();
			else
				this.audioSet = new File[0];
			
			if (settings.getDictionaryPath() != null)
				this.loadDictionary(settings.getDictionaryPath());
		}
		catch (IOException | ClassNotFoundException e) {
			System.err.println("ERROR LOADING SETTINGS\nLOADING DEFUALT SETTINGS ...");
			e.printStackTrace();
			this.audioSet = new File[0];
		}
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws MaryConfigurationException, LineUnavailableException {
		sentenceLabel.setText("\"" + currentSentence.getConstructedScentence() + "\"");

		addVerbiage("Subject", subjects);
		addVerbiage("Verb", verbs);
		addVerbiage("Object", objects);
		
		for (File f : audioSet) {
			addAudioButton(f);
		}

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
			try {
				if (currentClip.isOpen())
					currentClip.close();
				currentClip.open(marytts.generateAudio(sentenceLabel.getText()));
				currentClip.start();
			} catch (SynthesisException | LineUnavailableException | IOException e) {
				System.err.println("ERROR GENERATING SPEECH");
			} 
		});
		
		Button launch = new Button("Launch Configurator");
		launch.setOnAction(value -> {
			try {
				@SuppressWarnings("unused")
				Process proc = Runtime.getRuntime().exec("java -jar Configurator.jar");
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		});

		/* construct scene from elements */
		HBox verbiageBox = new HBox();
		verbiageBox.getChildren().addAll(verbiage);
		HBox customAudioBox = new HBox();
		customAudioBox.getChildren().addAll(custom);
		HBox selectionsBox = new HBox();
		selectionsBox.getChildren().add(questionToggleButton);
		selectionsBox.getChildren().add(tenseBox);
		selectionsBox.getChildren().add(playButton);
		
		VBox vbox = new VBox(sentenceLabel, verbiageBox, selectionsBox, customAudioBox, launch);
		Scene scene = new Scene(vbox);

		primaryStage.setTitle("TalkBox TTS Prototype");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}// end start
	
	public void addAudioButton(File audio) {
		Button customAudio = new Button(audio.getName().substring(0, audio.getName().indexOf('.')));
		customAudio.setOnAction(value -> {
			try {
				if (currentClip.isOpen())
					currentClip.close();
				currentClip.open(AudioSystem.getAudioInputStream(audio));
				currentClip.start();
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
				System.err.println("PLAYBACK ERROR");
			} 
		});
		custom.add(customAudio);
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
	
	private void loadDictionary(Path dictionaryPath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(dictionaryPath.toFile()));
		while (br.ready()) {
			String next = br.readLine().trim();
			switch (next) {
			case "SUBJECTS":
				while (br.ready() && !(next = br.readLine().trim()).isEmpty())
					subjects.add(new Word(next, Part_Of_Speech.Subject, Phrase_Type.Noun));
				break;
			case "OBJECTS":
				while (br.ready() && !(next = br.readLine().trim()).isEmpty())
					objects.add(new Word(next, Part_Of_Speech.Object, Phrase_Type.Noun));
				break;
			case "VERBS":
				while (br.ready() && !(next = br.readLine().trim()).isEmpty())
					verbs.add(new Word(next, Part_Of_Speech.Verb, Phrase_Type.Verb));
				break;
			case "INDIRECT_OBJECT":
				while (br.ready() && !(next = br.readLine().trim()).isEmpty())
					verbs.add(new Word(next, Part_Of_Speech.Indirect_Object, Phrase_Type.Noun));
				break;
			case "COMPLIMENTS":
				while (br.ready() && !(next = br.readLine().trim()).isEmpty())
					verbs.add(new Word(next, Part_Of_Speech.Compliment, Phrase_Type.Adjective));
				break;
			default:
			}
		}
		br.close();
	}
	
}