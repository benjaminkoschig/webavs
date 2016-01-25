/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author dvh
 */
public class IJInscriptionCI extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_ANNEE = "XVDANN";

    /**
     */
    public static final String FIELDNAME_IDINSCRIPTIONCI = "XVIICI";

    /**
     */
    public static final String FIELDNAME_MOISDEBUT = "XVDMDE";

    /**
     */
    public static final String FIELDNAME_MOISFIN = "XVDMFI";

    /**
     */
    public static final String FIELDNAME_MONTANTBRUT = "XVMMBR";

    /**
     */
    public static final String FIELDNAME_NOAVS = "XVLAVS";

    /**
	 */
    public static final String FIELDNAME_NOPASSAGE = "XVNOPA";

    /**
     */
    public static final String FIELDNAME_STATUT = "XVTSTA";

    /**
     */
    public static final String TABLE_NAME = "IJINSCCI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String annee = "";
    private String idInscriptionCI = "";
    private String moisDebut = "";
    private String moisFin = "";
    private String montantBrut = "";
    private String noAVS = "";
    private String noPassage = "";
    private String statut = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdInscriptionCI(_incCounter(transaction, "0"));
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idInscriptionCI = statement.dbReadNumeric(FIELDNAME_IDINSCRIPTIONCI);
        noAVS = statement.dbReadString(FIELDNAME_NOAVS);
        moisDebut = statement.dbReadNumeric(FIELDNAME_MOISDEBUT);
        annee = statement.dbReadNumeric(FIELDNAME_ANNEE);
        montantBrut = statement.dbReadNumeric(FIELDNAME_MONTANTBRUT, 2);
        statut = statement.dbReadNumeric(FIELDNAME_STATUT);
        moisFin = statement.dbReadNumeric(FIELDNAME_MOISFIN);
        noPassage = statement.dbReadNumeric(FIELDNAME_NOPASSAGE);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_IDINSCRIPTIONCI,
                _dbWriteNumeric(statement.getTransaction(), idInscriptionCI, "idInscriptionCI"));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_IDINSCRIPTIONCI,
                _dbWriteNumeric(statement.getTransaction(), idInscriptionCI, "idInscriptionCI"));
        statement.writeField(FIELDNAME_NOAVS, _dbWriteString(statement.getTransaction(), noAVS, "noAVS"));
        statement.writeField(FIELDNAME_MOISDEBUT, _dbWriteNumeric(statement.getTransaction(), moisDebut, "moisDebut"));
        statement.writeField(FIELDNAME_ANNEE, _dbWriteNumeric(statement.getTransaction(), annee, "annee"));
        statement.writeField(FIELDNAME_MONTANTBRUT,
                _dbWriteNumeric(statement.getTransaction(), montantBrut, "montantBrut"));
        statement.writeField(FIELDNAME_STATUT, _dbWriteNumeric(statement.getTransaction(), statut, "statut"));
        statement.writeField(FIELDNAME_MOISFIN, _dbWriteNumeric(statement.getTransaction(), moisFin, "moisFin"));
        statement.writeField(FIELDNAME_NOPASSAGE, _dbWriteNumeric(statement.getTransaction(), noPassage, "noPassage"));
    }

    /**
     * getter pour l'attribut annee
     * 
     * @return la valeur courante de l'attribut annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * getter pour l'attribut id inscription CI
     * 
     * @return la valeur courante de l'attribut id inscription CI
     */
    public String getIdInscriptionCI() {
        return idInscriptionCI;
    }

    /**
     * getter pour l'attribut mois debut
     * 
     * @return la valeur courante de l'attribut mois debut
     */
    public String getMoisDebut() {
        return moisDebut;
    }

    /**
     * getter pour l'attribut mois fin
     * 
     * @return la valeur courante de l'attribut mois fin
     */
    public String getMoisFin() {
        return moisFin;
    }

    /**
     * getter pour l'attribut montant brut
     * 
     * @return la valeur courante de l'attribut montant brut
     */
    public String getMontantBrut() {
        return montantBrut;
    }

    /**
     * getter pour l'attribut no AVS
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * getter pour l'attribut noPassage
     * 
     * @return la valeur courante de l'attribut noPassage
     */
    public String getNoPassage() {
        return noPassage;
    }

    /**
     * getter pour l'attribut statut
     * 
     * @return la valeur courante de l'attribut statut
     */
    public String getStatut() {
        return statut;
    }

    /**
     * setter pour l'attribut annee
     * 
     * @param annee
     *            une nouvelle valeur pour cet attribut
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * setter pour l'attribut id inscription CI
     * 
     * @param idInscriptionCI
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdInscriptionCI(String idInscriptionCI) {
        this.idInscriptionCI = idInscriptionCI;
    }

    /**
     * setter pour l'attribut mois debut
     * 
     * @param moisDebut
     *            une nouvelle valeur pour cet attribut
     */
    public void setMoisDebut(String moisDebut) {
        this.moisDebut = moisDebut;
    }

    /**
     * setter pour l'attribut mois fin
     * 
     * @param moisFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setMoisFin(String moisFin) {
        this.moisFin = moisFin;
    }

    /**
     * setter pour l'attribut montant brut
     * 
     * @param montantBrut
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    /**
     * setter pour l'attribut no AVS
     * 
     * @param noAVS
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }

    /**
     * setter pour l'attribut noPassage
     * 
     * @param noPassage
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoPassage(String noPassage) {
        this.noPassage = noPassage;
    }

    /**
     * setter pour l'attribut statut
     * 
     * @param statut
     *            une nouvelle valeur pour cet attribut
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }
}
