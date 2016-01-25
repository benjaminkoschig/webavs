package globaz.apg.module.calcul;

import globaz.framework.util.FWCurrency;
import java.math.BigDecimal;

/**
 * Descpription
 * 
 * @author scr Date de création 31 mai 05
 */
public class APResultatCalculSituationProfessionnel {

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
    private FWCurrency montant = null;
    private String noAffilie = "";
    private String nom = null;
    private FWCurrency salaireJournalierNonArrondi = null;
    private FWCurrency salaireJournalierVerse = null;
    // valeur par défaut.
    // Obligatoire, car dans certain cas, CAOR ne retourne par cette
    // Information -> nullPointerException.
    private FWCurrency tauxProRata = new FWCurrency(1);

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
     * Retourne le montant versé à cet employeur suivant le montant total de la prestation.
     * <p>
     * Le montant retourné est soit le résultat de la multiplication de montantBase et du tauxProRata ou alors, s'il est
     * renseigné, la valeur exacte attribuée au moyen de la méthode setMontant de cette classe.
     * </p>
     * 
     * @param montantBase
     *            le montant de base pour le calcul du montant versé à cet employeur.
     * @return la valeur courante de l'attribut montant
     */
    public BigDecimal getMontant(BigDecimal montantBase) {
        if (montant != null) {
            return montant.getBigDecimalValue();
        } else {
            return montantBase.multiply(tauxProRata.getBigDecimalValue());
        }
    }

    /**
     * @return the noAffilie
     */
    public final String getNoAffilie() {
        return noAffilie;
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
     * getter pour l'attribut salaire journalier sans arrondi
     * 
     * @return la valeur courante de l'attribut salaire journalier sans arrondi
     */
    public FWCurrency getSalaireJournalierNonArrondi() {
        return salaireJournalierNonArrondi;
    }

    /**
     * getter pour l'attribut salaire journalier verse
     * 
     * @return la valeur courante de l'attribut salaire journalier verse
     */
    public FWCurrency getSalaireJournalierVerse() {
        if (salaireJournalierVerse == null) {
            return new FWCurrency(0);
        } else {
            return salaireJournalierVerse;
        }
    }

    /**
     * getter pour l'attribut taux pro rata
     * 
     * @return la valeur courante de l'attribut taux pro rata
     */
    public FWCurrency getTauxProRata() {
        return tauxProRata;
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
     * setter pour l'attribut montant
     * 
     * @param montant
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontant(FWCurrency montant) {
        this.montant = montant;
    }

    /**
     * @param noAffilie
     *            the noAffilie to set
     */
    public final void setNoAffilie(String noAffilie) {
        this.noAffilie = noAffilie;
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
     * setter pour l'attribut salaire journalier sans arrondi
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setSalaireJournalierNonArrondi(FWCurrency currency) {
        salaireJournalierNonArrondi = currency;
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
     * setter pour l'attribut taux pro rata
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setTauxProRata(FWCurrency currency) {
        tauxProRata = currency;
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
