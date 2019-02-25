package Configurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import talkbox.*;

public class Configurator extends Application {
	
	private Label dictionaryPathTxt;
	private Label audioPathTxt;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void init() {
		dictionaryPathTxt = new Label("");
		audioPathTxt = new Label("");
		try {
			FileInputStream file = new FileInputStream("TalkBoxData/Settings/settings.tbc");
			ObjectInputStream in = new ObjectInputStream(file); 
			Settings settings = (Settings) in.readObject();
			file.close();
			in.close();
			
			if (settings.getDictionaryPath() != null)
				dictionaryPathTxt = new Label(settings.getDictionaryPath().toString());
			
			if (settings.getRelativePathToAudioFiles() != null)
				audioPathTxt = new Label(settings.getRelativePathToAudioFiles().toString());
		}
		catch (IOException | ClassNotFoundException e) {
			System.err.println("ERROR LOADING PREVIOUS SETTINGS");
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Label saveTxt = new Label("");
		
		Button loadDictionary = new Button("Load Custom Dictionary");
		loadDictionary.setTooltip(new Tooltip("Select path to a dictionary\nor leave blank for default"));
		loadDictionary.setOnAction(value -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select Dictionary");
			fileChooser.setInitialDirectory(Paths.get("TalkBoxData/Resources/Dictionaries").toFile());
			fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Text Files", "*.txt"));
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				dictionaryPathTxt.setText(selectedFile.getPath());
				saveTxt.setText("");
				primaryStage.setTitle(primaryStage.getTitle() + '*');
				primaryStage.sizeToScene();
			}
		});
		
		Button loadAudio = new Button("Load Audio Set");
		loadAudio.setTooltip(new Tooltip("Select path to an audioset\nor leave blank for none"));
		loadAudio.setOnAction(value -> {
			DirectoryChooser  directoryChooser  = new DirectoryChooser();
			directoryChooser.setTitle("Select Audio Set");
			directoryChooser.setInitialDirectory(Paths.get("TalkBoxData/Resources/AudioFiles").toFile());
			File selectedFile = directoryChooser.showDialog(primaryStage);
			if (selectedFile != null) {
				saveTxt.setText("");
				audioPathTxt.setText(selectedFile.getPath());
				primaryStage.setTitle(primaryStage.getTitle() + '*');
				primaryStage.sizeToScene();
			}
		});
		
		Button save = new Button("Save Settings");
		save.setOnAction(value -> {
			try {
				Settings settings = new Settings(dictionaryPathTxt.getText(), audioPathTxt.getText());
	            FileOutputStream file = new FileOutputStream("TalkBoxData/Settings/settings.tbc"); 
	            ObjectOutputStream out = new ObjectOutputStream(file);
	            out.writeObject(settings);
	            out.close();
	            file.close();
	            if (primaryStage.getTitle().charAt(primaryStage.getTitle().length()-1) == '*') {
	            	primaryStage.setTitle(primaryStage.getTitle().substring(0, primaryStage.getTitle().length()-1));
	            	saveTxt.setText("Your changes have been saved!");
	            }
	            else
	            	saveTxt.setText("No changes to save!");
			}
			catch (IOException e) {
				System.err.println("COULD NOT SAVE");
			}
		});
		
		Button launch = new Button("Launch Simulator");
		launch.setOnAction(value -> {
			try {
				@SuppressWarnings("unused")
				Process proc = Runtime.getRuntime().exec("java -jar Simulator.jar");
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		});

		HBox saveBox = new HBox(save, saveTxt);
		saveBox.setSpacing(20);
		HBox dictionaryBox = new HBox(loadDictionary, dictionaryPathTxt);
		HBox audioBox = new HBox(loadAudio, audioPathTxt);
		VBox group = new VBox(dictionaryBox, audioBox, saveBox, launch);
		Scene scene = new Scene(group);

		primaryStage.setTitle("TalkBox TTS Configuration");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

}
