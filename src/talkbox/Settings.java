package talkbox;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

public class Settings implements TalkBoxConfiguration {
	
	private static final long serialVersionUID = 5254129808184630659L;
	private static final int NUM_AUDIO_BUTTONS = 5;
	private static final int NUM_MISC_BUTTONS = 6;
	
	private final String dictionaryURL;
	private final String audioFilesURL;
	private final int numOfAudioButtons;
	private final int numOfAudioSets;
	private final int totalNumOfButtons;
	
	private final TreeMap<String, String> audioSetData;
	
	public Settings(String dictionaryURL, String audioFilesURL, TreeMap<String, String> audioSetData) {
		this.dictionaryURL = dictionaryURL;
		this.audioFilesURL = audioFilesURL;
		this.audioSetData = audioSetData;
		this.numOfAudioSets = (audioFilesURL.isEmpty()) ? 0 : Paths.get(audioFilesURL).toFile().listFiles().length;
		this.numOfAudioButtons = NUM_AUDIO_BUTTONS;
		this.totalNumOfButtons = NUM_MISC_BUTTONS + NUM_AUDIO_BUTTONS;
	}

	@Override
	public int getNumberOfAudioButtons() {
		return this.numOfAudioButtons;
	}

	@Override
	public int getNumberOfAudioSets() {
		return this.numOfAudioSets;
	}

	@Override
	public int getTotalNumberOfButtons() {
		return this.totalNumOfButtons;
	}
	
	public Path getDictionaryPath() {
		return (this.dictionaryURL.isEmpty()) ? null : Paths.get(this.dictionaryURL) ;
	}

	@Override
	public Path getRelativePathToAudioFiles() {
		return (this.audioFilesURL.isEmpty()) ? null : Paths.get(this.audioFilesURL);
	}
	
	public TreeMap<String, String> getAudioSetData() {
		return this.audioSetData;
	}

	@Override
	public String[][] getAudioFileNames() {
		String[][] names = new String[this.getNumberOfAudioButtons()][this.getNumberOfAudioSets()];
		int i = 0, j = 0;
		for (File d : this.getRelativePathToAudioFiles().toFile().listFiles()) {
			if (d.isDirectory())
				for (File f : d.listFiles())
					if (f.exists())
						names[i++][j] = f.getName();
			i=0;
			j++;
		}
		return names;
	}

}
