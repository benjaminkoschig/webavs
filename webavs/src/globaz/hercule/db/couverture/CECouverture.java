package globaz.hercule.db.couverture;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hercule.service.CECouvertureService;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Class représentant la date de couverture du controle d'un employeur.<BR>
 * La propriété <b>"année"</b> représente l'année ou l'employeur est couvert.<BR>
 * L'année N+1 sera donc l'année du contrôle.
 * 
 * @author SCO
 * @since SCO 1 juin 2010
 */
public class CECouverture extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ANNEE = "CENANE";

    public static final String FIELD_COUVERTUREACTIVE = "CEBCAV";
    public static final String FIELD_DATEMODIFICATION = "CENDAT";
    public static final String FIELD_IDAFFILIE = "MAIAFF";
    public static final String FIELD_IDCOUVERTURE = "CEICOU";
    public static final String FIELD_NUMAFFILIE = "MALNAF";
    public static final String TABLE_CECOUVP = "CECOUVP";

    private String annee;
    private Boolean couvertureActive;
    private String dateModification;
    private String idAffilie;
    private String idCouverture;
    private String numAffilie;

    /**
     * Constructeur de CECouverture
     */
    public CECouverture() {
        super();
        couvertureActive = new Boolean(false);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCouverture(this._incCounter(transaction, idCouverture));
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CECouverture.TABLE_CECOUVP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCouverture = statement.dbReadNumeric(CECouverture.FIELD_IDCOUVERTURE);
        idAffilie = statement.dbReadNumeric(CECouverture.FIELD_IDAFFILIE);
        numAffilie = statement.dbReadString(CECouverture.FIELD_NUMAFFILIE);
        annee = statement.dbReadNumeric(CECouverture.FIELD_ANNEE);
        dateModification = statement.dbReadNumeric(CECouverture.FIELD_DATEMODIFICATION);
        couvertureActive = statement.dbReadBoolean(CECouverture.FIELD_COUVERTUREACTIVE);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Controle qu'on ait bien un id affilié
        if (JadeStringUtil.isBlank(getIdAffilie())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_AFFILIE"));
        }
        // Controle que l'année soit pas vide
        if (JadeStringUtil.isIntegerEmpty(getAnnee())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_ANNEE_COUVERTURE"));
        }
        // Controle qu'il n'existe pas déjà une couverture active pour cette
        // affilié
        if (isCouvertureActive()
                && CECouvertureService.isCouvertureActiveForIdAffilie(getSession(), statement.getTransaction(),
                        getIdAffilie(), getIdCouverture())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_COUVERTURE_ACTIVE_AFFILIE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CECouverture.FIELD_IDCOUVERTURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCouverture(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CECouverture.FIELD_IDCOUVERTURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCouverture(), "idCouverture"));
        statement.writeField(CECouverture.FIELD_IDAFFILIE,
                this._dbWriteNumeric(statement.getTransaction(), getIdAffilie(), "idAffilie"));
        statement.writeField(CECouverture.FIELD_NUMAFFILIE,
                this._dbWriteString(statement.getTransaction(), getNumAffilie(), "numAffilie"));
        statement.writeField(CECouverture.FIELD_ANNEE,
                this._dbWriteNumeric(statement.getTransaction(), getAnnee(), "annee"));
        statement.writeField(CECouverture.FIELD_DATEMODIFICATION,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateModification(), "dateModification"));
        statement.writeField(CECouverture.FIELD_COUVERTUREACTIVE, this._dbWriteBoolean(statement.getTransaction(),
                isCouvertureActive(), BConstants.DB_TYPE_BOOLEAN_CHAR, "couvertureActive"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getAnnee() {
        return annee;
    }

    public String getDateModification() {
        return dateModification;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdCouverture() {
        return idCouverture;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public Boolean isCouvertureActive() {
        return couvertureActive;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCouvertureActive(Boolean couvertureActive) {
        this.couvertureActive = couvertureActive;
    }

    public void setDateModification(String dateModification) {
        this.dateModification = dateModification;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdCouverture(String idCouverture) {
        this.idCouverture = idCouverture;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

}
