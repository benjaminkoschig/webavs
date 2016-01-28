package globaz.ij.db.prestations;

import globaz.globall.db.BStatement;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJRepartitionJointPrestation extends IJRepartitionPaiements {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME);

        // jointure avec la table des prestations
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(IJPrestation.TABLE_NAME);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_IDPRESTATION);
        fromClause.append("=");
        fromClause.append(IJPrestation.FIELDNAME_IDPRESTATION);

        // jointure avec la table des bases d'indemnisation
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(IJBaseIndemnisation.TABLE_NAME);
        fromClause.append(" ON ");
        fromClause.append(IJPrestation.FIELDNAME_ID_BASEINDEMNISATION);
        fromClause.append("=");
        fromClause.append(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);

        return fromClause.toString();
    }

    private String csTypeBaseIndemnisation = "";
    private String csTypeIJ = "";
    private String dateDebutPrestation = "";
    private String dateFinPrestation = "";
    private String idBaseIndemnisation = "";
    private String idIJCalculee = "";
    private String idPrononce = "";
    private String montantBrutPrestation = "";

    private String montantJournalierExternePrestation = "";
    private String montantJournalierInternePrestation = "";
    private String nombreJourExternePrestation = "";
    private String nombreJourInternePrestation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String remarque = "";

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        dateDebutPrestation = statement.dbReadDateAMJ(IJPrestation.FIELDNAME_DATEDEBUT);
        dateFinPrestation = statement.dbReadDateAMJ(IJPrestation.FIELDNAME_DATEFIN);
        idBaseIndemnisation = statement.dbReadNumeric(IJPrestation.FIELDNAME_ID_BASEINDEMNISATION);
        idIJCalculee = statement.dbReadNumeric(IJPrestation.FIELDNAME_ID_IJCALCULEE);
        montantBrutPrestation = statement.dbReadNumeric(IJPrestation.FIELDNAME_MONTANTBRUT);
        idPrononce = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_IDPRONONCE);
        csTypeIJ = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_TYPE_IJ);
        csTypeBaseIndemnisation = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_TYPE);
        remarque = statement.dbReadString(IJBaseIndemnisation.FIELDNAME_REMARQUE);

        nombreJourInternePrestation = statement.dbReadNumeric(IJPrestation.FIELDNAME_NOMBRE_JOURS_INT);
        nombreJourExternePrestation = statement.dbReadNumeric(IJPrestation.FIELDNAME_NOMBRE_JOURS_EXT);
        montantJournalierInternePrestation = statement.dbReadNumeric(IJPrestation.FIELDNAME_MONTANT_JOURNALIER_INT);
        montantJournalierExternePrestation = statement.dbReadNumeric(IJPrestation.FIELDNAME_MONTANT_JOURNALIER_EXT);
    }

    /**
     * getter pour l'attribut cs type base indemnisation.
     * 
     * @return la valeur courante de l'attribut cs type base indemnisation
     */
    public String getCsTypeBaseIndemnisation() {
        return csTypeBaseIndemnisation;
    }

    /**
     * getter pour l'attribut cs type IJ.
     * 
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut date debut prestation.
     * 
     * @return la valeur courante de l'attribut date debut prestation
     */
    public String getDateDebutPrestation() {
        return dateDebutPrestation;
    }

    /**
     * getter pour l'attribut date fin prestation.
     * 
     * @return la valeur courante de l'attribut date fin prestation
     */
    public String getDateFinPrestation() {
        return dateFinPrestation;
    }

    /**
     * getter pour l'attribut id base indemnisation.
     * 
     * @return la valeur courante de l'attribut id base indemnisation
     */
    public String getIdBaseIndemnisation() {
        return idBaseIndemnisation;
    }

    /**
     * getter pour l'attribut id IJCalculee.
     * 
     * @return la valeur courante de l'attribut id IJCalculee
     */
    public String getIdIJCalculee() {
        return idIJCalculee;
    }

    /**
     * getter pour l'attribut id prononce.
     * 
     * @return la valeur courante de l'attribut id prononce
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    /**
     * getter pour l'attribut montant brut prestation.
     * 
     * @return la valeur courante de l'attribut montant brut prestation
     */
    public String getMontantBrutPrestation() {
        return montantBrutPrestation;
    }

    public String getMontantJournalierExternePrestation() {
        return montantJournalierExternePrestation;
    }

    public String getMontantJournalierInternePrestation() {
        return montantJournalierInternePrestation;
    }

    public String getNombreJourExternePrestation() {
        return nombreJourExternePrestation;
    }

    public String getNombreJourInternePrestation() {
        return nombreJourInternePrestation;
    }

    /**
     * getter pour l'attribut remarque.
     * 
     * @return la valeur courante de l'attribut remarque
     */
    public String getRemarque() {
        return remarque;
    }

    /**
     * setter pour l'attribut cs type base indemnisation.
     * 
     * @param csTypeBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeBaseIndemnisation(String csTypeBaseIndemnisation) {
        this.csTypeBaseIndemnisation = csTypeBaseIndemnisation;
    }

    /**
     * setter pour l'attribut cs type IJ.
     * 
     * @param csTypeIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String csTypeIJ) {
        this.csTypeIJ = csTypeIJ;
    }

    /**
     * setter pour l'attribut date debut prestation.
     * 
     * @param dateDebutPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPrestation(String dateDebutPrestation) {
        this.dateDebutPrestation = dateDebutPrestation;
    }

    /**
     * setter pour l'attribut date fin prestation.
     * 
     * @param dateFinPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinPrestation(String dateFinPrestation) {
        this.dateFinPrestation = dateFinPrestation;
    }

    /**
     * setter pour l'attribut id base indemnisation.
     * 
     * @param idBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdBaseIndemnisation(String idBaseIndemnisation) {
        this.idBaseIndemnisation = idBaseIndemnisation;
    }

    /**
     * setter pour l'attribut id IJCalculee.
     * 
     * @param idIJCalculee
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdIJCalculee(String idIJCalculee) {
        this.idIJCalculee = idIJCalculee;
    }

    /**
     * setter pour l'attribut id prononce.
     * 
     * @param idPrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    /**
     * setter pour l'attribut montant brut prestation.
     * 
     * @param montantBrutPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrutPrestation(String montantBrutPrestation) {
        this.montantBrutPrestation = montantBrutPrestation;
    }

    public void setMontantJournalierExternePrestation(String montantJournalierExternePrestation) {
        this.montantJournalierExternePrestation = montantJournalierExternePrestation;
    }

    public void setMontantJournalierInternePrestation(String montantJournalierInternePrestation) {
        this.montantJournalierInternePrestation = montantJournalierInternePrestation;
    }

    public void setNombreJourExternePrestation(String nombreJourExternePrestation) {
        this.nombreJourExternePrestation = nombreJourExternePrestation;
    }

    public void setNombreJourInternePrestation(String nombreJourInternePrestation) {
        this.nombreJourInternePrestation = nombreJourInternePrestation;
    }

    /**
     * setter pour l'attribut remarque.
     * 
     * @param remarque
     *            une nouvelle valeur pour cet attribut
     */
    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
}
