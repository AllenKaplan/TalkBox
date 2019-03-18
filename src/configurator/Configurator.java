package configurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import talkbox.*;

@SuppressWarnings("restriction")
public class Configurator extends Application {

	private Label dictionaryPathTxt;
	private Label audioPathTxt;
	TreeMap<String, String> fileMap;
	ObservableMap<String, String> observableFileMap;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void init() {
		fileMap = new TreeMap<String, String>();
		observableFileMap = FXCollections.observableMap(fileMap);
		dictionaryPathTxt = new Label("");
		audioPathTxt = new Label("");

		try {
			File filepath = new File("TalkBoxData/Settings");
			if (!filepath.exists()) {
				filepath.mkdirs();
			}
			FileInputStream file = new FileInputStream("TalkBoxData/Settings/settings.tbc");
			ObjectInputStream in = new ObjectInputStream(file);
			Settings settings = (Settings) in.readObject();
			file.close();
			in.close();

			if (settings.getDictionaryPath() != null)
				dictionaryPathTxt = new Label(settings.getDictionaryPath().toString());

			if (settings.getRelativePathToAudioFiles() != null) {
				audioPathTxt = new Label(settings.getRelativePathToAudioFiles().toString());
				for (File f : settings.getRelativePathToAudioFiles().toFile().listFiles()) {
					fileMap.put(f.getPath().toString(), f.getName());
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("ERROR LOADING PREVIOUS SETTINGS");
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label saveTxt = new Label("");

		Button editAudioSet = new Button("Edit AudioSet");
		editAudioSet.setDisable(audioPathTxt.getText().isEmpty());
		editAudioSet.setOnAction(value -> {
			this.displayEditWindow();
			primaryStage.setTitle(primaryStage.getTitle() + '*');
		});

		Button loadDictionary = new Button("Load Custom Dictionary");
		loadDictionary.setTooltip(new Tooltip("Select path to a dictionary\nor leave blank for default"));
		loadDictionary.setOnAction(value -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select Dictionary");
			File filepath = new File("TalkBoxData/Resources/Dictionaries");
			if (!filepath.exists())
				filepath.mkdirs();
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
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("Select Audio Set");
			File filepath = new File("TalkBoxData/Resources/AudioFiles");
			if (!filepath.exists())
				filepath.mkdirs();
			directoryChooser.setInitialDirectory(Paths.get("TalkBoxData/Resources/AudioFiles").toFile());
			File selectedFile = directoryChooser.showDialog(primaryStage);
			if (selectedFile != null) {
				saveTxt.setText("");
				audioPathTxt.setText(selectedFile.getPath());
				primaryStage.setTitle(primaryStage.getTitle() + '*');
				primaryStage.sizeToScene();
				for (File f : selectedFile.listFiles()) {
					fileMap.put(f.getPath().toString(), f.getName());
				}
				editAudioSet.setDisable(audioPathTxt.getText().isEmpty());
			}
		});

		Button save = new Button("Save Settings");
		save.setOnAction(value -> {
			try {
				Settings settings = new Settings(dictionaryPathTxt.getText(), audioPathTxt.getText(), fileMap);
				FileOutputStream file = new FileOutputStream("TalkBoxData/Settings/settings.tbc");
				ObjectOutputStream out = new ObjectOutputStream(file);
				out.writeObject(settings);
				out.close();
				file.close();
				if (primaryStage.getTitle().charAt(primaryStage.getTitle().length() - 1) == '*') {
					primaryStage.setTitle(primaryStage.getTitle().substring(0, primaryStage.getTitle().length() - 1));
					saveTxt.setText("Your changes have been saved!");
				} else
					saveTxt.setText("No changes to save!");
			} catch (IOException e) {
				System.err.println("COULD NOT SAVE");
			}
		});
		
		Button load = new Button("Load Settings");
		load.setOnAction(value -> {
			//TODO write thiss
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
		dictionaryBox.setSpacing(10);
		HBox audioBox = new HBox(loadAudio, audioPathTxt);
		audioBox.setSpacing(10);
		VBox group = new VBox(dictionaryBox, audioBox, editAudioSet, saveBox, launch);
		group.setSpacing(15);
		Scene scene = new Scene(group);

		primaryStage.setTitle("TalkBox TTS Configuration");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	void displayEditWindow() {
		Label editLabel = new Label("Double click any audiofile name to change it");
		ListView<String> fileView = new ListView<>();
		fileView.setEditable(true);
		fileView.setCellFactory(TextFieldListCell.forListView());
		fileView.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
			@Override
			public void handle(ListView.EditEvent<String> t) {
				fileView.getItems().set(t.getIndex(), t.getNewValue());
			}
		});
		fileView.getItems().setAll(observableFileMap.values());
		fileView.autosize();
		Stage fileStage = new Stage();
		VBox fileBox = new VBox(editLabel, fileView);
		fileBox.setSpacing(10);
		fileStage.setTitle("Edit Audio Files");
		fileStage.setScene(new Scene(fileBox));
		fileStage.sizeToScene();
		fileStage.show();
	}

}
