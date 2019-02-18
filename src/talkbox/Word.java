package talkbox;

public class Word {
	
	private Part_Of_Speech partOfSpeech;
	private Phrase_Type phraseType;
	private String content;
	
	Word(String content, Part_Of_Speech partOfSpeech, Phrase_Type phraseType) {
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
	
	public String toString() {
		return this.content;
	}
	
	@Override
	public int hashCode(){ 
		  return 0; //just for overriding purpose
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Word) {
			Word other = (Word) obj;
			return (this.content == other.content && this.partOfSpeech == other.partOfSpeech && this.phraseType == other.phraseType);
		}
		return false;
	}
	
}
