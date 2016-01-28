/*
 * Créé le 3 juin 05
 */
package globaz.apg.db.prestation;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APPrestationsControlees extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_ID_AFF = "MAIAFF";

    /**
     */
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";

    /**
     */
    public static final String FIELDNAME_NO_AFF = "MALNAF";

    /**
     */
    public static final String FIELDNAME_NOM = "HTLDE1";

    /**
     */
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";

    /**
	 */
    public static final String FIELDNAME_PRENOM = "HTLDE2";

    /**
     */
    public static final String FIELDNAME_SOMMEMONTANT = "SOMME";

    /**
     */
    public static final String TABLE_AFFILIE = "AFAFFIP";

    /**
     */
    public static final String TABLE_AVS = "TIPAVSP";

    /**
     */
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFields(String schema) {
        return FIELDNAME_NUM_AVS + ", " + FIELDNAME_NOM + ", " + FIELDNAME_PRENOM + ", "
                + APPrestation.FIELDNAME_DATEDEBUT + ", " + APPrestation.FIELDNAME_DATEFIN + ", "
                + APPrestation.FIELDNAME_NOMBREJOURSSOLDES + ", " + APRepartitionPaiements.FIELDNAME_MONTANTBRUT + ", "
                + APRepartitionPaiements.FIELDNAME_MONTANTBRUT + "-" + APRepartitionPaiements.FIELDNAME_MONTANTNET
                + " AS " + FIELDNAME_SOMMEMONTANT + ", " + APRepartitionPaiements.FIELDNAME_MONTANTNET + ", "
                + APPrestation.FIELDNAME_IDPRESTATIONAPG + ", " + FIELDNAME_NO_AFF + ", "
                + APRepartitionPaiements.FIELDNAME_IDTIERS + ", " + APRepartitionPaiements.FIELDNAME_IDPARENT + ","
                + APRepartitionPaiements.FIELDNAME_MONTANTVENTILE;
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
        String leftJoin = " LEFT JOIN ";
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);

        // jointure entre tables prestation et repartition
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);

        // jointure entre prestation et droitLAPG
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APPrestation.FIELDNAME_IDDROIT);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);

        // jointure entre table droitlapg et demandes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPG.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);

        // jointure entre demande et tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table tiers et AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre tiers et affilié
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AFFILIE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APRepartitionPaiements.FIELDNAME_IDAFFILIE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AFFILIE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_AFF);

        return fromClauseBuffer.toString();
    }

    private String dateDebut = "";
    private String dateFin = "";
    private String fields = null;
    private String fromClause = null;
    private String idPrestation = "";
    private String idRepartitionPaiementsParent = "";
    private String idTiersBeneficiaire = "";
    private String montant = "";
    private String montantBrut = "";
    private String montantNet = "";
    private String montantVentile = "";
    private String nbrJoursSoldes = "";
    private String noAff = "";
    private String noAVS = "";
    private String nom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String nomBeneficiaire = "";

    private String prenom = "";

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
        PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(), idTiersBeneficiaire);
        if (tier != null) {
            nomBeneficiaire = tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        } else {
            tier = PRTiersHelper.getAdministrationParId(getSession(), idTiersBeneficiaire);
            if (tier != null) {
                nomBeneficiaire = tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            } else {
                nomBeneficiaire = "Nom pas trouvé";
            }
        }

    }

    /**
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (fields == null) {
            fields = createFields(_getCollection());
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
        return APPrestation.TABLE_NAME;
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
        noAVS = statement.dbReadString(FIELDNAME_NUM_AVS);
        noAff = statement.dbReadString(FIELDNAME_NO_AFF);
        nom = statement.dbReadString(FIELDNAME_NOM);
        prenom = statement.dbReadString(FIELDNAME_PRENOM);
        dateDebut = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATEFIN);
        nbrJoursSoldes = statement.dbReadNumeric(APPrestation.FIELDNAME_NOMBREJOURSSOLDES);
        montantBrut = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_MONTANTBRUT, 2);
        montant = statement.dbReadNumeric(FIELDNAME_SOMMEMONTANT, 2);
        montantNet = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_MONTANTNET, 2);
        idPrestation = statement.dbReadNumeric(APPrestation.FIELDNAME_IDPRESTATIONAPG);
        idTiersBeneficiaire = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDTIERS);
        idRepartitionPaiementsParent = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDPARENT);
        montantVentile = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_MONTANTVENTILE);
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
     * getter pour l'attribut from clause
     * 
     * @return la valeur courante de l'attribut from clause
     */
    public String getFromClause() {
        return fromClause;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getIdPrestation() {
        return idPrestation;
    }

    /**
     * getter pour l'attribut id repartition paiements parent
     * 
     * @return la valeur courante de l'attribut id repartition paiements parent
     */
    public String getIdRepartitionPaiementsParent() {
        return idRepartitionPaiementsParent;
    }

    /**
     * getter pour l'attribut id tiers beneficiaire
     * 
     * @return la valeur courante de l'attribut id tiers beneficiaire
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * getter pour l'attribut montant
     * 
     * @return la valeur courante de l'attribut montant
     */
    public String getMontant() {
        return montant;
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
     * getter pour l'attribut montant net
     * 
     * @return la valeur courante de l'attribut montant net
     */
    public String getMontantNet() {
        return montantNet;
    }

    /**
     * getter pour l'attribut montant ventile
     * 
     * @return la valeur courante de l'attribut montant ventile
     */
    public String getMontantVentile() {
        return montantVentile;
    }

    /**
     * getter pour l'attribut nbr jours soldes
     * 
     * @return la valeur courante de l'attribut nbr jours soldes
     */
    public String getNbrJoursSoldes() {
        return nbrJoursSoldes;
    }

    /**
     * getter pour l'attribut no aff
     * 
     * @return la valeur courante de l'attribut no aff
     */
    public String getNoAff() {
        return noAff;
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
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * getter pour l'attribut nom beneficiaire
     * 
     * @return la valeur courante de l'attribut nom beneficiaire
     */
    public String getNomBeneficiaire() {
        return nomBeneficiaire;
    }

    /**
     * @return
     */
    public String getPrenom() {
        return prenom;
    }

}
