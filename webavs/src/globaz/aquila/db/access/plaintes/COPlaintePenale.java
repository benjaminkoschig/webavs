/*
 * Créé le 27 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.access.plaintes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;

/**
 * @author dvh
 */
public class COPlaintePenale extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // codes système
    // Description_plainte
    /** DOCUMENT ME! */
    public static final String CS_GROUPE_DESCIPTION_PLAINTE = "CODESPLA";

    // Motif_plainte
    /** DOCUMENT ME! */
    public static final String CS_GROUPE_MOTIF_PLAINTE = "COMOTPLA";

    // Type_plainte
    /** DOCUMENT ME! */
    public static final String CS_GROUPE_TYPE_PLAINTE = "COTYPPLA";

    /** DOCUMENT ME! */
    public final static String CS_MENACE = "5700002";

    /** DOCUMENT ME! */
    public final static String CS_PARITAIRE = "5800001";

    /** DOCUMENT ME! */
    public final static String CS_PLAINTE = "5700001";

    /** DOCUMENT ME! */
    public final static String CS_SOUSTRACTION_DE_COTISATIONS = "5900001";

    /** Commentaires */
    public static final String FIELDNAME_COMMENTAIRES = "OLLCOM";

    /** Description de la plainte */
    public static final String FIELDNAME_CS_DESCRIPTIONPLAINTE = "OLTDES";

    /** Motif de la plainte */
    public static final String FIELDNAME_CS_MOTIFPLAINTE = "OLTMOT";

    /** Type de plainte */
    public static final String FIELDNAME_CS_TYPEPLAINTE = "OLTTYP";

    /** Date d'annulation de la plainte */
    public static final String FIELDNAME_DATEANNULATION = "OLDANN";

    /** Date de citation */
    public static final String FIELDNAME_DATECITATION = "OLDCIT";

    /** Date de jugement */
    public static final String FIELDNAME_DATEJUGEMENT = "OLDJUG";

    /** Date de la plainte */
    public static final String FIELDNAME_DATEPLAINTE = "OLDPLA";

    /** Date de relance */
    public static final String FIELDNAME_DATERELANCE = "OLDREL";

    /** Date de suspension */
    public static final String FIELDNAME_DATESUSPENSION = "OLDSUS";

    /** Identifiant du compte annexe auxiliaire */
    public static final String FIELDNAME_IDCOMPTEAUXILIAIRE = "OLICOA";

    /** Identifiant de la plainte */
    public static final String FIELDNAME_IDPLAINTE = "OLIPLA";

    /** Montant de la plainte */
    public static final String FIELDNAME_MONTANTPLAINTE = "OLMMON";

    /** Période au */
    public static final String FIELDNAME_PERIODEAU = "OLDPEA";

    /** Période du */
    public static final String FIELDNAME_PERIODEDU = "OLDPED";

    /** DOCUMENT ME! */
    public static final String TABLE_NAME = "COPLAIP";

    private String commentaires = "";
    private String csDescriptionPlainte = "";
    private String csMotifPlainte = "";
    private String csTypePlainte = "";
    private String dateAnnulation = "";
    private String dateCitation = "";
    private String dateJugement = "";
    private String datePlainte = "";
    private String dateRelance = "";
    private String dateSuspension = "";
    private String idCompteAuxiliaire = "";
    private String idPlainte = "";
    private String montantPlainte = "";
    private String nomPrenom = "";
    private String numeroAdministrateur = "";
    private String numeroEmployeur = "";
    private String periodeAu = "";
    private String periodeDu = "";
    private String societe = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idPlainte = this._incCounter(transaction, "0");
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return COPlaintePenale.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        commentaires = statement.dbReadString(COPlaintePenale.FIELDNAME_COMMENTAIRES);
        csDescriptionPlainte = statement.dbReadNumeric(COPlaintePenale.FIELDNAME_CS_DESCRIPTIONPLAINTE);
        csMotifPlainte = statement.dbReadNumeric(COPlaintePenale.FIELDNAME_CS_MOTIFPLAINTE);
        csTypePlainte = statement.dbReadNumeric(COPlaintePenale.FIELDNAME_CS_TYPEPLAINTE);
        dateAnnulation = statement.dbReadDateAMJ(COPlaintePenale.FIELDNAME_DATEANNULATION);
        datePlainte = statement.dbReadDateAMJ(COPlaintePenale.FIELDNAME_DATEPLAINTE);
        dateRelance = statement.dbReadDateAMJ(COPlaintePenale.FIELDNAME_DATERELANCE);
        dateSuspension = statement.dbReadDateAMJ(COPlaintePenale.FIELDNAME_DATESUSPENSION);
        idCompteAuxiliaire = statement.dbReadNumeric(COPlaintePenale.FIELDNAME_IDCOMPTEAUXILIAIRE);
        idPlainte = statement.dbReadNumeric(COPlaintePenale.FIELDNAME_IDPLAINTE);
        montantPlainte = statement.dbReadNumeric(COPlaintePenale.FIELDNAME_MONTANTPLAINTE, 2);
        periodeDu = statement.dbReadDateAMJ(COPlaintePenale.FIELDNAME_PERIODEDU);
        periodeAu = statement.dbReadDateAMJ(COPlaintePenale.FIELDNAME_PERIODEAU);
        dateCitation = statement.dbReadDateAMJ(COPlaintePenale.FIELDNAME_DATECITATION);
        dateJugement = statement.dbReadDateAMJ(COPlaintePenale.FIELDNAME_DATEJUGEMENT);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        BTransaction transaction = statement.getTransaction();
        _propertyMandatory(transaction, datePlainte, getSession().getLabel("AQUILA_DATE_PLAINTE_OBLIGATOIRE"));
        _propertyMandatory(transaction, csDescriptionPlainte,
                getSession().getLabel("AQUILA_DESCRIPTION_PLAINTE_OBLIGATOIRE"));
        _propertyMandatory(transaction, csTypePlainte, getSession().getLabel("AQUILA_TYPE_OBLIGATOIRE"));
        _propertyMandatory(transaction, csMotifPlainte, getSession().getLabel("AQUILA_MOTIF_PLAINTE_OBLIGATOIRE"));
        _propertyMandatory(transaction, periodeDu, getSession().getLabel("AQUILA_PERIODES_OBLIGATOIRE"));
        _propertyMandatory(transaction, periodeAu, getSession().getLabel("AQUILA_PERIODES_OBLIGATOIRE"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(COPlaintePenale.FIELDNAME_IDPLAINTE,
                this._dbWriteNumeric(statement.getTransaction(), idPlainte, "idPlainte"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        BTransaction transaction = statement.getTransaction();
        statement.writeField(COPlaintePenale.FIELDNAME_COMMENTAIRES,
                this._dbWriteString(transaction, commentaires, "commentaires"));
        statement.writeField(COPlaintePenale.FIELDNAME_CS_DESCRIPTIONPLAINTE,
                this._dbWriteNumeric(transaction, csDescriptionPlainte, "csDescriptionPlainte"));
        statement.writeField(COPlaintePenale.FIELDNAME_CS_MOTIFPLAINTE,
                this._dbWriteNumeric(transaction, csMotifPlainte, "csMotifPlainte"));
        statement.writeField(COPlaintePenale.FIELDNAME_CS_TYPEPLAINTE,
                this._dbWriteNumeric(transaction, csTypePlainte, "csTypePlainte"));
        statement.writeField(COPlaintePenale.FIELDNAME_DATEANNULATION,
                this._dbWriteDateAMJ(transaction, dateAnnulation, "dateAnnulation"));
        statement.writeField(COPlaintePenale.FIELDNAME_DATEPLAINTE,
                this._dbWriteDateAMJ(transaction, datePlainte, "datePlainte"));
        statement.writeField(COPlaintePenale.FIELDNAME_DATERELANCE,
                this._dbWriteDateAMJ(transaction, dateRelance, "dateRelance"));
        statement.writeField(COPlaintePenale.FIELDNAME_DATESUSPENSION,
                this._dbWriteDateAMJ(transaction, dateSuspension, "dateSuspension"));
        statement.writeField(COPlaintePenale.FIELDNAME_IDCOMPTEAUXILIAIRE,
                this._dbWriteNumeric(transaction, idCompteAuxiliaire, "idCompteAuxiliaire"));
        statement.writeField(COPlaintePenale.FIELDNAME_IDPLAINTE,
                this._dbWriteNumeric(transaction, idPlainte, "idPlainte"));
        statement.writeField(COPlaintePenale.FIELDNAME_MONTANTPLAINTE,
                this._dbWriteNumeric(transaction, montantPlainte, "montantPlainte"));
        statement.writeField(COPlaintePenale.FIELDNAME_PERIODEDU,
                this._dbWriteDateAMJ(transaction, periodeDu, "periodeDu"));
        statement.writeField(COPlaintePenale.FIELDNAME_PERIODEAU,
                this._dbWriteDateAMJ(transaction, periodeAu, "periodeAu"));
        statement.writeField(COPlaintePenale.FIELDNAME_DATECITATION,
                this._dbWriteDateAMJ(transaction, dateCitation, "dateCitation"));
        statement.writeField(COPlaintePenale.FIELDNAME_DATEJUGEMENT,
                this._dbWriteDateAMJ(transaction, dateJugement, "dateJugement"));

    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getCommentaires() {
        return commentaires;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getCsDescriptionPlainte() {
        return csDescriptionPlainte;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getCsMotifPlainte() {
        return csMotifPlainte;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getCsTypePlainte() {
        return csTypePlainte;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getDateAnnulation() {
        return dateAnnulation;
    }

    public String getDateCitation() {
        return dateCitation;
    }

    public String getDateJugement() {
        return dateJugement;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getDatePlainte() {
        return datePlainte;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getDateRelance() {
        return dateRelance;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getDateSuspension() {
        return dateSuspension;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getIdCompteAuxiliaire() {
        return idCompteAuxiliaire;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getIdPlainte() {
        return idPlainte;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getMontantPlainte() {
        return JANumberFormatter.deQuote(montantPlainte);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getNumeroAdministrateur() {
        return numeroAdministrateur;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getNumeroEmployeur() {
        return numeroEmployeur;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getPeriodeAu() {
        return periodeAu;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getPeriodeDu() {
        return periodeDu;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getSociete() {
        return societe;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setCommentaires(String string) {
        commentaires = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setCsDescriptionPlainte(String string) {
        csDescriptionPlainte = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setCsMotifPlainte(String string) {
        csMotifPlainte = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setCsTypePlainte(String string) {
        csTypePlainte = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setDateAnnulation(String string) {
        dateAnnulation = string;
    }

    public void setDateCitation(String dateCitation) {
        this.dateCitation = dateCitation;
    }

    public void setDateJugement(String dateJugement) {
        this.dateJugement = dateJugement;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setDatePlainte(String string) {
        datePlainte = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setDateRelance(String string) {
        dateRelance = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setDateSuspension(String string) {
        dateSuspension = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setIdCompteAuxiliaire(String string) {
        idCompteAuxiliaire = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setIdPlainte(String string) {
        idPlainte = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setMontantPlainte(String string) {
        montantPlainte = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setNomPrenom(String string) {
        nomPrenom = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setNumeroAdministrateur(String string) {
        numeroAdministrateur = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setNumeroEmployeur(String string) {
        numeroEmployeur = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setPeriodeAu(String string) {
        periodeAu = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setPeriodeDu(String string) {
        periodeDu = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setSociete(String string) {
        societe = string;
    }

}
