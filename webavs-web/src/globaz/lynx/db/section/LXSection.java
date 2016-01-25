package globaz.lynx.db.section;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;

public class LXSection extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Code systeme TYPE (LXTYPESECT)
    public static final String CS_TYPE = "LXTYPESECT";
    public static final String CS_TYPE_FACTURE = "7600001";
    public static final String CS_TYPE_NOTEDECREDIT = "7600002";

    public static final String FIELD_CSTYPESECTION = "CSTYPESECTION";
    public static final String FIELD_DATESECTION = "DATESECTION";
    public static final String FIELD_IDEXTERNE = "IDEXTERNE";
    public static final String FIELD_IDFOURNISSEUR = "IDFOURNISSEUR";
    public static final String FIELD_IDJOURNAL = "IDJOURNAL";
    // Colonnes de la table
    public static final String FIELD_IDSECTION = "IDSECTION";
    public static final String FIELD_IDSOCIETE = "IDSOCIETE";

    // Nom de la table
    public static final String TABLE_LXSECTP = "LXSECTP";

    private String csTypeSection = "";
    private String dateSection = "";
    private String idExterne = "";
    private String idFournisseur = "";
    private String idJournal = "";
    private String idSection = "";
    private String idSociete = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdSection(_incCounter(transaction, idSection));

        if (JadeStringUtil.isBlank(getDateSection())) {
            setDateSection(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_LXSECTP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdSection(statement.dbReadNumeric(FIELD_IDSECTION));
        setIdSociete(statement.dbReadNumeric(FIELD_IDSOCIETE));
        setIdFournisseur(statement.dbReadNumeric(FIELD_IDFOURNISSEUR));
        setIdJournal(statement.dbReadNumeric(FIELD_IDJOURNAL));
        setCsTypeSection(statement.dbReadNumeric(FIELD_CSTYPESECTION));
        setDateSection(statement.dbReadDateAMJ(FIELD_DATESECTION));
        setIdExterne(statement.dbReadString(FIELD_IDEXTERNE));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Controle de l'id societe
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
        }
        // Controle de l'id fournisseur
        if (JadeStringUtil.isIntegerEmpty(getIdFournisseur())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_FOURNISSEUR"));
        }
        // Controle de l'id journal
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_JOURNAL"));
        }
        // Controle de l'id externe
        if (JadeStringUtil.isIntegerEmpty(getIdExterne())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_EXTERNE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDSECTION, _dbWriteNumeric(statement.getTransaction(), getIdSection(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELD_IDSECTION, _dbWriteNumeric(statement.getTransaction(), getIdSection(), "idSection"));
        statement.writeField(FIELD_IDSOCIETE, _dbWriteNumeric(statement.getTransaction(), getIdSociete(), "idSociete"));
        statement.writeField(FIELD_IDFOURNISSEUR,
                _dbWriteNumeric(statement.getTransaction(), getIdFournisseur(), "idFournisseur"));
        statement.writeField(FIELD_IDJOURNAL, _dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(FIELD_CSTYPESECTION,
                _dbWriteNumeric(statement.getTransaction(), getCsTypeSection(), "csTypeSection"));
        statement.writeField(FIELD_DATESECTION,
                _dbWriteDateAMJ(statement.getTransaction(), getDateSection(), "dateSection"));
        statement.writeField(FIELD_IDEXTERNE, _dbWriteString(statement.getTransaction(), getIdExterne(), "idExterne"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getCsTypeSection() {
        return csTypeSection;
    }

    public String getDateSection() {
        return dateSection;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdSociete() {
        return idSociete;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setCsTypeSection(String csTypeSection) {
        this.csTypeSection = csTypeSection;
    }

    public void setDateSection(String dateSection) {
        this.dateSection = dateSection;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

}
