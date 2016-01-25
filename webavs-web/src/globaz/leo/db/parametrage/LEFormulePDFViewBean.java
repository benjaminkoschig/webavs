package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.ENFormule;
import globaz.envoi.db.parametreEnvoi.access.IENFormuleDefTable;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.leo.constantes.ILEConstantes;

/**
 * @author SCO
 * @since 13 juil. 2010
 */
public class LEFormulePDFViewBean extends ENFormule implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String classeName = new String();
    private String domaine = new String();
    private String typeDocument = new String();

    /**
     * Constructeur de LEFormulePDF
     */
    public LEFormulePDFViewBean() {
        super();
        // par defaut une formule n'est :
        // pas recto-verso
        setCsRectoVerso(ILEConstantes.CS_NON);
        // pas automatique
        setCsManuAuto(ILEConstantes.CS_NON);
        // pas indexée dans la ged
        setCsIndexationGed(ILEConstantes.CS_NON);
        // pas de sauvegarde
        setCsSauvegarde(ILEConstantes.CS_NON);
        // et sans fenêtre de copies de decriptif
        setCsFenetreCopiesDes(ILEConstantes.CS_NON);
        // de type formule pdf
        setCsType(ILEConstantes.CS_TYPEFORMULEPDF);
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);
        setCsDocument(getDefinitionFormule().getCsDocument());
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdFormule(_incCounter(transaction, "0", IENFormuleDefTable.TABLE_NAME, "0", "0"));
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String table1 = ILEFormuleDefTable.TABLE_NAME;
        String table2 = IENFormuleDefTable.TABLE_NAME;

        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + "." + ILEFormuleDefTable.ID_FORMULE + "=" + _getCollection() + table2 + "."
                + IENFormuleDefTable.ID_FORMULE + ")";
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return ILEFormuleDefTable.TABLE_NAME;
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        classeName = statement.dbReadString(ILEFormuleDefTable.NOM_CLASSE);
        domaine = statement.dbReadString(ILEFormuleDefTable.DOMAINE);
        typeDocument = statement.dbReadString(ILEFormuleDefTable.TYPE_DOC);
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(
                _getCollection() + IENFormuleDefTable.TABLE_NAME + "." + IENFormuleDefTable.ID_FORMULE,
                _dbWriteNumeric(statement.getTransaction(), getIdFormule(),
                        "idFormule - clé primaire du fichier des formules"));
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_writeProperties(globaz.globall.db.BStatement)
     */
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
        statement.writeField(ILEFormuleDefTable.ID_FORMULE,
                _dbWriteNumeric(statement.getTransaction(), getIdFormule(), "id formule"));
        statement.writeField(ILEFormuleDefTable.NOM_CLASSE,
                _dbWriteString(statement.getTransaction(), getClasseName(), "nom de la classe"));
        statement.writeField(ILEFormuleDefTable.DOMAINE,
                _dbWriteNumeric(statement.getTransaction(), getClasseName(), "domaine"));
        statement.writeField(ILEFormuleDefTable.TYPE_DOC,
                _dbWriteNumeric(statement.getTransaction(), getClasseName(), "type de document"));
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

    public String getTypeDocument() {
        return typeDocument;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setClasseName(String string) {
        classeName = string;
    }

    public void setDomaine(String string) {
        domaine = string;
    }

    public void setTypeDocument(String string) {
        typeDocument = string;
    }
}
