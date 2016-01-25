package globaz.pavo.api.helper;

import globaz.globall.api.BITransaction;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.pavo.api.ICIEcriture;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICIEcritureHelper extends GlobazHelper implements ICIEcriture {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICIEcritureHelper
     */
    public ICIEcritureHelper() {
        super("globaz.pavo.db.compte.CIEcriture");
    }

    /**
     * Constructeur du type ICIEcritureHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICIEcritureHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICIEcritureHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICIEcritureHelper(String implementationClassName) {
        super(implementationClassName);
    }

    /**
     * Passe l'inscription au CI.
     * 
     * @param transaction
     *            la transasction à utiliser pour l'opération.
     * @exception si
     *                l'appel distant de la fonction n'a pas pu être effectué.
     */
    @Override
    public void comptabiliser(BITransaction transaction) throws Exception {
        _execute("comptabiliser", new Object[] { transaction });
    }

    @Override
    public String getAnnee() {
        return (String) _getValueObject().getProperty("annee");
    }

    @Override
    public java.lang.String getAvs() {
        return (java.lang.String) _getValueObject().getProperty("avs");
    }

    @Override
    public String getBrancheEconomique() {
        return (String) _getValueObject().getProperty("brancheEconomique");
    }

    @Override
    public String getCaisseChomage() {
        return (String) _getValueObject().getProperty("caisseChomage");
    }

    @Override
    public String getCategoriePersonnel() {
        return (String) _getValueObject().getProperty("categoriePersonnel");
    }

    @Override
    public String getCode() {
        return (String) _getValueObject().getProperty("code");
    }

    @Override
    public String getCodeSpecial() {
        return (String) _getValueObject().getProperty("codeSpecial");
    }

    @Override
    public String getDateAnnonceCentrale() {
        return (String) _getValueObject().getProperty("dateAnnonceCentrale");
    }

    @Override
    public String getDateCiAdditionnel() {
        return (String) _getValueObject().getProperty("dateCiAdditionnel");
    }

    /**
     * Retourne la date d'inscription en fonction le l'espion saisie. Date de création : (10.01.2003 11:55:57)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDateInscription() {
        return (String) _getValueObject().getProperty("dateInscription");
    }

    @Override
    public String getEcritureId() {
        return (String) _getValueObject().getProperty("ecritureId");
    }

    @Override
    public String getGre() {
        return (String) _getValueObject().getProperty("greFormat");
    }

    @Override
    public String getIdJournal() {
        return (String) _getValueObject().getProperty("idJournal");
    }

    @Override
    public String getIdTypeCompte() {
        return (String) _getValueObject().getProperty("idTypeCompte");
    }

    @Override
    public String getMoisDebut() {
        return (String) _getValueObject().getProperty("moisDebut");
    }

    @Override
    public String getMoisFin() {
        return (String) _getValueObject().getProperty("moisFin");
    }

    @Override
    public String getMontant() {
        return (String) _getValueObject().getProperty("montant");
    }

    /**
     * Retourne le montant signé de l'inscription.<br>
     * Le montant est positif si la propriété <tt>extourne</tt> est 0, 6 ou 8, et négative dans les autres cas. . Date
     * de création : (12.11.2002 13:46:16)
     * 
     * @return le montant signé.
     */
    @Override
    public String getMontantSigne() {
        return (String) _getValueObject().getProperty("montantSigne");
    }

    @Override
    public String getPartBta() {
        return (String) _getValueObject().getProperty("partBta");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getRemarque() {
        return (java.lang.String) _getValueObject().getProperty("remarque");
    }

    @Override
    public void setAnnee(String newAnnee) {
        _getValueObject().setProperty("annee", newAnnee);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 12:00:34)
     * 
     * @param newAVS
     *            java.lang.String
     */
    @Override
    public void setAvs(java.lang.String newAvs) {
        _getValueObject().setProperty("avs", newAvs);
    }

    @Override
    public void setBrancheEconomique(String newBrancheEconomique) {
        _getValueObject().setProperty("brancheEconomique", newBrancheEconomique);
    }

    @Override
    public void setCaisseChomage(String newCaisseChomage) {
        _getValueObject().setProperty("caisseChomage", newCaisseChomage);
    }

    @Override
    public void setCategoriePersonnel(String newCategoriePersonnel) {
        _getValueObject().setProperty("categoriePersonnel", newCategoriePersonnel);
    }

    @Override
    public void setCode(String newCode) {
        _getValueObject().setProperty("code", newCode);
    }

    @Override
    public void setCodeSpecial(String newCodeSpecial) {
        _getValueObject().setProperty("codeSpecial", newCodeSpecial);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    @Override
    public void setEcritureId(String newEcritureId) {
        _getValueObject().setProperty("ecritureId", newEcritureId);
    }

    @Override
    public void setEmployeurPartenaire(String newEmployeurPartenaire) {
        _getValueObject().setProperty("employeurPartenaire", newEmployeurPartenaire);
    }

    @Override
    public void setGre(String newGre) {
        _getValueObject().setProperty("gre", newGre);
    }

    @Override
    public void setIdJournal(String newIdJournal) {
        _getValueObject().setProperty("idJournal", newIdJournal);
    }

    @Override
    public void setIdTypeCompte(String newIdTypeCompte) {
        _getValueObject().setProperty("idTypeCompte", newIdTypeCompte);
    }

    @Override
    public void setMoisDebut(String newMoisDebut) {
        _getValueObject().setProperty("moisDebut", newMoisDebut);
    }

    @Override
    public void setMoisFin(String newMoisFin) {
        _getValueObject().setProperty("moisFin", newMoisFin);
    }

    @Override
    public void setMontant(String newMontant) {
        _getValueObject().setProperty("montant", newMontant);
    }

    @Override
    public void setPartBta(String newPartBta) {
        _getValueObject().setProperty("partBta", newPartBta);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @param newRemarque
     *            java.lang.String
     */
    @Override
    public void setRemarque(java.lang.String newRemarque) {
        _getValueObject().setProperty("remarque", newRemarque);
    }
}
