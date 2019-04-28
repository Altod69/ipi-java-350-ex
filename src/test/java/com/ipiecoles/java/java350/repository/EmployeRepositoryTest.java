package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
public class EmployeRepositoryTest {

    @Autowired
    private EmployeRepository employeRepository;

    @BeforeEach
    @AfterEach
    public void setup(){
        employeRepository.deleteAll();
    }

    @Test
    public void testFindLastMatriculeEmpty(){
        //Given

        //When
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertNull(lastMatricule);
    }

    @Test
    public void testFindLastMatriculeSingle(){
        //Given
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));

        //When
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertEquals("12345", lastMatricule);
    }

    @Test
    public void testFindLastMatriculeMultiple(){
        //Given
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
        employeRepository.save(new Employe("Doe", "Jane", "M40325", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
        employeRepository.save(new Employe("Doe", "Jim", "C06432", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));

        //When
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertEquals("40325", lastMatricule);
    }
    
    @Test
    public void testAvgPerformanceWhereMatriculeStartsWithT(){
        //Given
        employeRepository.save(new Employe("Kenobi", "ObiWan", "T00001", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
        employeRepository.save(new Employe("Windu", "Mace", "T00002", LocalDate.now(), Entreprise.SALAIRE_BASE, 5, 1.0));
        employeRepository.save(new Employe("Maitre", "Kylo", "T00003", LocalDate.now(), Entreprise.SALAIRE_BASE, 2, 1.0));
        employeRepository.save(new Employe("Maitre", "Kylo", "T00003", LocalDate.now(), Entreprise.SALAIRE_BASE, 2, 1.0));

        //When
        Double perfMoy = employeRepository.avgPerformanceWhereMatriculeStartsWith("T");

        //Then
        Assertions.assertEquals(2.5, perfMoy.doubleValue());
    }
    
    @Test
    public void testAvgPerformanceWhereMatriculeStartsWithC(){
        //Given
        employeRepository.save(new Employe("Legris", "Gandalf", "C00001", LocalDate.now(), Entreprise.SALAIRE_BASE, 2, 1.0));
        employeRepository.save(new Employe("Sacquet", "Frodon", "C00002", LocalDate.now(), Entreprise.SALAIRE_BASE, 5, 1.0));
        employeRepository.save(new Employe("Gamegie", "Sam", "C00003", LocalDate.now(), Entreprise.SALAIRE_BASE, 6, 1.0));
        employeRepository.save(new Employe("Le Vilain", "Sauron", "C00003", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));

        //When
        Double perfMoy = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");

        //Then
        Assertions.assertEquals(3.5, perfMoy.doubleValue());
    }
    
    @Test
    public void testAvgPerformanceWhereMatriculeStartsWithM(){
        //Given
        employeRepository.save(new Employe("Parker", "Peter", "M00001", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
        employeRepository.save(new Employe("Wayne", "Bruce", "M00002", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
        employeRepository.save(new Employe("Kent", "Clark", "M00003", LocalDate.now(), Entreprise.SALAIRE_BASE, 4, 1.0));
        employeRepository.save(new Employe("Stark", "Tony", "M00003", LocalDate.now(), Entreprise.SALAIRE_BASE, 2, 1.0));

        //When
        Double perfMoy = employeRepository.avgPerformanceWhereMatriculeStartsWith("M");

        //Then
        Assertions.assertEquals(2.0, perfMoy.doubleValue());
    }
}