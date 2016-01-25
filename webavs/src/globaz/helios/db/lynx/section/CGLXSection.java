package globaz.helios.db.lynx.section;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * La classe permettant de retrouver une entité Section (retrieve only). <br>
 * Copie de LX afin d'éviter les références récursives entre projet.
 * 
 * @author DDA
 * 
 */
public class CGLXSection extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
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

    private String csTypeSection = new String();
    private String dateSection = new String();
    private String idExterne = new String();
    private String idFournisseur = new String();
    private String idJournal = new String();
    private String idSection = new String();
    private String idSociete = new String();

    @Override
    protected String _getTableName() {
        return TABLE_LXSECTP;
    }

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

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Do nothing
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDSECTION, _dbWriteNumeric(statement.getTransaction(), getIdSection(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Do nothing
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
