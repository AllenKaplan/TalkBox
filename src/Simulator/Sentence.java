package Simulator;

import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.features.Tense;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;

import java.util.ArrayList;

public class Sentence {
	
	//SimpleNLG attributes
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	private Realiser realiser;
	
	//Other atttributes
	ArrayList<Word> words;
	private Tense tense;
	private boolean question;
	
	/*
	 * Generates a new Sentence object and declares the class attributes to defaults
	 * 
	 * Defaults: Empty word arraylist, Tense = Present, Question = false;
	 */
	Sentence() {
		words = new ArrayList<Word>();
		lexicon = Lexicon.getDefaultLexicon();
		nlgFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
		
		setTense(Tense.PRESENT);
		setQuestion(false);
	}
	
	public void addWord(Word w) {
		this.words.add(w);
	}
	
	public void setTense(Tense tense) {
		this.tense = tense;
	}
	
	public void setQuestion(boolean tf) {
		this.question = tf;
	}
	
	public String getConstructedScentence() {
		SPhraseSpec p = nlgFactory.createClause();
		for (Word w: words) {
			switch (w.getPartOfSpeech()) {
				case Subject:
					p.setSubject(w.toString());
					break;
				case Verb:
					p.setVerb(w.toString());
					break;
				case Object:
					p.setObject(w.toString());
					break;
				case Compliment:
					p.setComplement(w.toString());
					break;
				case Indirect_Object:
					p.setIndirectObject(w.toString());
					break;
				default:
					break;
			}
		}
		
		//set features
		p.setFeature(Feature.TENSE, this.tense);
		if (this.question)
			p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		
		return realiser.realiseSentence(p);
	}

	public ArrayList<String> getTenses() {
		ArrayList<String> tenses = new ArrayList<String>();
		tenses.add("Past");
		tenses.add("Present");
		tenses.add("Future");
		return tenses;
	}
	
	

}
