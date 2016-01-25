/*
 * Créé le 2 août 07
 */
package globaz.corvus.db.interetsmoratoires;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

/**
 * @author BSC
 * 
 */
public class REInteretMoratoire extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_DOMAINE_ADR_PMT = "ZITDAP";
    public static final String FIELDNAME_DATE_CAULCUL_IM = "ZIDCAL";
    public static final String FIELDNAME_ID_AFFILIE_ADR_PMT = "ZIIAAP";
    public static final String FIELDNAME_ID_INTERET_MORATOIRE = "ZIIIMO";
    public static final String FIELDNAME_ID_TIERS_ADR_PMT = "ZIITAP";
    public static final String FIELDNAME_MONTANT_INTERET = "ZIMINT";
    public static final String FIELDNAME_TAUX_REPARTITION = "ZINTRE";
    public static final String TABLE_NAME_INTERET_MORATOIRE = "REINTMO";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csDomaineAdrPmt = "";
    private String dateCalculIM = "";
    private String idAffilieAdrPmt = "";
    private String idInteretMoratoire = "";
    private String idTiersAdrPmt = "";
    private String montantInteret = "";
    private String tauxRepartition = "";

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
        setIdInteretMoratoire(_incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // on supprime les calculs d'interets moratoires pour cette demande de
        // rente
        RECalculInteretMoratoireManager cimManager = new RECalculInteretMoratoireManager();
        cimManager.setSession(getSession());
        cimManager.setForIdInteretMoratoire(getIdInteretMoratoire());
        cimManager.find(transaction);

        for (int i = 0; i < cimManager.size(); i++) {
            RECalculInteretMoratoire cim = (RECalculInteretMoratoire) cimManager.getEntity(i);
            cim.retrieve(transaction);
            cim.delete(transaction);
        }

        super._beforeDelete(transaction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        StringBuffer fromClauseBuffer = new StringBuffer(super._getFrom(statement));

        // jointure entre table des calcul interets moratoires et table des
        // interets moratoires
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RECalculInteretMoratoire.TABLE_NAME_CALCUL_INTERET_MORATOIRE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RECalculInteretMoratoire.TABLE_NAME_CALCUL_INTERET_MORATOIRE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RECalculInteretMoratoire.FIELDNAME_ID_INTERET_MORATOIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REInteretMoratoire.TABLE_NAME_INTERET_MORATOIRE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REInteretMoratoire.FIELDNAME_ID_INTERET_MORATOIRE);

        return fromClauseBuffer.toString();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_INTERET_MORATOIRE;
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

        idInteretMoratoire = statement.dbReadNumeric(FIELDNAME_ID_INTERET_MORATOIRE);
        idTiersAdrPmt = statement.dbReadNumeric(FIELDNAME_ID_TIERS_ADR_PMT);
        idAffilieAdrPmt = statement.dbReadNumeric(FIELDNAME_ID_AFFILIE_ADR_PMT);
        csDomaineAdrPmt = statement.dbReadNumeric(FIELDNAME_CS_DOMAINE_ADR_PMT);
        montantInteret = statement.dbReadNumeric(FIELDNAME_MONTANT_INTERET);
        dateCalculIM = statement.dbReadDateAMJ(FIELDNAME_DATE_CAULCUL_IM);
        tauxRepartition = statement.dbReadNumeric(FIELDNAME_TAUX_REPARTITION);
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
        statement.writeKey(FIELDNAME_ID_INTERET_MORATOIRE,
                _dbWriteNumeric(statement.getTransaction(), idInteretMoratoire, "idInteretMoratoire"));
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

        // pas de modification de "CalculInteretMoratoire"

        statement.writeField(FIELDNAME_ID_INTERET_MORATOIRE,
                _dbWriteNumeric(statement.getTransaction(), idInteretMoratoire, "idInteretMoratoire"));
        statement.writeField(FIELDNAME_ID_TIERS_ADR_PMT,
                _dbWriteNumeric(statement.getTransaction(), idTiersAdrPmt, "idTiersAdrPmt"));
        statement.writeField(FIELDNAME_ID_AFFILIE_ADR_PMT,
                _dbWriteNumeric(statement.getTransaction(), idAffilieAdrPmt, "idAffilieAdrPmt"));
        statement.writeField(FIELDNAME_CS_DOMAINE_ADR_PMT,
                _dbWriteNumeric(statement.getTransaction(), csDomaineAdrPmt, "csDomaineAdrPmt"));
        statement.writeField(FIELDNAME_MONTANT_INTERET,
                _dbWriteNumeric(statement.getTransaction(), montantInteret, "montantInteret"));
        statement.writeField(FIELDNAME_DATE_CAULCUL_IM,
                _dbWriteDateAMJ(statement.getTransaction(), dateCalculIM, "dateCalculIM"));
        statement.writeField(FIELDNAME_TAUX_REPARTITION,
                _dbWriteNumeric(statement.getTransaction(), tauxRepartition, "tauxRepartition"));
    }

    /**
     * @return
     */
    public String getCsDomaineAdrPmt() {
        return csDomaineAdrPmt;
    }

    public String getDateCalculIM() {
        return dateCalculIM;
    }

    public String getIdAffilieAdrPmt() {
        return idAffilieAdrPmt;
    }

    /**
     * @return
     */
    public String getIdInteretMoratoire() {
        return idInteretMoratoire;
    }

    /**
     * @return
     */
    public String getIdTiersAdrPmt() {
        return idTiersAdrPmt;
    }

    /**
     * @return
     */
    public String getMontantInteret() {
        return montantInteret;
    }

    public String getTauxRepartition() {
        return tauxRepartition;
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

    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * setter pour l'attribut adresse paiement.
     * 
     * @param adressePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setAdressePaiement(TIAdressePaiementData adressePaiement) {
        if (adressePaiement != null) {
            idTiersAdrPmt = adressePaiement.getIdTiers();
            csDomaineAdrPmt = adressePaiement.getIdApplication();
        } else {
            idTiersAdrPmt = "";
            csDomaineAdrPmt = "";
        }
    }

    /**
     * @param string
     */
    public void setCsDomaineAdrPmt(String string) {
        csDomaineAdrPmt = string;
    }

    public void setDateCalculIM(String dateCalculIM) {
        this.dateCalculIM = dateCalculIM;
    }

    public void setIdAffilieAdrPmt(String idAffilieAdrPmt) {
        this.idAffilieAdrPmt = idAffilieAdrPmt;
    }

    /**
     * @param string
     */
    public void setIdInteretMoratoire(String string) {
        idInteretMoratoire = string;
    }

    /**
     * @param string
     */
    public void setIdTiersAdrPmt(String string) {
        idTiersAdrPmt = string;
    }

    /**
     * @param string
     */
    public void setMontantInteret(String string) {
        montantInteret = string;
    }

    public void setTauxRepartition(String tauxRepartition) {
        this.tauxRepartition = tauxRepartition;
    }
}
