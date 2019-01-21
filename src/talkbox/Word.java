package talkbox;

public class Word {
	
	private Part_Of_Speech partOfSpeech;
	private Phrase_Type phraseType;
	private String content;
	
	Word(Part_Of_Speech partOfSpeech, Phrase_Type phraseType, String content) {
		this.partOfSpeech = partOfSpeech;
		this.phraseType = phraseType;
		this.content = content;
	}
	
	public Part_Of_Speech getPartOfSpeech() {
		return this.partOfSpeech;
	}
	
	public Phrase_Type getPhraseType() {
		return this.phraseType;
	}
	
	public String getContent() {
		return this.content;
	}
	
}
