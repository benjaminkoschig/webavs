/*
 * Créé le 12 févr. 07
 */
package globaz.naos.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un entity contenant toutes les informations necessaires à l'impression des contrôles attribués pour les contrôles
 * employeurs
 * </p>
 * 
 * <p>
 * Cet entity est un peu special, il ne peut qu'etre lu. Le seul moyen d'en obtenir une instance correcte est de passer
 * par le manager correspondant.
 * </p>
 * 
 * @author hpe
 */
public class AFControlesAttribues extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebutControle = "";
    private String dateFinControle = "";
    private String datePrecControle = "";
    private String idAffilie = "";
    private String idTiers = "";
    private String montantMasse_1 = "";
    private String montantMasse_2 = "";
    private String montantMasse_3 = "";
    private String montantMasse_4 = "";
    private String montantMasse_5 = "";
    private String nbInscCI = "";
    private String numAffilie = "";
    private String tempsJour = "";
    private String typeControle = "";
    private String visaReviseur = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return null; // PAS DE TABLES !!!
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAffilie = statement.dbReadNumeric("MAIAFF");
        idTiers = statement.dbReadNumeric("HTITIE");
        numAffilie = statement.dbReadNumeric("MALNAF");
        dateDebutControle = statement.dbReadDateAMJ("MDDCDE");
        dateFinControle = statement.dbReadDateAMJ("MDDCFI");
        datePrecControle = statement.dbReadDateAMJ("MDDPRC");
        montantMasse_1 = statement.dbReadNumeric("MASSE1");
        montantMasse_2 = statement.dbReadNumeric("MASSE2");
        montantMasse_3 = statement.dbReadNumeric("MASSE3");
        montantMasse_4 = statement.dbReadNumeric("MASSE4");
        montantMasse_5 = statement.dbReadNumeric("MASSE5");
        nbInscCI = statement.dbReadNumeric("NBCI");
        tempsJour = statement.dbReadNumeric("MDNTJO");
        visaReviseur = statement.dbReadString("MDLNOM");
        typeControle = statement.dbReadNumeric("MDTGEN");
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _addError(statement.getTransaction(), "interdit d'ajouter");
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    /**
     * @return
     */
    public String getDateDebutControle() {
        return dateDebutControle;
    }

    /**
     * @return
     */
    public String getDateFinControle() {
        return dateFinControle;
    }

    /**
     * @return
     */
    public String getDatePrecControle() {
        return datePrecControle;
    }

    /**
     * @return
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    public String getMontantMasse_1() {
        return montantMasse_1;
    }

    /**
     * @return
     */
    public String getMontantMasse_2() {
        return montantMasse_2;
    }

    /**
     * @return
     */
    public String getMontantMasse_3() {
        return montantMasse_3;
    }

    /**
     * @return
     */
    public String getMontantMasse_4() {
        return montantMasse_4;
    }

    /**
     * @return
     */
    public String getMontantMasse_5() {
        return montantMasse_5;
    }

    /**
     * @return
     */
    public String getNbInscCI() {
        return nbInscCI;
    }

    /**
     * @return
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @return
     */
    public String getTempsJour() {
        return tempsJour;
    }

    /**
     * @return
     */
    public String getTypeControle() {
        return typeControle;
    }

    /**
     * @return
     */
    public String getVisaReviseur() {
        return visaReviseur;
    }

    /**
     * @param string
     */
    public void setDateDebutControle(String string) {
        dateDebutControle = string;
    }

    /**
     * @param string
     */
    public void setDateFinControle(String string) {
        dateFinControle = string;
    }

    /**
     * @param string
     */
    public void setDatePrecControle(String string) {
        datePrecControle = string;
    }

    /**
     * @param string
     */
    public void setIdAffilie(String string) {
        idAffilie = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setMontantMasse_1(String string) {
        montantMasse_1 = string;
    }

    /**
     * @param string
     */
    public void setMontantMasse_2(String string) {
        montantMasse_2 = string;
    }

    /**
     * @param string
     */
    public void setMontantMasse_3(String string) {
        montantMasse_3 = string;
    }

    /**
     * @param string
     */
    public void setMontantMasse_4(String string) {
        montantMasse_4 = string;
    }

    /**
     * @param string
     */
    public void setMontantMasse_5(String string) {
        montantMasse_5 = string;
    }

    /**
     * @param string
     */
    public void setNbInscCI(String string) {
        nbInscCI = string;
    }

    /**
     * @param string
     */
    public void setNumAffilie(String string) {
        numAffilie = string;
    }

    /**
     * @param string
     */
    public void setTempsJour(String string) {
        tempsJour = string;
    }

    /**
     * @param string
     */
    public void setTypeControle(String string) {
        typeControle = string;
    }

    /**
     * @param string
     */
    public void setVisaReviseur(String string) {
        visaReviseur = string;
    }

}
