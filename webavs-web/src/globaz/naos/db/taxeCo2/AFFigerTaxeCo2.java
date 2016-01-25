package globaz.naos.db.taxeCo2;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class AFFigerTaxeCo2 extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_AFFILIATION_ID = "MAIAFF";
    public static final String FIELDNAME_ANNEE = "ANNEE";
    public static final String FIELDNAME_ID_RUBRIQUE = "IDRUBRIQUE";
    public static final String FIELDNAME_MASSE = "CUMULMASSE";
    public static final String FIELDNAME_MOTIF_FIN = "MATMOT";
    public static final String FIELDNAME_NUM_AFFILIE = "MALNAF";
    // DB
    // Fields
    private String affiliationId = new String();

    private String annee = new String();
    private String Email = new String();
    private String idRubrique = new String();
    private String masse = new String();
    private String motifFin = new String();
    private String numAffilie = new String();

    /**
     * Constructeur de AFAffiliation
     */
    public AFFigerTaxeCo2() {
        super();
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        numAffilie = statement.dbReadString(AFFigerTaxeCo2.FIELDNAME_NUM_AFFILIE);
        affiliationId = statement.dbReadNumeric(AFFigerTaxeCo2.FIELDNAME_AFFILIATION_ID);
        annee = statement.dbReadNumeric(AFFigerTaxeCo2.FIELDNAME_ANNEE);
        masse = statement.dbReadNumeric(AFFigerTaxeCo2.FIELDNAME_MASSE);
        motifFin = statement.dbReadNumeric(AFFigerTaxeCo2.FIELDNAME_MOTIF_FIN);
        idRubrique = statement.dbReadNumeric(AFFigerTaxeCo2.FIELDNAME_ID_RUBRIQUE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    public String getAnnee() {
        return annee;
    }

    public String getEmail() {
        return Email;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFTaxeCo2Manager();
    }

    public String getMasse() {
        return masse;
    }

    public String getMotifFin() {
        return motifFin;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setAffiliationId(java.lang.String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setMasse(String masse) {
        this.masse = masse;
    }

    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }
}
