/*
 * Créé le 14 févr. 07
 */
package globaz.naos.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un entity contenant toutes les informations necessaires à l'impression des contrôles à effectuer pour les contrôles
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

public class AFControlesAEffectuer extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String affDateDebut = "";
    private String affDateFin = "";
    private String dateDebutControle = "";
    private String dateEffective = "";
    private String dateFinControle = "";
    private String datePrecControle = "";
    private String datePrevue = "";
    private String idAffilie = "";
    private String idControle = "";
    private String idTiers = "";
    private String montantMasse_1 = "";
    private String montantMasse_2 = "";
    private String montantMasse_3 = "";
    private String montantMasse_4 = "";
    private String nbInscCI = "";
    private String numAffilie = "";
    private String periodiciteAff = "";
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
        numAffilie = statement.dbReadNumeric("MALNAF");
        dateDebutControle = statement.dbReadNumeric("MDDCDE");
        dateFinControle = statement.dbReadNumeric("MDDCFI");
        datePrecControle = statement.dbReadDateAMJ("MDDPRC");
        visaReviseur = statement.dbReadString("MDLNOM");
        idTiers = statement.dbReadNumeric("HTITIE");
        nbInscCI = statement.dbReadNumeric("NBCI");
        periodiciteAff = statement.dbReadNumeric("PERIODICITE");
        tempsJour = statement.dbReadNumeric("MDNTJO");
        montantMasse_1 = statement.dbReadNumeric("MASSE1");
        montantMasse_2 = statement.dbReadNumeric("MASSE2");
        montantMasse_3 = statement.dbReadNumeric("MASSE3");
        montantMasse_4 = statement.dbReadNumeric("MASSE4");
        typeControle = statement.dbReadNumeric("MDTGEN");
        datePrevue = statement.dbReadNumeric("MDDPRE");
        affDateDebut = statement.dbReadNumeric("MADDEB");
        affDateFin = statement.dbReadNumeric("MADFIN");
        dateEffective = statement.dbReadNumeric("MDDEFF");
        idControle = statement.dbReadNumeric("MDICON");
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
    public String getAffDateDebut() {
        return affDateDebut;
    }

    /**
     * @return
     */
    public String getAffDateFin() {
        return affDateFin;
    }

    /**
     * @return
     */
    public String getDateDebutControle() {
        return dateDebutControle;
    }

    public String getDateEffective() {
        return dateEffective;
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
    public String getDatePrevue() {
        return datePrevue;
    }

    public String getDateReviseurPrecControle() {
        String s = "";
        AFControleEmployeurManager controleMana = new AFControleEmployeurManager();
        controleMana.setSession(getSession());
        controleMana.setForAffiliationId(getIdAffilie());
        if (!JadeStringUtil.isEmpty(getDatePrecControle())) {
            controleMana.setForAnnee(getDatePrecControle().substring(6));
            try {
                controleMana.find();
                if (controleMana.size() > 0) {
                    AFControleEmployeur controle = (AFControleEmployeur) controleMana.getFirstEntity();
                    s = controle.getAnnee() + " / " + controle.getControleurVisa();
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }
        return s;
    }

    /**
     * @return
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdControle() {
        return idControle;
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
    public String getPeriodiciteAff() {
        return periodiciteAff;
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
    public void setAffDateDebut(String string) {
        affDateDebut = string;
    }

    /**
     * @param string
     */
    public void setAffDateFin(String string) {
        affDateFin = string;
    }

    /**
     * @param string
     */
    public void setDateDebutControle(String string) {
        dateDebutControle = string;
    }

    public void setDateEffective(String dateEffective) {
        this.dateEffective = dateEffective;
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
    public void setDatePrevue(String string) {
        datePrevue = string;
    }

    /**
     * @param string
     */
    public void setIdAffilie(String string) {
        idAffilie = string;
    }

    public void setIdControle(String idControle) {
        this.idControle = idControle;
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
    public void setPeriodiciteAff(String string) {
        periodiciteAff = string;
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
