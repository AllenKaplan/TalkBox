package talkbox;
public class Word {
	
	Part_Of_Speech partOfSpeech;
	Phrase_Type phraseType;
	String content;
	
	Word(Part_Of_Speech partOfSpeech, Phrase_Type phraseType, String content) {
		this.partOfSpeech = partOfSpeech;
		this.phraseType = phraseType;
		this.content = content;
	}
	
	public String getPartOfSpeech() {
		return this.partOfSpeech.toString();
	}
	
	public String getPhraseType() {
		return this.phraseType.toString();
	}
	
	public String getContent() {
		return this.content;
	}
	
}
