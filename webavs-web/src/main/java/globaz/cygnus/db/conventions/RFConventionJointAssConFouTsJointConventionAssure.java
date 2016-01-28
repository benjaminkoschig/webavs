// créé le 24 mars 2010
package globaz.cygnus.db.conventions;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BStatement;

/**
 * author fha
 */
public class RFConventionJointAssConFouTsJointConventionAssure extends RFConvention {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

    public static final String FIELDNAME_ID_GESTIONNAIRE = "KUSER";

    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";

    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";

    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String FIELDNAME_VISA_GESTIONNAIRE = "FVISA";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";

    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConvention.TABLE_NAME);

        // jointure entre la table des conventions et la table COFOS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVENTION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConvention.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConvention.FIELDNAME_ID_CONVENTION);

        // jointure entre la table CONVENTION et la table convention assuré
        // LEFT JOIN car il n'y a pas forcement d'assure
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionAssure.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionAssure.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConventionAssure.FIELDNAME_ID_CONVENTION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConvention.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConvention.FIELDNAME_ID_CONVENTION);

        // jointure entre la table convention assuré et la table des numeros AVS
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionAssure.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConventionAssure.FIELDNAME_ID_ASSURE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointConventionAssure.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    private String dateDebut = null;
    private String dateFin = null;
    private transient String fromClause = null;
    private String idAssure = null;
    private String idCofos = null;
    private String idConas = null;
    private String idConvention = null;
    private String idFournisseur = null;
    private String idSousTypeSoin = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String NSS = null;

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFConventionJointAssConFouTsJointConventionAssure.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idConvention = statement.dbReadNumeric(RFConvention.FIELDNAME_ID_CONVENTION);
        idCofos = statement.dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVFOUSTS);
        idFournisseur = statement.dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_FOURNISSEUR);
        idSousTypeSoin = statement
                .dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
        idConas = statement.dbReadNumeric(RFConventionAssure.FIELDNAME_ID_CONVENTION_ASSURE);
        idAssure = statement.dbReadNumeric(RFConventionAssure.FIELDNAME_ID_ASSURE);
        dateDebut = statement.dbReadDateAMJ(RFConventionAssure.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(RFConventionAssure.FIELDNAME_DATE_FIN);
        NSS = NSUtil.formatAVSUnknown(statement
                .dbReadString(RFConventionJointAssConFouTsJointConventionAssure.FIELDNAME_NUM_AVS));

    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdAssure() {
        return idAssure;
    }

    public String getIdCofos() {
        return idCofos;
    }

    public String getIdConas() {
        return idConas;
    }

    @Override
    public String getIdConvention() {
        return idConvention;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    public String getNSS() {
        return NSS;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdAssure(String idAssure) {
        this.idAssure = idAssure;
    }

    public void setIdCofos(String idCofos) {
        this.idCofos = idCofos;
    }

    public void setIdConas(String idConas) {
        this.idConas = idConas;
    }

    @Override
    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

    public void setNSS(String nSS) {
        NSS = nSS;
    }

}
