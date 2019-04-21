package com.ipiecoles.java.java350.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Employe {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nom;

    private String prenom;

    private String matricule;

    private LocalDate dateEmbauche;

    private Double salaire = Entreprise.SALAIRE_BASE;

    private Integer performance = Entreprise.PERFORMANCE_BASE;

    private Double tempsPartiel = 1.0;

    public Employe() {
    }

    public Employe(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.dateEmbauche = dateEmbauche;
        this.salaire = salaire;
        this.performance = performance;
        this.tempsPartiel = tempsPartiel;
    }

    /**
     * Retourne le nombre d'année d'ancienneté de l'employé par rapport à sa date d'embauche (on ne prend pas en compte
     * les mois et les jours. Il faut en revanche que la d'embauche soit non nulle et l'année antérieure à l'année courante
     * sinon on renvoie une ancienneté de 0
     *
     * @return le nombre d'année d'ancienneté
     */
    public final Integer getNombreAnneeAnciennete() {
        return dateEmbauche != null && LocalDate.now().getYear() >= dateEmbauche.getYear() ? LocalDate.now().getYear() - dateEmbauche.getYear() : 0;
    }

    public Integer getNbConges() {
        return Entreprise.NB_CONGES_BASE + this.getNombreAnneeAnciennete();
    }

    /**
     * Nombre de jours de RTT =
     *   Nombre de jours dans l'année
     * – plafond maximal du forfait jours de la convention collective
     * – nombre de jours de repos hebdomadaires
     * – jours de congés payés
     * – nombre de jours fériés tombant un jour ouvré
     *
     * Au prorata de son pourcentage d'activité (arrondi au supérieur)
     *
     * @return le nombre de jours de RTT
     */
	 public Integer getNbRtt(LocalDate d){
    	//Nombre de jours dans l'année. Si l'année est bissextile, nbJoursAnnee vaut 366 sinon vaut 365. 
        int nbJoursAnnee = d.isLeapYear() ? 366 : 365;
        
        //Somme des samedi et dimanche sur l'annee
        int nbJoursNonOuvres = 104;
        
        //Controle sur le premier jour de l'annee passée en paramètre
        switch (LocalDate.of(d.getYear(),1,1).getDayOfWeek()){
        	//Si c'est une année bissextile, ajoute 1 aux jours non travaillés si le premier jour de l'année est un jeudi
            case THURSDAY: 
            	if(d.isLeapYear()) {
            		nbJoursNonOuvres =  nbJoursNonOuvres + 1;
            	}
            break;
            
            //Si c'est une année bissextile, ajoute 2 aux jours non travaillés si le premier jour de l'année est un vendredi
            //Si l'année n'est pas bissextile, ajoute 1
            case FRIDAY: 
            	if(d.isLeapYear()) {
            		nbJoursNonOuvres =  nbJoursNonOuvres + 2;
            	}else {
            		nbJoursNonOuvres =  nbJoursNonOuvres + 1;
            	}
            break;
            //Ajoute 1 aux jours non travaillés si le premier jour de l'année est un samedi.
            case SATURDAY: 
            	nbJoursNonOuvres = nbJoursNonOuvres + 1; 
            break;
            default: nbJoursNonOuvres = 104;
        }
        
        //Récupère la liste des jours fériés pour l'année en cours. La liste est paramétrée dans la classe Entreprise.
        //Parcours la liste de jour fériés. Ajoute 1 à nbJoursFeriesJoursOuvres pour chaque jour férié n'étant pas un samedi ou un Dimanche
        int nbJoursFeriesJoursOuvres = (int) Entreprise.joursFeries(d).stream().filter(localDate -> localDate.getDayOfWeek().getValue() <= DayOfWeek.FRIDAY.getValue()).count();
        
        return (int) Math.ceil((nbJoursAnnee - Entreprise.NB_JOURS_MAX_FORFAIT - nbJoursNonOuvres - Entreprise.NB_CONGES_BASE - nbJoursFeriesJoursOuvres) * tempsPartiel);
    }

    /**
     * Calcul de la prime annuelle selon la règle :
     * Pour les managers : Prime annuelle de base bonnifiée par l'indice prime manager
     * Pour les autres employés, la prime de base plus éventuellement la prime de performance calculée si l'employé
     * n'a pas la performance de base, en multipliant la prime de base par un l'indice de performance
     * (égal à la performance à laquelle on ajoute l'indice de prime de base)
     *
     * Pour tous les employés, une prime supplémentaire d'ancienneté est ajoutée en multipliant le nombre d'année
     * d'ancienneté avec la prime d'ancienneté. La prime est calculée au pro rata du temps de travail de l'employé
     *
     * @return la prime annuelle de l'employé en Euros et cents
     */
    public Double getPrimeAnnuelle(){
        //Calcule de la prime d'ancienneté
        Double primeAnciennete = Entreprise.PRIME_ANCIENNETE * this.getNombreAnneeAnciennete();
        Double prime;
        //Prime du manager (matricule commençant par M) : Prime annuelle de base multipliée par l'indice prime manager
        //plus la prime d'anciennté.
        if(matricule != null && matricule.startsWith("M")) {
            prime = Entreprise.primeAnnuelleBase() * Entreprise.INDICE_PRIME_MANAGER + primeAnciennete;
        }
        //Pour les autres employés en performance de base, uniquement la prime annuelle plus la prime d'ancienneté.
        else if (this.performance == null || Entreprise.PERFORMANCE_BASE.equals(this.performance)){
            prime = Entreprise.primeAnnuelleBase() + primeAnciennete;
        }
        //Pour les employés plus performance, on bonnifie la prime de base en multipliant par la performance de l'employé
        // et l'indice de prime de base.
        else {
            prime = Entreprise.primeAnnuelleBase() * (this.performance + Entreprise.INDICE_PRIME_BASE) + primeAnciennete;
        }
        //Au pro rata du temps partiel.
        return Math.round(prime * this.tempsPartiel * 100)/100.0;
    }

    //Augmenter salaire
    public void augmenterSalaire(double pourcentage){
    	//definition d'un format de décimal en limitant à 2 chiffres après la virgule
    	DecimalFormat df = new DecimalFormat("#.##");
    	//arrondie à la valeur supérieur du format défini au dessus
    	df.setRoundingMode(RoundingMode.HALF_UP);
    	
    	//Calcul de l'augmentation de salaire en fonction du pourcentage du salaire actuel
    	Double salaireAugmente = (1 + (pourcentage/100))*this.getSalaire();
    	
    	//Conversion en string pour passer le format de décimal voulu
    	String salaireArrondi = df.format(salaireAugmente);
    	
    	//Enregistrement de la valeur dans l'attribut salaire après "parsage" de la String en Double
    	this.salaire = Double.parseDouble(salaireArrondi);	
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the matricule
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * @param matricule the matricule to set
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * @return the dateEmbauche
     */
    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }

    /**
     * @param dateEmbauche the dateEmbauche to set
     */
    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    /**
     * @return the salaire
     */
    public Double getSalaire() {
        return salaire;
    }

    /**
     * @param salaire the salaire to set
     */
    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }

    public Integer getPerformance() {
        return performance;
    }

    public void setPerformance(Integer performance) {
        this.performance = performance;
    }

    public Double getTempsPartiel() {
        return tempsPartiel;
    }

    public void setTempsPartiel(Double tempsPartiel) {
        if(tempsPartiel > 1.0) {
        	this.tempsPartiel = 1.0;
        }else {
        	this.tempsPartiel = tempsPartiel;
        }    	
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employe)) return false;
        Employe employe = (Employe) o;
        return Objects.equals(id, employe.id) &&
                Objects.equals(nom, employe.nom) &&
                Objects.equals(prenom, employe.prenom) &&
                Objects.equals(matricule, employe.matricule) &&
                Objects.equals(dateEmbauche, employe.dateEmbauche) &&
                Objects.equals(salaire, employe.salaire) &&
                Objects.equals(performance, employe.performance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, matricule, dateEmbauche, salaire, performance);
    }
}
