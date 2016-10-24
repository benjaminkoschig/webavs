package globaz.apg.interfaces;

/**
 * Déclaration des getters pour la représentation de la table APSIPRP.
 * 
 * @author PBA
 */
public interface SituationProfessionnelle {

    public String getAutreRemuneration();

    public String getAutreSalaire();

    public String getDateDebut();

    public String getDateFin();

    public String getDateFinContrat();

    public Boolean getDeletePrestationsRequis();

    public Boolean getHasAcmAlphaPrestations();

    public Boolean getHasAcm2AlphaPrestations();

    public Boolean getHasLaMatPrestations();

    public String getHeuresSemaine();

    public String getIdDroit();

    public String getIdEmployeur();

    public String getIdSituationProf();

    public Boolean getIsAllocationExploitation();

    public Boolean getIsAllocationMax();

    public Boolean getIsCollaborateurAgricole();

    public Boolean getIsIndependant();

    public Boolean getIsNonActif();

    public Boolean getIsNonSoumisCotisation();

    public Boolean getIsPourcentAutreRemun();

    public Boolean getIsPourcentMontantVerse();

    public Boolean getIsSituationProf();

    public Boolean getIsTravailleurAgricole();

    public Boolean getIsTravailleurSansEmploi();

    public Boolean getIsVersementEmployeur();

    public String getMontantVerse();

    public String getPeriodiciteAutreRemun();

    public String getPeriodiciteAutreSalaire();

    public String getPeriodiciteMontantVerse();

    public String getPeriodiciteSalaireNature();

    public String getRevenuIndependant();

    public String getSalaireHoraire();

    public String getSalaireMensuel();

    public String getSalaireNature();

}