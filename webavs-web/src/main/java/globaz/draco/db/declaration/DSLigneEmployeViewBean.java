package globaz.draco.db.declaration;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class DSLigneEmployeViewBean extends BEntity {
    /** Fichier DSLIEMP */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (KAIIND) */
    private String compteIndividuelId = new String();
    /** (TCDDEB) */
    private String dateDebut = new String();
    /** (TAIDDE) */
    private String idDeclaration = new String();
    /** (TCILIE) */
    private String idLigneEmploye = new String();
    /** (KALNOM) */
    private String nomPrenom = new String();
    /** (KANAVS) */
    private String numeroAVS = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur DSLigneEmploye
     */
    public DSLigneEmployeViewBean() {
        super();
    }

    /**
     * Permet d'effectuer des op�rations avant de lancer l'ajout
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incr�mente de +1 le num�ro
        setIdLigneEmploye(_incCounter(transaction, getIdLigneEmploye()));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "DSLIEMP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLigneEmploye = statement.dbReadNumeric("TCILIE");
        idDeclaration = statement.dbReadNumeric("TAIDDE");
        compteIndividuelId = statement.dbReadNumeric("KAIIND");
        numeroAVS = statement.dbReadString("KANAVS");
        nomPrenom = statement.dbReadString("KALNOM");
        dateDebut = statement.dbReadDateAMJ("TCDDEB");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
	
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("TCILIE", _dbWriteNumeric(statement.getTransaction(), getIdLigneEmploye(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField("TCILIE",
                _dbWriteNumeric(statement.getTransaction(), getIdLigneEmploye(), "idLigneEmploye"));
        statement
                .writeField("TAIDDE", _dbWriteNumeric(statement.getTransaction(), getIdDeclaration(), "idDeclaration"));
        statement.writeField("KAIIND",
                _dbWriteNumeric(statement.getTransaction(), getCompteIndividuelId(), "compteIndividuelId"));
        statement.writeField("KANAVS", _dbWriteString(statement.getTransaction(), getNumeroAVS(), "numeroAVS"));
        statement.writeField("KALNOM", _dbWriteString(statement.getTransaction(), getNomPrenom(), "nomPrenom"));
        statement.writeField("TCDDEB", _dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
    }

    public String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getIdDeclaration() {
        return idDeclaration;
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @return String
     */
    public String getIdLigneEmploye() {
        return idLigneEmploye;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getNumeroAVS() {
        return numeroAVS;
    }

    public void setCompteIndividuelId(String newCompteIndividuelId) {
        compteIndividuelId = newCompteIndividuelId;
    }

    public void setDateDebut(String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setIdDeclaration(String newIdDeclaration) {
        idDeclaration = newIdDeclaration;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
     * 
     * @param newD
     *            String
     */
    public void setIdLigneEmploye(String newIdLigneEmploye) {
        idLigneEmploye = newIdLigneEmploye;
    }

    public void setNomPrenom(String newNomPrenom) {
        nomPrenom = newNomPrenom;
    }

    public void setNumeroAVS(String newNumeroAVS) {
        numeroAVS = newNumeroAVS;
    }

}