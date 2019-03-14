package com.ipiecoles.java.java350.model;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
