package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.IENFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENFormulePDFDefTable;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author SCO
 * @since 14 juil. 2010
 */
public class LEPdf extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String classeName = new String();
    private String domaine = new String();
    private String idFormule = new String();
    private String typeDocument = new String();

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return ILEFormuleDefTable.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idFormule = statement.dbReadNumeric(IENFormuleDefTable.ID_FORMULE);
        classeName = statement.dbReadString(IENFormulePDFDefTable.NOM_CLASSE);
        domaine = statement.dbReadString(IENFormulePDFDefTable.DOMAINE);
        typeDocument = statement.dbReadString(IENFormulePDFDefTable.TYPE_DOC);
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
        statement.writeKey(
                _getCollection() + ILEFormuleDefTable.TABLE_NAME + "." + ILEFormuleDefTable.ID_FORMULE,
                _dbWriteNumeric(statement.getTransaction(), getIdFormule(),
                        "idFormule - clé primaire du fichier des formulesPdf"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(ILEFormuleDefTable.ID_FORMULE,
                _dbWriteNumeric(statement.getTransaction(), getIdFormule(), "id formule"));
        statement.writeField(ILEFormuleDefTable.NOM_CLASSE,
                _dbWriteString(statement.getTransaction(), getClasseName(), "nom de la classe"));
        statement.writeField(ILEFormuleDefTable.DOMAINE,
                _dbWriteNumeric(statement.getTransaction(), getDomaine(), "domaine"));
        statement.writeField(ILEFormuleDefTable.TYPE_DOC,
                _dbWriteNumeric(statement.getTransaction(), getTypeDocument(), "type de doc"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getClasseName() {
        return classeName;
    }

    public String getDomaine() {
        return domaine;
    }

    public String getIdFormule() {
        return idFormule;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setClasseName(String classeName) {
        this.classeName = classeName;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public void setIdFormule(String idFormule) {
        this.idFormule = idFormule;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

}
