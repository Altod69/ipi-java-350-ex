package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

    @InjectMocks
    EmployeService employeService;

    @Mock
    EmployeRepository employeRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this.getClass());
    }

    @Test
    public void testEmbaucheEmployeTechnicienPleinTempsBts() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        when(employeRepository.findLastMatricule()).thenReturn("00345");
        when(employeRepository.findByMatricule("T00346")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        verify(employeRepository, times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertEquals(nom, employeArgumentCaptor.getValue().getNom());
        Assertions.assertEquals(prenom, employeArgumentCaptor.getValue().getPrenom());
        Assertions.assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), employeArgumentCaptor.getValue().getDateEmbauche().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Assertions.assertEquals("T00346", employeArgumentCaptor.getValue().getMatricule());
        Assertions.assertEquals(tempsPartiel, employeArgumentCaptor.getValue().getTempsPartiel());

        //1521.22 * 1.2 * 1.0
        Assertions.assertEquals(1825.46, employeArgumentCaptor.getValue().getSalaire().doubleValue());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMaster() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn("00345");
        when(employeRepository.findByMatricule("M00346")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        verify(employeRepository, times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertEquals(nom, employeArgumentCaptor.getValue().getNom());
        Assertions.assertEquals(prenom, employeArgumentCaptor.getValue().getPrenom());
        Assertions.assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), employeArgumentCaptor.getValue().getDateEmbauche().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Assertions.assertEquals("M00346", employeArgumentCaptor.getValue().getMatricule());
        Assertions.assertEquals(tempsPartiel, employeArgumentCaptor.getValue().getTempsPartiel());

        //1521.22 * 1.4 * 0.5
        Assertions.assertEquals(1064.85, employeArgumentCaptor.getValue().getSalaire().doubleValue());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMasterNoLastMatricule() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn(null);
        when(employeRepository.findByMatricule("M00001")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        verify(employeRepository, times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertEquals("M00001", employeArgumentCaptor.getValue().getMatricule());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMasterExistingEmploye(){
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn(null);
        when(employeRepository.findByMatricule("M00001")).thenReturn(new Employe());

        //When/Then
        EntityExistsException e = Assertions.assertThrows(EntityExistsException.class, () -> employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel));
        Assertions.assertEquals("L'employé de matricule M00001 existe déjà en BDD", e.getMessage());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMaster99999(){
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn("99999");

        //When/Then
        EmployeException e = Assertions.assertThrows(EmployeException.class, () -> employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel));
        Assertions.assertEquals("Limite des 100000 matricules atteinte !", e.getMessage());
    }
    
    @Test
    public void testCalculPerformanceCommercialeMatriculeNonCommercial() throws EmployeException {
    	//Given
    	String matricule = "T00000";
    	Long caTraite = 100000L;
    	Long objectifCa = 100000L;
    	//When/then
    	EmployeException e = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa));
    	Assertions.assertEquals("Le matricule ne peut être null et doit commencer par un C !", e.getMessage());
    }
    
    @Test
    public void testCalculPerformanceCommercialeMatriculeEmployeRepoNull() throws EmployeException {
    	//Given
    	String matricule = "C00000";
    	Long caTraite = 100000L;
    	Long objectifCa = 100000L;
    	when(employeRepository.findByMatricule("C00000")).thenReturn(null);
    	
    	//When/then
    	EmployeException e = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa));
    	Assertions.assertEquals("Le matricule C00000 n'existe pas !", e.getMessage());
    }
    
    @Test
    public void testCalculPerformanceCommercialeMatriculeCaTraiteNull() throws EmployeException {
    	//Given
    	String matricule = "C00000";
    	Long objectifCa = 100000L;
    	//When/then
    	EmployeException e = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule, null, objectifCa));
    	Assertions.assertEquals("Le chiffre d'affaire traité ne peut être négatif ou null !", e.getMessage());
    }
    
    @Test
    public void testCalculPerformanceCommercialeMatriculeCaTraiteNegatif() throws EmployeException {
    	//Given
    	String matricule = "C00000";
    	Long objectifCa = 100000L;
    	//When/then
    	EmployeException e = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule, -1L, objectifCa));
    	Assertions.assertEquals("Le chiffre d'affaire traité ne peut être négatif ou null !", e.getMessage());
    }
    
    @Test
    public void testCalculPerformanceCommercialeMatriculeObjectifCaNull() throws EmployeException {
    	//Given
    	String matricule = "C00000";
    	Long caTraite = 100000L;
    	//When/then
    	EmployeException e = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule, caTraite, null));
    	Assertions.assertEquals("L'objectif de chiffre d'affaire ne peut être négatif ou null !", e.getMessage());
    }
    
    @Test
    public void testCalculPerformanceCommercialeMatriculeObjectifCaNegatif() throws EmployeException {
    	//Given
    	String matricule = "C00000";
    	Long caTraite = 100000L;
    	//When/then
    	EmployeException e = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule, caTraite, -1L));
    	Assertions.assertEquals("L'objectif de chiffre d'affaire ne peut être négatif ou null !", e.getMessage());
    }
    
    @Test
    public void testCalculPerformanceCommercialeMatriculeCasNominal() throws EmployeException {
    	//Given
    	Employe employe = new Employe();
    	String matricule = "C00000";
    	Long caTraite = 120000L;
    	Long objectifCa = 100000L;
    	employe.setPerformance(0);
    	when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(2.0);
    	
    	//When/then
    	ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        verify(employeRepository, times(1)).save(employeArgumentCaptor.capture());
    	EmployeException e = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa));
    	Assertions.assertEquals(null, e.getMessage());
    	Assertions.assertEquals(5,employeArgumentCaptor.getValue().getPerformance().intValue());
    }
}