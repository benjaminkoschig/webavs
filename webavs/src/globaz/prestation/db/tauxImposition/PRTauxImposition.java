/*
 * Créé le 23 juin 05
 */
package globaz.prestation.db.tauxImposition;

import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class PRTauxImposition extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_CS_CANTON = "WBTCAN";

    /**
     */
    public static final String FIELDNAME_DATEDEBUT = "WBDDEB";

    /**
     */
    public static final String FIELDNAME_DATEFIN = "WBDFIN";

    /**
     */
    public static final String FIELDNAME_IDTAUXIMPOSITION = "WBITAI";

    /**
     */
    public static final String FIELDNAME_TAUX = "WBMTAU";

    /**
     */
    public static final String FIELDNAME_TYPEIMPOTSOURCE = "WBTTYP";

    /**
     */
    public static final String TABLE_NAME = "PRTAIMP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csCanton = "";
    private String dateDebut = "";
    private String dateFin = "";
    private String idTauxImposition = "";
    private String taux = "";
    private String typeImpotSource = "";

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
        setIdTauxImposition(_incCounter(transaction, "0"));
    }

    /**
     * DOCUMENT ME!
     * 
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
        idTauxImposition = statement.dbReadNumeric(FIELDNAME_IDTAUXIMPOSITION);
        dateDebut = statement.dbReadDateAMJ(FIELDNAME_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(FIELDNAME_DATEFIN);
        taux = statement.dbReadNumeric(FIELDNAME_TAUX, 6);
        csCanton = statement.dbReadNumeric(FIELDNAME_CS_CANTON);
        typeImpotSource = statement.dbReadNumeric(FIELDNAME_TYPEIMPOTSOURCE);
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

        // la nouvelle periode du taux d'imposition ne doit pas superposer une
        // periode existante
        // pour un meme canton et type d'imposition a la source
        PRTauxImpositionManager periodes = new PRTauxImpositionManager();
        periodes.setSession(getSession());
        periodes.setForCsCanton(getCsCanton());
        periodes.setForTypeImpot(getTypeImpotSource());
        periodes.setAfterDateFin(getDateDebut());
        periodes.setForIdTauxImpositionDifferentFrom(getIdTauxImposition());
        if (periodes.getCount() > 0) {
            _addError(statement.getTransaction(),
                    "La période du nouveau taux d'imposition recouvre la période d'un taux existant pour le même canton et le même type.");
        }

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
        statement.writeKey("WBITAI", _dbWriteNumeric(statement.getTransaction(), idTauxImposition, "idTauxImposition"));
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
        statement.writeField(FIELDNAME_IDTAUXIMPOSITION,
                _dbWriteNumeric(statement.getTransaction(), idTauxImposition, "idTauxImposition"));
        statement.writeField(FIELDNAME_DATEDEBUT, _dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(FIELDNAME_DATEFIN, _dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(FIELDNAME_TAUX, _dbWriteNumeric(statement.getTransaction(), taux, "taux"));
        statement.writeField(FIELDNAME_CS_CANTON, _dbWriteNumeric(statement.getTransaction(), csCanton, "csCanton"));
        statement.writeField(FIELDNAME_TYPEIMPOTSOURCE,
                _dbWriteNumeric(statement.getTransaction(), typeImpotSource, "typeImpotSource"));
    }

    /**
     * getter pour l'attribut id tiers
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getCsCanton() {
        return csCanton;
    }

    /**
     * getter pour l'attribut date debut
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut id taux imposition
     * 
     * @return la valeur courante de l'attribut id taux imposition
     */
    public String getIdTauxImposition() {
        return idTauxImposition;
    }

    /**
     * getter pour l'attribut taux
     * 
     * @return la valeur courante de l'attribut taux
     */
    public String getTaux() {
        return taux;
    }

    /**
     * getter pour l'attribut type impot source
     * 
     * @return la valeur courante de l'attribut type impot source
     */
    public String getTypeImpotSource() {
        return typeImpotSource;
    }

    /**
     * setter pour l'attribut id tiers
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsCanton(String idTiers) {
        csCanton = idTiers;
    }

    /**
     * setter pour l'attribut date debut
     * 
     * @param dateDebut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date fin
     * 
     * @param dateFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * setter pour l'attribut id taux imposition
     * 
     * @param idTauxImposition
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTauxImposition(String idTauxImposition) {
        this.idTauxImposition = idTauxImposition;
    }

    /**
     * setter pour l'attribut taux
     * 
     * @param taux
     *            une nouvelle valeur pour cet attribut
     */
    public void setTaux(String taux) {
        this.taux = taux;
    }

    /**
     * setter pour l'attribut type impot source
     * 
     * @param typeImpotSource
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypeImpotSource(String typeImpotSource) {
        this.typeImpotSource = typeImpotSource;
    }
}
