/*
 * Créé le 30 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APSitProJointEmployeur extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** DOCUMENT ME! */
    public static final String FIELDNAME_AF_ID_AFFILIE = "MAIAFF";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_AF_NO_AFFILIE = "MALNAF";

    /** DOCUMENT ME! */
    public static final String TABLE_AFFILIATIONS = "AFAFFIP";

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFields() {
        return APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF + ", "
                + APSituationProfessionnelle.FIELDNAME_ISVERSEMENTEMPLOYEUR + ", "
                + APSitProJointEmployeur.FIELDNAME_AF_NO_AFFILIE + ", "
                + APSitProJointEmployeur.FIELDNAME_AF_ID_AFFILIE + ", " + APDroitLAPGJointDemande.FIELDNAME_NOM + ", "
                + APSituationProfessionnelle.FIELDNAME_HAS_ACM_ALPHA_PRESTATION + ", "
                + APSituationProfessionnelle.FIELDNAME_HAS_ACM2_ALPHA_PRESTATION + ", "
                + APSituationProfessionnelle.FIELDNAME_HAS_LAMAT_PRESTATION + ", "
                + APSituationProfessionnelle.FIELDNAME_DATEDEBUT + ", " + APSituationProfessionnelle.FIELDNAME_DATEFIN
                + ", " + APSituationProfessionnelle.FIELDNAME_MONTANT_JOURNALIER_ACM_NE + ", "
                + APSituationProfessionnelle.FIELDNAME_CS_ASSURANCE_ASSOCIATION + " , "
                + APEmployeur.FIELDNAME_ID_TIERS + ", " + APSituationProfessionnelle.FIELDNAME_IDDOMAINE_PAIEMENT
                + ", " + APSituationProfessionnelle.FIELDNAME_IDTIERS_PAIEMENT + " ";
    }

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        // jointure entre table des situation prof et employeur
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE);
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEmployeur.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APSituationProfessionnelle.FIELDNAME_IDEMPLOYEUR);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEmployeur.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APEmployeur.FIELDNAME_ID_EMPLOYEUR);

        // jointure entre table des employeurs et tables des affiliations
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APSitProJointEmployeur.TABLE_AFFILIATIONS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append("(");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEmployeur.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APEmployeur.FIELDNAME_ID_AFFILIE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APSitProJointEmployeur.TABLE_AFFILIATIONS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APSitProJointEmployeur.FIELDNAME_AF_ID_AFFILIE);
        fromClauseBuffer.append(" AND ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEmployeur.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APEmployeur.FIELDNAME_ID_TIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APSitProJointEmployeur.TABLE_AFFILIATIONS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPGJointDemande.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(")");

        // jointure entre table des employeurs et tables des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEmployeur.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APEmployeur.FIELDNAME_ID_TIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPGJointDemande.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    private String csAssuranceAssociation = "";
    private String dateDebut = "";
    private String dateFin = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient String fields = null;
    private transient String fromClause = null;

    private boolean hasAcmAlphaPrestations = false;
    private boolean hasAcm2AlphaPrestations = false;
    private boolean hasLaMatPrestations = false;
    private String idAffilie = "";
    private String idSitPro = "";
    private String idTiers = "";
    private Boolean isVersementEmployeur = Boolean.FALSE;
    private String montantJournalierAcmNe = "";
    private String idDomainePaiementEmployeur = "";
    private String idTiersPaiementEmployeur = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String noAffilie = "";

    private String nom = "";

    /**
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (fields == null) {
            fields = APSitProJointEmployeur.createFields();
        }

        return fields;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = APSitProJointEmployeur.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSitPro = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF);
        isVersementEmployeur = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISVERSEMENTEMPLOYEUR);
        nom = statement.dbReadString(APDroitLAPGJointDemande.FIELDNAME_NOM);
        noAffilie = statement.dbReadString(APSitProJointEmployeur.FIELDNAME_AF_NO_AFFILIE);
        idAffilie = statement.dbReadString(APSitProJointEmployeur.FIELDNAME_AF_ID_AFFILIE);
        idTiers = statement.dbReadString(APEmployeur.FIELDNAME_ID_TIERS);

        dateDebut = statement.dbReadDateAMJ(APSituationProfessionnelle.FIELDNAME_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(APSituationProfessionnelle.FIELDNAME_DATEFIN);
        hasLaMatPrestations = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_HAS_LAMAT_PRESTATION);
        hasAcmAlphaPrestations = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_HAS_ACM_ALPHA_PRESTATION);
        hasAcm2AlphaPrestations = statement
                .dbReadBoolean(APSituationProfessionnelle.FIELDNAME_HAS_ACM2_ALPHA_PRESTATION);
        montantJournalierAcmNe = statement.dbReadNumeric(
                APSituationProfessionnelle.FIELDNAME_MONTANT_JOURNALIER_ACM_NE, 2);
        csAssuranceAssociation = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_CS_ASSURANCE_ASSOCIATION);
        idDomainePaiementEmployeur = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_IDDOMAINE_PAIEMENT);
        idTiersPaiementEmployeur = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_IDTIERS_PAIEMENT);
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
        statement.writeKey(APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF,
                this._dbWriteNumeric(statement.getTransaction(), idSitPro, "idSitPro"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getCsAssuranceAssociation() {
        return csAssuranceAssociation;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * getter pour l'attribut id sit pro
     * 
     * @return la valeur courante de l'attribut id sit pro
     */
    public String getIdSitPro() {
        return idSitPro;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut is versement employeur
     * 
     * @return la valeur courante de l'attribut is versement employeur
     */
    public Boolean getIsVersementEmployeur() {
        return isVersementEmployeur;
    }

    public String getMontantJournalierAcmNe() {
        return montantJournalierAcmNe;
    }

    /**
     * getter pour l'attribut no affilie
     * 
     * @return la valeur courante de l'attribut no affilie
     */
    public String getNoAffilie() {
        return noAffilie;
    }

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    public boolean isHasAcmAlphaPrestations() {
        return hasAcmAlphaPrestations;
    }

    public boolean isHasLaMatPrestations() {
        return hasLaMatPrestations;
    }

    /**
     * retrouve le numero AVS d'un affilie
     * 
     * @return le numero AVS de l'affilie ou un String vide si pas de numero AVS pour cet affilie
     */
    public String retrieveNoAVS() {

        try {
            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(getSession(), getNoAffilie());
            if (affilie != null) {
                return affilie.getNoAVS();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public void setCsAssuranceAssociation(String csAssuranceAssociation) {
        this.csAssuranceAssociation = csAssuranceAssociation;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setHasAcmAlphaPrestations(boolean hasAcmAlphaPrestations) {
        this.hasAcmAlphaPrestations = hasAcmAlphaPrestations;
    }

    public void setHasLaMatPrestations(boolean hasLaMatPrestations) {
        this.hasLaMatPrestations = hasLaMatPrestations;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    /**
     * setter pour l'attribut id sit pro
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSitPro(String string) {
        idSitPro = string;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * setter pour l'attribut is versement employeur
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsVersementEmployeur(Boolean boolean1) {
        isVersementEmployeur = boolean1;
    }

    public void setMontantJournalierAcmNe(String montantJournalierAcmNe) {
        this.montantJournalierAcmNe = montantJournalierAcmNe;
    }

    /**
     * setter pour l'attribut no affilie
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAffilie(String string) {
        noAffilie = string;
    }

    /**
     * setter pour l'attribut nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String string) {
        nom = string;
    }

    public boolean getHasAcm2AlphaPrestations() {
        return hasAcm2AlphaPrestations;
    }

    public void setHasAcm2AlphaPrestations(boolean hasAcm2AlphaPrestations) {
        this.hasAcm2AlphaPrestations = hasAcm2AlphaPrestations;
    }

    public void setIdDomainePaiementEmployeur(String idDomainePaiementEmployeur) {
        this.idDomainePaiementEmployeur = idDomainePaiementEmployeur;
    }

    public void setIdTiersPaiementEmployeur(String idTiersPaiementEmployeur) {
        this.idTiersPaiementEmployeur = idTiersPaiementEmployeur;
    }

    public String getIdDomainePaiementEmployeur() {
        return idDomainePaiementEmployeur;
    }

    public String getIdTiersPaiementEmployeur() {
        return idTiersPaiementEmployeur;
    }
}
