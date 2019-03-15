package com.ipiecoles.java.java350.model;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class EmployeTest {

	@Test
	public void testGetNombreAnneeAnciennete() {
		//Given
		Employe employe = new Employe();
		LocalDate dateEmbauche = LocalDate.now();
		employe.setDateEmbauche(dateEmbauche);
		
		//When
		Integer nbAnnee = employe.getNombreAnneeAnciennete();
		
		//Then
		//nbAnnee = 0
		Assertions.assertThat(nbAnnee).isEqualTo(0);
	}
	
	@Test
	public void testGetNombreAnneeAncienneteNull() {
		//Given
		Employe employe = new Employe();
		employe.setDateEmbauche(null);
		
		//When
		Integer nbAnnee = employe.getNombreAnneeAnciennete();
		
		//Then
		//nbAnnee = 0
		Assertions.assertThat(nbAnnee).isEqualTo(0);
	}
	
	@Test
	public void testGetNombreAnneeAncienneteNmoins2() {
		//Given
		Employe employe = new Employe();
		employe.setDateEmbauche(LocalDate.now().minusYears(2));
		
		//When
		Integer nbAnnee = employe.getNombreAnneeAnciennete();
		
		//Then
		//nbAnnee = 0
		Assertions.assertThat(nbAnnee).isEqualTo(2);
	}
	
	@Test
	public void testGetNombreAnneeAncienneteNplus2() {
		//Given
		Employe employe = new Employe();
		employe.setDateEmbauche(LocalDate.now().plusYears(2));
		
		//When
		Integer nbAnnee = employe.getNombreAnneeAnciennete();
		
		//Then
		//nbAnnee = 0
		Assertions.assertThat(nbAnnee).isEqualTo(0);
	}
	
	@ParameterizedTest
	@CsvSource({
		"1, 'M12345', 0, 1.0, 1700.0",
		"1, 'T12345', 2, 1.0, 1200.0",
		"1, 'T12345', 2, 0.5, 600.0",
		"2, 'T12345', 0, 1.0, 2300.0",
	})
	public void testGetPrimeAnnuelle(Integer performance, String matricule, Long nbYearsAnciennete, Double tempsPartiel, Double primeAnnuelle) {
		//Given
		/* 
		 * 
		 * PrimeAncienneté
		 * Prime
		 * Matricule
		 * Performance
		 * TempsPartiel
		 * 
		 */
		Employe employe = new Employe();
		employe.setPerformance(performance);
		employe.setMatricule(matricule);
		employe.setTempsPartiel(tempsPartiel);
		employe.setDateEmbauche(LocalDate.now().minusYears(nbYearsAnciennete));
		//When
		
		Double prime = employe.getPrimeAnnuelle();
		//Then
				
		Assertions.assertThat(prime).isEqualTo(primeAnnuelle);
	}
}
