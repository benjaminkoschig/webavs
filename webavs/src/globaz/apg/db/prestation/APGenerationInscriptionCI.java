/*
 * Créé le 20 juin 05
 */
package globaz.apg.db.prestation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <p>
 * Classe utilisée pour générer les inscriptions aux CI
 * </p>
 * 
 * <p>
 * <b>ATTENTION :</b> tester d'abord la valaeur de isInscriptionOK() avant de faire l'inscription !!
 * </p>
 * 
 * <p>
 * Si la répartition ne doit pas être inscrite aux CI, certains champs ne seront pas renseignés (gain de temps). Tester
 * avec {@link #isInscriptionOK() isInscriptionOK()} pour le savoir.
 * </p>
 * 
 * @author dvh
 */
public class APGenerationInscriptionCI extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String TOTAL_COTISATION = "TOTCOT";

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFields(String schema) {
        StringBuffer machin = new StringBuffer();
        machin.append(APPrestation.FIELDNAME_DATEDEBUT);
        machin.append(", ");
        machin.append(APPrestation.FIELDNAME_DATEFIN);
        machin.append(", ");
        machin.append(APRepartitionPaiements.FIELDNAME_MONTANTBRUT);
        machin.append(", ");
        machin.append(APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT);
        machin.append(", ");
        machin.append(APPrestation.FIELDNAME_IDRESTITUTION);
        machin.append(", ");
        machin.append(APRepartitionPaiements.FIELDNAME_IDTIERS);
        machin.append(", ");
        machin.append("SUM(" + APCotisation.FIELDNAME_MONTANT + ") AS " + TOTAL_COTISATION);

        return machin.toString();
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

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APRepartitionPaiements.TABLE_NAME);

        // jointure entre table des repartitions et prestations
        fromClauseBuffer.append(" INNER JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);
        fromClauseBuffer.append(" ON ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);
        fromClauseBuffer.append("=");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);

        // jointure entre table des répartitions et cotisations
        fromClauseBuffer.append(" INNER JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APCotisation.TABLE_NAME);
        fromClauseBuffer.append(" ON ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT);
        fromClauseBuffer.append("=");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APCotisation.TABLE_NAME);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(APCotisation.FIELDNAME_IDREPARTITIONBENEFICIAIREPAIEMENT);

        return fromClauseBuffer.toString();
    }

    private String dateDebut = "";
    private String dateFin = "";
    private String fields = null;
    private String fromClause = null;
    private String idRepartinionPaiement = "";
    private String idRestitution = "";
    private String idTiers = "";
    private boolean inscriptionOK = false;

    private String montantBrut = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String noAVS = "";

    private String totalCotisation = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isEmpty(noAVS)) {
            if (!JadeStringUtil.isEmpty(idTiers)) {
                noAVS = PRTiersHelper.getTiersParId(getSession(), idTiers).getProperty(
                        PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            }
        }

        super._beforeAdd(transaction);
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
            fields = createFields(getCollection());
        }

        return fields;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return APRepartitionPaiements.TABLE_NAME;
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
        dateDebut = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATEFIN);
        montantBrut = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_MONTANTBRUT, 2);
        idRepartinionPaiement = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT);
        idRestitution = statement.dbReadNumeric(APPrestation.FIELDNAME_IDRESTITUTION);
        idTiers = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDTIERS);
        totalCotisation = statement.dbReadNumeric(TOTAL_COTISATION);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // lecture seule
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // lecture seule
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // lecture seule
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
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getIdRepartinionPaiement() {
        return idRepartinionPaiement;
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
        if (JadeStringUtil.isEmpty(noAVS)) {

            if (!JadeStringUtil.isEmpty(idTiers)) {
                try {
                    return noAVS = PRTiersHelper.getTiersParId(getSession(), idTiers).getProperty(
                            PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return noAVS;
    }

    /**
     * @return
     */
    public String getTotalCotisation() {
        return totalCotisation;
    }

    /**
     * getter pour l'attribut inscription OK
     * 
     * @return la valeur courante de l'attribut inscription OK
     */
    public boolean isInscriptionOK() {
        return inscriptionOK;
    }

    /**
     * @param string
     */
    public void setTotalCotisation(String string) {
        totalCotisation = string;
    }

}
