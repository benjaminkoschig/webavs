package globaz.naos.api.musca;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * La classe définissant l'entité SuiviAssurance.
 * 
 * @author administrator
 */
public class AFOrganeExternes extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = new String();
    private String dateFin = new String();
    private java.lang.String idAff = new String();
    private java.lang.String idEnteteFacture = new String();
    private java.lang.String idExterneFacture = new String();
    private java.lang.String numAff = new String();

    private java.lang.String totalFacture = new String();

    private java.lang.String typeOrgane = new String();

    /**
     * Constructeur d'AFSuiviAssurance.
     */
    public AFOrganeExternes() {
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
        setNumAff(statement.dbReadString("MALNAF"));
        setIdAff(statement.dbReadString("MAIAFF"));
        setTypeOrgane(statement.dbReadString("MFTPAR"));
        setDateFin(statement.dbReadDateAMJ("MFDFIN"));
        setDateDebut(statement.dbReadDateAMJ("MFDDEB"));
        try {
            setIdExterneFacture(statement.dbReadString("IDEXTERNEFACTURE"));
        } catch (Exception e) {
            setIdExterneFacture(null);
        }
        try {
            setTotalFacture(statement.dbReadString("TOTALFACTURE"));
        } catch (Exception e) {
            setTotalFacture(null);
        }
        try {
            setIdEnteteFacture(statement.dbReadString("IDENTETEFACTURE"));
        } catch (Exception e) {
            setIdEnteteFacture(null);
        }
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

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MALNAF", this._dbWriteString(statement.getTransaction(), getNumAff(), ""));
        statement.writeField("MFTPAR", this._dbWriteString(statement.getTransaction(), getTypeOrgane(), ""));
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public java.lang.String getIdAff() {
        return idAff;
    }

    public java.lang.String getIdEnteteFacture() {
        return idEnteteFacture;
    }

    public java.lang.String getIdExterneFacture() {
        return idExterneFacture;
    }

    public java.lang.String getNumAff() {
        return numAff;
    }

    public java.lang.String getTotalFacture() {
        return totalFacture;
    }

    public java.lang.String getTypeOrgane() {
        return typeOrgane;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdAff(java.lang.String idAff) {
        this.idAff = idAff;
    }

    public void setIdEnteteFacture(java.lang.String idEnteteFacture) {
        this.idEnteteFacture = idEnteteFacture;
    }

    public void setIdExterneFacture(java.lang.String idExternefacutre) {
        idExterneFacture = idExternefacutre;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public void setNumAff(java.lang.String newNumAff) {
        numAff = newNumAff;
    }

    public void setTotalFacture(java.lang.String totalFacture) {
        this.totalFacture = totalFacture;
    }

    public void setTypeOrgane(java.lang.String newTypeOrgane) {
        typeOrgane = newTypeOrgane;
    }
}
