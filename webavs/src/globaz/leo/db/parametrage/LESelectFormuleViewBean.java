package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.IENDefinitionFormuleDefTable;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author SCO
 * @since 12 juil. 2010
 */
public class LESelectFormuleViewBean extends BEntity implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDocument = new String();
    private String idDefinitionFormule = new String();
    private String libelle = new String();

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        csDocument = statement.dbReadNumeric(IENDefinitionFormuleDefTable.CS_DOCUMENT);
        idDefinitionFormule = statement.dbReadNumeric(IENDefinitionFormuleDefTable.ID_DEFINITION_FORMULE);
        libelle = statement.dbReadString("PCOLUT");
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

    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getCsDocument() {
        return csDocument;
    }

    public String getIdDefinitionFormule() {
        return idDefinitionFormule;
    }

    public String getLibelle() {
        return libelle;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setCsDocument(String csDocument) {
        this.csDocument = csDocument;
    }

    public void setIdDefinitionFormule(String idDefinitionFormule) {
        this.idDefinitionFormule = idDefinitionFormule;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

}
