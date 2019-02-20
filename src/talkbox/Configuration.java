package talkbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Configuration extends Application implements TalkBoxConfiguration {
	
	private static final long serialVersionUID = 5254129808184630659L;
	
	private static final Path audioPath = Paths.get("resources/AudioFiles");
	public static final int MAX_CUSTOM_AUDIO_BUTTONS = 5, NUM_OF_TOTAL_BUTTONS = 11;
	
	static Simulator sim;

	public static void main(String[] args) {
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
		Label dictionaryPathTxt = new Label("");
		Button loadDictionary = new Button("Load Custom Dictionary");
		loadDictionary.setTooltip(new Tooltip("Select path to a dictionary\nor leave blank for default"));
		loadDictionary.setOnAction(value -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select Dictionary");
			fileChooser.setInitialDirectory(Paths.get("resources/dictionaries").toFile());
			fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Text Files", "*.txt"));
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				dictionaryPathTxt.setText(selectedFile.getPath());
				primaryStage.sizeToScene();
			}
		});
		
		Label audioPathTxt = new Label("");
		Button loadAudio = new Button("Load Audio Set");
		loadAudio.setTooltip(new Tooltip("Select path to an audioset\\nor leave blank for none"));
		loadAudio.setOnAction(value -> {
			DirectoryChooser  directoryChooser  = new DirectoryChooser();
			directoryChooser.setTitle("Select Audio Set");
			directoryChooser.setInitialDirectory(Paths.get("resources/AudioFiles").toFile());
			File selectedFile = directoryChooser.showDialog(primaryStage);
			if (selectedFile != null) {
				audioPathTxt.setText(selectedFile.getPath());
				primaryStage.sizeToScene();
			}
		});

		Button launcher = new Button("Launch Sim");
		launcher.setOnAction(value -> {
			try {
				//TODO: REPLACE WITH SERIALIZATION ???
				sim = new Simulator();
				if (!dictionaryPathTxt.getText().isEmpty())
					this.loadDictionary(Paths.get(dictionaryPathTxt.getText()), sim);
				if (!audioPathTxt.getText().isEmpty())
					this.loadAudioSet(Paths.get(audioPathTxt.getText()), sim);
				sim.start(new Stage());
			} catch (Exception e) {
				System.err.println("Error Launching Simulator");
			}
		});

		HBox launcherBox = new HBox(launcher);
		HBox dictionaryBox = new HBox(loadDictionary, dictionaryPathTxt);
		HBox audioBox = new HBox(loadAudio, audioPathTxt);
		
		VBox group = new VBox(dictionaryBox, audioBox, launcherBox);
		Scene scene = new Scene(group);

		primaryStage.setTitle("TalkBox TTS Configuration");
		primaryStage.setScene(scene);
		primaryStage.setWidth(500);
		primaryStage.show();
	}
	
	private void loadDictionary(Path dictionaryPath, Simulator sim) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(dictionaryPath.toFile()));
		Part_Of_Speech pa = null;
		Phrase_Type pr = null;
		while (br.ready()) {
			String next = br.readLine().trim();
			switch (next) {
			case "SUBJECTS":
				pa = Part_Of_Speech.Subject;
				pr = Phrase_Type.Noun;
				break;
			case "OBJECTS":
				pa = Part_Of_Speech.Object;
				pr = Phrase_Type.Noun;
				break;
			case "VERBS":
				pa = Part_Of_Speech.Verb;
				pr = Phrase_Type.Verb;
				break;
			default:
				if (!next.isEmpty())
					sim.addWord(new Word(next, pa, pr)); //should the simulator be doing this?
				//maybe instead we create an arraylist of words that we can serialize?
			}
		}
		br.close();
	}
	
	private void loadAudioSet(Path audioPath, Simulator sim) {
		if (audioPath.toString().isEmpty())
			return;
		sim.audioSet = audioPath.toFile().listFiles();
	}

}
