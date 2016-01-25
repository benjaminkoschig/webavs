/*
 * Créé le 25 juil. 07
 */
package globaz.corvus.db.prestations;

import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.REOrdresVersementsManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author HPE
 * 
 */
public class REPrestations extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ETAT = "YUTETA";
    public static final String FIELDNAME_ID_DECISION = "YUIDEC";
    public static final String FIELDNAME_ID_DEMANDE_RENTE = "YUIDEM";
    public static final String FIELDNAME_ID_LOT = "YUILOT";
    public static final String FIELDNAME_ID_PRESTATION = "YUIPRE";
    public static final String FIELDNAME_MOIS_ANNEE = "YUDMAN";
    public static final String FIELDNAME_MONTANT_PRESTATION = "YUMPRS";
    public static final String FIELDNAME_TYPE = "YUTTYP";
    public static final String TABLE_NAME_PRESTATION = "REPREST";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtat = "";
    private String csType = "";
    private String idDecision = "";
    private String idDemandeRente = "";
    private String idLot = "";
    private String idPrestation = "";
    private String moisAnnee = "";
    private String montantPrestation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdPrestation(_incCounter(transaction, "0"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_PRESTATION;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idPrestation = statement.dbReadNumeric(FIELDNAME_ID_PRESTATION);
        idLot = statement.dbReadNumeric(FIELDNAME_ID_LOT);
        csEtat = statement.dbReadNumeric(FIELDNAME_ETAT);
        moisAnnee = statement.dbReadNumeric(FIELDNAME_MOIS_ANNEE);
        csType = statement.dbReadNumeric(FIELDNAME_TYPE);
        idDemandeRente = statement.dbReadNumeric(FIELDNAME_ID_DEMANDE_RENTE);
        montantPrestation = statement.dbReadNumeric(FIELDNAME_MONTANT_PRESTATION);
        idDecision = statement.dbReadNumeric(FIELDNAME_ID_DECISION);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_PRESTATION,
                _dbWriteNumeric(statement.getTransaction(), idPrestation, "idPrestation"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_PRESTATION,
                _dbWriteNumeric(statement.getTransaction(), idPrestation, "idPrestation"));
        statement.writeField(FIELDNAME_ID_LOT, _dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
        statement.writeField(FIELDNAME_ETAT, _dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(FIELDNAME_MOIS_ANNEE, _dbWriteNumeric(statement.getTransaction(), moisAnnee, "moisAnnee"));
        statement.writeField(FIELDNAME_TYPE, _dbWriteNumeric(statement.getTransaction(), csType, "csType"));
        statement.writeField(FIELDNAME_ID_DEMANDE_RENTE,
                _dbWriteNumeric(statement.getTransaction(), idDemandeRente, "idDemandeRente"));
        statement.writeField(FIELDNAME_ID_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(FIELDNAME_MONTANT_PRESTATION,
                _dbWriteNumeric(statement.getTransaction(), montantPrestation, "montantPrestation"));

    }

    /**
     * @return
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return
     */
    public String getCsType() {
        return csType;
    }

    public String getIdDecision() {
        return idDecision;
    }

    /**
     * @return
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * @return
     */
    public String getIdPrestation() {
        return idPrestation;
    }

    /**
     * @return
     */
    public String getMoisAnnee() {
        return moisAnnee;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public REOrdresVersements[] getOrdresVersement(BTransaction transaction) throws Exception {

        REOrdresVersementsManager mgr = new REOrdresVersementsManager();
        mgr.setSession(getSession());
        mgr.setForIdPrestation(getIdPrestation());
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        return (REOrdresVersements[]) mgr.getContainer().toArray(new REOrdresVersements[mgr.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * @param string
     */
    public void setCsEtat(String string) {
        csEtat = string;
    }

    /**
     * @param string
     */
    public void setCsType(String string) {
        csType = string;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * @param string
     */
    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    /**
     * @param string
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    /**
     * @param string
     */
    public void setIdPrestation(String string) {
        idPrestation = string;
    }

    /**
     * @param string
     */
    public void setMoisAnnee(String string) {
        moisAnnee = string;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    // /**
    // * Retourne au tableau de String (2 éléments max) contenant
    // * respectivement la date de début et date de fin de la période de
    // validité de la prestation.
    // *
    // * @return String[2]
    // * String[0] == Date de début de la prestation [MMxAAAA]
    // * String[1] == Date de fin de la prestation [MMxAAAA]
    // *
    // * String[1] peut être vide.
    // */
    // public String[] getPeriodePrestation() throws Exception {
    //
    // String[] result = new String[2];
    //
    // REDecisions decision = new REDecisions();
    // decision.setSession(getSession());
    // decision.setIdDecision(getIdDecision());
    // decision.retrieve();
    //
    // //Recherche de la date de début de la décision.
    // JADate ddDroit = null;
    // JADate dfDroit = null;
    // /* if
    // (IREDecision.CS_TYPE_DECISION_COURANT.equals(decision.getCsTypeDecision()))
    // {
    // ddDroit = new JADate(decision.getDecisionDepuis());
    // }
    // */
    // if
    // (IREDecision.CS_TYPE_DECISION_RETRO.equals(decision.getCsTypeDecision()))
    // {
    // ddDroit = new JADate(decision.getDateDebutRetro());
    // dfDroit = new JADate(decision.getDateFinRetro());
    // }
    // else {
    // //Date début correspond à la plus petite date de début des RA de la
    // décision
    // REDecisionJointDemandeRenteManager mgr = new
    // REDecisionJointDemandeRenteManager();
    // mgr.setForIdDecision(decision.getIdDecision());
    // mgr.setSession(getSession());
    // mgr.find();
    //
    // ddDroit = new JADate("31.12.2999");
    // dfDroit = null;
    //
    // JACalendar cal = new JACalendarGregorian();
    // for (int i = 0; i < mgr.size(); i++) {
    // REDecisionJointDemandeRente elm =
    // (REDecisionJointDemandeRente)mgr.getEntity(i);
    // JADate ddRA = new JADate(elm.getDateDebutDroit());
    // if (cal.compare(ddDroit, ddRA)==JACalendar.COMPARE_SECONDLOWER) {
    // ddDroit = new JADate(elm.getDateDebutDroit());
    // }
    //
    // if (dfDroit==null) {
    // if (!JadeStringUtil.isBlankOrZero(elm.getDateFinDroit())) {
    // dfDroit = new JADate(elm.getDateFinDroit());
    // }
    // else {
    // dfDroit = null;
    // }
    // }
    // else {
    // if (!JadeStringUtil.isBlankOrZero(elm.getDateFinDroit())) {
    // JADate dfRA = new JADate(elm.getDateFinDroit());
    // if (cal.compare(dfDroit, dfRA)==JACalendar.COMPARE_FIRSTLOWER) {
    // dfDroit = new JADate(elm.getDateFinDroit());
    // }
    // }
    // else {
    // dfDroit = null;
    // }
    // }
    // }
    //
    // if
    // (IREDecision.CS_TYPE_DECISION_COURANT.equals(decision.getCsTypeDecision()))
    // {
    // ddDroit = new JADate(decision.getDecisionDepuis());
    // }
    // }
    //
    // result[0] = new
    // String(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(ddDroit.toStrAMJ()));
    // if (dfDroit==null) {
    // result[1] = new String("");
    // }
    // else {
    // result[1] = new
    // String(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dfDroit.toStrAMJ()));
    // }
    //
    // return result;
    // }

    // public String getPeriodePrestationFormatee() throws Exception {
    // String[] r = getPeriodePrestation();
    //
    // return r[0] + " - " + r[1];
    // }
}
