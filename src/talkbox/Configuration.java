package talkbox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Configuration extends Application implements TalkBoxConfiguration {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5254129808184630659L;

	private int numOfAudioButtons, numOfAudioSets, numOfTotalButtons;
	private String[][] audioFileNames;
	private Path audioPath;
	static Simulator sim;

	public static void main(String[] args) {
		// load simulator to configure
		sim = new Simulator();

		// load default library
		FileReader in;
		try {
			in = new FileReader("resources/dictionaries/StandardDictionary"); // TODO: replace this with audioPath Path
																				// Object
			BufferedReader br = new BufferedReader(in);
			String line;
			String category = null;
			while (br.ready()) {
				line = br.readLine();

				if (line.equals("SUBJECTS")) {
					category = "SUBJECT";
				} else if (line.equals("OBJECTS")) {
					category = "OBJECT";
				} else if (line.equals("VERBS")) {
					category = "VERB";
				} else if (line.length() == 0) {
					// do nothing?
				} else {
					switch (category) {
					case "SUBJECT":
						sim.addSubject(line);
						break;
					case "OBJECT":
						sim.addObject(line);
						break;
					case "VERB":
						sim.addVerb(line);
						break;
					}
				}
			}
			br.close();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		// launch configurator
		Application.launch(args);
	}

	@Override
	public int getNumberOfAudioButtons() {
		return this.numOfAudioButtons;
	}

	@Override
	public int getNumberOfAudioSets() {
		// TODO Auto-generated method stub
		return this.numOfAudioSets;
	}

	@Override
	public int getTotalNumberOfButtons() {
		// TODO Auto-generated method stub
		return this.numOfTotalButtons;
	}

	@Override
	public Path getRelativePathToAudioFiles() {
		// TODO Auto-generated method stub
		return this.audioPath;
	}

	@Override
	public String[][] getAudioFileNames() {
		// TODO Auto-generated method stub
		return audioFileNames;
	}

	public static long choose(long total, long choose) {
		if (total < choose)
			return 0;
		if (choose == 0 || choose == total)
			return 1;
		return choose(total - 1, choose - 1) + choose(total - 1, choose);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		numOfAudioButtons = 1;
		// numOfAudioSets= (int)
		// (this.choose(subjects.size(),1)*this.choose(objects.size(),1)*this.choose(verbs.size(),1));
		numOfTotalButtons = 6;
		audioPath = Paths.get("/audiofiles");
		audioFileNames = null;

		Label label1 = new Label("Word:");
		TextField textField = new TextField();
		HBox hb = new HBox(label1, textField);
		hb.setSpacing(20);

		Button addSubject = new Button("Add Subject");
		addSubject.setOnAction(value -> {
			if (!textField.getText().trim().isEmpty())
				sim.addSubject(textField.getText());
		});

		Button addObject = new Button("Add Object");
		addObject.setOnAction(value -> {
			if (!textField.getText().trim().isEmpty())
				sim.addObject(textField.getText());
		});

		Button addVerb = new Button("Add Verb");
		addVerb.setOnAction(value -> {
			if (!textField.getText().trim().isEmpty())
				sim.addVerb(textField.getText());
		});

		Button launcher = new Button("Launch Sim");
		launcher.setOnAction(value -> {
			try {
				sim.start(primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		HBox hbox = new HBox(launcher, addSubject, addObject, addVerb);
		VBox vbox = new VBox(hbox, hb);
		Scene scene = new Scene(vbox);

		primaryStage.setTitle("TalkBox TTS Configuration");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();

	}

}
