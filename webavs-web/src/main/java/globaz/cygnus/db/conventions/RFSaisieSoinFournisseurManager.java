// cr?? le 24 mars 2010
package globaz.cygnus.db.conventions;

import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.interfaces.tiers.IPRTiers;

/**
 * author fha
 */
public class RFSaisieSoinFournisseurManager extends PRAbstractManager {

    // ~ Static fields/initializers
    // ------------------------------------------------------------------------------------

    // ~ Instance fields
    // ----------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeSousTypeDeSoinList = "";
    private String codeTypeDeSoinList = "";
    private Boolean forActif = Boolean.FALSE;
    private String forCsSexe = "";
    private String forDateDebut = "";
    private String forDateFin = "";
    private String forDateNaissance = "";
    private String forFournisseur = "";
    private String forFournisseurSuite = "";
    private String forIdAdressePaiement = "";
    private String forIdCfs = "";
    private String forIdConas = "";
    private String forIdConvention = "";
    private String forIdFournisseur = "";
    private String forIdSousTypeSoin = "";
    private String forIdTypeSoin = "";
    private String forLibelle = "";
    private String forMontantAssure = "";
    private String forOrderBy = "";
    private Boolean forParConvention = Boolean.FALSE;
    private transient String fromClause = null;
    private String idGestionnaire = "";

    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";

    private String likePrenom = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr?e une nouvelle instance de la classe LIDossiersJointTiersManager.
     */
    public RFSaisieSoinFournisseurManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement) selection uniquement des champs dont on
     * a besoin selon si on est dans un group by ou non on ne va pas s?l?ctionner les m?mes champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        String schema = _getCollection();

        StringBuffer fields = new StringBuffer();
        fields.append(RFConvention.FIELDNAME_ID_CONVENTION).append(",");
        fields.append(RFConvention.FIELDNAME_TEXT_LIBELLE).append(",");
        fields.append(RFConvention.FIELDNAME_BOOL_ACTIF).append(",");
        fields.append(RFConvention.FIELDNAME_DATE_CREATION);

        // si on est pas dans un group by on ajoute les champs qui suivent
        if (!forParConvention) {
            fields.append(",").append(RFConvention.FIELDNAME_ID_GESTIONNAIRE).append(",");
            fields.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVFOUSTS).append(",");
            fields.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_FOURNISSEUR).append(",");
            fields.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_ADRESSE_PAIEMENT).append(",");
            fields.append(
                    "FOURNISSEUR." + RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_NOM
                            + " AS NOM_FOURNISSEUR").append(",");
            fields.append(
                    "FOURNISSEUR." + RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_PRENOM
                            + " AS PRENOM_FOURNISSEUR").append(",");

            fields.append(RFSousTypeDeSoin.FIELDNAME_CODE).append(",");
            fields.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN).append(",");
            fields.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN).append(",");
            fields.append(RFTypeDeSoin.FIELDNAME_CODE).append(",");
            fields.append(RFConventionAssure.FIELDNAME_ID_CONVENTION_ASSURE).append(",");
            fields.append(RFConventionAssure.FIELDNAME_DATE_DEBUT).append(",");
            fields.append(RFConventionAssure.FIELDNAME_DATE_FIN).append(",");
            fields.append(RFConventionAssure.FIELDNAME_MONTANT_ASSURE).append(",");

            // les details requerants
            fields.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_DATEDECES)
                    .append(",");
            fields.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_DATENAISSANCE)
                    .append(",");
            fields.append(
                    schema + "TITIERP."
                            + RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_NATIONALITE)
                    .append(",");
            fields.append(
                    schema + "TITIERP."
                            + RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_NOM).append(
                    ",");
            fields.append(
                    schema + "TITIERP."
                            + RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_PRENOM)
                    .append(",");
            fields.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_SEXE).append(",");
            fields.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_NUM_AVS);
        }

        return fields.toString();

    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFConventionJointAssConFouTsJointFournisseurJointConventionAssure
                            .createFromClause(_getCollection()));

            if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
                from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS "
                        + IPRTiers.TABLE_AVS_HIST + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "."
                        + IPRTiers.FIELD_TI_IDTIERS + " = " + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS
                        + ")");
            }

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();
        if (!JadeStringUtil.isEmpty(forOrderBy)) {
            sqlOrder.append(forOrderBy);
        }
        return sqlOrder.toString();
    }

    /**
     * Red?finition de la m?thode _getWhere du parent afin de g?n?rer le WHERE de la requ?te en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isEmpty(forIdConvention)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFConvention.FIELDNAME_ID_CONVENTION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdConvention));
        }

        return sqlWhere.toString();
    }

    /**
     * D?finition de l'entit? (LIDossiersJointTiers)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFSaisieSoinFournisseur();
    }

    public String getCodeSousTypeDeSoinList() {
        return codeSousTypeDeSoinList;
    }

    public String getCodeTypeDeSoinList() {
        return codeTypeDeSoinList;
    }

    public Boolean getForActif() {
        return forActif;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForFournisseur() {
        return forFournisseur;
    }

    public String getForFournisseurSuite() {
        return forFournisseurSuite;
    }

    public String getForIdAdressePaiement() {
        return forIdAdressePaiement;
    }

    public String getForIdCfs() {
        return forIdCfs;
    }

    public String getForIdConas() {
        return forIdConas;
    }

    public String getForIdConvention() {
        return forIdConvention;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForIdSousTypeSoin() {
        return forIdSousTypeSoin;
    }

    public String getForIdTypeSoin() {
        return forIdTypeSoin;
    }

    public String getForLibelle() {
        return forLibelle;
    }

    public String getForMontantAssure() {
        return forMontantAssure;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public Boolean getForParConvention() {
        return forParConvention;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setCodeSousTypeDeSoinList(String codeSousTypeDeSoinList) {
        this.codeSousTypeDeSoinList = codeSousTypeDeSoinList;
    }

    public void setCodeTypeDeSoinList(String codeTypeDeSoinList) {
        this.codeTypeDeSoinList = codeTypeDeSoinList;
    }

    public void setForActif(Boolean forActif) {
        this.forActif = forActif;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForFournisseur(String forFournisseur) {
        this.forFournisseur = forFournisseur;
    }

    public void setForFournisseurSuite(String forFournisseurSuite) {
        this.forFournisseurSuite = forFournisseurSuite;
    }

    public void setForIdAdressePaiement(String forIdAdressePaiement) {
        this.forIdAdressePaiement = forIdAdressePaiement;
    }

    public void setForIdCfs(String forIdCfs) {
        this.forIdCfs = forIdCfs;
    }

    public void setForIdConas(String forIdConas) {
        this.forIdConas = forIdConas;
    }

    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForIdSousTypeSoin(String forIdSousTypeSoin) {
        this.forIdSousTypeSoin = forIdSousTypeSoin;
    }

    public void setForIdTypeSoin(String forIdTypeSoin) {
        this.forIdTypeSoin = forIdTypeSoin;
    }

    public void setForLibelle(String forLibelle) {
        this.forLibelle = forLibelle;
    }

    public void setForMontantAssure(String forMontantAssure) {
        this.forMontantAssure = forMontantAssure;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setForParConvention(Boolean forParConvention) {
        this.forParConvention = forParConvention;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

}
