package globaz.apg.module.calcul;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JADate;

/**
 * Descpription
 *
 * @author scr Date de création 23 mai 05
 */
public class APPrestationCalculee {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private FWCurrency basicDailyAmount = null;

    private String contenuAnnonce = null;
    private JADate dateDebut = null;
    private JADate dateFin = null;
    private FWCurrency droitAcquis = null;
    private String etat = null;
    private FWCurrency fraisGarde = null;

    private String idDroit = null;
    private FWCurrency montantJournalier = null;
    private String nombreJoursSoldes = null;
    private APResultatCalcul resultatCalcul = null;
    private FWCurrency revenuDeterminantMoyen = null;
    private String typePrestation = null;
    private String csGenrePrestation = null;
    private String idPrestationApg = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APPrestationCalculee.
     */
    public APPrestationCalculee() {
        super();
    }

    /**
     * @return the basicDailyAmount
     */
    public FWCurrency getBasicDailyAmount() {
        return basicDailyAmount;
    }

    /**
     * getter pour l'attribut contenu annonce
     *
     * @return la valeur courante de l'attribut contenu annonce
     */
    public String getContenuAnnonce() {
        return contenuAnnonce;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut
     *
     * @return la valeur courante de l'attribut date debut
     */
    public JADate getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin
     *
     * @return la valeur courante de l'attribut date fin
     */
    public JADate getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut droit acquis
     *
     * @return la valeur courante de l'attribut droit acquis
     */
    public FWCurrency getDroitAcquis() {
        return droitAcquis;
    }

    /**
     * getter pour l'attribut etat
     *
     * @return la valeur courante de l'attribut etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * getter pour l'attribut frais garde
     *
     * @return la valeur courante de l'attribut frais garde
     */
    public FWCurrency getFraisGarde() {
        return fraisGarde;
    }

    /**
     * getter pour l'attribut id droit
     *
     * @return la valeur courante de l'attribut id droit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * getter pour l'attribut montant journalier
     *
     * @return la valeur courante de l'attribut montant journalier
     */
    public FWCurrency getMontantJournalier() {
        return montantJournalier;
    }

    /**
     * getter pour l'attribut nombre jours soldes
     *
     * @return la valeur courante de l'attribut nombre jours soldes
     */
    public String getNombreJoursSoldes() {
        return nombreJoursSoldes;
    }

    /**
     * getter pour l'attribut resultat calcul
     *
     * @return la valeur courante de l'attribut resultat calcul
     */
    public APResultatCalcul getResultatCalcul() {
        return resultatCalcul;
    }

    /**
     * getter pour l'attribut revenu determinant moyen
     *
     * @return la valeur courante de l'attribut revenu determinant moyen
     */
    public FWCurrency getRevenuDeterminantMoyen() {
        return revenuDeterminantMoyen;
    }

    /**
     * getter pour l'attribut type prestation
     *
     * @return la valeur courante de l'attribut type prestation
     */
    public String getTypePrestation() {
        return typePrestation;
    }

    /**
     * @param basicDailyAmount
     *                             the basicDailyAmount to set
     */
    public void setBasicDailyAmount(FWCurrency basicDailyAmount) {
        this.basicDailyAmount = basicDailyAmount;
    }

    /**
     * setter pour l'attribut contenu annonce
     *
     * @param string
     *                   une nouvelle valeur pour cet attribut
     */
    public void setContenuAnnonce(String string) {
        contenuAnnonce = string;
    }

    /**
     * setter pour l'attribut date debut
     *
     * @param date
     *                 une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(JADate date) {
        dateDebut = date;
    }

    /**
     * setter pour l'attribut date fin
     *
     * @param date
     *                 une nouvelle valeur pour cet attribut
     */
    public void setDateFin(JADate date) {
        dateFin = date;
    }

    /**
     * setter pour l'attribut droit acquis
     *
     * @param currency
     *                     une nouvelle valeur pour cet attribut
     */
    public void setDroitAcquis(FWCurrency currency) {
        droitAcquis = currency;
    }

    /**
     * setter pour l'attribut etat
     *
     * @param s
     *              une nouvelle valeur pour cet attribut
     */
    public void setEtat(String s) {
        etat = s;
    }

    /**
     * setter pour l'attribut frais garde
     *
     * @param currency
     *                     une nouvelle valeur pour cet attribut
     */
    public void setFraisGarde(FWCurrency currency) {
        fraisGarde = currency;
    }

    /**
     * setter pour l'attribut id droit
     *
     * @param string
     *                   une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(String string) {
        idDroit = string;
    }

    /**
     * setter pour l'attribut montant journalier
     *
     * @param currency
     *                     une nouvelle valeur pour cet attribut
     */
    public void setMontantJournalier(FWCurrency currency) {
        montantJournalier = currency;
    }

    /**
     * setter pour l'attribut nombre jours soldes
     *
     * @param s
     *              une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursSoldes(String s) {
        nombreJoursSoldes = s;
    }

    /**
     * setter pour l'attribut resultat calcul
     *
     * @param calcul
     *                   une nouvelle valeur pour cet attribut
     */
    public void setResultatCalcul(APResultatCalcul calcul) {
        resultatCalcul = calcul;
    }

    /**
     * setter pour l'attribut revenu determinant moyen
     *
     * @param currency
     *                     une nouvelle valeur pour cet attribut
     */
    public void setRevenuDeterminantMoyen(FWCurrency currency) {
        revenuDeterminantMoyen = currency;
    }

    /**
     * setter pour l'attribut type prestation
     *
     * @param s
     *              une nouvelle valeur pour cet attribut
     */
    public void setTypePrestation(String s) {
        typePrestation = s;
    }

    public String getCsGenrePrestation() {
        return csGenrePrestation;
    }

    public void setCsGenrePrestation(String csGenrePrestation) {
        this.csGenrePrestation = csGenrePrestation;
    }

    public String getIdPrestationApg() {
        return idPrestationApg;
    }

    public void setIdPrestationApg(String idPrestationApg) {
        this.idPrestationApg = idPrestationApg;
    }
}
