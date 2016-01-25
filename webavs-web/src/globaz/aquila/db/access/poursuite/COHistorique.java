package globaz.aquila.db.access.poursuite;

import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.common.COBEntity;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeInfo;
import globaz.aquila.db.access.batch.COEtapeInfoManager;
import globaz.aquila.db.access.batch.COSequence;
import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.List;

/**
 * Représente une entité de type Historique.
 * 
 * @author Pascal Lovy, 06-oct-2004
 */
public class COHistorique extends COBEntity implements ICOHistoriqueConstante {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 6642639960745211524L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean annule = new Boolean(false);
    private COContentieux contentieux;
    private String dateDeclenchement = "";
    private String dateExecution = "";
    /** (etape). */
    private COEtape etape = new COEtape();
    private transient List etapeInfos;
    private Boolean etapeSansInfluence = new Boolean(false);
    private String idContentieux = "";
    private String idEtape = "";
    private String idGroupement = "";
    private String idHistorique = "";
    private String idHistoriquePrecedant = "";
    private String idJournal = "";
    private String idSequence = "";
    protected boolean loadContentieux;
    private String motif = "";
    private String numPoursuite = "";

    private String solde = "";
    private String taxes = "";
    private String typeJournal = "";
    private String user = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        refreshLinks(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        refreshLinks(transaction);
        idHistorique = this._incCounter(transaction, "0");
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        COEtapeInfoManager infoManager = new COEtapeInfoManager();

        infoManager.setForIdHistorique(getIdHistorique());
        infoManager.setSession(getSession());
        infoManager.find();

        for (int idInfo = 0; idInfo < infoManager.size(); ++idInfo) {
            ((COEtapeInfo) infoManager.get(idInfo)).delete(transaction);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        refreshLinks(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return ICOHistoriqueConstante.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idHistorique = statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_HISTORIQUE);
        idContentieux = statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX);
        idSequence = statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_SEQUENCE);
        idEtape = statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_ETAPE);
        dateDeclenchement = statement.dbReadDateAMJ(ICOHistoriqueConstante.FNAME_DATE_DECLENCHEMENT);
        dateExecution = statement.dbReadDateAMJ(ICOHistoriqueConstante.FNAME_DATE_EXECUTION);
        solde = statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_SOLDE, 2);
        taxes = statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_TAXES, 2);
        user = statement.dbReadString(ICOHistoriqueConstante.FNAME_USERNAME);
        motif = statement.dbReadString(ICOHistoriqueConstante.FNAME_MOTIF);
        idHistoriquePrecedant = statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_HISTORIQUE_PRECEDENT);
        idJournal = statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_JOURNAL);
        idGroupement = statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_GROUPEMENT);
        typeJournal = statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_TYPE_JOURNAL);
        numPoursuite = statement.dbReadString(ICOHistoriqueConstante.FNAME_NUMERO_POURSUITE);
        annule = statement.dbReadBoolean(ICOHistoriqueConstante.FNAME_EST_ANNULE);
        etapeSansInfluence = statement.dbReadBoolean(ICOHistoriqueConstante.FNAME_ETAPE_SANS_INFLUENCE);

    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(ICOHistoriqueConstante.FNAME_ID_HISTORIQUE,
                this._dbWriteNumeric(statement.getTransaction(), idHistorique, "idHistorique"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(ICOHistoriqueConstante.FNAME_ID_HISTORIQUE,
                this._dbWriteNumeric(statement.getTransaction(), idHistorique, "idHistorique"));
        statement.writeField(ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX,
                this._dbWriteNumeric(statement.getTransaction(), idContentieux, "idContentieux"));
        statement.writeField(ICOHistoriqueConstante.FNAME_ID_SEQUENCE,
                this._dbWriteNumeric(statement.getTransaction(), idSequence, "idSequence"));
        statement.writeField(ICOHistoriqueConstante.FNAME_ID_ETAPE,
                this._dbWriteNumeric(statement.getTransaction(), idEtape, "etapeExecution"));
        statement.writeField(ICOHistoriqueConstante.FNAME_DATE_DECLENCHEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDeclenchement, "dateDeclenchement"));
        statement.writeField(ICOHistoriqueConstante.FNAME_DATE_EXECUTION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateExecution, "dateExecution"));
        statement.writeField(ICOHistoriqueConstante.FNAME_SOLDE,
                this._dbWriteNumeric(statement.getTransaction(), solde, "solde"));
        statement.writeField(ICOHistoriqueConstante.FNAME_TAXES,
                this._dbWriteNumeric(statement.getTransaction(), taxes, "taxes"));
        statement.writeField(ICOHistoriqueConstante.FNAME_USERNAME,
                this._dbWriteString(statement.getTransaction(), user, "respModif"));
        statement.writeField(ICOHistoriqueConstante.FNAME_MOTIF,
                this._dbWriteString(statement.getTransaction(), motif, "motif"));
        statement.writeField(ICOHistoriqueConstante.FNAME_ID_HISTORIQUE_PRECEDENT,
                this._dbWriteNumeric(statement.getTransaction(), idHistoriquePrecedant, "idHistoriquePrecedant"));
        statement.writeField(ICOHistoriqueConstante.FNAME_ID_JOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), idJournal, "idJournal"));
        statement.writeField(ICOHistoriqueConstante.FNAME_ID_GROUPEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idGroupement, "idGroupement"));
        statement.writeField(ICOHistoriqueConstante.FNAME_TYPE_JOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), typeJournal, "typeJournal"));
        statement.writeField(ICOHistoriqueConstante.FNAME_NUMERO_POURSUITE,
                this._dbWriteString(statement.getTransaction(), numPoursuite, "numPoursuite"));
        statement
                .writeField(ICOHistoriqueConstante.FNAME_ETAPE_SANS_INFLUENCE, this._dbWriteBoolean(
                        statement.getTransaction(), isImpute(), BConstants.DB_TYPE_BOOLEAN_CHAR, "impute"));
        statement
                .writeField(ICOHistoriqueConstante.FNAME_EST_ANNULE, this._dbWriteBoolean(statement.getTransaction(),
                        isAnnule(), BConstants.DB_TYPE_BOOLEAN_CHAR, "annule"));
    }

    /**
     * @return DOCUMENT ME!
     */
    public COContentieux getContentieux() {
        return contentieux;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateDeclenchement() {
        return dateDeclenchement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateExecution() {
        return dateExecution;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public COEtape getEtape() {
        return etape;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdContentieux() {
        return idContentieux;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdEtape() {
        return idEtape;
    }

    /**
     * @return DOCUMENT ME!
     */
    public String getIdGroupement() {
        return idGroupement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdHistorique() {
        return idHistorique;
    }

    /**
     * @return DOCUMENT ME!
     */
    public String getIdHistoriquePrecedant() {
        return idHistoriquePrecedant;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getIdSequence() {
        return idSequence;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getMotif() {
        return motif;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getNumPoursuite() {
        return numPoursuite;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getSolde() {
        return solde;
    }

    /**
     * @return La valeur courante de la propriété, formatée
     */
    public String getSoldeFormate() {
        return JANumberFormatter.formatNoRound(getSolde(), 2);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getTaxes() {
        return taxes;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getTypeJournal() {
        return typeJournal;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the estAnnule
     */
    public Boolean isAnnule() {
        return annule;
    }

    /**
     * @return the estImpute
     */
    public Boolean isImpute() {
        return etapeSansInfluence;
    }

    /**
     * retourne les infos d'étapes pour le libellé donné.
     * 
     * @param csLibelle
     * @return l'info d'etape ou null si aucun pour ce libellé
     * @throws Exception
     */
    public COEtapeInfo loadEtapeInfo(String csLibelle) throws Exception {
        List etapeInfos = loadEtapeInfos();

        if (etapeInfos != null) {
            for (Iterator infoIter = etapeInfos.iterator(); infoIter.hasNext();) {
                COEtapeInfo etapeInfo = (COEtapeInfo) infoIter.next();

                if (csLibelle.equals(etapeInfo.getCsLibelle())) {
                    return etapeInfo;
                }
            }
        }

        return null;
    }

    /**
     * Charge la liste des infos pour cet historique.
     * 
     * @return la liste des infos (COEtapeInfo) ou null si aucunes
     * @throws Exception
     */
    public List /* COEtapeInfo */loadEtapeInfos() throws Exception {
        if ((etapeInfos == null) && !JadeStringUtil.isIntegerEmpty(idHistorique)) {
            COEtapeInfoManager infoManager = new COEtapeInfoManager();

            infoManager.setForIdHistorique(idHistorique);
            infoManager.setSession(getSession());
            infoManager.find();
            etapeInfos = infoManager.getContainer();
        }

        return etapeInfos;
    }

    /**
     * Actualise les objets liés.
     * 
     * @param transaction
     *            La transaction courante
     * @throws Exception
     */
    private void refreshLinks(BTransaction transaction) throws Exception {
        etape = new COEtape();
        etape.setSession(getSession());
        etape.setIdEtape(idEtape);

        try {
            etape.retrieve(transaction);
        } catch (Exception e) {
            _addError(transaction, e.toString());
        }

        if (loadContentieux) {
            COSequence sequence = new COSequence();

            sequence.setIdSequence(idSequence);
            sequence.setSession(getSession());
            sequence.retrieve();

            contentieux = COContentieuxFactory.loadContentieux(getSession(), idContentieux);
        }
    }

    /**
     * @param estAnnule
     *            the estAnnule to set
     */
    public void setAnnule(Boolean estAnnule) {
        annule = estAnnule;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateDeclenchement(String string) {
        dateDeclenchement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateExecution(String string) {
        dateExecution = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdContentieux(String string) {
        idContentieux = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdEtape(String string) {
        idEtape = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setIdGroupement(String string) {
        idGroupement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdHistorique(String string) {
        idHistorique = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setIdHistoriquePrecedant(String string) {
        idHistoriquePrecedant = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setIdJournal(String string) {
        idJournal = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setIdSequence(String string) {
        idSequence = string;
    }

    /**
     * @param estImpute
     *            the estImpute to set
     */
    public void setImpute(Boolean estImpute) {
        etapeSansInfluence = estImpute;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setMotif(String string) {
        motif = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param numPoursuite
     *            DOCUMENT ME!
     */
    public void setNumPoursuite(String numPoursuite) {
        this.numPoursuite = numPoursuite;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setSolde(String string) {
        solde = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param taxes
     *            DOCUMENT ME!
     */
    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setTypeJournal(String string) {
        typeJournal = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setUser(String string) {
        user = string;
    }
}
