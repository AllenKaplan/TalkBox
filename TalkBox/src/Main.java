public class Main {

	public static void main(String[] args) {
		
		Word w1 = new Word(Part_Of_Speech.Object, Phrase_Type.Noun, "food");
		Word w2 = new Word(Part_Of_Speech.Verb, Phrase_Type.Noun, "hungry");
		Word w3 = new Word(Part_Of_Speech.Subject, Phrase_Type.Noun, "me");
		
		Sentence newPhrase = new Sentence();
		newPhrase.addWord(w1);
		newPhrase.addWord(w2);
		newPhrase.addWord(w3);
		
		System.out.println(newPhrase.getConstructedScentence());
	}

}
