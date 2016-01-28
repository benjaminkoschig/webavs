/*
 * Créé le 20 juin 05
 */
package globaz.ij.db.prestations;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <p>
 * Classe utilisée pour générer les inscriptions aux CI
 * </p>
 * 
 * <p>
 * <b>ATTENTION :</b> tester d'abord la valeur de isInscriptionOK() avant de faire l'inscription !!
 * </p>
 * 
 * <p>
 * Si la répartition ne doit pas être inscrite aux CI, certains champs ne seront pas renseignés (gain de temps). Tester
 * avec {@link #isInscriptionOK() isInscriptionOK()} pour le savoir.
 * </p>
 * 
 * @author dvh
 */
public class IJGenerationInscriptionCI extends BEntity {

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
    public static final String createFields(String schema) {
        StringBuffer machin = new StringBuffer();
        machin.append(IJPrestation.FIELDNAME_DATEDEBUT);
        machin.append(", ");
        machin.append(IJPrestation.FIELDNAME_DATEFIN);
        machin.append(", ");
        machin.append(IJRepartitionPaiements.FIELDNAME_MONTANTBRUT);
        machin.append(", ");
        machin.append(IJRepartitionPaiements.FIELDNAME_IDREPARTITION_PAIEMENT);
        machin.append(", ");
        machin.append(IJPrestation.FIELDNAME_CS_TYPE);
        machin.append(", ");
        machin.append(IJRepartitionPaiements.FIELDNAME_IDTIERS);

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
        fromClauseBuffer.append(IJRepartitionPaiements.TABLE_NAME);

        // jointure entre table des repartitions et prestations
        fromClauseBuffer.append(" INNER JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrestation.TABLE_NAME);
        fromClauseBuffer.append(" ON ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(IJRepartitionPaiements.FIELDNAME_IDPRESTATION);
        fromClauseBuffer.append("=");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrestation.TABLE_NAME);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(IJPrestation.FIELDNAME_IDPRESTATION);

        return fromClauseBuffer.toString();
    }

    private String dateDebut = "";
    private String dateFin = "";
    private String fields = null;
    private String fromClause = null;
    private String idRepartinionPaiement = "";
    private String idTiers = "";
    private boolean inscriptionOK = false;
    private String montantBrut = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String noAVS = "";

    private String typePrestation = "";

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
        // on regarde s'il y a des cotisations et leur signe. Si elle sont
        // négatives et que ce n'est pas une restitution
        // ou qu'elles sont positives et que c'est une restitution, il faut
        // faire l'inscription
        IJCotisationManager cotisationManager = new IJCotisationManager();
        cotisationManager.setSession(getSession());
        cotisationManager.setForIdRepartitionPaiements(idRepartinionPaiement);
        cotisationManager.find(transaction);

        if (cotisationManager.size() == 0) {
            inscriptionOK = false;
        } else {
            IJCotisation cotisation = (IJCotisation) cotisationManager.getEntity(0);
            FWCurrency montantCotisation = new FWCurrency(cotisation.getMontant());

            if (!typePrestation.equals(IIJPrestation.CS_RESTITUTION)) {
                // ce n'est pas une restitution
                inscriptionOK = montantCotisation.isNegative();
            } else {
                inscriptionOK = montantCotisation.isPositive();
            }
        }

        // si cette répartition doit être inscrite, on va récupérer qques
        // valeurs dont on a besoin
        if (inscriptionOK) {
            noAVS = PRTiersHelper.getTiersParId(getSession(), idTiers).getProperty(
                    PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        }
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
        return IJRepartitionPaiements.TABLE_NAME;
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
        dateDebut = statement.dbReadDateAMJ(IJPrestation.FIELDNAME_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(IJPrestation.FIELDNAME_DATEFIN);
        montantBrut = statement.dbReadNumeric(IJRepartitionPaiements.FIELDNAME_MONTANTBRUT, 2);
        idRepartinionPaiement = statement.dbReadNumeric(IJRepartitionPaiements.FIELDNAME_IDREPARTITION_PAIEMENT);
        typePrestation = statement.dbReadNumeric(IJPrestation.FIELDNAME_CS_TYPE);
        idTiers = statement.dbReadNumeric(IJRepartitionPaiements.FIELDNAME_IDTIERS);
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
        return noAVS;
    }

    /**
     * getter pour l'attribut inscription OK
     * 
     * @return la valeur courante de l'attribut inscription OK
     */
    public boolean isInscriptionOK() {
        return inscriptionOK;
    }
}
