package talkbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Configuration extends Application implements TalkBoxConfiguration {
	
	private static final long serialVersionUID = 5254129808184630659L;
	
	private static final Path audioPath = Paths.get("resources/AudioFiles");
	private static final Path dictionaryPath = Paths.get("resources/dictionaries/StandardDictionary");
	private static final int MAX_CUSTOM_AUDIO_BUTTONS = 5, NUM_OF_TOTAL_BUTTONS = 11;
	
	static Simulator sim;

	public static void main(String[] args) {
		// load simulator to configure
		sim = new Simulator();
		
		Application.launch(args);
	}

	@Override
	public int getNumberOfAudioButtons() {
		return Configuration.MAX_CUSTOM_AUDIO_BUTTONS;
	}

	@Override
	public int getNumberOfAudioSets() {
		return this.getRelativePathToAudioFiles().toFile().listFiles().length;
	}

	@Override
	public int getTotalNumberOfButtons() {
		return Configuration.NUM_OF_TOTAL_BUTTONS;
	}

	@Override
	public Path getRelativePathToAudioFiles() {
		return Configuration.audioPath;
	}

	@Override
	public String[][] getAudioFileNames() {
		File[] files =  this.getRelativePathToAudioFiles().toFile().listFiles();
		String[][] names = new String[this.getNumberOfAudioSets()][this.getNumberOfAudioButtons()];
		int i = 0, j = 0;
		for (File d : files) {
			if (d.isDirectory()) 
				for (File f : d.listFiles()) 
					if (f.isFile())
						names[i][j++] = f.getName();
			i++;
			j = 0;
		}
		return names;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Label label1 = new Label("Word:");
		Label label2 = new Label("");
		TextField textField = new TextField();
		HBox hb = new HBox(label1, textField, label2);
		hb.setSpacing(20);

		Button addSubject = new Button("Add Subject");
		addSubject.setOnAction(value -> {
			if (!textField.getText().trim().isEmpty())
				sim.addWord(new Word(textField.getText(), Part_Of_Speech.Subject, Phrase_Type.Noun));
		});

		Button addObject = new Button("Add Object");
		addObject.setOnAction(value -> {
			if (!textField.getText().trim().isEmpty())
				sim.addWord(new Word(textField.getText(), Part_Of_Speech.Object, Phrase_Type.Noun));
		});

		Button addVerb = new Button("Add Verb");
		addVerb.setOnAction(value -> {
			if (!textField.getText().trim().isEmpty())
				sim.addWord(new Word(textField.getText(), Part_Of_Speech.Verb, Phrase_Type.Verb));
		});
		
		Button loadDictionary = new Button("Load Dictionary");
		loadDictionary.setTooltip(new Tooltip("Enter the path to a dictionary\nor leave blank for default"));
		loadDictionary.setOnAction(value -> {
			if (textField.getText().trim().isEmpty())
				try {
					this.loadDictionary(dictionaryPath, sim);
					label2.setText("DICTIONARY LOADED");
				} catch (IOException e) {
					label2.setText("DICTIONARY NOT FOUND");
				}
		});

		Button launcher = new Button("Launch Sim");
		launcher.setOnAction(value -> {
			try {
				sim.start(primaryStage);
			} catch (Exception e) {
				System.err.println("Error Launching Simulator");
			}
		});

		HBox hbox = new HBox(launcher, addSubject, addObject, addVerb, loadDictionary);
		VBox vbox = new VBox(hbox, hb);
		Scene scene = new Scene(vbox);

		primaryStage.setTitle("TalkBox TTS Configuration");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}
	
	private void loadDictionary(Path dictionaryPath, Simulator sim) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(dictionaryPath.toFile()));
		Part_Of_Speech p = null;
		while (br.ready()) {
			String next = br.readLine();
			switch (next) {
			case "SUBJECTS":
				p = Part_Of_Speech.Subject;
				break;
			case "OBJECTS":
				p = Part_Of_Speech.Object;
				break;
			case "VERBS":
				p = Part_Of_Speech.Verb;
				break;
			default:
				if (!next.isEmpty())
					sim.addWord(new Word(next, p, Phrase_Type.Noun));
			}
		}
		br.close();
	}

}
