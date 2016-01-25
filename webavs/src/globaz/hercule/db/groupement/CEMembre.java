package globaz.hercule.db.groupement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hercule.db.controleEmployeur.CEAffilie;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * @author JMC
 * @since 2 juin 2010
 */
public class CEMembre extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_IDAFFILIATION = "MAIAFF";

    public static final String FIELD_IDGROUPE = "CEIDGR";
    public static final String FIELD_IDMEMBRE = "CEIDMEM";
    public static final String TABLE_CEMEMP = "CEMEMP";

    private String idAffiliation = "";
    private String idGroupe = "";
    private String idMembre = "";
    private String nomAffilie = "";
    private String numeroAffilie = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idMembre)) {
            setIdMembre(this._incCounter(transaction, "0"));
        }

        // Test si l'affilié n'est pas déjà dans un groupe
        if (!JadeStringUtil.isBlankOrZero(idAffiliation)) {
            CEMembreManager manager = new CEMembreManager();
            manager.setSession(getSession());
            manager.setForIdAffiliation(idAffiliation);
            manager.find(transaction);

            if (!manager.isEmpty()) {
                _addError(transaction, getSession().getLabel("ERR_AFFILIE_DEJA_DANS_GROUPE"));
            }
        }
    }

    /**
     * Méthode qui retourne le nom + prénom d'un affilié
     * 
     * @return
     */
    public String _getDescription() {
        try {
            CEAffilie aff = new CEAffilie();
            aff.setSession(getSession());
            aff.setIdAffiliation(getIdAffiliation());
            aff.retrieve();
            if (!aff.isNew()) {
                return aff._getDescription();
            }
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
        return "";

    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String joinStr = super._getFrom(statement);

        joinStr += " inner join ";
        joinStr += _getCollection() + "AFAFFIP ON " + _getCollection() + "AFAFFIP.MAIAFF = " + _getCollection()
                + CEMembre.TABLE_CEMEMP + "." + CEMembre.FIELD_IDAFFILIATION;
        joinStr += " inner join ";
        joinStr += _getCollection() + "TITIERP ON " + _getCollection() + "AFAFFIP.HTITIE = " + _getCollection()
                + "TITIERP.HTITIE";

        return joinStr;
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CEMembre.TABLE_CEMEMP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMembre = statement.dbReadNumeric(CEMembre.FIELD_IDMEMBRE);
        idGroupe = statement.dbReadNumeric(CEMembre.FIELD_IDGROUPE);
        idAffiliation = statement.dbReadNumeric(CEMembre.FIELD_IDAFFILIATION);
        numeroAffilie = statement.dbReadString("MALNAF");
        nomAffilie = statement.dbReadString("HTLDE1") + " " + statement.dbReadString("HTLDE2");
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isBlankOrZero(numeroAffilie) && JadeStringUtil.isBlankOrZero(idAffiliation)) {
            _addError(statement.getTransaction(), getSession().getLabel("ERR_NO_AFFILIE_VIDE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".CEIDMEM",
                this._dbWriteNumeric(statement.getTransaction(), getIdMembre(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CEMembre.FIELD_IDMEMBRE,
                this._dbWriteNumeric(statement.getTransaction(), idMembre, "idMembre"));
        statement.writeField(CEMembre.FIELD_IDGROUPE,
                this._dbWriteNumeric(statement.getTransaction(), idGroupe, "idGroupe"));
        statement.writeField(CEMembre.FIELD_IDAFFILIATION,
                this._dbWriteNumeric(statement.getTransaction(), idAffiliation, "idAffiliation"));

    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdGroupe() {
        return idGroupe;
    }

    public String getIdMembre() {
        return idMembre;
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdGroupe(String idGroupe) {
        this.idGroupe = idGroupe;
    }

    public void setIdMembre(String idMembre) {
        this.idMembre = idMembre;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }
}
