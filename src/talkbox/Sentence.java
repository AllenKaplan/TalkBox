package talkbox;

import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.features.Tense;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;

import java.util.ArrayList;

public class Sentence {

	ArrayList<Word> words;
	
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	private Realiser realiser;
	
	private Tense tense;
	
	private boolean question;
	
	Sentence() {
		this.words = new ArrayList<Word>();
		
		lexicon = Lexicon.getDefaultLexicon();
		nlgFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
		
		this.setTense(Tense.PRESENT);
		this.setQuestion(false);
	}
	
	public void addWord(Word w) {
		this.words.add(w);
	}
	
	public void setTense(Tense tense) {
		this.tense = tense;
	}
	
	public void rotateTense() {
		switch (this.tense) {
		case FUTURE:
			this.tense = Tense.PAST;
			break;
		case PAST:
			this.tense = Tense.PRESENT;
			break;
		case PRESENT:
			this.tense = Tense.FUTURE;
			break;
		}
	}
	
	public void setQuestion(boolean tf) {
		this.question = tf;
	}
	
	public String getConstructedScentence() {
		SPhraseSpec p = nlgFactory.createClause();
		for (Word w: words) {
			switch (w.getPartOfSpeech()) {
			case Subject:
				p.setSubject(w.getContent());
				break;
			case Verb:
				p.setVerb(w.getContent());
				break;
			case Object:
				p.setObject(w.getContent());
				break;
			case Compliment:
				p.setComplement(w.getContent());
				break;
			case Indirect_Object:
				p.setIndirectObject(w.getContent());
				break;
			default:
				break;
			}
		}
		p.setFeature(Feature.TENSE, this.tense);
		
		if (this.question)
			p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		
		return realiser.realiseSentence(p);
	}
}
