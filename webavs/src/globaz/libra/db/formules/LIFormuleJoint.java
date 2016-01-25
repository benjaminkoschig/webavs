package globaz.libra.db.formules;

import globaz.envoi.db.parametreEnvoi.access.ENFormule;
import globaz.envoi.db.parametreEnvoi.access.IENDefinitionFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENFormulePDFDefTable;
import globaz.globall.db.BStatement;

public class LIFormuleJoint extends ENFormule {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String csDomaine = new String();

    public String csTypeDoc = new String();
    private transient String fromClause = null;
    // TABLE FormulePDF (ENPPPDF)
    public String nomClasse = new String();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        nomClasse = statement.dbReadString(IENFormulePDFDefTable.NOM_CLASSE);
        csDomaine = statement.dbReadNumeric(IENFormulePDFDefTable.DOMAINE);
        csTypeDoc = statement.dbReadNumeric(IENFormulePDFDefTable.TYPE_DOC);

    }

    /**
     * Génération de la clause from pour la requête > Jointure depuis la table ENPPF01 (formule) sur :
     * 
     * > ENPPPDF (FormulePDF)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IENFormuleDefTable.TABLE_NAME);

        // Jointure entre table des formule et formule pdf
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IENFormulePDFDefTable.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IENFormuleDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IENFormuleDefTable.ID_FORMULE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IENFormulePDFDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IENFormulePDFDefTable.ID_FORMULE);

        // jointure sur ENPPDEF
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IENDefinitionFormuleDefTable.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IENFormuleDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IENFormuleDefTable.ID_DEFINITION_FORMULE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IENDefinitionFormuleDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IENDefinitionFormuleDefTable.ID_DEFINITION_FORMULE);

        // jointure sur FWCOUP pour recherche sur pcolut
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append("FWCOUP");
        fromClauseBuffer.append(" ON ").append(schema).append("FWCOUP").append(point).append("PCOSID");
        fromClauseBuffer.append("=").append(schema).append(IENDefinitionFormuleDefTable.TABLE_NAME).append(point)
                .append(IENDefinitionFormuleDefTable.CS_DOCUMENT);
        fromClauseBuffer.append(" AND ").append(schema).append("FWCOUP").append(point).append("PLAIDE").append("=")
                .append("'").append(getSession().getIdLangue()).append("'");

        return fromClauseBuffer.toString();
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getCsTypeDoc() {
        return csTypeDoc;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setCsTypeDoc(String csTypeDoc) {
        this.csTypeDoc = csTypeDoc;
    }

    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }

}
