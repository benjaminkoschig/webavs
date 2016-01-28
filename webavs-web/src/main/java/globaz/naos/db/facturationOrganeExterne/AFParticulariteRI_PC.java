package globaz.naos.db.facturationOrganeExterne;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * La classe définissant l'entité SuiviAssurance.
 * 
 * @author administrator
 */
public class AFParticulariteRI_PC extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idAffiliation = new String();
    private String numAffilie = new String();
    private String particularite = new String();

    /**
     * Constructeur d'AFSuiviAssurance.
     */
    public AFParticulariteRI_PC() {
        super();
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {

    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAffiliation = statement.dbReadString("MAIAFF");
        numAffilie = statement.dbReadString("MALNAF");
        particularite = statement.dbReadString("MFTPAR");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getParticularite() {
        return particularite;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setParticularite(String particularite) {
        this.particularite = particularite;
    }

}
