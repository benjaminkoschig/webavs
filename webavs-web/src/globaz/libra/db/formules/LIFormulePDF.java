package globaz.libra.db.formules;

import globaz.envoi.db.parametreEnvoi.access.ENFormule;
import globaz.envoi.db.parametreEnvoi.access.IENFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENFormulePDFDefTable;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class LIFormulePDF extends ENFormule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String classeName = new String();

    private String domaine = new String();
    private String typeDocument = new String();

    // protected void _beforeAdd(BTransaction transaction) throws Exception {}
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdFormule(_incCounter(transaction, idFormule, IENFormuleDefTable.TABLE_NAME));
        super._beforeAdd(transaction);
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        super._beforeUpdate(transaction);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String table1 = IENFormulePDFDefTable.TABLE_NAME;
        String table2 = IENFormuleDefTable.TABLE_NAME;
        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + "." + IENFormulePDFDefTable.ID_FORMULE + "=" + _getCollection() + table2 + "."
                + IENFormuleDefTable.ID_FORMULE + ")";
    }

    @Override
    protected String _getTableName() {
        return IENFormulePDFDefTable.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        classeName = statement.dbReadString(IENFormulePDFDefTable.NOM_CLASSE);
        domaine = statement.dbReadString(IENFormulePDFDefTable.DOMAINE);
        typeDocument = statement.dbReadString(IENFormulePDFDefTable.TYPE_DOC);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        super._validate(statement);
    }

    /**
     * Indique la clé principale NFormule() du fichier ENPPPDF
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écriture de la clé
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(
                _getCollection() + IENFormulePDFDefTable.TABLE_NAME + "." + IENFormulePDFDefTable.ID_FORMULE,
                _dbWriteNumeric(statement.getTransaction(), getIdFormule()));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // le test est nécessaire car lorsque le BEntity va faire le add du
        // super (ici ENFormule),
        // il va tout d'abord copier les propriétés du fils (ENFormulePDF) puis
        // faire effectivement
        // l'ajout
        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        }
        statement.writeField(IENFormulePDFDefTable.ID_FORMULE,
                _dbWriteNumeric(statement.getTransaction(), getIdFormule(), "id formule"));
        statement.writeField(IENFormulePDFDefTable.NOM_CLASSE,
                _dbWriteString(statement.getTransaction(), getClasseName(), "nom de la classe"));
        statement.writeField(IENFormulePDFDefTable.DOMAINE,
                _dbWriteNumeric(statement.getTransaction(), getDomaine(), "domaine"));
        statement.writeField(IENFormulePDFDefTable.TYPE_DOC,
                _dbWriteNumeric(statement.getTransaction(), getTypeDocument(), "type de doc"));
    }

    /**
     * @return
     */
    public String getClasseName() {
        return classeName;
    }

    /**
     * @return
     */
    public String getDomaine() {
        return domaine;
    }

    /**
     * @return
     */
    public String getTypeDocument() {
        return typeDocument;
    }

    /**
     * @param string
     */
    public void setClasseName(String string) {
        classeName = string;
    }

    /**
     * @param string
     */
    public void setDomaine(String string) {
        domaine = string;
    }

    /**
     * @param string
     */
    public void setTypeDocument(String string) {
        typeDocument = string;
    }

}
