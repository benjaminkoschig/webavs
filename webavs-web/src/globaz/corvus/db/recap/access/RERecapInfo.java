package globaz.corvus.db.recap.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri Nov 30 11:51:36 CET 2007
 */
public class RERecapInfo extends BEntity {
    /** Table : REINFREC */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** nb de rente */
    private String cas = new String();
    /** codeRecap - code r�cap (ZQNCOD) */
    private String codeRecap = new String();
    /** datePmt - date paiement MMxAAAA (ZQDDAT) */
    private String datePmt = new String();
    /** idRecapInfo - id r�cap info (pk) (ZQIIFR) */
    private String idRecapInfo = new String();
    /** idTiers - id tiers (fk) (ZQITIE) */
    private String idTiers = new String();

    /** montant - montant (ZQMMON) */
    private String montant = new String();
    /**
     * tag de restauration, en cas d'eereur grave. Pour r�cup�ration manuelle. Contient l'id du lot.
     */
    private String restoreTag = new String();

    /**
     * totalMontant - r�cup�re le montant total quand manager charg� avec "getTotalByCode==ture" (TOTAL)
     */
    private String totalMontant = new String();

    /**
     * Constructeur de la classe RERecapInfo
     */
    public RERecapInfo() {
        super();
    }

    /**
     * M�thode qui incr�mente la cl� primaire
     * 
     * @param transaction
     *            BTransaction transaction
     * @throws Exception
     *             exception
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdRecapInfo(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table REINFREC
     * 
     * @return String REINFREC
     */
    @Override
    protected String _getTableName() {
        return IRERecapInfoDefTable.TABLE_NAME;
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRecapInfo = statement.dbReadNumeric(IRERecapInfoDefTable.ID_RECAP_INFO);
        idTiers = statement.dbReadNumeric(IRERecapInfoDefTable.ID_TIERS);
        codeRecap = statement.dbReadNumeric(IRERecapInfoDefTable.CODE_RECAP);
        datePmt = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(IRERecapInfoDefTable.DATE_PMT));
        montant = statement.dbReadNumeric(IRERecapInfoDefTable.MONTANT, 2);
        totalMontant = statement.dbReadNumeric(IRERecapInfoDefTable.TOTAL_MONTANT, 2);
        cas = statement.dbReadNumeric(IRERecapInfoDefTable.CAS);
        restoreTag = statement.dbReadNumeric(IRERecapInfoDefTable.RESTORE_TAG);
    }

    /**
     * Valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
     * Indique la cl� principale ERecapInfo() du fichier REINFREC
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�criture de la cl�
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(IRERecapInfoDefTable.ID_RECAP_INFO,
                _dbWriteNumeric(statement.getTransaction(), getIdRecapInfo(), "idRecapInfo - id r�cap info (pk)"));
    }

    /**
     * Ecriture des propri�t�s
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�critrues des propri�t�s
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(IRERecapInfoDefTable.ID_RECAP_INFO,
                _dbWriteNumeric(statement.getTransaction(), getIdRecapInfo(), "idRecapInfo - id r�cap info (pk)"));
        statement.writeField(IRERecapInfoDefTable.ID_TIERS,
                _dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers - id tiers (fk)"));
        statement.writeField(IRERecapInfoDefTable.CODE_RECAP,
                _dbWriteNumeric(statement.getTransaction(), getCodeRecap(), "codeRecap - code r�cap"));
        statement.writeField(
                IRERecapInfoDefTable.DATE_PMT,
                _dbWriteNumeric(statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getDatePmt()),
                        "datePmt"));
        statement.writeField(IRERecapInfoDefTable.MONTANT,
                _dbWriteNumeric(statement.getTransaction(), getMontant(), "montant - montant"));
        statement.writeField(IRERecapInfoDefTable.RESTORE_TAG,
                _dbWriteNumeric(statement.getTransaction(), getRestoreTag(), "restoreTag"));

    }

    public String getCas() {
        if (!JadeStringUtil.isBlank(totalMontant)) {
            if ("-".equals(totalMontant.substring(0, 1))) {
                return "-" + cas;
            } else {
                return cas;
            }
        } else {
            return cas;
        }
    }

    /**
     * Renvoie la zone codeRecap - code r�cap (ZQNCOD)
     * 
     * @return String codeRecap - code r�cap
     */
    public String getCodeRecap() {
        return codeRecap;
    }

    /**
     * Renvoie la zone datePmt - date paiement MMxAAAA (ZQDDAT)
     * 
     * @return String datePmt - date paiement MMxAAAA
     */
    public String getDatePmt() {
        return datePmt;
    }

    /**
     * Renvoie la zone idRecapInfo - id r�cap info (pk) (ZQIIFR)
     * 
     * @return String idRecapInfo - id r�cap info (pk)
     */
    public String getIdRecapInfo() {
        return idRecapInfo;
    }

    /**
     * Renvoie la zone idTiers - id tiers (fk) (ZQITIE)
     * 
     * @return String idTiers - id tiers (fk)
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Renvoie la zone montant - montant (ZQMMON)
     * 
     * @return String montant - montant
     */
    public String getMontant() {
        return montant;
    }

    public String getRestoreTag() {
        return restoreTag;
    }

    /**
     * R�cup�re le montant total fourni par le manager
     * 
     * @return the totalMontant
     */
    public String getTotalMontant() {
        return totalMontant;
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

    public void setCas(String cas) {
        this.cas = cas;
    }

    /**
     * Modifie la zone codeRecap - code r�cap (ZQNCOD)
     * 
     * @param newCodeRecap
     *            - code r�cap String
     */
    public void setCodeRecap(String newCodeRecap) {
        codeRecap = newCodeRecap;
    }

    /**
     * Modifie la zone datePmt - date paiement MMxAAAA (ZQDDAT)
     * 
     * @param newDatePmt
     *            - date paiement MMxAAAA String
     */
    public void setDatePmt(String newDatePmt) {
        datePmt = newDatePmt;
    }

    /**
     * Modifie la zone idRecapInfo - id r�cap info (pk) (ZQIIFR)
     * 
     * @param newIdRecapInfo
     *            - id r�cap info (pk) String
     */
    public void setIdRecapInfo(String newIdRecapInfo) {
        idRecapInfo = newIdRecapInfo;
    }

    /**
     * Modifie la zone idTiers - id tiers (fk) (ZQITIE)
     * 
     * @param newIdTiers
     *            - id tiers (fk) String
     */
    public void setIdTiers(String newIdTiers) {
        idTiers = newIdTiers;
    }

    /**
     * Modifie la zone montant - montant (ZQMMON)
     * 
     * @param newMontant
     *            - montant String
     */
    public void setMontant(String newMontant) {
        montant = newMontant;
    }

    public void setRestoreTag(String restoreTag) {
        this.restoreTag = restoreTag;
    }

}
