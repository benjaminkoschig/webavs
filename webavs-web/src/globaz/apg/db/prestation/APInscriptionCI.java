package globaz.apg.db.prestation;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APInscriptionCI extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ANNEE = "VJNANN";
    public static final String FIELDNAME_IDINSCRIPTIONCI = "VJIICI";
    public static final String FIELDNAME_MOISDEBUT = "VJDDEB";
    public static final String FIELDNAME_MOISFIN = "VJDFIN";
    public static final String FIELDNAME_MONTANTBRUT = "VJMMOB";
    public static final String FIELDNAME_NOAVS = "VJLAVS";
    public static final String FIELDNAME_NOPASSAGE = "VJNOPA";
    public static final String FIELDNAME_STATUT = "VJTSTA";
    public static final String TABLE_NAME = "APINCIP";

    private String annee = "";
    private String idInscription = "";
    private String moisDebut = "";
    private String moisFin = "";
    private String montantBrut = "";
    private String noAVS = "";
    private String noPassage = "";
    private String statut = "";

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdInscription(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return APInscriptionCI.TABLE_NAME;
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idInscription = statement.dbReadNumeric(APInscriptionCI.FIELDNAME_IDINSCRIPTIONCI);
        noAVS = NSUtil.formatAVSUnknown(statement.dbReadString(APInscriptionCI.FIELDNAME_NOAVS));
        moisDebut = statement.dbReadNumeric(APInscriptionCI.FIELDNAME_MOISDEBUT);
        moisFin = statement.dbReadNumeric(APInscriptionCI.FIELDNAME_MOISFIN);
        annee = statement.dbReadNumeric(APInscriptionCI.FIELDNAME_ANNEE);
        montantBrut = statement.dbReadNumeric(APInscriptionCI.FIELDNAME_MONTANTBRUT, 2);
        statut = statement.dbReadNumeric(APInscriptionCI.FIELDNAME_STATUT);
        noPassage = statement.dbReadNumeric(APInscriptionCI.FIELDNAME_NOPASSAGE);
    }

    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(APInscriptionCI.FIELDNAME_IDINSCRIPTIONCI,
                this._dbWriteNumeric(statement.getTransaction(), idInscription, "idInscription"));
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(APInscriptionCI.FIELDNAME_IDINSCRIPTIONCI,
                this._dbWriteNumeric(statement.getTransaction(), idInscription, "idInscription"));
        statement.writeField(APInscriptionCI.FIELDNAME_NOAVS,
                this._dbWriteString(statement.getTransaction(), NSUtil.unFormatAVS(noAVS), "noAVS"));
        statement.writeField(APInscriptionCI.FIELDNAME_MOISDEBUT,
                this._dbWriteNumeric(statement.getTransaction(), moisDebut, "moisDebut"));
        statement.writeField(APInscriptionCI.FIELDNAME_MOISFIN,
                this._dbWriteNumeric(statement.getTransaction(), moisFin, "moisFin"));
        statement.writeField(APInscriptionCI.FIELDNAME_ANNEE,
                this._dbWriteNumeric(statement.getTransaction(), annee, "annee"));
        statement.writeField(APInscriptionCI.FIELDNAME_MONTANTBRUT,
                this._dbWriteNumeric(statement.getTransaction(), montantBrut, "montantBrut"));
        statement.writeField(APInscriptionCI.FIELDNAME_STATUT,
                this._dbWriteNumeric(statement.getTransaction(), statut, "statut"));
        statement.writeField(APInscriptionCI.FIELDNAME_NOPASSAGE,
                this._dbWriteNumeric(statement.getTransaction(), noPassage, "noPassage"));
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
     * getter pour l'attribut id inscription
     * 
     * @return la valeur courante de l'attribut id inscription
     */
    public String getIdInscription() {
        return idInscription;
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
     * setter pour l'attribut id inscription
     * 
     * @param idInscription
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdInscription(String idInscription) {
        this.idInscription = idInscription;
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
