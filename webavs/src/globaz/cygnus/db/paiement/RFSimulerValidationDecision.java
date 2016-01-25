/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.paiement;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.cygnus.db.qds.RFQd;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * 
 * @author fha
 * @revision jje -> Gestion de la cascade de l'adresse de paiement par les interface
 */
public class RFSimulerValidationDecision extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CLEARING = "HUCLEA";
    public static final String FIELDNAME_COMPTE_BANCAIRE = "HINCBA";
    public static final String FIELDNAME_CPP = "HICCP";
    public static final String FIELDNAME_DATEDECES = "HPDDEC";

    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

    public static final String FIELDNAME_DOM_ADR_PMT = "HFIAPP";
    public static final String FIELDNAME_IBAN = "HUIBAN";
    public static final String FIELDNAME_ID_ADRESSE_PAIEMENT_TI = "HIIAPA";
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
    public static final String TABLE_ADRESSE = "TIADREP";
    public static final String TABLE_ADRESSE_PAIEMENT = "TIADRPP";
    public static final String TABLE_AVOIR_PAIEMENT = "TIAPAIP";

    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_BANQUE = "TIBANQP";
    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_PERSONNE = "TIPERSP";

    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

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
        fromClauseBuffer.append(RFLot.TABLE_NAME);

        /************* Faire aussi la jointure sur le gestionnaire *******/

        // jointure entre la table RFLots et RELots
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RELot.TABLE_NAME_LOT);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFLot.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFLot.FIELDNAME_ID_LOT_RFM);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RELot.TABLE_NAME_LOT);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RELot.FIELDNAME_ID_LOT);

        // jointure entre la table des prestations et la table des lots
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFLot.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFLot.FIELDNAME_ID_LOT_RFM);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_LOT);

        // jointure entre la table des décisions et la table des prestations
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_DECISION);

        // jointure entre la table des décisions et la table RFQDBAS
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQd.FIELDNAME_ID_QD);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_QD_PRINICIPALE);

        // jointure entre la table RFQDBAS et ASSQDDossier
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssQdDossier.FIELDNAME_ID_QD);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQd.FIELDNAME_ID_QD);

        // jointure entre la table des AssQdDossier et la table des dossiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssQdDossier.FIELDNAME_ID_DOSSIER);

        // jointure entre la table des dossiers et la table des demandes prestation
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_PRDEM);

        // jointure entre la table des demandes et la table des numeros AVS
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSimulerValidationDecision.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSimulerValidationDecision.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSimulerValidationDecision.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSimulerValidationDecision.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSimulerValidationDecision.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSimulerValidationDecision.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSimulerValidationDecision.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSimulerValidationDecision.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSimulerValidationDecision.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSimulerValidationDecision.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSimulerValidationDecision.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSimulerValidationDecision.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSimulerValidationDecision.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    private String csCanton = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private String dateValidationDecision = "";
    private transient String fromClause = null;
    private String idAdressePaiement = "";
    private String idDecision = "";
    private String idGestionnaireDecision = "";
    private String idLot = "";
    private String idPrestation = "";
    private String idPrestationAccordee = "";
    private String idSousTypeSoin = "";
    private String idTiers = "";
    private String idTiersBanque = "";
    private String montantPrestation = "";
    private String nom = "";
    private String nss = "";
    private String prenom = "";
    private String referencePaiement = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFSimulerValidationDecision.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        nss = NSUtil.formatAVSUnknown(statement.dbReadString(RFSimulerValidationDecision.FIELDNAME_NUM_AVS));
        dateNaissance = statement.dbReadDateAMJ(RFSimulerValidationDecision.FIELDNAME_DATENAISSANCE);
        dateDeces = statement.dbReadDateAMJ(RFSimulerValidationDecision.FIELDNAME_DATEDECES);
        csSexe = statement.dbReadNumeric(RFSimulerValidationDecision.FIELDNAME_SEXE);
        nom = statement.dbReadString(RFSimulerValidationDecision.FIELDNAME_NOM);
        prenom = statement.dbReadString(RFSimulerValidationDecision.FIELDNAME_PRENOM);
        idTiers = statement.dbReadString(RFSimulerValidationDecision.FIELDNAME_ID_TIERS_TI);
        idPrestation = statement.dbReadNumeric(RFPrestation.FIELDNAME_ID_PRESTATION);
        montantPrestation = statement.dbReadNumeric(RFPrestation.FIELDNAME_MONTANT_TOTAL);
        idLot = statement.dbReadString(RFPrestation.FIELDNAME_ID_LOT);
        idDecision = statement.dbReadString(RFDecision.FIELDNAME_ID_DECISION);
        idPrestationAccordee = statement.dbReadString(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        idAdressePaiement = statement.dbReadString(RFDecision.FIELDNAME_ID_ADRESSE_PAIEMENT);
        csNationalite = statement.dbReadString(RFSimulerValidationDecision.FIELDNAME_NATIONALITE);
        idGestionnaireDecision = statement.dbReadString(RFDecision.FIELDNAME_ID_GESTIONNAIRE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
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

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateValidationDecision() {
        return dateValidationDecision;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdGestionnaireDecision() {
        return idGestionnaireDecision;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersBanque() {
        return idTiersBanque;
    }

    public String getMontantPrestation() {
        return montantPrestation;
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

    public String getReferencePaiement() {
        return referencePaiement;
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

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateValidationDecision(String dateValidationDecision) {
        this.dateValidationDecision = dateValidationDecision;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdGestionnaireDecision(String idGestionnaireDecision) {
        this.idGestionnaireDecision = idGestionnaireDecision;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersBanque(String idTiersBanque) {
        this.idTiersBanque = idTiersBanque;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
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

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

}
