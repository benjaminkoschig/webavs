/*
 * Créé le 27 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.access.suiviprocedure;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;

/**
 * @author sch
 */
public class CODetailARD extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Code d'arrêt TFA */
    public static final String FIELDNAME_CODE_ARRET_TFA = "OSBATF";

    /** Code de la décision */
    public static final String FIELDNAME_CODE_DECISION = "OSBCDD";

    /** Code de jugement */
    public static final String FIELDNAME_CODE_JUGEMENT = "OSBJUG";

    /** Date d'annulation */
    public static final String FIELDNAME_DATE_ANNULATION = "OSDANN";

    /** Date de l'ARD */
    public static final String FIELDNAME_DATE_ARD = "OSDARD";

    /** Date d'arrêt TFA */
    public static final String FIELDNAME_DATE_ARRET_TFA = "OSDATF";

    /** Date de jugement */
    public static final String FIELDNAME_DATE_JUGEMENT = "OSDJUG";

    /** Date d'opposition */
    public static final String FIELDNAME_DATE_OPPOSITION = "OSDOPP";

    /** Date de recours */
    public static final String FIELDNAME_DATE_RECOURS = "OSDREC";

    /** Date de recours TFA */
    public static final String FIELDNAME_DATE_RECOURS_TFA = "OSDRTF";

    /** Date de la requête de mainlevée / Décision sur opposion */
    public static final String FIELDNAME_DATE_REQUETE = "OSDREQ";

    /** Identifiant du compte annexe auxiliaire */
    public static final String FIELDNAME_IDCOMPTEANNEXE = "OSICOA";

    /** Identifiant du dossier contentieux */
    public static final String FIELDNAME_IDCONTENTIEUX = "OAICON";

    /** Identifiant du détail de l'ARD */
    public static final String FIELDNAME_IDDETAIL_ARD = "OSIARD";

    /** Montant initial ARD */
    public static final String FIELDNAME_MONTANT_ARD = "OSMMON";

    /** Nom de la table */
    public static final String TABLE_NAME = "COARDDP";

    // codes système

    private Boolean codeArretTFA = Boolean.FALSE;
    private Boolean codeDecision = Boolean.FALSE;
    private Boolean codeJugement = Boolean.FALSE;
    private String dateAnnulation = "";
    private String dateARD = "";
    private String dateArretTFA = "";
    private String dateJugement = "";
    private String dateOpposition = "";
    private String dateRecours = "";
    private String dateRecoursTFA = "";
    private String dateRequete = "";
    private String idCompteAnnexe = "";
    private String idContentieux = "";
    private String idDetailARD = "";
    private String montantARD = "";

    private String nomPrenom = "";
    private String numeroAdministrateur = "";
    private String numeroEmployeur = "";
    private String societe = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idDetailARD = this._incCounter(transaction, "0");
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CODetailARD.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDetailARD = statement.dbReadNumeric(CODetailARD.FIELDNAME_IDDETAIL_ARD);
        dateARD = statement.dbReadDateAMJ(CODetailARD.FIELDNAME_DATE_ARD);
        montantARD = statement.dbReadNumeric(CODetailARD.FIELDNAME_MONTANT_ARD, 2);
        dateOpposition = statement.dbReadDateAMJ(CODetailARD.FIELDNAME_DATE_OPPOSITION);
        dateRequete = statement.dbReadDateAMJ(CODetailARD.FIELDNAME_DATE_REQUETE);
        codeDecision = statement.dbReadBoolean(CODetailARD.FIELDNAME_CODE_DECISION);
        dateRecours = statement.dbReadDateAMJ(CODetailARD.FIELDNAME_DATE_RECOURS);
        dateJugement = statement.dbReadDateAMJ(CODetailARD.FIELDNAME_DATE_JUGEMENT);
        codeJugement = statement.dbReadBoolean(CODetailARD.FIELDNAME_CODE_JUGEMENT);
        dateRecoursTFA = statement.dbReadDateAMJ(CODetailARD.FIELDNAME_DATE_RECOURS_TFA);
        dateArretTFA = statement.dbReadDateAMJ(CODetailARD.FIELDNAME_DATE_ARRET_TFA);
        codeArretTFA = statement.dbReadBoolean(CODetailARD.FIELDNAME_CODE_ARRET_TFA);
        dateAnnulation = statement.dbReadDateAMJ(CODetailARD.FIELDNAME_DATE_ANNULATION);
        idCompteAnnexe = statement.dbReadNumeric(CODetailARD.FIELDNAME_IDCOMPTEANNEXE);
        idContentieux = statement.dbReadNumeric(CODetailARD.FIELDNAME_IDCONTENTIEUX);
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
        statement.writeKey(CODetailARD.FIELDNAME_IDDETAIL_ARD,
                this._dbWriteNumeric(statement.getTransaction(), idDetailARD, "idDetailARD"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        BTransaction transaction = statement.getTransaction();
        statement.writeField(CODetailARD.FIELDNAME_IDDETAIL_ARD,
                this._dbWriteNumeric(transaction, idDetailARD, "idDetailARD"));
        statement.writeField(CODetailARD.FIELDNAME_DATE_ARD, this._dbWriteDateAMJ(transaction, dateARD, "dateARD"));
        statement.writeField(CODetailARD.FIELDNAME_MONTANT_ARD,
                this._dbWriteNumeric(transaction, montantARD, "montantARD"));
        statement.writeField(CODetailARD.FIELDNAME_DATE_OPPOSITION,
                this._dbWriteDateAMJ(transaction, dateOpposition, "dateOpposition"));
        statement.writeField(CODetailARD.FIELDNAME_DATE_REQUETE,
                this._dbWriteDateAMJ(transaction, dateRequete, "dateRequete"));
        statement.writeField(CODetailARD.FIELDNAME_CODE_DECISION, this._dbWriteBoolean(statement.getTransaction(),
                codeDecision, BConstants.DB_TYPE_BOOLEAN_CHAR, "codeDecision"));
        statement.writeField(CODetailARD.FIELDNAME_DATE_RECOURS,
                this._dbWriteDateAMJ(transaction, dateRecours, "dateRecours"));
        statement.writeField(CODetailARD.FIELDNAME_DATE_JUGEMENT,
                this._dbWriteDateAMJ(transaction, dateJugement, "dateJugement"));
        statement.writeField(CODetailARD.FIELDNAME_CODE_JUGEMENT, this._dbWriteBoolean(statement.getTransaction(),
                codeJugement, BConstants.DB_TYPE_BOOLEAN_CHAR, "codeJugement"));
        statement.writeField(CODetailARD.FIELDNAME_DATE_RECOURS_TFA,
                this._dbWriteDateAMJ(transaction, dateRecoursTFA, "dateRecoursTFA"));
        statement.writeField(CODetailARD.FIELDNAME_DATE_ARRET_TFA,
                this._dbWriteDateAMJ(transaction, dateArretTFA, "dateArretTFA"));
        statement.writeField(CODetailARD.FIELDNAME_CODE_ARRET_TFA, this._dbWriteBoolean(statement.getTransaction(),
                codeArretTFA, BConstants.DB_TYPE_BOOLEAN_CHAR, "codeArretTFA"));
        statement.writeField(CODetailARD.FIELDNAME_DATE_ANNULATION,
                this._dbWriteDateAMJ(transaction, dateAnnulation, "dateAnnulation"));
        statement.writeField(CODetailARD.FIELDNAME_IDCOMPTEANNEXE,
                this._dbWriteNumeric(transaction, idCompteAnnexe, "idCompteAnnexe"));
        statement.writeField(CODetailARD.FIELDNAME_IDCONTENTIEUX,
                this._dbWriteNumeric(transaction, idContentieux, "idContentieux"));

    }

    public Boolean getCodeArretTFA() {
        return codeArretTFA;
    }

    public Boolean getCodeDecision() {
        return codeDecision;
    }

    public Boolean getCodeJugement() {
        return codeJugement;
    }

    public COContentieux getContentieux() throws Exception {
        COContentieux contentieux = new COContentieux();
        contentieux.setSession(getSession());
        contentieux.setIdContentieux(getIdContentieux());
        contentieux.retrieve();
        if (!contentieux.isNew()) {
            return contentieux;
        }
        return contentieux;
    }

    public String getDateAnnulation() {
        return dateAnnulation;
    }

    public String getDateARD() {
        return dateARD;
    }

    public String getDateArretTFA() {
        return dateArretTFA;
    }

    public String getDateJugement() {
        return dateJugement;
    }

    public String getDateOpposition() {
        return dateOpposition;
    }

    public String getDateRecours() {
        return dateRecours;
    }

    public String getDateRecoursTFA() {
        return dateRecoursTFA;
    }

    public String getDateRequete() {
        return dateRequete;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdContentieux() {
        return idContentieux;
    }

    public String getIdDetailARD() {
        return idDetailARD;
    }

    public String getMontantARD() {
        return JANumberFormatter.deQuote(montantARD);
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getNumeroAdministrateur() {
        return numeroAdministrateur;
    }

    public String getNumeroEmployeur() {
        return numeroEmployeur;
    }

    public String getSociete() {
        return societe;
    }

    public void setCodeArretTFA(Boolean codeArretTFA) {
        this.codeArretTFA = codeArretTFA;
    }

    public void setCodeDecision(Boolean codeDecision) {
        this.codeDecision = codeDecision;
    }

    public void setCodeJugement(Boolean codeJugement) {
        this.codeJugement = codeJugement;
    }

    public void setDateAnnulation(String dateAnnulation) {
        this.dateAnnulation = dateAnnulation;
    }

    public void setDateARD(String dateARD) {
        this.dateARD = dateARD;
    }

    public void setDateArretTFA(String dateArretTFA) {
        this.dateArretTFA = dateArretTFA;
    }

    public void setDateJugement(String dateJugement) {
        this.dateJugement = dateJugement;
    }

    public void setDateOpposition(String dateOpposition) {
        this.dateOpposition = dateOpposition;
    }

    public void setDateRecours(String dateRecours) {
        this.dateRecours = dateRecours;
    }

    public void setDateRecoursTFA(String dateRecoursTFA) {
        this.dateRecoursTFA = dateRecoursTFA;
    }

    public void setDateRequete(String dateRequete) {
        this.dateRequete = dateRequete;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdContentieux(String idContentieux) {
        this.idContentieux = idContentieux;
    }

    public void setIdDetailARD(String idDetailARD) {
        this.idDetailARD = idDetailARD;
    }

    public void setMontantARD(String montantARD) {
        this.montantARD = montantARD;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public void setNumeroAdministrateur(String numeroAdministrateur) {
        this.numeroAdministrateur = numeroAdministrateur;
    }

    public void setNumeroEmployeur(String numeroEmployeur) {
        this.numeroEmployeur = numeroEmployeur;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

}
