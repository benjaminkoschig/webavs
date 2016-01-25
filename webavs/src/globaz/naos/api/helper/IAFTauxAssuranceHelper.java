package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFTauxAssurance;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author sau
 */
public class IAFTauxAssuranceHelper extends GlobazHelper implements IAFTauxAssurance {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String[] METHODS_TO_LOAD = new String[] { "getAssuranceId", "getDateDebut", "getDateFin",
            "getFraction", "getFractionEmploye", "getFractionEmployeur", "getGenreValeur", "getPeriodiciteMontant",
            "getRang", "getSexe", "getTauxAssuranceId", "getTranche", "getValeurEmploye", "getValeurEmployeur",
            "getIsAPI" };

    /**
     * Constructeur du type IAFTauxAssuranceHelper
     */
    public IAFTauxAssuranceHelper() {
        super("globaz.naos.db.tauxAssurance.AFTauxAssurance");
        setMethodsToLoad(IAFTauxAssuranceHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFTauxAssuranceHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IAFTauxAssuranceHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFTauxAssuranceHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFTauxAssuranceHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IAFTauxAssuranceHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFTauxAssuranceHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * @see globaz.naos.api.IAFAdhesion#find(java.util.Hashtable)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public IAFTauxAssurance[] findTaux(Hashtable params) throws Exception {
        IAFTauxAssurance[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFTauxAssuranceHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject adhesion = (GlobazValueObject) objResult[i];
                result[i] = new IAFTauxAssuranceHelper(adhesion);
            }
        }
        return result;
    }

    /**
     * Retourne l'id de l'assurnce liée
     * 
     * @return id de l'assurnce liée
     */
    @Override
    public java.lang.String getAssuranceId() {
        return (java.lang.String) _getValueObject().getProperty("assuranceId");
    }

    /**
     * Retourne la date de début du taux
     * 
     * @return la date de début du taux
     */
    @Override
    public java.lang.String getDateDebut() {
        return (java.lang.String) _getValueObject().getProperty("dateDebut");
    }

    /**
     * Retourne la fraction du taux spécifié (valeur=5 et fration=100 pour 5%)
     * 
     * @return la fraction du taux spécifié
     */
    @Override
    public java.lang.String getFraction() {
        return (java.lang.String) _getValueObject().getProperty("fraction");
    }

    /**
     * Retourne la fraction du taux de la partie employé
     * 
     * @return fraction du taux de la partie employé
     */
    @Override
    public java.lang.String getFractionEmploye() {
        return (java.lang.String) _getValueObject().getProperty("fractionEmploye");
    }

    /**
     * Retourne la fraction du taux de la partie employeur
     * 
     * @return fraction du taux de la partie employeur
     */
    @Override
    public java.lang.String getFractionEmployeur() {
        return (java.lang.String) _getValueObject().getProperty("fractionEmployeur");
    }

    /**
     * Retourne le genre de la valeur (montant ou taux)
     * 
     * @return le genre de la valeur (montant ou taux)
     */
    @Override
    public java.lang.String getGenreValeur() {
        return (java.lang.String) _getValueObject().getProperty("genreValeur");
    }

    /**
     * Retourne la périodicité pour un montant donné (valeur = 6'000 et périodicité = semestriel pour une assurance de
     * 1'000/mois)
     * 
     * @return la périodicité pour un montant donné
     */
    @Override
    public java.lang.String getPeriodiciteMontant() {
        return (java.lang.String) _getValueObject().getProperty("periodiciteMontant");
    }

    /**
     * Retourne le rang pour taux variables
     * 
     * @return rang pour taux variables
     */
    @Override
    public java.lang.String getRang() {
        return (java.lang.String) _getValueObject().getProperty("rang");
    }

    /**
     * Retourne le sexe concerné pour le taux donné si spécifique ou vide si global
     * 
     * @return sexe concerné pour le taux donné si spécifique ou vide si global
     */
    @Override
    public java.lang.String getSexe() {
        return (java.lang.String) _getValueObject().getProperty("sexe");
    }

    /**
     * Retourne l'id de l'objet
     * 
     * @return id de l'objet
     */
    @Override
    public java.lang.String getTauxAssuranceId() {
        return (java.lang.String) _getValueObject().getProperty("tauxAssuranceId");
    }

    /**
     * Retourne la tranche suppérieur pour taux variables
     * 
     * @return tranche suppérieur pour taux variables
     */
    @Override
    public java.lang.String getTranche() {
        return (java.lang.String) _getValueObject().getProperty("tranche");
    }

    /**
     * Retourne la valeur de la partie employé
     * 
     * @return la valeur de la partie employé
     */
    @Override
    public java.lang.String getValeurEmploye() {
        return (java.lang.String) _getValueObject().getProperty("valeurEmploye");
    }

    /**
     * Retourne la valeur de la partie employeur
     * 
     * @return la valeur de la partie employeur
     */
    @Override
    public java.lang.String getValeurEmployeur() {
        return (java.lang.String) _getValueObject().getProperty("valeurEmployeur");
    }

    /**
     * Définit l'id de l'assurnce liée
     * 
     * @param newAssuranceId
     *            id de l'assurnce liée
     */
    @Override
    public void setAssuranceId(java.lang.String newAssuranceId) {
        _getValueObject().setProperty("assuranceId", newAssuranceId);
    }

    /**
     * Définit la date de début du taux
     * 
     * @param newDateDebut
     *            la date de début du taux
     */
    @Override
    public void setDateDebut(java.lang.String newDateDebut) {
        _getValueObject().setProperty("dateDebut", newDateDebut);
    }

    /**
     * Définit la fraction du taux spécifié (valeur=5 et fration=100 pour 5%)
     * 
     * @param newFraction
     *            la fraction du taux spécifié
     */
    @Override
    public void setFraction(java.lang.String newFraction) {
        _getValueObject().setProperty("fraction", newFraction);
    }

    /**
     * Définit la fraction du taux de la partie employé
     * 
     * @param string
     *            fraction du taux de la partie employé
     */
    @Override
    public void setFractionEmploye(java.lang.String string) {
        _getValueObject().setProperty("fractionEmploye", string);
    }

    /**
     * Définit la fraction du taux de la partie employeur
     * 
     * @param string
     *            fraction du taux de la partie employeur
     */
    @Override
    public void setFractionEmployeur(java.lang.String string) {
        _getValueObject().setProperty("fractionEmployeur", string);
    }

    /**
     * Définit le genre de la valeur (montant ou taux)
     * 
     * @param string
     *            le genre de la valeur (montant ou taux)
     */
    @Override
    public void setGenreValeur(java.lang.String string) {
        _getValueObject().setProperty("genreValeur", string);
    }

    /**
     * Définit la périodicité pour un montant donné (valeur = 6'000 et périodicité = semestriel pour une assurance de
     * 1'000/mois)
     * 
     * @param newPeriodiciteMontant
     *            la périodicité pour un montant donné
     */
    @Override
    public void setPeriodiciteMontant(java.lang.String newPeriodiciteMontant) {
        _getValueObject().setProperty("periodiciteMontant", newPeriodiciteMontant);
    }

    /**
     * Définit le rang pour taux variables
     * 
     * @param string
     *            rang pour taux variables
     */
    @Override
    public void setRang(java.lang.String string) {
        _getValueObject().setProperty("rang", string);
    }

    /**
     * Définit le sexe concerné pour le taux donné si spécifique ou vide si global
     * 
     * @param string
     *            sexe concerné pour le taux donné si spécifique ou vide si global
     */
    @Override
    public void setSexe(java.lang.String string) {
        _getValueObject().setProperty("sexe", string);
    }

    /**
     * Définit l'id de l'objet
     * 
     * @param newTauxAssuranceId
     *            id de l'objet
     */
    @Override
    public void setTauxAssuranceId(java.lang.String newTauxAssuranceId) {
        _getValueObject().setProperty("tauxAssuranceId", newTauxAssuranceId);
    }

    /**
     * Définit la tranche suppérieur pour taux variables
     * 
     * @param string
     *            tranche suppérieur pour taux variables
     */
    @Override
    public void setTranche(java.lang.String string) {
        _getValueObject().setProperty("tranche", string);
    }

    /**
     * Définit la valeur de la partie employé
     * 
     * @param string
     *            la valeur de la partie employé
     */
    @Override
    public void setValeurEmploye(java.lang.String string) {
        _getValueObject().setProperty("valeurEmploye", string);
    }

    /**
     * Définit la valeur de la partie employeur
     * 
     * @param string
     *            la valeur de la partie employeur
     */
    @Override
    public void setValeurEmployeur(java.lang.String string) {
        _getValueObject().setProperty("valeurEmployeur", string);
    }

}
