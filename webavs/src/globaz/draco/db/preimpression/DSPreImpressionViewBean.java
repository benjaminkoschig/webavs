package globaz.draco.db.preimpression;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class DSPreImpressionViewBean extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (KBTCAT) **/
    private String categoriePersonnel;
    /** (KAIIND) **/
    private String compteIndividuelId;
    /** (DAENG) **/
    private String dateEngagement;
    /** (DALIC) **/
    private String dateLicenciement;
    /** KADNAI **/
    private String dateNaissance;
    /**
     * Entity permettant d'afficher les assurés dans la liste de pré-impression des déclarations de salaire
     */
    /** (KALNOM) **/
    private String nomPrenom;
    /** (KANAVS) **/
    private String numeroAvs;

    /** KATSEX **/
    private String sexe;

    /**
     * Commentaire relatif au constructeur DSContentieux
     */
    public DSPreImpressionViewBean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        compteIndividuelId = statement.dbReadNumeric("KAIIND");
        nomPrenom = statement.dbReadString("KALNOM");
        numeroAvs = statement.dbReadString("KANAVS");
        dateEngagement = statement.dbReadString("DAENG");
        dateLicenciement = statement.dbReadString("DALIC");
        categoriePersonnel = statement.dbReadNumeric("KBTCAT");
        dateNaissance = statement.dbReadDateAMJ("KADNAI");
        sexe = statement.dbReadNumeric("KATSEX");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KANAVS", _dbWriteNumeric(statement.getTransaction(), getNumeroAvs(), "numeroAvs"));
        statement.writeField("KALNOM", _dbWriteString(statement.getTransaction(), getNomPrenom(), "nomPrenom"));
        statement.writeField("DALIC",
                _dbWriteDateAMJ(statement.getTransaction(), getDateEngagement(), "dateEngagement"));
        statement.writeField("DAENG",
                _dbWriteDateAMJ(statement.getTransaction(), getDateLicenciement(), "dateLicenciement"));
    }

    /**
     * @return
     */
    public String getCategoriePersonnel() {
        return categoriePersonnel;
    }

    /**
     * @return
     */
    public String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    /**
     * Retourne la date d'engagement
     * 
     * @return dateEngagement
     */
    public String getDateEngagement() {
        return dateEngagement;
    }

    /**
     * Retoune la date de licenciement
     * 
     * @return dateLicenciement
     */
    public String getDateLicenciement() {
        return dateLicenciement;
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Retourne le nom et le prénom
     * 
     * @return nomPrenom
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * Retourne le numéro Avs
     * 
     * @return numeroAvs
     */
    public String getNumeroAvs() {
        return numeroAvs;
    }

    /**
     * @return the sexe
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * @param string
     */
    public void setCategoriePersonnel(String string) {
        categoriePersonnel = string;
    }

    /**
     * @param string
     */
    public void setCompteIndividuelId(String string) {
        compteIndividuelId = string;
    }

    /**
     * Sette la date d'engagement
     * 
     * @param string
     */
    public void setDateEngagement(String string) {
        dateEngagement = string;
    }

    /**
     * Sette la date de licenciement
     * 
     * @param string
     */
    public void setDateLicenciement(String string) {
        dateLicenciement = string;
    }

    /**
     * @param dateNaissance
     *            the dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * Sette le nom et le prénom
     * 
     * @param string
     */
    public void setNomPrenom(String string) {
        nomPrenom = string;
    }

    /**
     * Sette le numéro Avs
     * 
     * @param string
     */
    public void setNumeroAvs(String string) {
        numeroAvs = string;
    }

    /**
     * @param sexe
     *            the sexe to set
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

}
