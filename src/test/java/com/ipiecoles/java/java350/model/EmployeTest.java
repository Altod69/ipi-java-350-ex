package com.ipiecoles.java.java350.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class EmployeTest {
	
	@ParameterizedTest
    @CsvSource({
            "1000.0, 0.0, 1000.0",
            "0.0, 10.0, 0",
            "0.0, 0.0, 0",
            "1000.0, 10.0, 1100.0",
            "1000.0, 0.1, 1001.0",
            "1000.0, 5, 1050.0",
            "1000.0, 110, 2100.0"
    })
    public void augmenterSalaireTest(Double salaire, Double pourcentage, Double resultatAttendu) throws Exception {
    	//Given
    	Employe e = new Employe();
    	e.setSalaire(salaire);
    	e.augmenterSalaire(pourcentage);
    	
    	//When
    	Double salaireAugmente = e.getSalaire();
    	
    	//Then
    	Assertions.assertEquals(resultatAttendu, salaireAugmente);
    }
	
	@ParameterizedTest
    @CsvSource({
            "2020, 1.0",
            "2019, 1.0",
            "2018, 1.0",
            "2017, 1.0",
            "2020, 0.5",
            "2019, 0.5",
            "2018, 0.5",
            "2017, 0.5"
    })
	public void getNbRttTest(Integer selecteurAnnee, Double tempsPartiel) {
		//Given
		Employe e = new Employe();
		e.setTempsPartiel(tempsPartiel);
		//When
		Integer nbRtt = e.getNbRtt(LocalDate.of(selecteurAnnee, 1, 1));
		//Then
		Assertions.assertEquals(0, nbRtt.intValue());
	}

    @Test
    public void getNombreAnneeAncienneteNow(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now());

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0, anneeAnciennete.intValue());
    }

    @Test
    public void getNombreAnneeAncienneteNminus2(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().minusYears(2L));

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(2, anneeAnciennete.intValue());
    }

    @Test
    public void getNombreAnneeAncienneteNull(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(null);

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0, anneeAnciennete.intValue());
    }

    @Test
    public void getNombreAnneeAncienneteNplus2(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().plusYears(2L));

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0, anneeAnciennete.intValue());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'T12345', 0, 1.0, 1000.0",
            "1, 'T12345', 2, 0.5, 600.0",
            "1, 'T12345', 2, 1.0, 1200.0",
            "2, 'T12345', 0, 1.0, 2300.0",
            "2, 'T12345', 1, 1.0, 2400.0",
            "1, 'M12345', 0, 1.0, 1700.0",
            "1, 'M12345', 5, 1.0, 2200.0",
            "2, 'M12345', 0, 1.0, 1700.0",
            "2, 'M12345', 8, 1.0, 2500.0"
    })
    public void getPrimeAnnuelle(Integer performance, String matricule, Long nbYearsAnciennete, Double tempsPartiel, Double primeAnnuelle){
        //Given
        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now().minusYears(nbYearsAnciennete), Entreprise.SALAIRE_BASE, performance, tempsPartiel);

        //When
        Double prime = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertEquals(primeAnnuelle, prime);

    }

}