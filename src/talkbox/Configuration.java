package talkbox;

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
	private int numOfAudioButtons, numOfAudioSets, numOfTotalButtons;
	private String[][] audioFileNames;
	private Path audioPath;

	public static void main(String[] args) {
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
//		numOfAudioSets=(int)(this.choose(subjects.size(),1)*this.choose(objects.size(),1)*this.choose(verbs.size(),1));
		numOfTotalButtons = 6;
		audioPath = Paths.get("/audiofiles");
		audioFileNames = null;
		Simulator sim = new Simulator();

		Label label1 = new Label("Word:");
		TextField textField = new TextField ();
		HBox hb = new HBox();
		hb.getChildren().addAll(label1, textField);
		hb.setSpacing(20);
		
		Button addSubject = new Button("Add Subject");
		addSubject.setOnAction(value -> {
			sim.addSubject(textField.getText());
		});
		
		Button addObject = new Button("Add Object");
		addObject.setOnAction(value -> {
			sim.addObject(textField.getText());
		});
		
		Button addVerb = new Button("Add Verb");
		addVerb.setOnAction(value -> {
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
		
        HBox hbox = new HBox(launcher,addSubject,addObject,addVerb);
        VBox vbox = new VBox(hbox,hb);
        Scene scene = new Scene(vbox);
		
		primaryStage.setTitle("TalkBox TTS Configuration");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

	}

}
