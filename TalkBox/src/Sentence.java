
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;

import java.util.ArrayList;

public class Sentence {

	ArrayList<Word> words;
	
	Lexicon lexicon;
	NLGFactory nlgFactory;
	Realiser realiser;
	SPhraseSpec p;
	
	Sentence() {
		this.words = new ArrayList<Word>();
		
		lexicon = Lexicon.getDefaultLexicon();
		nlgFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
	}
	
	public void addWord(Word w) {
		this.words.add(w);
	}
	
	public String getConstructedScentence() {
		p = nlgFactory.createClause();
		for (Word w: words) {
			switch (w.partOfSpeech) {
			case Subject:
				p.setSubject(w.getContent());
				break;
			case Verb:
				p.setVerb(w.getContent());
				break;
			case Object:
				p.setObject(w.getContent());
			case Article:
				break;
			case Compliment:
				break;
			case Indirect_Object:
				break;
			case Modifier:
				break;
			default:
				break;
			}
		}
		return realiser.realiseSentence(p);
	}
	
	
	
}
