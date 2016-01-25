package globaz.apg.module.calcul;

import globaz.framework.util.FWCurrency;

/**
 * Descpription
 * 
 * @author scr Date de création 31 mai 05
 */
public class APSituationProfessionnelleHelper {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idAffilie = null;
    private String idSituationProfessionnelle = "";

    private String idTiers = null;
    private boolean isCollaborateurAgricole = false;

    private boolean isIndependant = false;
    private boolean isSoumisCotisation = false;
    private boolean isTravailleurAgricole = false;
    private boolean isTravailleurSansEmployeur = false;

    private boolean isVersementEmployeur = false;
    private String nom = null;
    private FWCurrency revenuMoyenDeterminantSansArrondi = null;
    private FWCurrency salaireJournalier = null;
    private FWCurrency salaireJournalierVerse = null;
    private FWCurrency TL = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut id affilie
     * 
     * @return la valeur courante de l'attribut id affilie
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * getter pour l'attribut id situation professionnelle
     * 
     * @return la valeur courante de l'attribut id situation professionnelle
     */
    public String getIdSituationProfessionnelle() {
        return idSituationProfessionnelle;
    }

    /**
     * getter pour l'attribut id tiers
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * getter pour l'attribut revenu moyen determinant sans arrondi
     * 
     * @return la valeur courante de l'attribut revenu moyen determinant sans arrondi
     */
    public FWCurrency getRevenuMoyenDeterminantSansArrondi() {
        return revenuMoyenDeterminantSansArrondi;
    }

    /**
     * getter pour l'attribut salaire journalier
     * 
     * @return la valeur courante de l'attribut salaire journalier
     */
    public FWCurrency getSalaireJournalier() {
        return salaireJournalier;
    }

    /**
     * getter pour l'attribut salaire journalier verse
     * 
     * @return la valeur courante de l'attribut salaire journalier verse
     */
    public FWCurrency getSalaireJournalierVerse() {
        return salaireJournalierVerse;
    }

    /**
     * @return
     */
    public FWCurrency getTL() {
        return TL;
    }

    /**
     * getter pour l'attribut collaborateur agricole
     * 
     * @return la valeur courante de l'attribut collaborateur agricole
     */
    public boolean isCollaborateurAgricole() {
        return isCollaborateurAgricole;
    }

    /**
     * getter pour l'attribut independant
     * 
     * @return la valeur courante de l'attribut independant
     */
    public boolean isIndependant() {
        return isIndependant;
    }

    /**
     * getter pour l'attribut soumis cotisation
     * 
     * @return la valeur courante de l'attribut soumis cotisation
     */
    public boolean isSoumisCotisation() {
        return isSoumisCotisation;
    }

    /**
     * @return
     */
    public boolean isTravailleurAgricole() {
        return isTravailleurAgricole;
    }

    /**
     * getter pour l'attribut travailleur sans employeur
     * 
     * @return la valeur courante de l'attribut travailleur sans employeur
     */
    public boolean isTravailleurSansEmployeur() {
        return isTravailleurSansEmployeur;
    }

    /**
     * getter pour l'attribut versement employeur
     * 
     * @return la valeur courante de l'attribut versement employeur
     */
    public boolean isVersementEmployeur() {
        return isVersementEmployeur;
    }

    /**
     * setter pour l'attribut collaborateur agricole
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setCollaborateurAgricole(boolean b) {
        isCollaborateurAgricole = b;
    }

    /**
     * setter pour l'attribut id affilie
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAffilie(String string) {
        idAffilie = string;
    }

    /**
     * setter pour l'attribut id situation professionnelle
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationProfessionnelle(String string) {
        idSituationProfessionnelle = string;
    }

    /**
     * setter pour l'attribut id tiers
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * setter pour l'attribut independant
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setIndependant(boolean b) {
        isIndependant = b;
    }

    /**
     * setter pour l'attribut nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * setter pour l'attribut revenu moyen determinant sans arrondi
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setRevenuMoyenDeterminantSansArrondi(FWCurrency currency) {
        revenuMoyenDeterminantSansArrondi = currency;
    }

    /**
     * setter pour l'attribut salaire journalier
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setSalaireJournalier(FWCurrency currency) {
        salaireJournalier = currency;
    }

    /**
     * setter pour l'attribut salaire journalier verse
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setSalaireJournalierVerse(FWCurrency currency) {
        salaireJournalierVerse = currency;
    }

    /**
     * setter pour l'attribut soumis cotisation
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setSoumisCotisation(boolean b) {
        isSoumisCotisation = b;
    }

    /**
     * @param currency
     */
    public void setTL(FWCurrency currency) {
        TL = currency;
    }

    /**
     * @param b
     */
    public void setTravailleurAgricole(boolean b) {
        isTravailleurAgricole = b;
    }

    /**
     * setter pour l'attribut travailleur sans employeur
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setTravailleurSansEmployeur(boolean b) {
        isTravailleurSansEmployeur = b;
    }

    /**
     * setter pour l'attribut versement employeur
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setVersementEmployeur(boolean b) {
        isVersementEmployeur = b;
    }

}
