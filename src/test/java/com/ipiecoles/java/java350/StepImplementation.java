package com.ipiecoles.java.java350;
import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@Component
public class StepImplementation {
	
	@Autowired
	private EmployeService employeService;
	
	@Autowired
	private EmployeRepository employeRepository;

    private HashSet<Character> vowels;
    
    @Step("J'embauche une personne appelée <prenom> <nom> diplomée d'un <diplome> en tant que <poste> à <tempsActivite>.")
    public void embaucheEmploye(String prenom, String nom, String diplome, String poste, String tempsActivite) throws EmployeException{
    	Double tempsPartiel;
    	
    	if(tempsActivite.equalsIgnoreCase("plein temps")){
    		tempsPartiel = 1.0;
    	} else if(tempsActivite.equalsIgnoreCase("mi-temps")) {
    		tempsPartiel = 0.5;
    	} else {
    		tempsPartiel = 0.0;
    	}
    	
    	employeService.embaucheEmploye(nom, prenom, Poste.valueOf(poste.toUpperCase()), NiveauEtude.valueOf(diplome.toUpperCase()), tempsPartiel);
    }

    @Step("Vowels in English language are <vowelString>.")
    public void setLanguageVowels(String vowelString) {
        vowels = new HashSet<>();
        for (char ch : vowelString.toCharArray()) {
            vowels.add(ch);
        }
    }

    @Step("The word <word> has <expectedCount> vowels.")
    public void verifyVowelsCountInWord(String word, int expectedCount) {
        int actualCount = countVowels(word);
        assertThat(expectedCount).isEqualTo(actualCount);
    }

    @Step("Almost all words have vowels <wordsTable>")
    public void verifyVowelsCountInMultipleWords(Table wordsTable) {
        for (TableRow row : wordsTable.getTableRows()) {
            String word = row.getCell("Word");
            int expectedCount = Integer.parseInt(row.getCell("Vowel Count"));
            int actualCount = countVowels(word);

            assertThat(expectedCount).isEqualTo(actualCount);
        }
    }

    private int countVowels(String word) {
        int count = 0;
        for (char ch : word.toCharArray()) {
            if (vowels.contains(ch)) {
                count++;
            }
        }
        return count;
    }
}
