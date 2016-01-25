// créé le 24 mars 2010
package globaz.cygnus.db.conventions;

import globaz.commons.nss.NSUtil;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BStatement;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/*
 * author fha
 */
public class RFConventionJointAssConFouTsJointFournisseurJointConventionAssure extends RFConvention {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

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
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        String as = " AS ";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConvention.TABLE_NAME);

        // jointure entre la table des conventions et la table COFOS
        fromClauseBuffer.append(leftJoin);
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

        // jointure entre la table COFOS et la table SOUS TYPE DE SOIN
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);

        // jointure entre la table SOUS TYPE DE SOIN et la table TYPE DE SOIN
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        // jointure entre la table COFOS et la table des fournisseurs
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.TABLE_TIERS);
        fromClauseBuffer.append(as);
        fromClauseBuffer.append("FOURNISSEUR");
        fromClauseBuffer.append(on);
        fromClauseBuffer.append("FOURNISSEUR");
        fromClauseBuffer.append(point);
        fromClauseBuffer
                .append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_FOURNISSEUR);

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
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionAssure.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConventionAssure.FIELDNAME_ID_ASSURE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer
                .append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer
                .append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer
                .append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer
                .append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer
                .append(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    private String csCanton = "";
    private String csNationalite = "";
    private String csSexe = "";

    private String dateDebut = "";
    private String dateDeces = "";
    private String dateFin = "";
    private String dateNaissance = "";
    private String fournisseur = "";
    private String fournisseurSuite = "";
    private transient String fromClause = null;
    private String idAdressePaiement = "";
    private String idCfs = "";
    private String idConas = "";
    private String idConvention = "";
    private String idFournisseur = "";
    // champs nécessaires table fournisseur , sous type soin et type soin
    private String idSousTypeSoin = "";
    private String idTypeSoin = "";
    private String montantAssure = "";
    private String nom = "";

    // champs nécessaires description tiers
    private String nss = "";
    private String prenom = "";
    private String sousTypeSoin = "";

    private String typeSoin = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // champs nécessaires description gestionnaire : est ce nécessaire?
    private String visaGestionnaire = "";

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
                    RFConventionJointAssConFouTsJointFournisseurJointConventionAssure
                            .createFromClause(_getCollection()));
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
        typeSoin = statement.dbReadNumeric(RFTypeDeSoin.FIELDNAME_CODE);
        sousTypeSoin = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_CODE);
        idSousTypeSoin = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        idTypeSoin = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        fournisseur = statement.dbReadString("NOM_FOURNISSEUR");
        fournisseurSuite = statement.dbReadString("PRENOM_FOURNISSEUR");
        idFournisseur = statement.dbReadString(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_FOURNISSEUR);
        nss = NSUtil.formatAVSUnknown(statement
                .dbReadString(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_NUM_AVS));
        dateNaissance = statement
                .dbReadDateAMJ(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_DATENAISSANCE);
        dateDeces = statement
                .dbReadDateAMJ(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_DATEDECES);
        csSexe = statement
                .dbReadNumeric(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_SEXE);
        nom = statement.dbReadString(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_NOM);
        prenom = statement
                .dbReadString(RFConventionJointAssConFouTsJointFournisseurJointConventionAssure.FIELDNAME_PRENOM);
        visaGestionnaire = statement.dbReadString(RFConvention.FIELDNAME_ID_GESTIONNAIRE);

        idCfs = statement.dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVFOUSTS);
        idAdressePaiement = statement
                .dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_ADRESSE_PAIEMENT);
        idConas = statement.dbReadNumeric(RFConventionAssure.FIELDNAME_ID_CONVENTION_ASSURE);

        dateDebut = statement.dbReadDateAMJ(RFConventionAssure.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(RFConventionAssure.FIELDNAME_DATE_FIN);
        montantAssure = statement.dbReadNumeric(RFConventionAssure.FIELDNAME_MONTANT_ASSURE);
    }

    // est ce à mettre ici?
    public String getAdresse() throws Exception {
        PRTiersWrapper prTiersWrapper = PRTiersHelper.getTiers(getISession(), nss);

        return prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NPA);
    }

    public String getCsCanton() {
        return csCanton;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public String getFournisseurSuite() {
        return fournisseurSuite;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdCfs() {
        return idCfs;
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

    public String getIdTypeSoin() {
        return idTypeSoin;
    }

    public String getMontantAssure() {
        return montantAssure;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSousTypeSoin() {
        return sousTypeSoin;
    }

    public String getTypeSoin() {
        return typeSoin;
    }

    public String getVisaGestionnaire() {
        return visaGestionnaire;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public void setFournisseurSuite(String fournisseurSuite) {
        this.fournisseurSuite = fournisseurSuite;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdCfs(String idCfs) {
        this.idCfs = idCfs;
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

    public void setIdTypeSoin(String idTypeSoin) {
        this.idTypeSoin = idTypeSoin;
    }

    public void setMontantAssure(String montantAssure) {
        this.montantAssure = montantAssure;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSousTypeSoin(String sousTypeSoin) {
        this.sousTypeSoin = sousTypeSoin;
    }

    public void setTypeSoin(String typeSoin) {
        this.typeSoin = typeSoin;
    }

    public void setVisaGestionnaire(String visaGestionnaire) {
        this.visaGestionnaire = visaGestionnaire;
    }

}
